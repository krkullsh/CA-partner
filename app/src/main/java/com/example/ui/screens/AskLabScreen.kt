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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.curriculum.CaSyllabusData
import com.example.data.curriculum.CuratedCatchLabScenarios
import com.example.data.curriculum.CuratedQuestionBank
import com.example.data.curriculum.PracticeQuestion
import com.example.data.curriculum.StrategyBlueprintReader
import com.example.data.local.TopicProgressEntity
import com.example.data.model.CatchLabScenario
import com.example.data.model.ExperienceLevel
import com.example.data.model.ExplanationDepth
import com.example.data.model.PaperCode
import com.example.data.model.PriorityTier
import com.example.data.model.StandardBlueprint
import com.example.data.model.SubjectArchetype
import com.example.data.model.SyllabusTopic
import com.example.data.model.TeacherSection
import com.example.ui.components.FormattedText
import com.example.ui.theme.Amber500
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.Sky500
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.CaLabViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class StudyAskSubTab(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    OFFLINE_TEACHER("👨‍🏫 Offline Teacher", Icons.Default.Psychology),
    THREE_TIER_NOTES("📖 3-Tier Notes", Icons.AutoMirrored.Filled.MenuBook),
    TIMED_QBANK("⏱️ Timed Q-Bank", Icons.Default.Timer)
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

    var activeSubTab by remember { mutableStateOf(StudyAskSubTab.OFFLINE_TEACHER) }
    var showBlueprintDialog by remember { mutableStateOf(false) }

    // LazyListState used to sync the horizontal outline scrub bar
    val listState = rememberLazyListState()

    // 4 Rich offline teacher sections
    val teacherSections = remember(activeTopic.id, activePaper) {
        viewModel.repository.getOfflineTeacherArticle(activeTopic, activePaper.archetype)
    }

    val outlinePills = listOf("1. Logic", "2. Blueprint", "3. Traps", "4. Exam Format")

    // Determine currently visible outline pill
    val firstVisible = listState.firstVisibleItemIndex
    val activePillIndex = when {
        firstVisible >= 6 -> 3
        firstVisible == 5 -> 2
        firstVisible == 4 -> 1
        else -> 0
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("ask_lab_screen"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ITEM 0: COMPACT 1-LINE TOPIC HEADER (With Archetype, Tier, Blueprint button)
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("compact_topic_header_card")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Row 1: Badges & Action Buttons
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
                            // Subject Archetype Badge
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = activePaper.archetype.displayName(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                )
                            }

                            // Priority Tier Pill
                            val tierColor = Color(activeTopic.priorityTier.badgeColorHex)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = tierColor.copy(alpha = 0.15f),
                                border = BorderStroke(0.8.dp, tierColor.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = activeTopic.priorityTier.name.replace("_", " "),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = tierColor,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }

                        // Strategy Blueprint & Bookmark buttons
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                                modifier = Modifier
                                    .clickable { showBlueprintDialog = true }
                                    .testTag("header_open_blueprint_button")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Blueprint",
                                        fontSize = 11.sp,
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
                                    .size(34.dp)
                                    .testTag("header_bookmark_button")
                            ) {
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = if (isBookmarked) Amber500 else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Row 2: Topic Title & Chapter
                    Text(
                        text = activeTopic.topicName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${activePaper.shortName} • ${activeTopic.chapter} • Exam Freq: ${activeTopic.pastPaperFrequency} (${activeTopic.averageMarks.toInt()}M)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Row 3: Compact Doubt / Concept Query Field
                    OutlinedTextField(
                        value = state.customQuery,
                        onValueChange = { viewModel.updateAskCustomQuery(it) },
                        placeholder = { Text("Ask doubt or search concept...", fontSize = 12.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("ask_search_field"),
                        shape = RoundedCornerShape(25.dp),
                        trailingIcon = {
                            IconButton(
                                onClick = { viewModel.searchOrGenerateExplanation() },
                                modifier = Modifier.testTag("ask_search_button")
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search",
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        )
                    )
                }
            }
        }

        // ITEM 1: 3-MODE SEGMENTED PILL [👨‍🏫 Offline Teacher] | [📖 3-Tier Notes] | [⏱️ Timed Q-Bank]
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("study_segmented_modes")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StudyAskSubTab.entries.forEach { tab ->
                        val isSelected = activeSubTab == tab
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
                            border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)) else null,
                            shadowElevation = if (isSelected) 2.dp else 0.dp,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { activeSubTab = tab }
                                .testTag("tab_${tab.name.lowercase()}")
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 9.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = null,
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(15.dp)
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
        // SUB-TAB 0: 👨‍🏫 OFFLINE TEACHER
        // -------------------------------------------------------------
        if (activeSubTab == StudyAskSubTab.OFFLINE_TEACHER) {
            // ITEM 2: READING OUTLINE SCRUB BAR (Sticky-like horizontal scrubber)
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reading_outline_scrub_bar")
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "READING OUTLINE",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            // ExperienceLevel pill indicator
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Sky500.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = experienceLevel.label.split(" ").firstOrNull() ?: "Standard",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Sky500,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
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
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isPillActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier
                                            .clickable {
                                                coroutineScope.launch {
                                                    // Offset: items 0, 1, 2 are headers, so sections start at index 3
                                                    val targetIndex = (3 + index).coerceAtMost(6)
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
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ITEM 3: SECTION 1: "1. Core Logic & Everyday Analogy"
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

            // ITEM 4: SECTION 2: "2. Official Blueprint Breakdown"
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

            // ITEM 5: SECTION 3: "3. Examiner Traps & Amendments" + Spot the Trap Mini-Card + Mark Lesson Read
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
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, Amber500.copy(alpha = 0.35f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("teacher_section_3")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Amber500.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "3",
                                    color = Amber500,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = sec3?.heading ?: "3. Examiner Traps & Amendments",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Amber500
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        FormattedText(
                            text = sec3?.bodyMarkdown ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Interactive "Spot the Trap" Mini-Card
                        SpotTheTrapMiniCard(scenario = matchingScenario)

                        Spacer(modifier = Modifier.height(12.dp))

                        // [Mark Lesson Read ✓] Button
                        val isTeacherRead = progressMap[activeTopic.id]?.teacherReadCompleted == 1
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isTeacherRead) SuccessGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.primaryContainer,
                            border = BorderStroke(
                                1.dp,
                                if (isTeacherRead) SuccessGreen else MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val newStatus = !isTeacherRead
                                    viewModel.markTeacherRead(activeTopic.id, newStatus)
                                    Toast.makeText(
                                        context,
                                        if (newStatus) "✓ Marked '${activeTopic.topicName}' as Read! Progress saved." else "Lesson marked unread.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .testTag("mark_lesson_read_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = if (isTeacherRead) Icons.Default.CheckCircle else Icons.Default.Bookmark,
                                    contentDescription = null,
                                    tint = if (isTeacherRead) SuccessGreen else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isTeacherRead) "Lesson Completed ✓ (Tap to Undo)" else "Mark Lesson Read ✓",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isTeacherRead) SuccessGreen else MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }

            // ITEM 6: SECTION 4: "4. Exam Answer Presentation"
            item {
                val sec4 = teacherSections.getOrNull(3)
                TeacherSectionCard(
                    sectionNumber = "4",
                    title = sec4?.heading ?: "4. Exam Answer Presentation",
                    bodyMarkdown = sec4?.bodyMarkdown ?: "",
                    accentColor = SuccessGreen,
                    expLevel = experienceLevel,
                    testTag = "teacher_section_4"
                )
            }
        }

        // -------------------------------------------------------------
        // SUB-TAB 1: 📖 3-TIER NOTES
        // -------------------------------------------------------------
        if (activeSubTab == StudyAskSubTab.THREE_TIER_NOTES) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("three_tier_notes_card")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Inline 1x Analogy | 2x Revision | 3x ICAI Format Switcher
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            ExplanationDepth.entries.forEach { depth ->
                                val isDepthSelected = state.selectedDepth == depth
                                val depthLabel = when (depth) {
                                    ExplanationDepth.DEPTH_1 -> "1x Analogy"
                                    ExplanationDepth.DEPTH_2 -> "2x Revision"
                                    ExplanationDepth.DEPTH_3 -> "3x ICAI Format"
                                }
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isDepthSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { viewModel.setAskDepth(depth) }
                                        .testTag("depth_tab_${depth.level}")
                                ) {
                                    Text(
                                        text = depthLabel,
                                        fontSize = 11.sp,
                                        fontWeight = if (isDepthSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isDepthSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Subtitle explanation
                        Text(
                            text = state.selectedDepth.subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (state.isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(28.dp))
                            }
                        } else {
                            if (state.selectedDepth == ExplanationDepth.DEPTH_3) {
                                CollapsibleIcaiAnswerStepsCard(markdown = state.displayedContent)
                            } else {
                                FormattedText(
                                    text = state.displayedContent,
                                    style = MaterialTheme.typography.bodyMedium,
                                    lineHeight = 22.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Copy Note action row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("CA-Lab Note", state.displayedContent))
                                    Toast.makeText(context, "Copied note to clipboard!", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("copy_explanation_button")
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Copy Note", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // SUB-TAB 2: ⏱️ TIMED Q-BANK
        // -------------------------------------------------------------
        if (activeSubTab == StudyAskSubTab.TIMED_QBANK) {
            item {
                CollapsiblePracticeQuestionsCard(
                    topic = activeTopic,
                    isExpanded = true,
                    onToggleExpand = {},
                    onWrittenSolved = { viewModel.incrementWrittenSolved(activeTopic.id) },
                    onReviewInTeacher = {
                        activeSubTab = StudyAskSubTab.OFFLINE_TEACHER
                        coroutineScope.launch {
                            listState.animateScrollToItem(4) // Scroll to Section 2 (Blueprint)
                        }
                    },
                    hintText = viewModel.repository.getHintForTopic(activeTopic)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showBlueprintDialog) {
        com.example.ui.components.StrategyBlueprintBottomSheet(
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.35f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = sectionNumber,
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            }

            // Experience level study badge
            if (expLevel == ExperienceLevel.NOVICE && sectionNumber == "1") {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Sky500.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "💡 Novice Advice: Read the analogy first to build intuitive mental imagery before statutory sections.",
                        fontSize = 10.sp,
                        color = Sky500,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            } else if (expLevel == ExperienceLevel.EXAM_READY && sectionNumber == "4") {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = SuccessGreen.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "🎯 Exam Ready Polish: Memorize mandatory keywords and ensure Working Notes are numbered.",
                        fontSize = 10.sp,
                        color = SuccessGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            FormattedText(
                text = bodyMarkdown,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 20.sp
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
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Amber500.copy(alpha = 0.08f)),
        border = BorderStroke(1.dp, Amber500.copy(alpha = 0.45f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("spot_trap_mini_card")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
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
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "🎯 Spot the Trap (Mini-Drill)",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Amber500.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${scenario.errorCount} Trap${if (scenario.errorCount > 1) "s" else ""}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Amber500,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = scenario.title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = scenario.caseSnippet,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { isRevealed = !isRevealed },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Amber500),
                    modifier = Modifier.testTag("toggle_trap_reveal_button")
                ) {
                    Icon(
                        imageVector = if (isRevealed) Icons.Default.KeyboardArrowUp else Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = Amber500,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isRevealed) "Hide ICAI Correction" else "Reveal ICAI Correction",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Amber500
                    )
                }
            }

            if (isRevealed) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Official ICAI Provision & Correction:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        FormattedText(
                            text = scenario.correctIcaiProvision,
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 18.sp
                        )

                        if (scenario.subtleErrors.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Subtle Traps in Case:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                            scenario.subtleErrors.forEach { err ->
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "• \"${err.wrongStatement}\" ➔ ${err.errorExplanation}",
                                    fontSize = 11.sp,
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
fun CollapsibleIcaiAnswerStepsCard(
    markdown: String,
    modifier: Modifier = Modifier
) {
    var step1Expanded by remember { mutableStateOf(true) }
    var step2Expanded by remember { mutableStateOf(true) }
    var step3Expanded by remember { mutableStateOf(true) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Step 1: Applicable Provision
        CollapsibleStepCard(
            stepNumber = "Step 1",
            stepTitle = "Applicable Provision of Law / Standard",
            accentColor = Sky500,
            isExpanded = step1Expanded,
            onToggle = { step1Expanded = !step1Expanded }
        ) {
            FormattedText(
                text = markdown.substringBefore("**2. ").substringBefore("**Step 2:").ifBlank { markdown.take(300) },
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 19.sp
            )
        }

        // Step 2: Working Notes / Facts
        CollapsibleStepCard(
            stepNumber = "Step 2",
            stepTitle = "Working Notes & Factual Analysis",
            accentColor = MaterialTheme.colorScheme.primary,
            isExpanded = step2Expanded,
            onToggle = { step2Expanded = !step2Expanded }
        ) {
            val step2Text = markdown.substringAfter("**2. ", "")
                .substringAfter("**Step 2:", "")
                .substringBefore("**3. ")
                .substringBefore("**Step 3:")
            FormattedText(
                text = step2Text.ifBlank { "Apply statutory conditions, perform detailed workings, and structure notes." },
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 19.sp
            )
        }

        // Step 3: Conclusion & Final Opinion
        CollapsibleStepCard(
            stepNumber = "Step 3",
            stepTitle = "Conclusion & Examiner Guidance",
            accentColor = SuccessGreen,
            isExpanded = step3Expanded,
            onToggle = { step3Expanded = !step3Expanded }
        ) {
            val step3Text = markdown.substringAfter("**3. ", "").substringAfter("**Step 3:", "")
            FormattedText(
                text = step3Text.ifBlank { "State conclusion clearly with technical keywords and statutory limits." },
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 19.sp
            )
        }
    }
}

@Composable
private fun CollapsibleStepCard(
    stepNumber: String,
    stepTitle: String,
    accentColor: Color,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = accentColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = stepNumber,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stepTitle,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    content()
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.2.dp, Amber500.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("collapsible_practice_questions_card")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
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
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Amber500.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = Amber500,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Practice High-Chance Exam Questions (Timed)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${questions.size} ICAI Exam Questions Available • Speed Timer",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = onToggleExpand,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("practice_questions_expand_toggle")
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = Amber500
                    )
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))

                if (currentQuestion == null) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "No questions found in CuratedQuestionBank for this topic.",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    var userDraftInput by remember(currentQuestion.id) { mutableStateOf("") }
                    var isAnswerRevealed by remember(currentQuestion.id) { mutableStateOf(false) }
                    var isHintVisible by remember(currentQuestion.id) { mutableStateOf(false) }

                    // Countdown Timer
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
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(Amber500.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${activeQuestionIndex + 1}",
                                    fontWeight = FontWeight.Bold,
                                    color = Amber500,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${currentQuestion.questionNumber} (${activeQuestionIndex + 1} of ${questions.size})",
                                style = MaterialTheme.typography.titleSmall,
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
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
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
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Question Badges + [💡 Provision Hint] Chip + [👨‍🏫 Review in Teacher] Chip
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFEF4444).copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "${currentQuestion.marks} Marks",
                                color = Color(0xFFEF4444),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Sky500.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "${currentQuestion.minutesAllowed} Mins",
                                color = Sky500,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }

                        // [💡 Provision Hint] Chip
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isHintVisible) Amber500.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(1.dp, Amber500.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .clickable { isHintVisible = !isHintVisible }
                                .testTag("provision_hint_chip")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            ) {
                                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Amber500, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text("💡 Hint", color = Amber500, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            }
                        }

                        // [👨‍🏫 Review in Teacher] Chip
                        if (onReviewInTeacher != null) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                                modifier = Modifier
                                    .clickable { onReviewInTeacher() }
                                    .testTag("review_in_teacher_chip")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                ) {
                                    Icon(Icons.Default.Psychology, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text("👨‍🏫 Review", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                }
                            }
                        }
                    }

                    // Expandable Hint Banner
                    if (isHintVisible) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Amber500.copy(alpha = 0.1f),
                            border = BorderStroke(1.dp, Amber500.copy(alpha = 0.35f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("active_provision_hint_banner")
                        ) {
                            Text(
                                text = hintText ?: "💡 Look up key thresholds and Section criteria.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Countdown Timer Controls Banner
                    val minutesLeft = remainingSeconds / 60
                    val secondsLeft = remainingSeconds % 60
                    val timeString = String.format("%02d:%02d", minutesLeft, secondsLeft)
                    val isUrgent = remainingSeconds in 1..60

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUrgent) Color(0xFFFEF2F2) else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isUrgent) Color(0xFFEF4444) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("practice_timer_banner")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = if (isUrgent) Color(0xFFEF4444) else Amber500,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Exam Speed Timer (${currentQuestion.minutesAllowed} min target)",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = timeString,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isUrgent) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.testTag("practice_timer_text")
                                    )
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { isTimerRunning = !isTimerRunning },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isTimerRunning) Amber500 else MaterialTheme.colorScheme.primary
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("practice_timer_toggle_button")
                                ) {
                                    Icon(
                                        imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isTimerRunning) "Stop" else "Start",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        isTimerRunning = false
                                        remainingSeconds = currentQuestion.minutesAllowed * 60
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .testTag("practice_timer_reset_button")
                                ) {
                                    Icon(
                                        Icons.Default.Refresh,
                                        contentDescription = "Reset Timer",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Question Statement
                    Text(
                        text = currentQuestion.questionText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Answer Draft Input Box
                    OutlinedTextField(
                        value = userDraftInput,
                        onValueChange = { userDraftInput = it },
                        label = { Text("Draft your solution / working notes here...", fontSize = 12.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .testTag("practice_draft_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Reveal Answer Button
                    Button(
                        onClick = {
                            if (!isAnswerRevealed) {
                                isAnswerRevealed = true
                                onWrittenSolved()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reveal_answer_traps_button")
                            .testTag("reveal_practice_answer_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isAnswerRevealed) SuccessGreen else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = if (isAnswerRevealed) Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.FactCheck,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAnswerRevealed) "ICAI Answer & Traps Revealed ✓" else "Reveal ICAI Answer & Examiner Traps",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (isAnswerRevealed) {
                        Spacer(modifier = Modifier.height(14.dp))

                        // Suggested Answer Card
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("practice_official_answer_card")
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = SuccessGreen,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        "Official ICAI Suggested Answer",
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessGreen,
                                        fontSize = 13.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                FormattedText(
                                    text = currentQuestion.officialAnswer,
                                    style = MaterialTheme.typography.bodySmall,
                                    lineHeight = 20.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Tips Card
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Sky500.copy(alpha = 0.08f)),
                            border = BorderStroke(1.dp, Sky500.copy(alpha = 0.35f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("practice_approach_tips_card")
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Lightbulb,
                                        contentDescription = null,
                                        tint = Sky500,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        "ICAI Approach & Time-Saving Tips",
                                        fontWeight = FontWeight.Bold,
                                        color = Sky500,
                                        fontSize = 13.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                FormattedText(
                                    text = currentQuestion.approachTips,
                                    style = MaterialTheme.typography.bodySmall,
                                    lineHeight = 19.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Observations Card
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                            border = BorderStroke(1.dp, ErrorRed.copy(alpha = 0.35f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("practice_examiner_observations_card")
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = ErrorRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        "Examiner Observations & Common Traps",
                                        fontWeight = FontWeight.Bold,
                                        color = ErrorRed,
                                        fontSize = 13.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                FormattedText(
                                    text = currentQuestion.examinerObservations,
                                    style = MaterialTheme.typography.bodySmall,
                                    lineHeight = 19.sp,
                                    color = Color(0xFF7F1D1D)
                                )
                            }
                        }

                        if (activeQuestionIndex < questions.size - 1) {
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(
                                onClick = {
                                    activeQuestionIndex++
                                    isAnswerRevealed = false
                                    isTimerRunning = false
                                    isHintVisible = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("practice_next_question_button")
                            ) {
                                Text("Next Question →")
                            }
                        }
                    }
                }
            }
        }
    }
}

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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = topic.topicName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onToggleBookmark) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) Amber500 else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${topic.chapter} • Target: W:${topic.writtenTarget} | M:${topic.mcqTarget} | O:${topic.oralRecallTarget}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = tierColor.copy(alpha = 0.15f),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Written", fontSize = 10.sp, color = tierColor)
                        Text("$writtenSolved/${topic.writtenTarget}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("MCQs", fontSize = 10.sp)
                        Text("$mcqCorrect/$mcqAttempted", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Recall", fontSize = 10.sp)
                        Text("$oralRecalls/${topic.oralRecallTarget}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun StandardSummaryBlueprintSection(
    topicName: String,
    blueprint: StandardBlueprint,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("standard_summary_blueprint")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Blueprint: $topicName",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            BlueprintPillarCard(
                stepNumber = "1",
                title = "Scope & Applicability",
                content = blueprint.scope,
                accentColor = Sky500,
                icon = Icons.AutoMirrored.Filled.MenuBook
            )
            Spacer(modifier = Modifier.height(6.dp))
            BlueprintPillarCard(
                stepNumber = "2",
                title = "Recognition & Measurement",
                content = blueprint.recognitionAndMeasurement,
                accentColor = MaterialTheme.colorScheme.primary,
                icon = Icons.Default.Psychology
            )
            Spacer(modifier = Modifier.height(6.dp))
            BlueprintPillarCard(
                stepNumber = "3",
                title = "Presentation & Disclosures",
                content = blueprint.disclosure,
                accentColor = SuccessGreen,
                icon = Icons.AutoMirrored.Filled.FactCheck
            )
        }
    }
}

@Composable
private fun BlueprintPillarCard(
    stepNumber: String,
    title: String,
    content: String,
    accentColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.35f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stepNumber,
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            FormattedText(
                text = content,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun StrategyBlueprintBanner(
    paper: PaperCode,
    onOpenBlueprint: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOpenBlueprint() }
            .testTag("strategy_blueprint_banner")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "${paper.shortName} Master Strategy Blueprint",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "12-Attempt Trend Analysis • Priority Matrix • Traps",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = "Read Full Blueprint →",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
fun StrategyBlueprintDialog(
    paper: PaperCode,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val blueprintMarkdown = remember(paper) {
        StrategyBlueprintReader.getBlueprintMarkdown(context, paper)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .testTag("strategy_blueprint_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = paper.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Faculty Master Strategy Blueprint (12-Attempt Data)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_blueprint_dialog")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    item {
                        FormattedText(
                            text = blueprintMarkdown,
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close Blueprint")
                }
            }
        }
    }
}
