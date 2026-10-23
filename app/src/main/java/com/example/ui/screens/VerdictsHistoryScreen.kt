package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.curriculum.CaSyllabusData
import com.example.data.local.ErrorLogEntity
import com.example.data.model.LabMode
import com.example.data.model.LabVerdict
import com.example.data.model.McqQuestion
import com.example.data.model.PaperCode
import com.example.data.model.PriorityTier
import com.example.data.model.SyllabusTopic
import com.example.ui.theme.Amber500
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.Sky500
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.CaLabViewModel

@Composable
fun VerdictsHistoryScreen(viewModel: CaLabViewModel) {
    val dashboard by viewModel.verdictsDashboard.collectAsState()
    val mcqState by viewModel.mcqTestState.collectAsState()
    val errorLogs by viewModel.errorLogs.collectAsState()
    val progressMap by viewModel.topicProgressMap.collectAsState()

    var activeSubTab by remember { mutableIntStateOf(0) } // 0 = MCQ Test, 1 = Weakness Radar, 2 = Error Log & Verdicts
    var selectedRadarPaper by remember { mutableStateOf<PaperCode?>(null) } // null = All papers

    LaunchedEffect(Unit) {
        viewModel.loadVerdictsDashboard()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("verdicts_history_screen")
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        // 3 SUB-TABS ROW AT TOP
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            TabRow(
                selectedTabIndex = activeSubTab,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = activeSubTab == 0,
                    onClick = { activeSubTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(15.dp), tint = Amber500)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("MCQ Test", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    },
                    modifier = Modifier.testTag("subtab_mcq_test")
                )
                Tab(
                    selected = activeSubTab == 1,
                    onClick = { activeSubTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Assessment, contentDescription = null, modifier = Modifier.size(15.dp), tint = Sky500)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Weakness Radar", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    },
                    modifier = Modifier.testTag("subtab_weakness_radar")
                )
                Tab(
                    selected = activeSubTab == 2,
                    onClick = { activeSubTab = 2 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(15.dp), tint = ErrorRed)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Errors (${errorLogs.size})", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    },
                    modifier = Modifier.testTag("subtab_error_log_verdicts")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // SUB-TAB 0: DIAGNOSTIC MCQ TEST
        if (activeSubTab == 0) {
            McqTestSection(
                viewModel = viewModel,
                mcqState = mcqState
            )
        }
        // SUB-TAB 1: WEAKNESS RADAR
        else if (activeSubTab == 1) {
            WeaknessRadarSection(
                selectedRadarPaper = selectedRadarPaper,
                onSelectPaper = { selectedRadarPaper = it },
                progressMap = progressMap,
                onJumpToTopic = { topic ->
                    viewModel.selectAskTopic(topic)
                    viewModel.setMode(LabMode.ASK_LAB)
                }
            )
        }
        // SUB-TAB 2: ERROR LOG & VERDICTS HISTORY
        else {
            ErrorLogAndVerdictsSection(
                dashboard = dashboard,
                errorLogs = errorLogs,
                onRefreshVerdicts = { viewModel.loadVerdictsDashboard() },
                onResolveError = { viewModel.resolveErrorLog(it) },
                onStartTeachLab = { viewModel.setMode(LabMode.TEACH_LAB) }
            )
        }
    }
}

// =========================================================================
// SUB-TAB 1: DIAGNOSTIC MCQ TEST SECTION
// =========================================================================
@Composable
fun McqTestSection(
    viewModel: CaLabViewModel,
    mcqState: com.example.ui.viewmodel.McqTestState
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Paper Selector Chips
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(PaperCode.entries, key = { it.name }) { paper ->
                    val isSelected = mcqState.selectedPaper == paper
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectMcqTestPaper(paper) },
                        label = { Text(paper.shortName, fontSize = 12.sp) },
                        modifier = Modifier.testTag("mcq_paper_chip_${paper.name}"),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
        }

        // Action Controls Header: AI MCQ Generator & Reset
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${mcqState.selectedPaper.title} • 2-Mark Diagnostic Test",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "ICAI Pattern • Integrated Case Scenarios & Standalone MCQs",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = { viewModel.generateAiMcqTest() },
                            enabled = !mcqState.isGeneratingAi,
                            colors = ButtonDefaults.buttonColors(containerColor = Amber500),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("generate_ai_mcqs_button")
                        ) {
                            if (mcqState.isGeneratingAi) {
                                CircularProgressIndicator(modifier = Modifier.size(14.dp), color = Color.White, strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("AI MCQs", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        IconButton(
                            onClick = { viewModel.resetMcqTest() },
                            modifier = Modifier.size(36.dp).testTag("reset_mcq_test_button")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Reset Test", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        // Error message banner if offline / API fallback
        mcqState.errorMessage?.let { msg ->
            item {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Amber500.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, Amber500.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚡ $msg",
                        fontSize = 11.sp,
                        color = Amber500,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }

        // Score Banner after submission
        if (mcqState.isSubmitted) {
            item {
                val percent = if (mcqState.totalQuestions > 0) (mcqState.scoreCorrect * 100) / mcqState.totalQuestions else 0
                val marksScored = mcqState.scoreCorrect * 2
                val totalMarks = mcqState.totalQuestions * 2
                val bannerColor = if (percent >= 70) SuccessGreen else Amber500

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = bannerColor.copy(alpha = 0.12f)),
                    border = BorderStroke(1.5.dp, bannerColor.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth().testTag("mcq_score_banner")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🎯 Test Complete! Score: $marksScored / $totalMarks Marks ($percent%)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = bannerColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (percent >= 70) "Strong grasp! Wrong questions automatically logged to your Error Log for 3-Day Recall." else "Review the detailed ICAI explanations below. Missed rules saved to Error Log.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Question items list
        itemsIndexed(mcqState.questions, key = { index, q -> "${q.id}_$index" }) { index, q ->
            McqQuestionCard(
                qNumber = index + 1,
                question = q,
                selectedOption = mcqState.userAnswers[q.id],
                isSubmitted = mcqState.isSubmitted,
                onSelectOption = { optIdx -> viewModel.selectMcqOption(q.id, optIdx) }
            )
        }

        // Submit Button
        if (!mcqState.isSubmitted && mcqState.questions.isNotEmpty()) {
            item {
                Button(
                    onClick = { viewModel.submitMcqTest() },
                    enabled = mcqState.userAnswers.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).testTag("submit_mcq_test_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Submit Diagnostic Test (${mcqState.userAnswers.size} / ${mcqState.questions.size} Answered) →",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun McqQuestionCard(
    qNumber: Int,
    question: McqQuestion,
    selectedOption: Int?,
    isSubmitted: Boolean,
    onSelectOption: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("mcq_question_card_$qNumber")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Question # & Priority Tier Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$qNumber",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = question.topicName.take(30) + if (question.topicName.length > 30) "..." else "",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(question.priorityTier.badgeColorHex).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "2 Marks • ${question.priorityTier.label.split("•")[0].trim()}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(question.priorityTier.badgeColorHex),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Integrated Case Scenario memo box if present
            question.caseScenarioText?.let { caseText ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null, tint = Sky500, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("📖 Integrated Case Scenario Memo", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Sky500)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(caseText, fontSize = 11.sp, lineHeight = 17.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Question Stem
            Text(
                text = question.questionStem,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 4 Options A, B, C, D
            question.options.forEachIndexed { optIndex, optionText ->
                val isChosen = selectedOption == optIndex
                val isCorrect = optIndex == question.correctOptionIndex

                val optionBg = when {
                    isSubmitted && isCorrect -> SuccessGreen.copy(alpha = 0.15f)
                    isSubmitted && isChosen && !isCorrect -> ErrorRed.copy(alpha = 0.15f)
                    isChosen -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surface
                }

                val optionBorder = when {
                    isSubmitted && isCorrect -> BorderStroke(1.5.dp, SuccessGreen)
                    isSubmitted && isChosen && !isCorrect -> BorderStroke(1.5.dp, ErrorRed)
                    isChosen -> BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                    else -> BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = optionBg,
                    border = optionBorder,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = !isSubmitted) { onSelectOption(optIndex) }
                        .testTag("mcq_${qNumber}_option_$optIndex")
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = optionText,
                            fontSize = 12.sp,
                            fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSubmitted) {
                            if (isCorrect) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = SuccessGreen, modifier = Modifier.size(16.dp))
                            } else if (isChosen) {
                                Icon(Icons.Default.Warning, contentDescription = "Wrong", tint = ErrorRed, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            // Explanation Card after submission
            if (isSubmitted) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Sky500.copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, Sky500.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Sky500, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("💡 Working Note & ICAI Provision Reference", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Sky500)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(question.explanation, fontSize = 11.sp, lineHeight = 17.sp, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("🏛️ Ref: ${question.icaiProvisionRef}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Sky500)
                    }
                }
            }
        }
    }
}

// =========================================================================
// SUB-TAB 2: WEAKNESS RADAR SECTION
// =========================================================================
@Composable
fun WeaknessRadarSection(
    selectedRadarPaper: PaperCode?,
    onSelectPaper: (PaperCode?) -> Unit,
    progressMap: Map<String, com.example.data.local.TopicProgressEntity>,
    onJumpToTopic: (SyllabusTopic) -> Unit
) {
    val allTopics = remember(selectedRadarPaper) {
        if (selectedRadarPaper != null) {
            CaSyllabusData.getTopicsForPaper(selectedRadarPaper)
        } else {
            CaSyllabusData.topics
        }
    }

    // Rank topics by Urgency Score = (100 - accuracy) * priorityWeightMultiplier
    val rankedTopics = remember(allTopics, progressMap) {
        allTopics.map { topic ->
            val progress = progressMap[topic.id]
            val mcqAttempted = progress?.mcqAttempted ?: 0
            val mcqCorrect = progress?.mcqCorrect ?: 0
            val accuracy = if (mcqAttempted > 0) (mcqCorrect * 100.0 / mcqAttempted) else 50.0
            val urgencyScore = (100.0 - accuracy) * topic.priorityTier.weightMultiplier
            Triple(topic, accuracy, urgencyScore)
        }.sortedByDescending { it.third }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Paper filter row
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedRadarPaper == null,
                        onClick = { onSelectPaper(null) },
                        label = { Text("All 6 Papers", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
                items(PaperCode.entries, key = { it.name }) { paper ->
                    val isSelected = selectedRadarPaper == paper
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectPaper(paper) },
                        label = { Text(paper.shortName, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
        }

        // Header Explainer Banner
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "🎯 Weakness Radar — Ranked by Revision Urgency",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Urgency Score = (100 - Accuracy) × Priority Tier Weight. Focus first on red flagged chapters!",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Priority Table Items
        itemsIndexed(rankedTopics, key = { _, item -> item.first.id }) { index, (topic, accuracy, urgencyScore) ->
            val progress = progressMap[topic.id]
            val writtenSolved = progress?.writtenSolved ?: 0
            val mcqAttempted = progress?.mcqAttempted ?: 0
            val oralRecalls = progress?.oralRecallsCompleted ?: 0

            val isCritical = (topic.priorityTier == PriorityTier.TIER_1 || topic.priorityTier == PriorityTier.TIER_2) && accuracy < 65.0
            val tierColor = Color(topic.priorityTier.badgeColorHex)

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = if (isCritical) Color(0xFFFEF2F2) else MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, if (isCritical) ErrorRed else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth().testTag("radar_row_${topic.id}")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (index < 3) ErrorRed.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "Rank #${index + 1}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 11.sp,
                                    color = if (index < 3) ErrorRed else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = topic.topicName,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${topic.paper.shortName} • ${topic.moduleName} • ${topic.pastPaperFrequency}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (accuracy < 65.0) ErrorRed.copy(alpha = 0.15f) else SuccessGreen.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "${accuracy.toInt()}% Acc",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = if (accuracy < 65.0) ErrorRed else SuccessGreen,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Progress Quotas Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Quotas: Written $writtenSolved/${topic.writtenTarget} | MCQs $mcqAttempted/${topic.mcqTarget} | Recalls $oralRecalls/${topic.oralRecallTarget}",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = tierColor.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = topic.priorityTier.label.split("•")[0].trim(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = tierColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    if (isCritical) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("CRITICAL EXAM RISK — REVISE FIRST", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = ErrorRed)
                            }

                            Button(
                                onClick = { onJumpToTopic(topic) },
                                colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                                modifier = Modifier.height(28.dp),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text("Jump to Ask Lab →", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// SUB-TAB 3: ERROR LOG & VERDICTS HISTORY SECTION
// =========================================================================
@Composable
fun ErrorLogAndVerdictsSection(
    dashboard: com.example.ui.viewmodel.VerdictsDashboardState,
    errorLogs: List<ErrorLogEntity>,
    onRefreshVerdicts: () -> Unit,
    onResolveError: (Long) -> Unit,
    onStartTeachLab: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Summary Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Avg Mastery", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text(
                            text = if (dashboard.verdicts.isEmpty()) "--" else "${dashboard.averageScore.toInt()}%",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Box(modifier = Modifier.width(1.dp).height(40.dp).background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Active Errors", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text(
                            text = "${errorLogs.size}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = ErrorRed
                        )
                    }

                    Box(modifier = Modifier.width(1.dp).height(40.dp).background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Graded Sessions", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text(
                            text = "${dashboard.totalSessions}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        // Section 1: Active Error Log
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⚡ Active Error Log (${errorLogs.size} Saved Mistakes)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ErrorRed
                )
            }
        }

        if (errorLogs.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.1f)),
                    border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "🎉 No active error logs! Wrong MCQ answers or missed provisos in diagnostic tests automatically save here.",
                        fontSize = 11.sp,
                        color = SuccessGreen,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        } else {
            items(errorLogs, key = { it.id }) { error ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    border = BorderStroke(1.dp, ErrorRed.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth().testTag("error_card_${error.id}")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(error.paperName, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ErrorRed)
                                Text(error.topicName, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                            }

                            Button(
                                onClick = { onResolveError(error.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text("Mark Resolved ✓", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text("❓ Question: ${error.questionOrConcept}", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("❌ Your Mistake: ${error.userMistake}", fontSize = 11.sp, color = ErrorRed, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("✓ Correct Rule: ${error.correctRule}", fontSize = 11.sp, color = SuccessGreen, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Section 2: Past Lab's Verdicts
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📊 Past Lab's Verdicts & Gap Analyses",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onRefreshVerdicts, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh Verdicts", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                }
            }
        }

        if (dashboard.verdicts.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(48.dp).clip(CircleShape).background(Amber500.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.School, contentDescription = null, tint = Amber500)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No Teaching Sessions Yet!", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Hop over to 'Teach Lab', explain a provision in your own words, and tap 'Give Verdict' to get graded by Lab!",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(onClick = onStartTeachLab, modifier = Modifier.testTag("start_first_teach_button")) {
                            Text("Start First Session in Teach Lab")
                        }
                    }
                }
            }
        } else {
            items(dashboard.verdicts, key = { it.id }) { verdict ->
                VerdictHistoryCard(verdict = verdict)
            }
        }
    }
}

@Composable
fun VerdictHistoryCard(verdict: LabVerdict) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .testTag("verdict_card_${verdict.topicName.take(6)}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = verdict.paperName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = verdict.topicName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (verdict.masteryScore >= 75) SuccessGreen.copy(alpha = 0.15f) else Amber500.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${verdict.masteryScore}/100",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = if (verdict.masteryScore >= 75) SuccessGreen else Amber500,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    // Nailed items
                    Text("What You Nailed:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SuccessGreen)
                    verdict.whatYouNailed.forEach {
                        Row(modifier = Modifier.padding(vertical = 2.dp)) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(it, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Slipped items
                    Text("What Slipped (Gaps):", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = ErrorRed)
                    verdict.whatSlipped.forEach {
                        Row(modifier = Modifier.padding(vertical = 2.dp)) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(it, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Examiner Tip
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Amber500, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ICAI Examiner Comment", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                            Text(verdict.icaiExaminerTip, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Next action
                    Text("Next Action: ${verdict.nextAction}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Sky500)
                }
            }
        }
    }
}
