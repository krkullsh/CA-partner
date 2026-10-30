package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.curriculum.CaSyllabusData
import com.example.data.local.ErrorLogEntity
import com.example.data.local.TopicProgressEntity
import com.example.data.model.LabMode
import com.example.data.model.LabVerdict
import com.example.data.model.McqQuestion
import com.example.data.model.PaperCode
import com.example.data.model.SyllabusTopic
import com.example.ui.theme.Amber500
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.Sky500
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.CaLabViewModel
import com.example.ui.viewmodel.VerdictsDashboardState

@Composable
fun VerdictsHistoryScreen(viewModel: CaLabViewModel) {
    val dashboard by viewModel.verdictsDashboard.collectAsState()
    val errorLogs by viewModel.errorLogs.collectAsState()
    val progressMap by viewModel.topicProgressMap.collectAsState()

    // 0 = Ledger, 1 = MCQ Test, 2 = Weakness Radar & Errors, 3 = Verdicts
    var activeSubTab by remember { mutableIntStateOf(0) }

    // Shared Paper & View Filters (Controlled via unified [Filter ▾] dropdown)
    var selectedPaper by remember { mutableStateOf<PaperCode?>(null) }
    var showPinnedOnly by remember { mutableStateOf(false) }
    var filterMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadVerdictsDashboard()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .testTag("verdicts_history_screen")
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        // SLEEK SINGLE-LINE 28.DP SUB-TAB SEGMENTED BAR + 26.DP FILTER DROPDOWN
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 4-Tab Segmented Pill Bar
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                modifier = Modifier
                    .weight(1f)
                    .height(28.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val tabs = listOf(
                        "Ledger" to 0,
                        "MCQ Test" to 1,
                        "Weakness (${errorLogs.size})" to 2,
                        "Verdicts" to 3
                    )

                    tabs.forEach { (label, index) ->
                        val isSelected = activeSubTab == index
                        val testTagString = when (index) {
                            0 -> "subtab_syllabus_ledger"
                            1 -> "subtab_mcq_test"
                            2 -> "subtab_weakness_radar"
                            else -> "subtab_verdicts_history"
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .height(24.dp)
                                .clickable { activeSubTab = index }
                                .testTag(testTagString)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 10.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Unified [Filter ▾] dropdown button (26.dp height)
            Box {
                val hasActiveFilter = selectedPaper != null || showPinnedOnly
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (hasActiveFilter) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                    border = BorderStroke(
                        0.5.dp,
                        if (hasActiveFilter) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier
                        .height(26.dp)
                        .clickable { filterMenuExpanded = true }
                        .testTag("ledger_filter_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Filter",
                            modifier = Modifier.size(11.dp),
                            tint = if (hasActiveFilter) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (selectedPaper != null) selectedPaper!!.shortName else if (showPinnedOnly) "Pinned" else "Filter",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (hasActiveFilter) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = if (hasActiveFilter) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Unified Dropdown Menu for Paper and Chapter Filter
                DropdownMenu(
                    expanded = filterMenuExpanded,
                    onDismissRequest = { filterMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "FILTER BY PAPER",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        onClick = {},
                        enabled = false
                    )

                    DropdownMenuItem(
                        text = { Text("All 6 Papers", fontSize = 11.sp, fontWeight = if (selectedPaper == null) FontWeight.Bold else FontWeight.Normal) },
                        leadingIcon = {
                            if (selectedPaper == null) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        onClick = {
                            selectedPaper = null
                            filterMenuExpanded = false
                        }
                    )

                    PaperCode.entries.forEach { paper ->
                        val isSelected = selectedPaper == paper
                        DropdownMenuItem(
                            text = { Text("${paper.code}: ${paper.shortName}", fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            leadingIcon = {
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                                }
                            },
                            onClick = {
                                selectedPaper = paper
                                viewModel.selectMcqTestPaper(paper)
                                filterMenuExpanded = false
                            }
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "CHAPTER VIEW",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        onClick = {},
                        enabled = false
                    )

                    DropdownMenuItem(
                        text = { Text("All Chapters", fontSize = 11.sp, fontWeight = if (!showPinnedOnly) FontWeight.Bold else FontWeight.Normal) },
                        leadingIcon = {
                            if (!showPinnedOnly) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        onClick = {
                            showPinnedOnly = false
                            filterMenuExpanded = false
                        },
                        modifier = Modifier.testTag("ledger_filter_all")
                    )

                    DropdownMenuItem(
                        text = { Text("★ Pinned / Weak Only", fontSize = 11.sp, fontWeight = if (showPinnedOnly) FontWeight.Bold else FontWeight.Normal) },
                        leadingIcon = {
                            if (showPinnedOnly) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp), tint = Amber500)
                            }
                        },
                        onClick = {
                            showPinnedOnly = true
                            filterMenuExpanded = false
                        },
                        modifier = Modifier.testTag("ledger_filter_pinned")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (activeSubTab) {
            0 -> SyllabusLedgerSection(
                viewModel = viewModel,
                progressMap = progressMap,
                verdicts = dashboard.verdicts,
                selectedPaper = selectedPaper,
                showPinnedOnly = showPinnedOnly
            )
            1 -> McqTestSection(
                viewModel = viewModel,
                selectedPaper = selectedPaper
            )
            2 -> RadarAndErrorsSection(
                viewModel = viewModel,
                progressMap = progressMap,
                errorLogs = errorLogs,
                selectedPaper = selectedPaper
            )
            3 -> PastVerdictsSection(
                dashboard = dashboard,
                onRefreshVerdicts = { viewModel.loadVerdictsDashboard() },
                onStartTeachLab = { viewModel.setMode(LabMode.TEACH_LAB) }
            )
        }
    }
}

// =========================================================================
// SUB-TAB 0: MODERN CALM SYLLABUS LEDGER
// =========================================================================
@Composable
fun SyllabusLedgerSection(
    viewModel: CaLabViewModel,
    progressMap: Map<String, TopicProgressEntity>,
    verdicts: List<LabVerdict>,
    selectedPaper: PaperCode?,
    showPinnedOnly: Boolean
) {
    val rawTopics = remember(selectedPaper) {
        if (selectedPaper != null) CaSyllabusData.getTopicsForPaper(selectedPaper)
        else CaSyllabusData.topics
    }

    // Always sort cleanly by Official ICAI Chapter order: Paper -> Chapter Number -> Topic Name
    val sortedTopics = remember(rawTopics) {
        rawTopics.sortedWith(
            compareBy<SyllabusTopic> { it.paper.ordinal }
                .thenBy { it.icaiChapterNumber }
                .thenBy { it.topicName }
        )
    }

    // Filter topics: All vs Pinned/Weak only
    val filteredTopics = remember(sortedTopics, showPinnedOnly, progressMap) {
        if (showPinnedOnly) {
            sortedTopics.filter { topic ->
                val p = progressMap[topic.id]
                (p?.swipeDrillStatus == 1) || (p?.teacherReadCompleted ?: 0) == 0 ||
                    ((p?.mcqAttempted ?: 0) > 0 && (p?.mcqCorrect ?: 0) * 2 <= (p?.mcqAttempted ?: 1))
            }
        } else {
            sortedTopics
        }
    }

    // 5-Min Quick Revision pinned topics (horizontal compact strip)
    val revisionTopics = remember(progressMap, selectedPaper) {
        val pool = if (selectedPaper != null) CaSyllabusData.getTopicsForPaper(selectedPaper) else CaSyllabusData.topics
        val pinned = pool.filter { progressMap[it.id]?.swipeDrillStatus == 1 }
        val unread = pool.filter { (progressMap[it.id]?.teacherReadCompleted ?: 0) == 0 }
        (pinned + unread).distinctBy { it.id }.take(6)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // COMPACT 5-MIN QUICK REVISION LAZYROW (height = 56.dp, width = 210.dp)
        if (revisionTopics.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("low_energy_revision_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 2.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = Amber500,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "5-Min Quick Revision (${revisionTopics.size})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Amber500
                            )
                        }
                        Text(
                            text = "Swipe Drill & Weak Rules",
                            fontSize = 9.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(revisionTopics, key = { it.id }) { topic ->
                            val isPinned = progressMap[topic.id]?.swipeDrillStatus == 1
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(
                                    0.5.dp,
                                    if (isPinned) Amber500.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                ),
                                modifier = Modifier
                                    .width(210.dp)
                                    .height(56.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Row 1: Chapter and Name
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Ch ${topic.icaiChapterNumber} • ${topic.topicName}",
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (isPinned) {
                                            Text(
                                                text = "🔁",
                                                fontSize = 9.sp,
                                                modifier = Modifier.padding(start = 2.dp)
                                            )
                                        }
                                    }

                                    // Row 2: Paper Tag + Micro Action Pills (22.dp height)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
                                        ) {
                                            Text(
                                                text = topic.paper.shortName,
                                                fontSize = 8.5.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }

                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            // 1-Tap Revise Pill (22.dp)
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = MaterialTheme.colorScheme.primaryContainer,
                                                modifier = Modifier
                                                    .height(22.dp)
                                                    .clickable {
                                                        viewModel.setActivePaper(topic.paper)
                                                        viewModel.setActiveTopic(topic)
                                                        viewModel.setMode(LabMode.ASK_LAB)
                                                    }
                                            ) {
                                                Box(
                                                    modifier = Modifier.padding(horizontal = 6.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = "Revise",
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                                    )
                                                }
                                            }

                                            // 1-Tap Drill Pill (22.dp)
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = Amber500.copy(alpha = 0.2f),
                                                modifier = Modifier
                                                    .height(22.dp)
                                                    .clickable {
                                                        viewModel.setActivePaper(topic.paper)
                                                        viewModel.setActiveTopic(topic)
                                                        viewModel.setMode(LabMode.SPOT_DIFFERENCE)
                                                    }
                                            ) {
                                                Box(
                                                    modifier = Modifier.padding(horizontal = 6.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = "Drill 🔥",
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Amber500
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // COMPACT CHAPTER LEDGER CARDS LIST (Official ICAI Order + Marks Weightage)
        items(filteredTopics, key = { it.id }) { topic ->
            val progress = progressMap[topic.id]
            val hasVerdict = verdicts.any { it.topicName == topic.topicName }

            ChapterLedgerCard(
                topic = topic,
                progress = progress,
                hasVerdict = hasVerdict,
                onToggleRead = {
                    val currentRead = progress?.teacherReadCompleted == 1
                    viewModel.markTeacherRead(topic.id, !currentRead)
                },
                onToggleSwipe = {
                    val currentStatus = progress?.swipeDrillStatus ?: 0
                    val nextStatus = when (currentStatus) {
                        0 -> 1 // Need Practice
                        1 -> 2 // Mastered
                        else -> 0 // Reset
                    }
                    viewModel.recordSwipeDrillStatus(topic.id, nextStatus)
                },
                onJumpToAsk = {
                    viewModel.setActivePaper(topic.paper)
                    viewModel.setActiveTopic(topic)
                    viewModel.setMode(LabMode.ASK_LAB)
                },
                onJumpToTeach = {
                    viewModel.setActivePaper(topic.paper)
                    viewModel.setActiveTopic(topic)
                    viewModel.setMode(LabMode.TEACH_LAB)
                }
            )
        }

        if (filteredTopics.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (showPinnedOnly) "No pinned or weak chapters found. Great job!" else "No chapters found.",
                        fontSize = 11.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(14.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// Compact 1-Card Row for Syllabus Chapter
@Composable
fun ChapterLedgerCard(
    topic: SyllabusTopic,
    progress: TopicProgressEntity?,
    hasVerdict: Boolean,
    onToggleRead: () -> Unit,
    onToggleSwipe: () -> Unit,
    onJumpToAsk: () -> Unit,
    onJumpToTeach: () -> Unit
) {
    val isRead = progress?.teacherReadCompleted == 1
    val isRecalled = (progress?.mcqAttempted ?: 0) > 0
    val drillStatus = progress?.swipeDrillStatus ?: 0 // 0=Unseen, 1=Need Practice, 2=Mastered

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(
            0.5.dp,
            if (drillStatus == 1) Amber500.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ledger_card_${topic.id}")
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            // ROW 1: Chapter Number & Name (Bold) + Marks Weightage on Right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Ch ${topic.icaiChapterNumber} • ${topic.topicName}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Official Marks Weightage Badge
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f)
                ) {
                    Text(
                        text = "${topic.averageMarks.toInt()}M",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // ROW 2: 4 Completion Dots on Left + Two Compact 24.dp Action Pills on Right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 4 Completion Indicators (Read • Recalled • Drill • Verdict)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. Read Indicator
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (isRead) SuccessGreen.copy(alpha = 0.15f) else Color.Transparent,
                        border = BorderStroke(
                            0.5.dp,
                            if (isRead) SuccessGreen else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier
                            .clickable { onToggleRead() }
                            .testTag("segment_read_${topic.id}")
                    ) {
                        Text(
                            text = if (isRead) "✓ Read" else "○ Read",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isRead) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }

                    // 2. Recalled Indicator (MCQ)
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (isRecalled) SuccessGreen.copy(alpha = 0.15f) else Color.Transparent,
                        border = BorderStroke(
                            0.5.dp,
                            if (isRecalled) SuccessGreen else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Text(
                            text = if (isRecalled) "✓ MCQ" else "○ MCQ",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isRecalled) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }

                    // 3. Drill Indicator (Swipe Drill)
                    val (drillLabel, drillColor, drillBg) = when (drillStatus) {
                        2 -> Triple("✓ Drill", SuccessGreen, SuccessGreen.copy(alpha = 0.15f))
                        1 -> Triple("🔁 Drill", Amber500, Amber500.copy(alpha = 0.2f))
                        else -> Triple("○ Drill", MaterialTheme.colorScheme.onSurfaceVariant, Color.Transparent)
                    }
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = drillBg,
                        border = BorderStroke(
                            0.5.dp,
                            if (drillStatus != 0) drillColor else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier
                            .clickable { onToggleSwipe() }
                            .testTag("segment_drill_${topic.id}")
                    ) {
                        Text(
                            text = drillLabel,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = drillColor,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }

                    // 4. Verdict Indicator
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (hasVerdict) SuccessGreen.copy(alpha = 0.15f) else Color.Transparent,
                        border = BorderStroke(
                            0.5.dp,
                            if (hasVerdict) SuccessGreen else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Text(
                            text = if (hasVerdict) "✓ Exam" else "○ Exam",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (hasVerdict) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }

                // Two Compact 24.dp Action Pills: [Study] and [Teach]
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .height(24.dp)
                            .clickable { onJumpToAsk() }
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Study 💡",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .height(24.dp)
                            .clickable { onJumpToTeach() }
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Teach 🎤",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// SUB-TAB 1: COMPACT DIAGNOSTIC MCQ TEST SECTION (Dark/Light Safe)
// =========================================================================
@Composable
fun McqTestSection(
    viewModel: CaLabViewModel,
    selectedPaper: PaperCode?
) {
    val mcqState by viewModel.mcqTestState.collectAsState()

    // Sync selectedPaper with McqTest if changed externally
    LaunchedEffect(selectedPaper) {
        if (selectedPaper != null && mcqState.selectedPaper != selectedPaper) {
            viewModel.selectMcqTestPaper(selectedPaper)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Compact Diagnostic Test Banner Header (without redundant multi-row chips)
        item {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${mcqState.selectedPaper.title} • Diagnostic Test",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Wrong answers automatically save to your Active Error Log.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = mcqState.selectedPaper.shortName,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // Test Submission Results Banner if submitted
        if (mcqState.isSubmitted) {
            item {
                val marksScored = mcqState.scoreCorrect * 2
                val totalMarks = if (mcqState.totalQuestions > 0) mcqState.totalQuestions * 2 else mcqState.questions.size * 2
                val percent = if (totalMarks > 0) (marksScored * 100 / totalMarks) else 0
                val bannerColor = if (percent >= 70) SuccessGreen else if (percent >= 40) Amber500 else ErrorRed

                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = bannerColor.copy(alpha = 0.12f)),
                    border = BorderStroke(1.dp, bannerColor.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Diagnostic Result: $marksScored / $totalMarks Marks ($percent%)",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp,
                            color = bannerColor
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (percent >= 70) "Strong statutory grasp! Missed subtleties are saved to your Active Error Log." else "Review the detailed ICAI working notes below. Missed rules saved for revision.",
                            fontSize = 10.5.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Question items list (No Tier 1/2 badges, Clean Dark Mode Surfaces)
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .testTag("submit_mcq_test_button"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Submit Diagnostic Test (${mcqState.userAnswers.size} / ${mcqState.questions.size} Answered) →",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
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
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("mcq_question_card_$qNumber")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header Row: Question # & 2 Marks Badge (No Tier Badges)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$qNumber",
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.5.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = question.topicName,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
                ) {
                    Text(
                        text = "2 Marks",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Integrated Case Scenario memo box if present
            question.caseScenarioText?.let { caseText ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null, tint = Sky500, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("📖 Integrated Case Scenario Memo", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Sky500)
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(caseText, fontSize = 10.5.sp, lineHeight = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Question Stem
            Text(
                text = question.questionStem,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 19.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 4 Options A, B, C, D
            question.options.forEachIndexed { optIndex, optionText ->
                val isChosen = selectedOption == optIndex
                val isCorrect = optIndex == question.correctOptionIndex

                val optionBg = when {
                    isSubmitted && isCorrect -> SuccessGreen.copy(alpha = 0.15f)
                    isSubmitted && isChosen && !isCorrect -> ErrorRed.copy(alpha = 0.15f)
                    isChosen -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                }

                val optionBorder = when {
                    isSubmitted && isCorrect -> BorderStroke(1.2.dp, SuccessGreen)
                    isSubmitted && isChosen && !isCorrect -> BorderStroke(1.2.dp, ErrorRed)
                    isChosen -> BorderStroke(1.2.dp, MaterialTheme.colorScheme.primary)
                    else -> BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = optionBg,
                    border = optionBorder,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clickable(enabled = !isSubmitted) { onSelectOption(optIndex) }
                        .testTag("mcq_${qNumber}_option_$optIndex")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = optionText,
                            fontSize = 11.5.sp,
                            fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSubmitted) {
                            if (isCorrect) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = SuccessGreen, modifier = Modifier.size(15.dp))
                            } else if (isChosen) {
                                Icon(Icons.Default.Warning, contentDescription = "Wrong", tint = ErrorRed, modifier = Modifier.size(15.dp))
                            }
                        }
                    }
                }
            }

            // Explanation Card after submission
            if (isSubmitted) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Sky500.copy(alpha = 0.08f),
                    border = BorderStroke(0.5.dp, Sky500.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Sky500, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("💡 Working Note & ICAI Reference", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Sky500)
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(question.explanation, fontSize = 10.5.sp, lineHeight = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(3.dp))
                        Text("🏛️ Ref: ${question.icaiProvisionRef}", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = Sky500)
                    }
                }
            }
        }
    }
}

// =========================================================================
// SUB-TAB 2: WEAKNESS RADAR & ACTIVE ERROR LOG (100% Theme Adaptive)
// =========================================================================
@Composable
fun RadarAndErrorsSection(
    viewModel: CaLabViewModel,
    progressMap: Map<String, TopicProgressEntity>,
    errorLogs: List<ErrorLogEntity>,
    selectedPaper: PaperCode?
) {
    var viewMode by remember { mutableIntStateOf(0) } // 0 = Weakness Radar, 1 = Active Error Log

    val allTopics = remember(selectedPaper) {
        if (selectedPaper != null) CaSyllabusData.getTopicsForPaper(selectedPaper)
        else CaSyllabusData.topics
    }

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
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Sleek 24.dp Switcher between Weakness Radar and Active Error Log
        item {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (viewMode == 0) MaterialTheme.colorScheme.primary else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .height(24.dp)
                            .clickable { viewMode = 0 }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "🎯 Weakness Radar",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (viewMode == 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (viewMode == 1) ErrorRed else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .height(24.dp)
                            .clickable { viewMode = 1 }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "⚡ Active Errors (${errorLogs.size})",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (viewMode == 1) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        if (viewMode == 0) {
            // WEAKNESS RADAR (Adaptive Dark / Light Colors)
            itemsIndexed(rankedTopics, key = { _, item -> item.first.id }) { index, (topic, accuracy, _) ->
                val isCritical = accuracy < 60.0

                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCritical) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        0.5.dp,
                        if (isCritical) ErrorRed.copy(alpha = 0.7f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("radar_row_${topic.id}")
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (index < 3) ErrorRed.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    Text(
                                        text = "#${index + 1}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 10.sp,
                                        color = if (index < 3) ErrorRed else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "Ch ${topic.icaiChapterNumber} • ${topic.topicName}",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${topic.paper.shortName} • ${topic.averageMarks.toInt()}M Weight",
                                        fontSize = 9.5.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = if (accuracy < 65.0) ErrorRed.copy(alpha = 0.15f) else SuccessGreen.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "${accuracy.toInt()}% Acc",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = if (accuracy < 65.0) ErrorRed else SuccessGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        if (isCritical) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "HIGH EXAM RISK • REVISE FIRST",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ErrorRed
                                )
                                Button(
                                    onClick = {
                                        viewModel.setActivePaper(topic.paper)
                                        viewModel.setActiveTopic(topic)
                                        viewModel.setMode(LabMode.ASK_LAB)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                                    modifier = Modifier.height(24.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text("Revise in Study Lab →", fontSize = 9.5.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // ACTIVE ERROR LOG
            if (errorLogs.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.1f)),
                        border = BorderStroke(0.5.dp, SuccessGreen.copy(alpha = 0.4f)),
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
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)),
                        border = BorderStroke(0.5.dp, ErrorRed.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("error_card_${error.id}")
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(error.paperName, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = ErrorRed)
                                    Text(
                                        text = error.topicName,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Button(
                                    onClick = { viewModel.resolveErrorLog(error.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                    shape = RoundedCornerShape(4.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(24.dp)
                                ) {
                                    Text("Resolve ✓", fontSize = 9.5.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text("❓ Question: ${error.questionOrConcept}", fontSize = 10.5.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(3.dp))
                            Text("❌ Mistake: ${error.userMistake}", fontSize = 10.5.sp, color = ErrorRed, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("✓ Rule: ${error.correctRule}", fontSize = 10.5.sp, color = SuccessGreen, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// SUB-TAB 3: PAST VERDICTS HISTORY
// =========================================================================
@Composable
fun PastVerdictsSection(
    dashboard: VerdictsDashboardState,
    onRefreshVerdicts: () -> Unit,
    onStartTeachLab: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Summary Card
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Avg Mastery", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text(
                            text = if (dashboard.verdicts.isEmpty()) "--" else "${dashboard.averageScore.toInt()}%",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(36.dp)
                            .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Graded Sessions", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text(
                            text = "${dashboard.totalSessions}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        if (dashboard.verdicts.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Amber500.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.School, contentDescription = null, tint = Amber500, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("No Teaching Sessions Yet!", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Explain a provision in Teach Lab and tap 'Give Verdict' to get graded by ICAI Examiner Lab!",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onStartTeachLab,
                            modifier = Modifier.testTag("start_first_teach_button"),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Start First Session in Teach Lab", fontSize = 11.5.sp)
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
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
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
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (verdict.masteryScore >= 75) SuccessGreen.copy(alpha = 0.15f) else Amber500.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${verdict.masteryScore}/100",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        color = if (verdict.masteryScore >= 75) SuccessGreen else Amber500,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 6.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Text("What You Nailed:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SuccessGreen)
                    verdict.whatYouNailed.forEach {
                        Row(modifier = Modifier.padding(vertical = 1.dp)) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(it, fontSize = 10.5.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text("What Slipped (Gaps):", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = ErrorRed)
                    verdict.whatSlipped.forEach {
                        Row(modifier = Modifier.padding(vertical = 1.dp)) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(it, fontSize = 10.5.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Amber500, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ICAI Examiner Comment", fontWeight = FontWeight.Bold, fontSize = 10.5.sp, color = MaterialTheme.colorScheme.onSurface)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(verdict.icaiExaminerTip, fontSize = 10.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text("Next Action: ${verdict.nextAction}", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Sky500)
                }
            }
        }
    }
}
