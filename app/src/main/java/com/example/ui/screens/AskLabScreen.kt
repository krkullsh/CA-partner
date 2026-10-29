package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.curriculum.CuratedCatchLabScenarios
import com.example.data.curriculum.CuratedQuestionBank
import com.example.data.curriculum.PracticeQuestion
import com.example.data.curriculum.StrategyBlueprintReader
import com.example.data.local.TopicProgressEntity
import com.example.data.model.CatchLabScenario
import com.example.data.model.ExperienceLevel
import com.example.data.model.PaperCode
import com.example.data.model.PriorityTier
import com.example.data.model.StandardBlueprint
import com.example.data.model.SubjectArchetype
import com.example.data.model.SyllabusTopic
import com.example.data.model.TeacherSection
import com.example.ui.components.FormattedText
import com.example.ui.components.StrategyBlueprintBottomSheet
import com.example.ui.theme.Amber500
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.Sky500
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.CaLabViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class StudyAskSubTab(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    TEACHER_PYQ_RADAR("Teacher (PYQ Radar)", Icons.Default.Psychology),
    CHAPTER_NOTES("Chapter Notes", Icons.AutoMirrored.Filled.MenuBook),
    PRACTICE_QBANK("Practice Q-Bank", Icons.Default.Timer)
}

fun SubjectArchetype.displayName(): String = when (this) {
    SubjectArchetype.STANDARDS_AND_ACCOUNTS -> "Standards & Accounts"
    SubjectArchetype.STATUTE_AND_CASE_LAW -> "Statute & Case Law"
    SubjectArchetype.TAX_AMENDMENTS_AND_COMPUTATION -> "Tax & Computation"
    SubjectArchetype.FORMULAS_AND_PROFORMA -> "Formulas & Proforma"
    SubjectArchetype.AUDIT_PROCEDURES_AND_CLAUSES -> "Audit & Clauses"
    SubjectArchetype.STRATEGIC_FRAMEWORKS -> "Strategic Frameworks"
}

@Composable
fun AskLabScreen(viewModel: CaLabViewModel) {
    val state by viewModel.askState.collectAsState()
    val activePaper by viewModel.activePaper.collectAsState()
    val activeTopic by viewModel.activeTopic.collectAsState()
    val experienceLevel by viewModel.experienceLevel.collectAsState()
    val bookmarkedIds by viewModel.bookmarkedIds.collectAsState()
    val progressMap by viewModel.topicProgressMap.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var activeSubTab by remember { mutableStateOf(StudyAskSubTab.TEACHER_PYQ_RADAR) }
    var showBlueprintDialog by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    // 4 Rich offline teacher sections
    val teacherSections = remember(activeTopic.id, activePaper) {
        viewModel.repository.getOfflineTeacherArticle(activeTopic, activePaper.archetype)
    }

    val outlinePills = listOf("1. Logic", "2. Blueprint", "3. Traps", "4. Presentation")
    val firstVisible = listState.firstVisibleItemIndex
    val activePillIndex = when {
        firstVisible >= 7 -> 3
        firstVisible == 6 -> 2
        firstVisible == 5 -> 1
        else -> 0
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
            .testTag("ask_lab_screen"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // ITEM 0: COMPACT TOPIC BANNER & SEARCH BAR
        item {
            Spacer(modifier = Modifier.height(2.dp))
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("compact_topic_header_card")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Row 1: Exam Probability Badge & Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            // Exam Probability Badge
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Amber500.copy(alpha = 0.14f),
                                border = BorderStroke(0.5.dp, Amber500.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Whatshot,
                                        contentDescription = null,
                                        tint = Amber500,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "Exam Prob: ${activeTopic.pastPaperFrequency}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Amber500
                                    )
                                }
                            }

                            // Priority Tier Pill
                            val tierColor = Color(activeTopic.priorityTier.badgeColorHex)
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = tierColor.copy(alpha = 0.12f),
                                border = BorderStroke(0.5.dp, tierColor.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = activeTopic.priorityTier.name.replace("_", " "),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = tierColor,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        // Compact Blueprint & Bookmark buttons
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                                modifier = Modifier
                                    .height(28.dp)
                                    .clickable { showBlueprintDialog = true }
                                    .testTag("header_open_blueprint_button")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "Blueprint",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            val isBookmarked = bookmarkedIds.contains(activeTopic.id)
                            IconButton(
                                onClick = {
                                    viewModel.toggleBookmark(
                                        activeTopic.id,
                                        "TOPIC",
                                        activeTopic.topicName,
                                        activeTopic.paper.title
                                    )
                                },
                                modifier = Modifier
                                    .size(30.dp)
                                    .testTag("header_bookmark_button")
                            ) {
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = if (isBookmarked) Amber500 else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Title & Archetype
                    Text(
                        text = activeTopic.topicName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${activePaper.shortName} • ${activeTopic.chapter} • ${activePaper.archetype.displayName()} (${activeTopic.averageMarks.toInt()} Marks)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Compact Doubt / Query Field (36dp height)
                    OutlinedTextField(
                        value = state.customQuery,
                        onValueChange = { viewModel.updateAskCustomQuery(it) },
                        placeholder = { Text("Search concept or ask specific doubt...", fontSize = 11.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("ask_search_field"),
                        shape = RoundedCornerShape(8.dp),
                        trailingIcon = {
                            IconButton(
                                onClick = { viewModel.searchOrGenerateExplanation() },
                                modifier = Modifier
                                    .size(32.dp)
                                    .testTag("ask_search_button")
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search",
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                        )
                    )
                }
            }
        }

        // ITEM 1: 3-TAB STRIP (28.dp height): [Teacher (PYQ Radar)] | [Chapter Notes] | [Practice Q-Bank]
        item {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("study_segmented_modes")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StudyAskSubTab.entries.forEach { tab ->
                        val isSelected = activeSubTab == tab
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
                            border = if (isSelected) BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)) else null,
                            shadowElevation = if (isSelected) 1.dp else 0.dp,
                            modifier = Modifier
                                .weight(1f)
                                .height(28.dp)
                                .clickable { activeSubTab = tab }
                                .testTag("tab_${tab.name.lowercase()}")
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = null,
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = tab.label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // TAB 1: 👨‍🏫 TEACHER (PYQ RADAR)
        // -------------------------------------------------------------
        if (activeSubTab == StudyAskSubTab.TEACHER_PYQ_RADAR) {
            // ITEM 2: PYQ RADAR & EXAM TREND CARD
            item {
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(0.5.dp, Amber500.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Whatshot,
                                    contentDescription = null,
                                    tint = Amber500,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ICAI Exam Radar & Trend Breakdown",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = "Avg: ${activeTopic.averageMarks.toInt()} Marks",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // PYQ Trend
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(6.dp)) {
                                    Text("Past Exam (PYQ)", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Sky500)
                                    Text(
                                        text = activeTopic.pastPaperFrequency,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1
                                    )
                                }
                            }
                            // RTP Trend
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(6.dp)) {
                                    Text("RTP / MTP Series", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Amber500)
                                    Text(
                                        text = if (activeTopic.hasRecentAmendment) "Nov 2024 RTP Updated" else "Regular ICAI Focus",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1
                                    )
                                }
                            }
                            // Weightage
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(6.dp)) {
                                    Text("ICAI Quota Tier", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                                    Text(
                                        text = activeTopic.priorityTier.name.replace("_", " "),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ITEM 3: READING OUTLINE SCRUB BAR
            item {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reading_outline_scrub_bar")
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "TEACHER OUTLINE",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Sky500.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = experienceLevel.label.split(" ").firstOrNull() ?: "Standard",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Sky500,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            outlinePills.forEachIndexed { index, pillLabel ->
                                item {
                                    val isPillActive = activePillIndex == index
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (isPillActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier
                                            .height(26.dp)
                                            .clickable {
                                                coroutineScope.launch {
                                                    // Offset: items 0, 1, 2, 3 are headers, so sections start at index 4
                                                    val targetIndex = (4 + index).coerceAtMost(7)
                                                    listState.animateScrollToItem(targetIndex)
                                                }
                                            }
                                            .testTag("outline_pill_$index")
                                    ) {
                                        Text(
                                            text = pillLabel,
                                            fontSize = 11.sp,
                                            fontWeight = if (isPillActive) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isPillActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ITEM 4: SECTION 1: Core Logic & Analogy
            item {
                val sec1 = teacherSections.getOrNull(0)
                TeacherSectionCard(
                    sectionNumber = "1",
                    title = sec1?.heading ?: "1. Core Logic & Everyday Analogy",
                    bodyMarkdown = sec1?.bodyMarkdown ?: "",
                    accentColor = Sky500,
                    expLevel = experienceLevel,
                    testTag = "teacher_section_1"
                )
            }

            // ITEM 5: SECTION 2: Official Blueprint Breakdown
            item {
                val sec2 = teacherSections.getOrNull(1)
                TeacherSectionCard(
                    sectionNumber = "2",
                    title = sec2?.heading ?: "2. Official Blueprint Breakdown",
                    bodyMarkdown = sec2?.bodyMarkdown ?: "",
                    accentColor = MaterialTheme.colorScheme.primary,
                    expLevel = experienceLevel,
                    testTag = "teacher_section_2"
                )
            }

            // ITEM 6: SECTION 3: Examiner Observations, Traps & Spot the Trap Mini-Card
            item {
                val sec3 = teacherSections.getOrNull(2)
                val matchingScenario = remember(activeTopic.id, activePaper) {
                    CuratedCatchLabScenarios.getScenariosForPaper(activePaper).firstOrNull { sc ->
                        sc.topic.contains(activeTopic.topicName, ignoreCase = true) ||
                        activeTopic.topicName.contains(sc.topic, ignoreCase = true) ||
                        sc.id.contains(activeTopic.id, ignoreCase = true)
                    } ?: CuratedCatchLabScenarios.getScenariosForPaper(activePaper).firstOrNull()
                }

                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(0.5.dp, Amber500.copy(alpha = 0.45f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("teacher_section_3")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Amber500.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "3",
                                    color = Amber500,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = sec3?.heading ?: "3. Examiner Traps & Student Mark Loss Points",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Amber500
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        FormattedText(
                            text = sec3?.bodyMarkdown ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 19.sp
                        )

                        // Official Examiner Pitfalls from topic metadata
                        if (activeTopic.commonExaminerPitfalls.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ErrorRed.copy(alpha = 0.08f),
                                border = BorderStroke(0.5.dp, ErrorRed.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = "⚠️ Frequent ICAI Examiner Trap / Mark Deduction:",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ErrorRed
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = activeTopic.commonExaminerPitfalls,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Interactive "Spot the Trap" Mini-Card
                        SpotTheTrapMiniCard(scenario = matchingScenario)

                        Spacer(modifier = Modifier.height(10.dp))

                        // [Mark Lesson Read ✓] Button (28.dp height)
                        val isTeacherRead = progressMap[activeTopic.id]?.teacherReadCompleted == 1
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isTeacherRead) SuccessGreen.copy(alpha = 0.14f) else MaterialTheme.colorScheme.primaryContainer,
                            border = BorderStroke(
                                0.5.dp,
                                if (isTeacherRead) SuccessGreen else MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(28.dp)
                                .clickable {
                                    val newStatus = !isTeacherRead
                                    viewModel.markTeacherRead(activeTopic.id, newStatus)
                                    Toast.makeText(
                                        context,
                                        if (newStatus) "✓ Marked '${activeTopic.topicName}' as Read!" else "Lesson marked unread.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .testTag("mark_lesson_read_button")
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = if (isTeacherRead) Icons.Default.CheckCircle else Icons.Default.Bookmark,
                                    contentDescription = null,
                                    tint = if (isTeacherRead) SuccessGreen else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isTeacherRead) "Lesson Completed ✓ (Tap to Undo)" else "Mark Lesson Read ✓",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = if (isTeacherRead) SuccessGreen else MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }

            // ITEM 7: SECTION 4: Exam Answer Presentation
            item {
                val sec4 = teacherSections.getOrNull(3)
                TeacherSectionCard(
                    sectionNumber = "4",
                    title = sec4?.heading ?: "4. Exam Answer Presentation & Step Marks",
                    bodyMarkdown = sec4?.bodyMarkdown ?: "",
                    accentColor = SuccessGreen,
                    expLevel = experienceLevel,
                    testTag = "teacher_section_4"
                )
            }
        }

        // -------------------------------------------------------------
        // TAB 2: 📖 CHAPTER NOTES
        // -------------------------------------------------------------
        if (activeSubTab == StudyAskSubTab.CHAPTER_NOTES) {
            item {
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("chapter_notes_card")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Statutory & Standard Provisions",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            // Compact Copy Note Button (26dp)
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                                modifier = Modifier
                                    .height(26.dp)
                                    .clickable {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        clipboard.setPrimaryClip(ClipData.newPlainText("CA-Lab Note", "${activeTopic.topicName}\n\n${activeTopic.keyProvisions.joinToString("\n• ", prefix = "• ")}\n\n${state.displayedContent}"))
                                        Toast.makeText(context, "Copied notes to clipboard!", Toast.LENGTH_SHORT).show()
                                    }
                                    .testTag("copy_explanation_button")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text("Copy Notes", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Key Statutory Provisions from syllabus
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "📌 Core Statutory Provisions & ICAI Criteria",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                FormattedText(
                                    text = activeTopic.keyProvisions.joinToString("\n• ", prefix = "• "),
                                    style = MaterialTheme.typography.bodySmall,
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        // Formats & Formulas / Monospace Proforma Card
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "📐 Working Format / Journal Entry Proforma",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Sky500
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = when (activePaper.archetype) {
                                        SubjectArchetype.STANDARDS_AND_ACCOUNTS -> "Journal Entry Proforma:\nDebit: Specific Asset / Expense A/c (At Cost / FV)\n  Credit: Bank / Equity / Reserve A/c\nWorking Note: Segregate Capital vs Revenue portions clearly."
                                        SubjectArchetype.TAX_AMENDMENTS_AND_COMPUTATION -> "Computation Proforma:\nGross Total Income (Under 5 Heads)\nLess: Deductions under Chapter VI-A\nTotal Income (Rounded off to nearest ₹10 u/s 288A)\nTax Liability: Default u/s 115BAC vs Normal Rates."
                                        SubjectArchetype.FORMULAS_AND_PROFORMA -> "Standard Formula:\nBEP (Units) = Fixed Cost / Contribution per Unit\nP/V Ratio = (Contribution / Sales) × 100\nMOS = Actual Sales - BEP Sales."
                                        SubjectArchetype.STATUTE_AND_CASE_LAW -> "Statutory Rule Structure:\n1. Statutory Threshold & Applicability Criteria\n2. Mandatory Approval (Ordinary vs Special Resolution)\n3. Filing & Form Requirements with Registrar."
                                        SubjectArchetype.AUDIT_PROCEDURES_AND_CLAUSES -> "Audit Assertion Format:\nCompleteness | Valuation | Allocation | Existence\nReporting Checklist under CARO, 2020 & SA 700 series."
                                        SubjectArchetype.STRATEGIC_FRAMEWORKS -> "Strategic Framework:\nThreat of New Entrants | Bargaining Power of Buyers\nInternal Capabilities vs External Industry Matrix."
                                    },
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 17.sp
                                )
                            }
                        }

                        // Student Confusions & Statutory Thresholds
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Amber500.copy(alpha = 0.08f),
                            border = BorderStroke(0.5.dp, Amber500.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "⚠️ Student Confusions & Threshold Pitfalls",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Amber500
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (activeTopic.commonExaminerPitfalls.isNotBlank())
                                        activeTopic.commonExaminerPitfalls
                                    else
                                        "Ensure exact compliance with monetary limits, effective dates of notification, and statutory exceptions.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 17.sp
                                )
                            }
                        }

                        // AI / Search Notes content if queried
                        if (state.displayedContent.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "🔍 Concept Doubt Solution",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    if (state.isLoading) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(60.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                        }
                                    } else {
                                        FormattedText(
                                            text = state.displayedContent,
                                            style = MaterialTheme.typography.bodySmall,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // TAB 3: ⏱️ PRACTICE Q-BANK
        // -------------------------------------------------------------
        if (activeSubTab == StudyAskSubTab.PRACTICE_QBANK) {
            item {
                CollapsiblePracticeQuestionsCard(
                    topic = activeTopic,
                    isExpanded = true,
                    onToggleExpand = {},
                    onWrittenSolved = { viewModel.incrementWrittenSolved(activeTopic.id) },
                    onReviewInTeacher = {
                        activeSubTab = StudyAskSubTab.TEACHER_PYQ_RADAR
                        coroutineScope.launch {
                            listState.animateScrollToItem(5) // Scroll to Section 2 (Blueprint)
                        }
                    },
                    hintText = viewModel.repository.getHintForTopic(activeTopic)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showBlueprintDialog) {
        StrategyBlueprintBottomSheet(
            paper = activePaper,
            onDismiss = { showBlueprintDialog = false }
        )
    }
}

@Composable
private fun TeacherSectionCard(
    sectionNumber: String,
    title: String,
    bodyMarkdown: String,
    accentColor: Color,
    expLevel: ExperienceLevel,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, accentColor.copy(alpha = 0.35f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = sectionNumber,
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            }

            if (expLevel == ExperienceLevel.NOVICE && sectionNumber == "1") {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Sky500.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "💡 Novice Advice: Read the analogy first to build intuitive mental imagery before statutory sections.",
                        fontSize = 9.sp,
                        color = Sky500,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            } else if (expLevel == ExperienceLevel.EXAM_READY && sectionNumber == "4") {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = SuccessGreen.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "🎯 Exam Ready Polish: Memorize mandatory keywords and ensure Working Notes are numbered.",
                        fontSize = 9.sp,
                        color = SuccessGreen,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            FormattedText(
                text = bodyMarkdown,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 19.sp
            )
        }
    }
}

@Composable
fun SpotTheTrapMiniCard(
    scenario: CatchLabScenario?,
    modifier: Modifier = Modifier
) {
    if (scenario == null) return
    var isRevealed by remember(scenario.id) { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Amber500.copy(alpha = 0.06f)),
        border = BorderStroke(0.5.dp, Amber500.copy(alpha = 0.4f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("spot_trap_mini_card")
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Amber500,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Spot the Trap (Mini-Drill)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Amber500.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${scenario.errorCount} Trap${if (scenario.errorCount > 1) "s" else ""}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Amber500,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = scenario.title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = scenario.caseSnippet,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(0.5.dp, Amber500),
                    modifier = Modifier
                        .height(26.dp)
                        .clickable { isRevealed = !isRevealed }
                        .testTag("toggle_trap_reveal_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = if (isRevealed) Icons.Default.KeyboardArrowUp else Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = Amber500,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (isRevealed) "Hide Correction" else "Reveal ICAI Correction",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Amber500
                        )
                    }
                }
            }

            if (isRevealed) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(0.5.dp, SuccessGreen.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Official ICAI Provision & Correction:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        FormattedText(
                            text = scenario.correctIcaiProvision,
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 17.sp
                        )

                        if (scenario.subtleErrors.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Subtle Traps in Case:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                            scenario.subtleErrors.forEach { err ->
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "• \"${err.wrongStatement}\" ➔ ${err.errorExplanation}",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CollapsiblePracticeQuestionsCard(
    topic: SyllabusTopic,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onWrittenSolved: () -> Unit,
    onReviewInTeacher: (() -> Unit)? = null,
    hintText: String? = null,
    modifier: Modifier = Modifier
) {
    val questions = remember(topic.id, topic.paper) {
        val paperQuestions = CuratedQuestionBank.getQuestionsForPaper(topic.paper)
        val chapLower = topic.chapter.lowercase()
        val topicLower = topic.topicName.lowercase()
        val matched = paperQuestions.filter { q ->
            q.chapterName.contains(chapLower, ignoreCase = true) ||
            q.questionText.contains(topicLower, ignoreCase = true) ||
            topic.fusedWithTopics.any { fused -> q.chapterName.contains(fused, ignoreCase = true) || q.questionText.contains(fused, ignoreCase = true) }
        }
        if (matched.isNotEmpty()) matched else paperQuestions
    }

    var activeQuestionIndex by remember(topic.id) { mutableIntStateOf(0) }
    val currentQuestion = questions.getOrNull(activeQuestionIndex) ?: questions.firstOrNull()

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, Amber500.copy(alpha = 0.45f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("collapsible_practice_questions_card")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() }
                    .testTag("practice_questions_header"),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Amber500.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = Amber500,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Practice High-Chance Exam Questions (Timed)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${questions.size} ICAI Exam Questions Available • Speed Timer",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = onToggleExpand,
                    modifier = Modifier
                        .size(30.dp)
                        .testTag("practice_questions_expand_toggle")
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = Amber500,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(10.dp))

                if (currentQuestion == null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "No questions found for this topic.",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    var userDraftInput by remember(currentQuestion.id) { mutableStateOf("") }
                    var isAnswerRevealed by remember(currentQuestion.id) { mutableStateOf(false) }
                    var isHintVisible by remember(currentQuestion.id) { mutableStateOf(false) }

                    var remainingSeconds by remember(currentQuestion.id) {
                        mutableIntStateOf(currentQuestion.minutesAllowed * 60)
                    }
                    var isTimerRunning by remember(currentQuestion.id) { mutableStateOf(false) }

                    LaunchedEffect(currentQuestion.id, topic.id) {
                        isTimerRunning = false
                        remainingSeconds = currentQuestion.minutesAllowed * 60
                    }

                    LaunchedEffect(isTimerRunning, remainingSeconds) {
                        if (isTimerRunning && remainingSeconds > 0) {
                            delay(1000L)
                            remainingSeconds--
                        } else if (remainingSeconds <= 0) {
                            isTimerRunning = false
                        }
                    }

                    // Question Switcher Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(Amber500.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${activeQuestionIndex + 1}",
                                    fontWeight = FontWeight.Bold,
                                    color = Amber500,
                                    fontSize = 11.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${currentQuestion.questionNumber} (${activeQuestionIndex + 1} of ${questions.size})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (questions.size > 1) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        if (activeQuestionIndex > 0) {
                                            activeQuestionIndex--
                                            isAnswerRevealed = false
                                            isTimerRunning = false
                                            isHintVisible = false
                                        }
                                    },
                                    enabled = activeQuestionIndex > 0,
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous", modifier = Modifier.size(16.dp))
                                }
                                IconButton(
                                    onClick = {
                                        if (activeQuestionIndex < questions.size - 1) {
                                            activeQuestionIndex++
                                            isAnswerRevealed = false
                                            isTimerRunning = false
                                            isHintVisible = false
                                        }
                                    },
                                    enabled = activeQuestionIndex < questions.size - 1,
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next", modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Badges & Actions Strip
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFEF4444).copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "${currentQuestion.marks} Marks",
                                color = Color(0xFFEF4444),
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Sky500.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "${currentQuestion.minutesAllowed} Mins",
                                color = Sky500,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }

                        // Hint Chip
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (isHintVisible) Amber500.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(0.5.dp, Amber500.copy(alpha = 0.45f)),
                            modifier = Modifier
                                .height(24.dp)
                                .clickable { isHintVisible = !isHintVisible }
                                .testTag("provision_hint_chip")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Amber500, modifier = Modifier.size(11.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text("Hint", color = Amber500, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            }
                        }

                        // Review Chip
                        if (onReviewInTeacher != null) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                                modifier = Modifier
                                    .height(24.dp)
                                    .clickable { onReviewInTeacher() }
                                    .testTag("review_in_teacher_chip")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(Icons.Default.Psychology, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(11.dp))
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text("Review", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                }
                            }
                        }
                    }

                    if (isHintVisible) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Amber500.copy(alpha = 0.08f),
                            border = BorderStroke(0.5.dp, Amber500.copy(alpha = 0.3f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("active_provision_hint_banner")
                        ) {
                            Text(
                                text = hintText ?: "💡 Look up key thresholds and statutory conditions.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Countdown Timer Banner
                    val minutesLeft = remainingSeconds / 60
                    val secondsLeft = remainingSeconds % 60
                    val timeString = String.format("%02d:%02d", minutesLeft, secondsLeft)
                    val isUrgent = remainingSeconds in 1..60

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUrgent) Color(0xFFFEF2F2) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        ),
                        border = BorderStroke(
                            0.5.dp,
                            if (isUrgent) Color(0xFFEF4444) else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("practice_timer_banner")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = if (isUrgent) Color(0xFFEF4444) else Amber500,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "Exam Speed Timer (${currentQuestion.minutesAllowed}m)",
                                        fontSize = 9.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = timeString,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isUrgent) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.testTag("practice_timer_text")
                                    )
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (isTimerRunning) Amber500 else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .height(26.dp)
                                        .clickable { isTimerRunning = !isTimerRunning }
                                        .testTag("practice_timer_toggle_button")
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = if (isTimerRunning) "Stop" else "Start",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = {
                                        isTimerRunning = false
                                        remainingSeconds = currentQuestion.minutesAllowed * 60
                                    },
                                    modifier = Modifier
                                        .size(26.dp)
                                        .testTag("practice_timer_reset_button")
                                ) {
                                    Icon(
                                        Icons.Default.Refresh,
                                        contentDescription = "Reset Timer",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Question Statement
                    Text(
                        text = currentQuestion.questionText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Answer Draft Box (Compact)
                    OutlinedTextField(
                        value = userDraftInput,
                        onValueChange = { userDraftInput = it },
                        placeholder = { Text("Draft solution / working notes outline...", fontSize = 11.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(95.dp)
                            .testTag("practice_draft_input"),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Reveal Answer Button (28.dp height)
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isAnswerRevealed) SuccessGreen else MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .clickable {
                                if (!isAnswerRevealed) {
                                    isAnswerRevealed = true
                                    onWrittenSolved()
                                }
                            }
                            .testTag("reveal_answer_traps_button")
                            .testTag("reveal_practice_answer_button")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = if (isAnswerRevealed) Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.FactCheck,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAnswerRevealed) "ICAI Answer & Traps Revealed ✓" else "Reveal ICAI Answer & Examiner Traps",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color.White
                            )
                        }
                    }

                    if (isAnswerRevealed) {
                        Spacer(modifier = Modifier.height(10.dp))

                        // Suggested Answer Card
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                            border = BorderStroke(0.5.dp, SuccessGreen.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("practice_official_answer_card")
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = SuccessGreen,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "Official ICAI Suggested Answer",
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessGreen,
                                        fontSize = 11.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                FormattedText(
                                    text = currentQuestion.officialAnswer,
                                    style = MaterialTheme.typography.bodySmall,
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Tips Card
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Sky500.copy(alpha = 0.07f)),
                            border = BorderStroke(0.5.dp, Sky500.copy(alpha = 0.3f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("practice_approach_tips_card")
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Lightbulb,
                                        contentDescription = null,
                                        tint = Sky500,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "ICAI Approach & Time-Saving Tips",
                                        fontWeight = FontWeight.Bold,
                                        color = Sky500,
                                        fontSize = 11.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                FormattedText(
                                    text = currentQuestion.approachTips,
                                    style = MaterialTheme.typography.bodySmall,
                                    lineHeight = 17.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Observations Card
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                            border = BorderStroke(0.5.dp, ErrorRed.copy(alpha = 0.3f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("practice_examiner_observations_card")
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = ErrorRed,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "Examiner Observations & Common Traps",
                                        fontWeight = FontWeight.Bold,
                                        color = ErrorRed,
                                        fontSize = 11.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                FormattedText(
                                    text = currentQuestion.examinerObservations,
                                    style = MaterialTheme.typography.bodySmall,
                                    lineHeight = 17.sp,
                                    color = Color(0xFF7F1D1D)
                                )
                            }
                        }

                        if (activeQuestionIndex < questions.size - 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(28.dp)
                                    .clickable {
                                        activeQuestionIndex++
                                        isAnswerRevealed = false
                                        isTimerRunning = false
                                        isHintVisible = false
                                    }
                                    .testTag("practice_next_question_button")
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text("Next Question →", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Backward-compatible helpers
@Composable
fun TimedPracticeQuestionSection(
    topic: SyllabusTopic,
    onWrittenSolved: () -> Unit,
    modifier: Modifier = Modifier
) {
    CollapsiblePracticeQuestionsCard(
        topic = topic,
        isExpanded = true,
        onToggleExpand = {},
        onWrittenSolved = onWrittenSolved,
        modifier = modifier
    )
}

@Composable
fun TopicStrategyProgressCard(
    topic: SyllabusTopic,
    progress: TopicProgressEntity?,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onIncrementWritten: () -> Unit,
    onIncrementOralRecall: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tierColor = Color(topic.priorityTier.badgeColorHex)
    val writtenSolved = progress?.writtenSolved ?: 0
    val mcqAttempted = progress?.mcqAttempted ?: 0
    val mcqCorrect = progress?.mcqCorrect ?: 0
    val oralRecalls = progress?.oralRecallsCompleted ?: 0

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = topic.topicName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onToggleBookmark, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) Amber500 else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${topic.chapter} • Target: W:${topic.writtenTarget} | M:${topic.mcqTarget} | O:${topic.oralRecallTarget}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = tierColor.copy(alpha = 0.12f),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Written", fontSize = 9.sp, color = tierColor)
                        Text("$writtenSolved/${topic.writtenTarget}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("MCQs", fontSize = 9.sp)
                        Text("$mcqCorrect/$mcqAttempted", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Recall", fontSize = 9.sp)
                        Text("$oralRecalls/${topic.oralRecallTarget}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
