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
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Whatshot
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.curriculum.CaSyllabusData
import com.example.data.local.ErrorLogEntity
import com.example.data.local.TopicProgressEntity
import com.example.data.model.CatalogSortOrder
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
    val catalogSortOrder by viewModel.catalogSortOrder.collectAsState()

    var activeSubTab by remember { mutableIntStateOf(0) } // 0 = Syllabus Ledger, 1 = Diagnostic MCQ, 2 = Weakness Radar & Errors, 3 = Verdicts

    LaunchedEffect(Unit) {
        viewModel.loadVerdictsDashboard()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("verdicts_history_screen")
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        // 4 SUB-TABS ROW AT TOP
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
                            Icon(Icons.AutoMirrored.Filled.FactCheck, contentDescription = null, modifier = Modifier.size(14.dp), tint = Amber500)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ledger", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    },
                    modifier = Modifier.testTag("subtab_syllabus_ledger")
                )
                Tab(
                    selected = activeSubTab == 1,
                    onClick = { activeSubTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(14.dp), tint = Sky500)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("MCQ Test", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    },
                    modifier = Modifier.testTag("subtab_mcq_test")
                )
                Tab(
                    selected = activeSubTab == 2,
                    onClick = { activeSubTab = 2 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Assessment, contentDescription = null, modifier = Modifier.size(14.dp), tint = ErrorRed)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Radar (${errorLogs.size})", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    },
                    modifier = Modifier.testTag("subtab_weakness_radar")
                )
                Tab(
                    selected = activeSubTab == 3,
                    onClick = { activeSubTab = 3 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(14.dp), tint = SuccessGreen)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Verdicts", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    },
                    modifier = Modifier.testTag("subtab_verdicts_history")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        when (activeSubTab) {
            0 -> SyllabusLedgerSection(
                viewModel = viewModel,
                progressMap = progressMap,
                catalogSortOrder = catalogSortOrder,
                verdicts = dashboard.verdicts
            )
            1 -> McqTestSection(viewModel = viewModel)
            2 -> RadarAndErrorsSection(
                viewModel = viewModel,
                progressMap = progressMap,
                errorLogs = errorLogs
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
// SUB-TAB 0: ANTI-DUOLINGO CALM "MY PROGRESS" SYLLABUS LEDGER
// =========================================================================
@Composable
fun SyllabusLedgerSection(
    viewModel: CaLabViewModel,
    progressMap: Map<String, TopicProgressEntity>,
    catalogSortOrder: CatalogSortOrder,
    verdicts: List<LabVerdict>
) {
    var selectedPaper by remember { mutableStateOf<PaperCode?>(null) } // null = All
    // Filter mode: "ALL", "PINNED_SWIPE", "TIER_1", "NEEDS_WORK"
    var filterMode by remember { mutableStateOf("ALL") }

    val rawTopics = remember(selectedPaper) {
        if (selectedPaper != null) CaSyllabusData.getTopicsForPaper(selectedPaper!!)
        else CaSyllabusData.topics
    }

    // Sort according to catalogSortOrder
    val sortedTopics = remember(rawTopics, catalogSortOrder) {
        when (catalogSortOrder) {
            CatalogSortOrder.OFFICIAL_ICAI -> rawTopics.sortedWith(
                compareBy<SyllabusTopic> { it.paper.ordinal }
                    .thenBy { it.icaiChapterNumber }
                    .thenBy { it.topicName }
            )
            CatalogSortOrder.PRIORITY_TIER -> rawTopics.sortedWith(
                compareBy<SyllabusTopic> { it.priorityTier.ordinal }
                    .thenBy { it.paper.ordinal }
                    .thenBy { it.icaiChapterNumber }
            )
        }
    }

    // Pinned from Swipe Drill topics count
    val pinnedCount = remember(progressMap) {
        CaSyllabusData.topics.count { progressMap[it.id]?.swipeDrillStatus == 1 }
    }

    // Filter topics
    val filteredTopics = remember(sortedTopics, filterMode, progressMap) {
        when (filterMode) {
            "PINNED_SWIPE" -> sortedTopics.filter { progressMap[it.id]?.swipeDrillStatus == 1 }
            "TIER_1" -> sortedTopics.filter { it.priorityTier == PriorityTier.TIER_1 }
            "NEEDS_WORK" -> sortedTopics.filter { topic ->
                val p = progressMap[topic.id]
                (p?.swipeDrillStatus == 1) || (p?.teacherReadCompleted ?: 0) == 0 || ((p?.mcqAttempted ?: 0) > 0 && (p?.mcqCorrect ?: 0) * 2 <= (p?.mcqAttempted ?: 1))
            }
            else -> sortedTopics
        }
    }

    // 5-Min Low-Energy Revision topics (pick 3 high priority unread or pinned topics)
    val lowEnergyTopics = remember(progressMap) {
        val pinned = CaSyllabusData.topics.filter { progressMap[it.id]?.swipeDrillStatus == 1 }
        val unreadTier1 = CaSyllabusData.topics.filter {
            it.priorityTier == PriorityTier.TIER_1 && (progressMap[it.id]?.teacherReadCompleted ?: 0) == 0
        }
        (pinned + unreadTier1).distinctBy { it.id }.take(3)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Calm Ledger Header Card (Anti-Duolingo: No streaks, no guilt bars)
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.AutoMirrored.Filled.FactCheck, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Statutory Syllabus Ledger",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Calm, dignified audit tracking. Study at your own pace without pressure or streaks.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // ⚡ 5-Min Low-Energy Revision Card
        if (lowEnergyTopics.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Amber500.copy(alpha = 0.12f)),
                    border = BorderStroke(1.dp, Amber500.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("low_energy_revision_card")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Amber500, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("⚡ 5-Min Low-Energy Revision", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = Amber500)
                            }
                            Text("Quick 1-Tap Catchup", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        lowEnergyTopics.forEach { topic ->
                            val isPinned = progressMap[topic.id]?.swipeDrillStatus == 1
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = if (isPinned) Amber500.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primaryContainer
                                    ) {
                                        Text(
                                            text = if (isPinned) "Pinned 🔁" else topic.paper.shortName,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isPinned) Amber500 else MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = topic.topicName,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.clickable {
                                            viewModel.setActivePaper(topic.paper)
                                            viewModel.setActiveTopic(topic)
                                            viewModel.setMode(LabMode.ASK_LAB)
                                        }
                                    ) {
                                        Text(
                                            text = "Revise 💡",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Amber500,
                                        modifier = Modifier.clickable {
                                            viewModel.setActivePaper(topic.paper)
                                            viewModel.setActiveTopic(topic)
                                            viewModel.setMode(LabMode.SPOT_DIFFERENCE)
                                        }
                                    ) {
                                        Text(
                                            text = "Drill 🔥",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Sort Order Switcher Bar
        item {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isOfficial = catalogSortOrder == CatalogSortOrder.OFFICIAL_ICAI
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isOfficial) MaterialTheme.colorScheme.primary else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.setCatalogSortOrder(CatalogSortOrder.OFFICIAL_ICAI) }
                            .testTag("sort_official_icai")
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.FormatListNumbered,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = if (isOfficial) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Official ICAI Order",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isOfficial) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (!isOfficial) MaterialTheme.colorScheme.primary else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.setCatalogSortOrder(CatalogSortOrder.PRIORITY_TIER) }
                            .testTag("sort_priority_tier")
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.Sort,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = if (!isOfficial) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Exam Priority Tier",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (!isOfficial) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Filter Chips Row (All, Pinned from Swipe Drill, Tier 1, Needs Work)
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item {
                    FilterChip(
                        selected = filterMode == "ALL",
                        onClick = { filterMode = "ALL" },
                        label = { Text("All (${sortedTopics.size})", fontSize = 11.sp) },
                        modifier = Modifier.testTag("ledger_filter_all")
                    )
                }

                item {
                    FilterChip(
                        selected = filterMode == "PINNED_SWIPE",
                        onClick = { filterMode = "PINNED_SWIPE" },
                        label = { Text("🔁 Pinned from Drill ($pinnedCount)", fontSize = 11.sp) },
                        modifier = Modifier.testTag("ledger_filter_pinned"),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Amber500.copy(alpha = 0.2f),
                            selectedLabelColor = Amber500
                        )
                    )
                }

                item {
                    FilterChip(
                        selected = filterMode == "TIER_1",
                        onClick = { filterMode = "TIER_1" },
                        label = { Text("🔥 Tier 1 Only", fontSize = 11.sp) },
                        modifier = Modifier.testTag("ledger_filter_tier1")
                    )
                }

                item {
                    FilterChip(
                        selected = filterMode == "NEEDS_WORK",
                        onClick = { filterMode = "NEEDS_WORK" },
                        label = { Text("⚠️ Needs Work", fontSize = 11.sp) },
                        modifier = Modifier.testTag("ledger_filter_needs_work")
                    )
                }
            }
        }

        // Paper Selection Row
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedPaper == null,
                        onClick = { selectedPaper = null },
                        label = { Text("All Papers", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
                items(PaperCode.entries, key = { it.name }) { paper ->
                    val isSelected = selectedPaper == paper
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedPaper = paper },
                        label = { Text(paper.shortName, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
        }

        // Ledger Topic Cards List with 4-Segment Completion Indicators
        items(filteredTopics, key = { it.id }) { topic ->
            val progress = progressMap[topic.id]
            val hasVerdict = verdicts.any { it.topicName == topic.topicName }
            SyllabusLedgerItemCard(
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
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No chapters found matching filter '$filterMode'.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// 4-Segment Topic Completion Card
@Composable
fun SyllabusLedgerItemCard(
    topic: SyllabusTopic,
    progress: TopicProgressEntity?,
    hasVerdict: Boolean,
    onToggleRead: () -> Unit,
    onToggleSwipe: () -> Unit,
    onJumpToAsk: () -> Unit,
    onJumpToTeach: () -> Unit
) {
    val tierColor = Color(topic.priorityTier.badgeColorHex)
    val isRead = progress?.teacherReadCompleted == 1
    val isRecalled = (progress?.mcqAttempted ?: 0) > 0
    val drillStatus = progress?.swipeDrillStatus ?: 0 // 0=Unseen, 1=Need Practice, 2=Mastered
    val isWrittenOrOral = (progress?.writtenSolved ?: 0) > 0 || (progress?.oralRecallsCompleted ?: 0) > 0 || hasVerdict

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, if (drillStatus == 1) Amber500.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ledger_card_${topic.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: ICAI Chapter + Title + Tier & Amendment Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "Ch ${topic.icaiChapterNumber}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
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
                            text = "${topic.paper.shortName} • ${topic.chapter}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (topic.hasRecentAmendment) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Amber500.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "🆕 BOS Amendment",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Amber500,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = tierColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = topic.priorityTier.label.split("•")[0].trim(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = tierColor,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4-SEGMENT COMPLETION INDICATOR
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. Read Segment
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isRead) SuccessGreen else Color.Transparent,
                        border = BorderStroke(1.dp, if (isRead) SuccessGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .clickable { onToggleRead() }
                            .testTag("segment_read_${topic.id}")
                    ) {
                        Text(
                            text = if (isRead) "✓ Read" else "○ Read",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isRead) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // 2. Recalled Segment (MCQs)
                    val mcqText = if (isRecalled) "✓ Recalled (${progress?.mcqCorrect}/${progress?.mcqAttempted})" else "○ Recalled"
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isRecalled) SuccessGreen.copy(alpha = 0.15f) else Color.Transparent,
                        border = BorderStroke(1.dp, if (isRecalled) SuccessGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = mcqText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isRecalled) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // 3. Drill Segment (Swipe Drill)
                    val (drillLabel, drillColor, drillBg) = when (drillStatus) {
                        2 -> Triple("✓ Mastered", SuccessGreen, SuccessGreen.copy(alpha = 0.15f))
                        1 -> Triple("🔁 Need Practice", Amber500, Amber500.copy(alpha = 0.2f))
                        else -> Triple("○ Drill", MaterialTheme.colorScheme.onSurfaceVariant, Color.Transparent)
                    }
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = drillBg,
                        border = BorderStroke(1.dp, drillColor),
                        modifier = Modifier
                            .clickable { onToggleSwipe() }
                            .testTag("segment_drill_${topic.id}")
                    ) {
                        Text(
                            text = drillLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = drillColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // 4. Written / Oral Segment
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isWrittenOrOral) SuccessGreen.copy(alpha = 0.15f) else Color.Transparent,
                        border = BorderStroke(1.dp, if (isWrittenOrOral) SuccessGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = if (isWrittenOrOral) "✓ Verdict" else "○ Verdict",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isWrittenOrOral) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Navigation Jump Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onJumpToAsk,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text("Study Material 💡", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(6.dp))

                Button(
                    onClick = onJumpToTeach,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text("Teach Lab 🎤", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// =========================================================================
// SUB-TAB 1: DIAGNOSTIC MCQ TEST SECTION
// =========================================================================
@Composable
fun McqTestSection(viewModel: CaLabViewModel) {
    val mcqState by viewModel.mcqTestState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Paper selection row for MCQs
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
                        label = { Text(paper.shortName, fontSize = 11.sp) },
                        modifier = Modifier.testTag("mcq_paper_${paper.name}"),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Amber500.copy(alpha = 0.2f),
                            selectedLabelColor = Amber500
                        )
                    )
                }
            }
        }

        // Diagnostic Test Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Speed, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "${mcqState.selectedPaper.title} • Diagnostic Test",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "ICAI Case Scenarios & Traps. Wrong answers automatically save to your Active Error Log.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
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
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = bannerColor.copy(alpha = 0.15f)),
                    border = BorderStroke(1.2.dp, bannerColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Diagnostic Result: $marksScored / $totalMarks Marks ($percent%)",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = bannerColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (percent >= 70) "Strong statutory grasp! Missed subtleties are saved to your Active Error Log." else "Review the detailed ICAI working notes below. Missed rules saved for revision.",
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .testTag("submit_mcq_test_button"),
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
        modifier = Modifier
            .fillMaxWidth()
            .testTag("mcq_question_card_$qNumber")
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
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
// SUB-TAB 2: WEAKNESS RADAR & ACTIVE ERROR LOG
// =========================================================================
@Composable
fun RadarAndErrorsSection(
    viewModel: CaLabViewModel,
    progressMap: Map<String, TopicProgressEntity>,
    errorLogs: List<ErrorLogEntity>
) {
    var selectedRadarPaper by remember { mutableStateOf<PaperCode?>(null) }
    var viewMode by remember { mutableIntStateOf(0) } // 0 = Weakness Radar, 1 = Active Error Log

    val allTopics = remember(selectedRadarPaper) {
        if (selectedRadarPaper != null) CaSyllabusData.getTopicsForPaper(selectedRadarPaper!!)
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Toggle between Radar and Error Log
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (viewMode == 0) MaterialTheme.colorScheme.primary else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewMode = 0 }
                    ) {
                        Text(
                            text = "🎯 Weakness Radar",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (viewMode == 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (viewMode == 1) ErrorRed else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewMode = 1 }
                    ) {
                        Text(
                            text = "⚡ Active Errors (${errorLogs.size})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (viewMode == 1) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                }
            }
        }

        if (viewMode == 0) {
            // WEAKNESS RADAR

            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedRadarPaper == null,
                            onClick = { selectedRadarPaper = null },
                            label = { Text("All 6 Papers", fontSize = 11.sp) }
                        )
                    }
                    items(PaperCode.entries, key = { it.name }) { paper ->
                        val isSelected = selectedRadarPaper == paper
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedRadarPaper = paper },
                            label = { Text(paper.shortName, fontSize = 11.sp) }
                        )
                    }
                }
            }

            itemsIndexed(rankedTopics, key = { _, item -> item.first.id }) { index, (topic, accuracy, _) ->
                val isCritical = (topic.priorityTier == PriorityTier.TIER_1 || topic.priorityTier == PriorityTier.TIER_2) && accuracy < 65.0
                val tierColor = Color(topic.priorityTier.badgeColorHex)

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isCritical) Color(0xFFFEF2F2) else MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, if (isCritical) ErrorRed else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("radar_row_${topic.id}")
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
                                    Text(topic.topicName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, maxLines = 1)
                                    Text("${topic.paper.shortName} • ${topic.chapter}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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

                        if (isCritical) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("CRITICAL EXAM RISK — REVISE FIRST", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = ErrorRed)
                                Button(
                                    onClick = {
                                        viewModel.setActivePaper(topic.paper)
                                        viewModel.setActiveTopic(topic)
                                        viewModel.setMode(LabMode.ASK_LAB)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                                    modifier = Modifier.height(28.dp),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("Revise in Ask Lab →", fontSize = 10.sp, fontWeight = FontWeight.Bold)
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
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            } else {
                items(errorLogs, key = { it.id }) { error ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                        border = BorderStroke(1.dp, ErrorRed.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("error_card_${error.id}")
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
                                    onClick = { viewModel.resolveErrorLog(error.id) },
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
        }
    }
}

// =========================================================================
// SUB-TAB 3: PAST VERDICTS HISTORY
// =========================================================================
@Composable
fun PastVerdictsSection(
    dashboard: com.example.ui.viewmodel.VerdictsDashboardState,
    onRefreshVerdicts: () -> Unit,
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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

        if (dashboard.verdicts.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Amber500.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.School, contentDescription = null, tint = Amber500)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No Teaching Sessions Yet!", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Explain a provision in Teach Lab and tap 'Give Verdict' to get graded by ICAI Examiner Lab!",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
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
                    Text("What You Nailed:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SuccessGreen)
                    verdict.whatYouNailed.forEach {
                        Row(modifier = Modifier.padding(vertical = 2.dp)) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(it, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("What Slipped (Gaps):", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = ErrorRed)
                    verdict.whatSlipped.forEach {
                        Row(modifier = Modifier.padding(vertical = 2.dp)) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(it, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

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

                    Text("Next Action: ${verdict.nextAction}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Sky500)
                }
            }
        }
    }
}
