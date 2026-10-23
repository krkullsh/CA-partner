package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Calculate
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
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.curriculum.StrategyBlueprintReader
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.curriculum.CaSyllabusData
import com.example.data.curriculum.CuratedQuestionBank
import com.example.data.curriculum.MarkdownQuestionParser
import com.example.data.curriculum.PracticeQuestion
import com.example.data.local.TopicProgressEntity
import com.example.data.model.ExplanationDepth
import com.example.data.model.PaperCode
import com.example.data.model.PriorityTier
import com.example.data.model.StandardBlueprint
import com.example.data.model.SyllabusTopic
import com.example.ui.components.FormattedText
import com.example.ui.theme.Amber500
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.Sky500
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.CaLabViewModel
import kotlinx.coroutines.delay

enum class TierFilterOption(val label: String) {
    ALL("All Tiers"),
    TIER_1("Tier 1 (MUST)"),
    TIER_2("Tier 2 (High)"),
    TIER_3("Tier 3"),
    TIER_4("Tier 4"),
    DUE_RECALL("Due for 3-Day Recall")
}

@Composable
fun AskLabScreen(viewModel: CaLabViewModel) {
    val state by viewModel.askState.collectAsState()
    val bookmarkedIds by viewModel.bookmarkedIds.collectAsState()
    val progressMap by viewModel.topicProgressMap.collectAsState()
    val context = LocalContext.current

    var selectedModule by remember(state.selectedPaper) { mutableStateOf("All") }
    var selectedTierFilter by remember { mutableStateOf(TierFilterOption.ALL) }
    var activeSubTab by remember { mutableIntStateOf(0) } // 0 = 3-Tier Depth Notes, 1 = Strategy & Q-Bank
    var showBlueprintDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("ask_lab_screen"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Paper selection chips & Bookmarked Only toggle
        item {
            Spacer(modifier = Modifier.height(4.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = state.showBookmarkedOnly,
                        onClick = { viewModel.toggleAskBookmarkedOnly() },
                        leadingIcon = {
                            Icon(
                                imageVector = if (state.showBookmarkedOnly) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        label = { Text("Bookmarked (LDR)", fontSize = 12.sp) },
                        modifier = Modifier.testTag("ask_bookmarked_filter_chip"),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Amber500.copy(alpha = 0.2f),
                            selectedLabelColor = Amber500
                        )
                    )
                }
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .clickable { showBlueprintDialog = true }
                            .testTag("ask_open_paper_blueprint_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Paper Strategy Blueprint",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
                items(PaperCode.entries, key = { it.name }) { paper ->
                    val isSelected = state.selectedPaper == paper
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectAskPaper(paper) },
                        label = { Text(paper.shortName, fontSize = 12.sp) },
                        modifier = Modifier.testTag("ask_paper_${paper.name}"),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
        }

        // 2. Custom search / doubt input
        item {
            OutlinedTextField(
                value = state.customQuery,
                onValueChange = { viewModel.updateAskCustomQuery(it) },
                placeholder = { Text("Ask any CA Inter provision, section, or concept...", fontSize = 13.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ask_search_field"),
                shape = RoundedCornerShape(24.dp),
                trailingIcon = {
                    IconButton(
                        onClick = { viewModel.searchOrGenerateExplanation() },
                        modifier = Modifier.testTag("ask_search_button")
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
        }

        // 3. Strategy Tier Filter Chips (All Tiers, Tier 1, Tier 2, Tier 3, Tier 4, Due for 3-Day Recall)
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(TierFilterOption.entries, key = { it.name }) { option ->
                    val isSelected = selectedTierFilter == option
                    val optionColor = when (option) {
                        TierFilterOption.TIER_1 -> Color(0xFFEF4444)
                        TierFilterOption.TIER_2 -> Color(0xFFF59E0B)
                        TierFilterOption.TIER_3 -> Color(0xFF0EA5E9)
                        TierFilterOption.TIER_4 -> Color(0xFF64748B)
                        TierFilterOption.DUE_RECALL -> Color(0xFFE11D48)
                        TierFilterOption.ALL -> MaterialTheme.colorScheme.primary
                    }

                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedTierFilter = option },
                        label = {
                            Text(
                                option.label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.testTag("tier_filter_${option.name.lowercase()}"),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = optionColor.copy(alpha = 0.2f),
                            selectedLabelColor = optionColor
                        )
                    )
                }
            }
        }

        // 4. Topic chips & Module filtering
        item {
            val allTopics = remember(state.selectedPaper) {
                CaSyllabusData.getTopicsForPaper(state.selectedPaper)
            }
            val availableModules = remember(allTopics) {
                listOf("All") + allTopics.map { it.moduleName }.distinct()
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                // Module filter chips when multiple modules exist
                if (availableModules.size > 2) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(availableModules, key = { it }) { mod ->
                            val isModSelected = selectedModule == mod
                            FilterChip(
                                selected = isModSelected,
                                onClick = { selectedModule = mod },
                                label = { Text(mod, fontSize = 11.sp) },
                                modifier = Modifier.testTag("ask_module_chip_${mod.replace(" ", "_")}"),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )
                        }
                    }
                }

                val now = System.currentTimeMillis()
                val threeDaysMillis = 3L * 24 * 60 * 60 * 1000L

                val filteredTopics = allTopics.filter { topic ->
                    val matchesModule = selectedModule == "All" || topic.moduleName == selectedModule
                    val matchesBookmark = !state.showBookmarkedOnly || bookmarkedIds.contains(topic.id)
                    val progress = progressMap[topic.id]
                    val matchesTier = when (selectedTierFilter) {
                        TierFilterOption.ALL -> true
                        TierFilterOption.TIER_1 -> topic.priorityTier == PriorityTier.TIER_1
                        TierFilterOption.TIER_2 -> topic.priorityTier == PriorityTier.TIER_2
                        TierFilterOption.TIER_3 -> topic.priorityTier == PriorityTier.TIER_3
                        TierFilterOption.TIER_4 -> topic.priorityTier == PriorityTier.TIER_4
                        TierFilterOption.DUE_RECALL -> {
                            val lastRev = progress?.lastRevisedAt ?: 0L
                            lastRev == 0L || (now - lastRev) >= threeDaysMillis
                        }
                    }
                    matchesModule && matchesBookmark && matchesTier
                }

                if (filteredTopics.isEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = if (state.showBookmarkedOnly) {
                                "⭐ No bookmarked topics for ${state.selectedPaper.shortName} yet! Tap the bookmark icon to save."
                            } else {
                                "🔍 No topics match the current filter (${selectedTierFilter.label}). Select 'All Tiers' to view all topics."
                            },
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                } else {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(filteredTopics, key = { it.id }) { t ->
                            val isSelected = state.selectedTopic?.id == t.id
                            val tierColor = Color(t.priorityTier.badgeColorHex)

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                border = if (t.priorityTier == PriorityTier.TIER_1 && !isSelected) {
                                    BorderStroke(1.dp, tierColor.copy(alpha = 0.7f))
                                } else null,
                                modifier = Modifier
                                    .clickable { viewModel.selectAskTopic(t) }
                                    .testTag("topic_chip_${t.id}")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else tierColor)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = t.topicName.take(28) + if (t.topicName.length > 28) "..." else "",
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Selected Topic Strategy Blueprint & Progress Master Card
        state.selectedTopic?.let { topic ->
            item {
                val progress = progressMap[topic.id]
                TopicStrategyProgressCard(
                    topic = topic,
                    progress = progress,
                    isBookmarked = bookmarkedIds.contains(topic.id),
                    onToggleBookmark = {
                        viewModel.toggleBookmark(topic.id, "TOPIC", topic.topicName, topic.paper.title)
                    },
                    onIncrementWritten = { viewModel.incrementWrittenSolved(topic.id) },
                    onIncrementOralRecall = { viewModel.incrementOralRecall(topic.id) }
                )
            }
        }

        // 6. Sub-Mode Tabs: "3-Tier Concept Notes" vs "Strategy & Q-Bank"
        item {
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
                                Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("3-Tier Concept Notes", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        },
                        modifier = Modifier.testTag("tab_concept_notes")
                    )
                    Tab(
                        selected = activeSubTab == 1,
                        onClick = { activeSubTab = 1 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(16.dp), tint = Amber500)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Strategy & Q-Bank", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (activeSubTab == 1) Amber500 else MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        },
                        modifier = Modifier.testTag("tab_strategy_qbank")
                    )
                }
            }
        }

        // 7. CONTENT BASED ON SUB-TAB
        if (activeSubTab == 0) {
            // TAB 0: 3-TIER CONCEPT NOTES
            // Standard Summary Blueprint (Scope, Recognition & Measurement, Disclosures)
            if (state.selectedTopic?.blueprint != null) {
                item {
                    StandardSummaryBlueprintSection(
                        topicName = state.selectedTopic!!.topicName,
                        blueprint = state.selectedTopic!!.blueprint!!
                    )
                }
            }

            // 3-Tier Depth Selector Tabs
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(6.dp)) {
                        TabRow(
                            selectedTabIndex = state.selectedDepth.ordinal,
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.primary
                        ) {
                            ExplanationDepth.entries.forEach { depth ->
                                Tab(
                                    selected = state.selectedDepth == depth,
                                    onClick = { viewModel.setAskDepth(depth) },
                                    text = {
                                        Text(
                                            text = when (depth) {
                                                ExplanationDepth.DEPTH_1 -> "Tier 1: Simple"
                                                ExplanationDepth.DEPTH_2 -> "Tier 2: Standard"
                                                ExplanationDepth.DEPTH_3 -> "Tier 3: ICAI Exam"
                                            },
                                            fontWeight = if (state.selectedDepth == depth) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 12.sp
                                        )
                                    },
                                    modifier = Modifier.testTag("depth_tab_${depth.level}")
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = state.selectedDepth.subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Main Explanation Content Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .testTag("explanation_content_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Psychology,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = state.selectedTopic?.topicName ?: "Concept Explanation",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    modifier = Modifier.widthIn(max = 210.dp)
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val isTopicBookmarked = state.selectedTopic?.let { bookmarkedIds.contains(it.id) } ?: false
                                IconButton(
                                    onClick = {
                                        state.selectedTopic?.let {
                                            viewModel.toggleBookmark(it.id, "TOPIC", it.topicName, it.paper.title)
                                        }
                                    },
                                    modifier = Modifier.testTag("bookmark_active_topic_button")
                                ) {
                                    Icon(
                                        imageVector = if (isTopicBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = "Bookmark Topic for LDR",
                                        tint = if (isTopicBookmarked) Amber500 else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        clipboard.setPrimaryClip(ClipData.newPlainText("CA-Lab Note", state.displayedContent))
                                        Toast.makeText(context, "Copied explanation to clipboard!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.testTag("copy_explanation_button")
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(20.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (state.isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    CircularProgressIndicator()
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Generating depth breakdown...", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        } else {
                            FormattedText(
                                text = state.displayedContent,
                                style = MaterialTheme.typography.bodyMedium,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }

            // Below the 3-Tier Depth explanation card:
            // Collapsible "Practice High-Chance Exam Questions (Timed)"
            state.selectedTopic?.let { topic ->
                item {
                    CollapsiblePracticeQuestionsCard(
                        topic = topic,
                        isExpanded = state.isPracticeQuestionsExpanded,
                        onToggleExpand = { viewModel.togglePracticeQuestionsExpanded() },
                        onWrittenSolved = { viewModel.incrementWrittenSolved(topic.id) }
                    )
                }
            }
        } else {
            // TAB 1: STRATEGY & EXAM QUESTION BANK (TIMED PRACTICE)
            item {
                StrategyBlueprintBanner(
                    paper = state.selectedPaper,
                    onOpenBlueprint = { showBlueprintDialog = true }
                )
            }

            state.selectedTopic?.let { topic ->
                item {
                    CollapsiblePracticeQuestionsCard(
                        topic = topic,
                        isExpanded = true,
                        onToggleExpand = {},
                        onWrittenSolved = { viewModel.incrementWrittenSolved(topic.id) }
                    )
                }
            }
        }
    }

    if (showBlueprintDialog) {
        com.example.ui.components.StrategyBlueprintBottomSheet(
            paper = state.selectedPaper,
            onDismiss = { showBlueprintDialog = false }
        )
    }
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
    val lastRevised = progress?.lastRevisedAt ?: 0L

    val isDueForRecall = lastRevised == 0L || (System.currentTimeMillis() - lastRevised) >= (3L * 24 * 60 * 60 * 1000L)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.5.dp, tierColor.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("topic_strategy_progress_card")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Top Row: Topic Title & Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = topic.topicName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${topic.moduleName} • ${topic.chapter}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) Amber500 else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Strategy Badges (Priority Tier, Past Paper Frequency, Average Marks, Trend)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = tierColor.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, tierColor.copy(alpha = 0.6f)),
                    modifier = Modifier.testTag("badge_priority_tier")
                ) {
                    Text(
                        text = topic.priorityTier.label,
                        color = tierColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.testTag("badge_past_paper_frequency")
                ) {
                    Text(
                        text = "📊 ${topic.pastPaperFrequency}",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Amber500.copy(alpha = 0.15f),
                    modifier = Modifier.testTag("badge_average_marks")
                ) {
                    Text(
                        text = "🎯 ~${topic.averageMarks.toInt()}M Avg",
                        color = Amber500,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Sky500.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = topic.trendDirection,
                        color = Sky500,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3 COMPACT PROGRESS COUNTERS (Written, MCQs, Oral Recall)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("compact_progress_counters_row"),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 1. Written
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Sky500.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, Sky500.copy(alpha = 0.35f)),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("progress_counter_written")
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.AutoMirrored.Filled.Assignment,
                                contentDescription = null,
                                tint = Sky500,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Written",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Sky500
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "$writtenSolved / ${topic.writtenTarget}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Sky500,
                            modifier = Modifier
                                .clickable { onIncrementWritten() }
                                .testTag("increment_written_button")
                        ) {
                            Text(
                                text = "+1 Solved",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // 2. MCQs
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SuccessGreen.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.35f)),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("progress_counter_mcq")
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Speed,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "MCQs",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "$mcqAttempted / ${topic.mcqTarget}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        val mcqSub = if (mcqAttempted > 0) "${(mcqCorrect * 100) / mcqAttempted}% Acc" else "Target"
                        Text(
                            text = mcqSub,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SuccessGreen
                        )
                    }
                }

                // 3. Oral Recall
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = tierColor.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, tierColor.copy(alpha = 0.35f)),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("progress_counter_oral")
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Psychology,
                                contentDescription = null,
                                tint = tierColor,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Oral Recall",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = tierColor
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "$oralRecalls / ${topic.oralRecallTarget}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = tierColor,
                            modifier = Modifier
                                .clickable { onIncrementOralRecall() }
                                .testTag("increment_oral_recall_button")
                        ) {
                            Text(
                                text = "+1 Recall",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // Fused Topics Alert Pill
            if (topic.fusedWithTopics.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFEF3C7),
                    border = BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "⚡ Compulsory Fused Pairing:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB45309)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = topic.fusedWithTopics.joinToString(", "),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF92400E)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3 OPEN-TIMELINE MASTERY PROGRESS BARS
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // 1. Written Practice Progress
                val writtenProgressRatio = (writtenSolved.toFloat() / topic.writtenTarget.toFloat()).coerceIn(0f, 1f)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null, tint = Sky500, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Written Problems: $writtenSolved / ${topic.writtenTarget}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Sky500.copy(alpha = 0.15f),
                        modifier = Modifier
                            .clickable { onIncrementWritten() }
                            .testTag("increment_written_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Sky500, modifier = Modifier.size(12.dp))
                            Text("+1 Solved", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Sky500)
                        }
                    }
                }
                LinearProgressIndicator(
                    progress = { writtenProgressRatio },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Sky500,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                // 2. MCQ Practice Progress
                val mcqProgressRatio = (mcqAttempted.toFloat() / topic.mcqTarget.toFloat()).coerceIn(0f, 1f)
                val mcqAccuracyText = if (mcqAttempted > 0) " (${(mcqCorrect * 100) / mcqAttempted}% correct)" else ""
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Speed, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("MCQ Target: $mcqAttempted / ${topic.mcqTarget}$mcqAccuracyText", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                    if (mcqAttempted >= topic.mcqTarget) {
                        Text("🎯 Mastered", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                    }
                }
                LinearProgressIndicator(
                    progress = { mcqProgressRatio },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = SuccessGreen,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                // 3. Oral Recall & 3-Day Rhythm Progress
                val oralProgressRatio = (oralRecalls.toFloat() / topic.oralRecallTarget.toFloat()).coerceIn(0f, 1f)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Psychology, contentDescription = null, tint = tierColor, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Oral Recall: $oralRecalls / ${topic.oralRecallTarget}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        if (isDueForRecall) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFEF4444).copy(alpha = 0.15f)
                            ) {
                                Text("⚡ DUE TODAY", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFEF4444), modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                            }
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = tierColor.copy(alpha = 0.15f),
                        modifier = Modifier
                            .clickable { onIncrementOralRecall() }
                            .testTag("increment_oral_recall_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = tierColor, modifier = Modifier.size(12.dp))
                            Text("+1 Recall", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = tierColor)
                        }
                    }
                }
                LinearProgressIndicator(
                    progress = { oralProgressRatio },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = tierColor,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Text(
                    text = "⏰ ${topic.oralRhythmNote}",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
            // Collapsible Header
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

                    // Start/Stop Countdown Timer (minutesAllowed minutes)
                    var remainingSeconds by remember(currentQuestion.id) {
                        mutableIntStateOf(currentQuestion.minutesAllowed * 60)
                    }
                    var isTimerRunning by remember(currentQuestion.id) { mutableStateOf(false) }

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

                    // Question Badges (Marks, Minutes, ICAI FAV, High Chance, tags)
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        item {
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
                        }
                        item {
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
                        }
                        items(currentQuestion.tags, key = { it }) { tag ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = tag,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
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
                        border = BorderStroke(1.dp, if (isUrgent) Color(0xFFEF4444) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
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

                    // "Reveal ICAI Answer & Examiner Traps" Button
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

                    // Revealed Answers, Approach Tips & Examiner Observations
                    if (isAnswerRevealed) {
                        Spacer(modifier = Modifier.height(14.dp))

                        // 1. Official Suggested Answer Card
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

                        // 2. Approach & Time-Saving Tips Card
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

                        // 3. Examiner Observations & Traps Card
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

                        // Next Question Button
                        if (activeQuestionIndex < questions.size - 1) {
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(
                                onClick = {
                                    activeQuestionIndex++
                                    isAnswerRevealed = false
                                    isTimerRunning = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("practice_next_question_button")
                            ) {
                                Text("Practice Next Question (${activeQuestionIndex + 2} of ${questions.size}) →")
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
fun StandardSummaryBlueprintSection(
    topicName: String,
    blueprint: StandardBlueprint,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("standard_summary_blueprint")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Standard Summary Blueprint",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "3-Pillar Rapid Architecture for Exam Revision",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                if (maxWidth >= 600.dp) {
                    // 3-Column Layout on larger displays / tablets
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        BlueprintPillarCard(
                            stepNumber = "1",
                            title = "Scope & Applicability",
                            content = blueprint.scope,
                            accentColor = Color(0xFF0284C7),
                            icon = Icons.Default.CheckCircle,
                            modifier = Modifier.weight(1f)
                        )
                        BlueprintPillarCard(
                            stepNumber = "2",
                            title = "Recognition & Measurement",
                            content = blueprint.recognitionAndMeasurement,
                            accentColor = Color(0xFF0D9488),
                            icon = Icons.Default.Calculate,
                            modifier = Modifier.weight(1f)
                        )
                        BlueprintPillarCard(
                            stepNumber = "3",
                            title = "Disclosures & Reporting",
                            content = blueprint.disclosure,
                            accentColor = Color(0xFFD97706),
                            icon = Icons.AutoMirrored.Filled.FactCheck,
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    // 3-Card Stacked Layout on phone screens
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BlueprintPillarCard(
                            stepNumber = "1",
                            title = "Scope & Applicability",
                            content = blueprint.scope,
                            accentColor = Color(0xFF0284C7),
                            icon = Icons.Default.CheckCircle,
                            modifier = Modifier.fillMaxWidth()
                        )
                        BlueprintPillarCard(
                            stepNumber = "2",
                            title = "Recognition & Measurement",
                            content = blueprint.recognitionAndMeasurement,
                            accentColor = Color(0xFF0D9488),
                            icon = Icons.Default.Calculate,
                            modifier = Modifier.fillMaxWidth()
                        )
                        BlueprintPillarCard(
                            stepNumber = "3",
                            title = "Disclosures & Reporting",
                            content = blueprint.disclosure,
                            accentColor = Color(0xFFD97706),
                            icon = Icons.AutoMirrored.Filled.FactCheck,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.35f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stepNumber,
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            FormattedText(
                text = content,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        ),
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
