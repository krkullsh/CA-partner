package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.curriculum.CaSyllabusData
import com.example.data.curriculum.CuratedDifferences
import com.example.data.curriculum.StrategyBlueprintReader
import com.example.data.model.ComparisonRow
import com.example.data.model.ConceptDifference
import com.example.data.model.ExperienceLevel
import com.example.data.model.PaperCode
import com.example.data.model.StatutoryCheatMatrix
import com.example.data.model.StatutoryMatrixRow
import com.example.data.model.SyllabusTopic
import com.example.ui.theme.Amber500
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.Sky500
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.CaLabViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

@Composable
fun SpotDifferenceScreen(viewModel: CaLabViewModel) {
    val state by viewModel.diffState.collectAsState()
    val bookmarkedIds by viewModel.bookmarkedIds.collectAsState()
    val customDiffs by viewModel.customDifferences.collectAsState()
    val expLevel by viewModel.experienceLevel.collectAsState()
    val progressMap by viewModel.topicProgressMap.collectAsState()

    // Combine curated and persistent custom differences
    val allDifferences = remember(customDiffs) {
        (CuratedDifferences.differences + customDiffs).distinctBy { it.id }
    }

    // Calculate count badges per paper
    val paperCounts = remember(allDifferences) {
        PaperCode.entries.associateWith { paper ->
            allDifferences.count { it.paper == paper }
        }
    }

    var modeSubTab by remember { mutableIntStateOf(0) } // 0 = Concept vs Concept, 1 = Statutory Cheat Matrices & Swipe Drill
    var activeMatrixPaper by remember { mutableStateOf(state.selectedPaper ?: PaperCode.PAPER_1) }
    var matrixSearchQuery by remember { mutableStateOf("") }

    // Sub-Mode A: Tap-to-Recall Mode (auto-enabled when KNOWLEDGED or EXAM_READY)
    var tapToRecallMode by remember {
        mutableStateOf(expLevel == ExperienceLevel.KNOWLEDGED || expLevel == ExperienceLevel.EXAM_READY)
    }

    // Sub-Mode B: Swipe Drill vs Table view toggle
    var isSwipeDrillActive by remember { mutableStateOf(false) }

    // Collapsible Topic Comparison State
    var isTopicComparisonExpanded by remember { mutableStateOf(false) }

    val currentPaperForPicker = state.selectedPaper ?: PaperCode.PAPER_1
    val availableTopics = remember(currentPaperForPicker) {
        CaSyllabusData.getTopicsForPaper(currentPaperForPicker)
    }
    var selectedTopicA by remember(currentPaperForPicker) { mutableStateOf(availableTopics.firstOrNull()) }
    var selectedTopicB by remember(currentPaperForPicker) { mutableStateOf(availableTopics.getOrNull(1) ?: availableTopics.firstOrNull()) }

    val filteredDifferences = remember(state.selectedPaper, state.searchQuery, state.showBookmarkedOnly, bookmarkedIds, allDifferences) {
        allDifferences.filter { diff ->
            (state.selectedPaper == null || diff.paper == state.selectedPaper) &&
            (!state.showBookmarkedOnly || bookmarkedIds.contains(diff.id)) &&
            (state.searchQuery.isBlank() ||
                    diff.title.contains(state.searchQuery, ignoreCase = true) ||
                    diff.conceptA.contains(state.searchQuery, ignoreCase = true) ||
                    diff.conceptB.contains(state.searchQuery, ignoreCase = true))
        }
    }
    val currentDiff = state.selectedDifference ?: filteredDifferences.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("spot_difference_screen")
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        // Main Tab Row: Concept vs Concept vs Statutory Cheat Matrices
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            TabRow(
                selectedTabIndex = modeSubTab,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = modeSubTab == 0,
                    onClick = { modeSubTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.CompareArrows, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Concept vs Concept (${allDifferences.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    },
                    modifier = Modifier.testTag("subtab_30_comparisons")
                )
                Tab(
                    selected = modeSubTab == 1,
                    onClick = { modeSubTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TableChart, contentDescription = null, modifier = Modifier.size(16.dp), tint = Amber500)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Excel Matrix & Drill", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (modeSubTab == 1) Amber500 else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    },
                    modifier = Modifier.testTag("subtab_statutory_matrices")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (modeSubTab == 0) {
            // -------------------------------------------------------------
            // SUB-MODE A: CONCEPT VS CONCEPT (BASIS-FIRST STACKED CARDS)
            // -------------------------------------------------------------
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Paper selection chips with Count Badges & Bookmarked Only toggle
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = state.showBookmarkedOnly,
                                onClick = { viewModel.toggleDiffBookmarkedOnly() },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (state.showBookmarkedOnly) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                label = { Text("Bookmarked", fontSize = 12.sp) },
                                modifier = Modifier.testTag("diff_bookmarked_chip"),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Amber500.copy(alpha = 0.2f),
                                    selectedLabelColor = Amber500
                                )
                            )
                        }
                        item {
                            FilterChip(
                                selected = state.selectedPaper == null,
                                onClick = { viewModel.selectDiffPaper(null) },
                                label = { Text("All 6 Papers (${allDifferences.size})", fontSize = 12.sp) },
                                modifier = Modifier.testTag("diff_paper_all")
                            )
                        }
                        items(PaperCode.entries, key = { it.name }) { paper ->
                            val isSelected = state.selectedPaper == paper
                            val count = paperCounts[paper] ?: 0
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.selectDiffPaper(paper) },
                                label = { Text("${paper.shortName} ($count)", fontSize = 12.sp) },
                                modifier = Modifier.testTag("diff_paper_${paper.name}")
                            )
                        }
                    }
                }

                // Tap-to-Recall Mode Toggle Bar
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (tapToRecallMode) Icons.Default.Lock else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = if (tapToRecallMode) Amber500 else Sky500,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Tap-to-Recall Active Memory", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        if (expLevel == ExperienceLevel.KNOWLEDGED || expLevel == ExperienceLevel.EXAM_READY) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = Amber500.copy(alpha = 0.15f)
                                            ) {
                                                Text("Auto-On", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Amber500, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                            }
                                        }
                                    }
                                    Text("Mask Concept B to test distinction before revealing", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Switch(
                                checked = tapToRecallMode,
                                onCheckedChange = { tapToRecallMode = it },
                                modifier = Modifier.testTag("tap_to_recall_switch")
                            )
                        }
                    }
                }

                // Collapsible "Compare Any 2 Syllabus Topics (AI)" Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("topic_compare_card"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isTopicComparisonExpanded = !isTopicComparisonExpanded },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text("Compare Any 2 Syllabus Topics (AI)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                                        Text("Pick two chapters or standards for an instant stacked comparison", fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                                    }
                                }
                                Icon(
                                    imageVector = if (isTopicComparisonExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Toggle Picker",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            AnimatedVisibility(visible = isTopicComparisonExpanded) {
                                Column(modifier = Modifier.padding(top = 12.dp)) {
                                    Text("Paper: ${currentPaperForPicker.title}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(modifier = Modifier.weight(1f)) {
                                            TopicDropdownPicker(
                                                label = "Topic A",
                                                topics = availableTopics,
                                                selectedTopic = selectedTopicA,
                                                onTopicSelected = { selectedTopicA = it },
                                                testTag = "topic_a_picker"
                                            )
                                        }

                                        Box(modifier = Modifier.weight(1f)) {
                                            TopicDropdownPicker(
                                                label = "Topic B",
                                                topics = availableTopics,
                                                selectedTopic = selectedTopicB,
                                                onTopicSelected = { selectedTopicB = it },
                                                testTag = "topic_b_picker"
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Button(
                                        onClick = {
                                            if (selectedTopicA != null && selectedTopicB != null) {
                                                viewModel.generateAiTopicComparison(selectedTopicA!!, selectedTopicB!!)
                                            }
                                        },
                                        enabled = selectedTopicA != null && selectedTopicB != null && !state.isGeneratingAiDifference,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(42.dp)
                                            .testTag("compare_topics_ai_button"),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        if (state.isGeneratingAiDifference) {
                                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Generating Comparison Matrix...", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        } else {
                                            Icon(Icons.AutoMirrored.Filled.CompareArrows, contentDescription = null, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Compare & Save to List (AI)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Search Bar
                item {
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = { viewModel.setDiffSearch(it) },
                        placeholder = { Text("Search comparisons (e.g. AS 2 vs AS 10, Sec 68 vs 77)...", fontSize = 12.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("diff_search_field"),
                        shape = RoundedCornerShape(20.dp),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (state.searchQuery.isNotBlank()) {
                                Button(
                                    onClick = { viewModel.generateAiDifference(state.searchQuery) },
                                    modifier = Modifier
                                        .padding(end = 4.dp)
                                        .height(36.dp)
                                        .testTag("compare_with_ai_button"),
                                    contentPadding = PaddingValues(horizontal = 8.dp),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    if (state.isGeneratingAiDifference) {
                                        CircularProgressIndicator(modifier = Modifier.size(14.dp), color = Color.White, strokeWidth = 2.dp)
                                    } else {
                                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Compare AI", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }

                if (state.aiErrorMessage != null) {
                    item {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = ErrorRed.copy(alpha = 0.15f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(state.aiErrorMessage!!, color = ErrorRed, fontSize = 12.sp, modifier = Modifier.weight(1f))
                                IconButton(onClick = { viewModel.dismissDiffAiError() }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = ErrorRed, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }

                // Horizontal scrollable comparison selection cards
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredDifferences, key = { it.id }) { diff ->
                            val isSelected = currentDiff?.id == diff.id
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clickable { viewModel.selectDifference(diff) }
                                    .testTag("diff_item_${diff.id}")
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                    Text(
                                        text = diff.title,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${diff.paper.shortName} • ${diff.pastExamFrequency}",
                                        fontSize = 10.sp,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }

                // Selected Comparison Details & Stacked Cards
                if (currentDiff != null) {
                    // Memory Hook Banner
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Amber500.copy(alpha = 0.12f)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Amber500, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("ICAI Exam Memory Hook 💡", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Amber500)
                                    Text(currentDiff.memoryHook, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }

                    // Examiner Trap Note if present
                    if (currentDiff.examinerTrapNote.isNotBlank()) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = ErrorRed.copy(alpha = 0.1f),
                                border = BorderStroke(1.dp, ErrorRed.copy(alpha = 0.35f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "🚨 ICAI Examiner Trap: ${currentDiff.examinerTrapNote}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ErrorRed
                                    )
                                }
                            }
                        }
                    }

                    // Comparison Header Banner Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${currentDiff.paper.title} • ${currentDiff.title}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                                        modifier = Modifier.weight(1f)
                                    )
                                    val isDiffBookmarked = bookmarkedIds.contains(currentDiff.id)
                                    IconButton(
                                        onClick = {
                                            viewModel.toggleBookmark(currentDiff.id, "DIFFERENCE", currentDiff.title, currentDiff.paper.title)
                                        },
                                        modifier = Modifier
                                            .size(24.dp)
                                            .testTag("bookmark_diff_button")
                                    ) {
                                        Icon(
                                            imageVector = if (isDiffBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                            contentDescription = "Bookmark Difference",
                                            tint = if (isDiffBookmarked) Amber500 else MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Sky500.copy(alpha = 0.2f),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = currentDiff.conceptA,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.sp,
                                            color = Sky500,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("VS", fontWeight = FontWeight.ExtraBold, fontSize = 10.sp, color = MaterialTheme.colorScheme.onPrimary)
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Amber500.copy(alpha = 0.2f),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = currentDiff.conceptB,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.sp,
                                            color = Amber500,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Stacked "Basis-First" Comparison Cards
                    items(currentDiff.comparisonPoints.indices.toList(), key = { "basis_$it" }) { index ->
                        val row = currentDiff.comparisonPoints[index]
                        BasisFirstStackedCard(
                            index = index + 1,
                            row = row,
                            conceptAName = currentDiff.conceptA,
                            conceptBName = currentDiff.conceptB,
                            isTapToRecall = tapToRecallMode
                        )
                    }
                }
            }
        } else {
            // -------------------------------------------------------------
            // SUB-MODE B: STATUTORY CHEAT MATRICES & TINDER SWIPE DRILL
            // -------------------------------------------------------------
            val context = LocalContext.current
            var selectedMatrixCategory by remember { mutableStateOf("All Rows") }
            val matrixCategories = remember { listOf("All Rows", "Statutory Limits", "Blueprint Tables", "Syllabus & Quotas") }

            val fullMatrixState by produceState<StatutoryCheatMatrix?>(
                initialValue = CuratedDifferences.getMatrixForPaper(activeMatrixPaper),
                activeMatrixPaper
            ) {
                value = withContext(Dispatchers.IO) {
                    StrategyBlueprintReader.getFullExcelMatrixForPaper(context, activeMatrixPaper)
                }
            }
            val matrix = fullMatrixState

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Top Action Row: Paper Picker & [🔥 Swipe Drill] Mode Toggle Button
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LazyRow(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(PaperCode.entries, key = { it.name }) { paper ->
                                val isSelected = activeMatrixPaper == paper
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { activeMatrixPaper = paper },
                                    label = { Text(paper.shortName, fontSize = 11.sp) },
                                    modifier = Modifier.testTag("matrix_paper_${paper.name}"),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Amber500.copy(alpha = 0.2f),
                                        selectedLabelColor = Amber500
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Swipe Drill Toggle Button
                        Button(
                            onClick = { isSwipeDrillActive = !isSwipeDrillActive },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSwipeDrillActive) Amber500 else MaterialTheme.colorScheme.primaryContainer,
                                contentColor = if (isSwipeDrillActive) Color.Black else MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("toggle_swipe_drill_button")
                        ) {
                            Icon(
                                imageVector = if (isSwipeDrillActive) Icons.Default.TableChart else Icons.Default.Whatshot,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (isSwipeDrillActive) Color.Black else Amber500
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isSwipeDrillActive) "Excel Sheet" else "🔥 Swipe Drill",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                if (isSwipeDrillActive) {
                    // SWIPE DRILL VIEW: Tinder-style Card Stack
                    item {
                        val drillTopics = remember(activeMatrixPaper) {
                            CaSyllabusData.getTopicsForPaper(activeMatrixPaper)
                        }
                        val drillRows = remember(matrix) {
                            matrix?.rows?.take(20) ?: emptyList()
                        }

                        TinderSwipeDrillSection(
                            paper = activeMatrixPaper,
                            topics = drillTopics,
                            matrixRows = drillRows,
                            progressMap = progressMap,
                            onSwipeMastered = { topicId ->
                                viewModel.recordSwipeDrillStatus(topicId, 2)
                            },
                            onSwipeNeedPractice = { topicId ->
                                viewModel.recordSwipeDrillStatus(topicId, 1)
                            }
                        )
                    }
                } else {
                    // EXCEL MATRIX VIEW: 1-Column Progressive Memory Expand
                    item {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(matrixCategories) { cat ->
                                val isSelected = selectedMatrixCategory == cat
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedMatrixCategory = cat },
                                    label = {
                                        Text(
                                            text = cat,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    modifier = Modifier.testTag("matrix_cat_${cat.replace(" ", "_").lowercase()}"),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )
                            }
                        }
                    }

                    // Search Field
                    item {
                        OutlinedTextField(
                            value = matrixSearchQuery,
                            onValueChange = { matrixSearchQuery = it },
                            placeholder = { Text("Filter matrix by Section, Limit, Penalty, or Rule...", fontSize = 12.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("matrix_search_field"),
                            shape = RoundedCornerShape(20.dp),
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            trailingIcon = {
                                if (matrixSearchQuery.isNotBlank()) {
                                    IconButton(onClick = { matrixSearchQuery = "" }) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                                    }
                                }
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Amber500,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    }

                    if (matrix != null) {
                        item {
                            ProgressiveMemoryMatrixCard(
                                matrix = matrix,
                                filterQuery = matrixSearchQuery,
                                selectedCategory = selectedMatrixCategory
                            )
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// SUB-MODE A: "BASIS-FIRST" STACKED CARD COMPONENT
// =========================================================================
@Composable
fun BasisFirstStackedCard(
    index: Int,
    row: ComparisonRow,
    conceptAName: String,
    conceptBName: String,
    isTapToRecall: Boolean
) {
    var isRevealed by remember(row, isTapToRecall) { mutableStateOf(!isTapToRecall) }
    var reviewStatus by remember(row) { mutableStateOf<Boolean?>(null) } // true = Got it, false = Review

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("diff_stacked_card_$index"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Basis of Comparison (Full Width)
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
                            text = "#$index",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = row.basisOfDifference,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Status Badge if rated
                reviewStatus?.let { gotIt ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (gotIt) SuccessGreen.copy(alpha = 0.15f) else Amber500.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = if (gotIt) "✓ Got it" else "🔁 Review",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (gotIt) SuccessGreen else Amber500,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Concept A Strip (Blue Accent, Full Width)
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Sky500.copy(alpha = 0.08f),
                border = BorderStroke(1.dp, Sky500.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "● $conceptAName",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Sky500
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = row.conceptAValue,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Concept B Strip (Amber Accent, Full Width with Tap-to-Recall Mask)
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Amber500.copy(alpha = 0.08f),
                border = BorderStroke(1.dp, Amber500.copy(alpha = 0.35f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !isRevealed) { isRevealed = true }
                    .testTag("concept_b_strip_$index")
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "● $conceptBName",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Amber500
                        )

                        if (!isRevealed) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = Amber500, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Tap to reveal", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Amber500)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    if (!isRevealed) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Amber500.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Visibility, contentDescription = null, tint = Amber500, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "🔒 Tap to reveal & compare answer",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Amber500
                                )
                            }
                        }
                    } else {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(tween(250))
                        ) {
                            Column {
                                Text(
                                    text = row.conceptBValue,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                if (isTapToRecall) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Self-Rating: ", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (reviewStatus == true) SuccessGreen else SuccessGreen.copy(alpha = 0.15f),
                                            modifier = Modifier.clickable { reviewStatus = true }
                                        ) {
                                            Text(
                                                text = "✓ Got it",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (reviewStatus == true) Color.White else SuccessGreen,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (reviewStatus == false) Amber500 else Amber500.copy(alpha = 0.15f),
                                            modifier = Modifier.clickable { reviewStatus = false }
                                        ) {
                                            Text(
                                                text = "🔁 Review",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (reviewStatus == false) Color.Black else Amber500,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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

// =========================================================================
// SUB-MODE B: 1-COLUMN PROGRESSIVE MEMORY EXPAND MATRIX
// =========================================================================
@Composable
fun ProgressiveMemoryMatrixCard(
    matrix: StatutoryCheatMatrix,
    filterQuery: String = "",
    selectedCategory: String = "All Rows"
) {
    // 0 = 1 Col (Self-Test), 1 = Expand Col 2, 2 = Full 3-Col Sheet
    var expandLevel by remember { mutableIntStateOf(0) }
    // Set of revealed row keys for individual cell peek
    val revealedCells = remember { mutableStateMapOf<String, Boolean>() }

    val filteredRows = remember(matrix, filterQuery, selectedCategory) {
        val categoryFiltered = when (selectedCategory) {
            "Statutory Limits" -> matrix.rows.filter {
                !it.penaltyOrException.contains("Quotas —") &&
                (it.sectionOrClause.startsWith("Sec") || it.sectionOrClause.startsWith("AS ") ||
                 it.sectionOrClause.startsWith("Clause") || it.sectionOrClause.startsWith("GST") ||
                 it.sectionOrClause.startsWith("FM") || it.sectionOrClause.startsWith("SM") ||
                 it.sectionOrClause.contains("Variance") || it.sectionOrClause.contains("Analysis"))
            }
            "Blueprint Tables" -> matrix.rows.filter {
                it.sectionOrClause.startsWith("Blueprint") ||
                it.thresholdOrLimit.contains("As per Blueprint") ||
                it.penaltyOrException.contains("Exam Trap & Blueprint")
            }
            "Syllabus & Quotas" -> matrix.rows.filter {
                it.penaltyOrException.contains("Quotas —") ||
                it.sectionOrClause.contains("Tier 1") || it.sectionOrClause.contains("Tier 2") ||
                it.sectionOrClause.contains("Tier 3") || it.sectionOrClause.contains("Tier 4")
            }
            else -> matrix.rows
        }

        if (filterQuery.isBlank()) categoryFiltered
        else categoryFiltered.filter { row ->
            row.sectionOrClause.contains(filterQuery, ignoreCase = true) ||
            row.provisionName.contains(filterQuery, ignoreCase = true) ||
            row.thresholdOrLimit.contains(filterQuery, ignoreCase = true) ||
            row.approvalOrRate.contains(filterQuery, ignoreCase = true) ||
            row.penaltyOrException.contains(filterQuery, ignoreCase = true)
        }
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.2.dp, Amber500.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("statutory_matrix_card_${matrix.paper.name}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.TableChart, contentDescription = null, tint = Amber500, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = matrix.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Excel Quick Reference • ${filteredRows.size} Entries",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 1-Column Progressive Expand Pill Bar
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Pill 1: 1 Col (Self-Test)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (expandLevel == 0) Amber500 else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { expandLevel = 0 }
                            .testTag("pill_1_col_test")
                    ) {
                        Text(
                            text = "1 Col (Self-Test)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (expandLevel == 0) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }

                    // Pill 2: + Expand Col 2
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (expandLevel == 1) Amber500 else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { expandLevel = 1 }
                            .testTag("pill_2_col_expand")
                    ) {
                        Text(
                            text = "+ Expand Col 2",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (expandLevel == 1) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }

                    // Pill 3: Full 3-Col Sheet
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (expandLevel == 2) Amber500 else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { expandLevel = 2 }
                            .testTag("pill_3_col_full")
                    ) {
                        Text(
                            text = "Full 3-Col Sheet",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (expandLevel == 2) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progressive Table View
            val horizontalScrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(horizontalScrollState)
            ) {
                // Table Header Row
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(vertical = 8.dp)
                ) {
                    Box(modifier = Modifier.width(180.dp).padding(horizontal = 8.dp)) {
                        Text(matrix.columnHeaders.getOrElse(0) { "Section / Clause" }, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    Box(modifier = Modifier.width(190.dp).padding(horizontal = 8.dp)) {
                        Text(matrix.columnHeaders.getOrElse(1) { "Provision Name" }, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }

                    if (expandLevel >= 1) {
                        Box(modifier = Modifier.width(180.dp).padding(horizontal = 8.dp)) {
                            Text(matrix.columnHeaders.getOrElse(2) { "Threshold / Limit" }, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }

                    if (expandLevel >= 2) {
                        Box(modifier = Modifier.width(220.dp).padding(horizontal = 8.dp)) {
                            Text(matrix.columnHeaders.getOrElse(3) { "Approval / Rate" }, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        Box(modifier = Modifier.width(260.dp).padding(horizontal = 8.dp)) {
                            Text(matrix.columnHeaders.getOrElse(4) { "Penalty / Exception" }, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                }

                // Table Data Rows
                filteredRows.forEachIndexed { rowIndex, row ->
                    val rowBg = if (rowIndex % 2 == 0) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    val cellKey = "${row.sectionOrClause}_$rowIndex"
                    val isCellPeeked = revealedCells[cellKey] == true

                    Row(
                        modifier = Modifier
                            .background(rowBg)
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Col 0: Section / Clause
                        Box(modifier = Modifier.width(180.dp).padding(horizontal = 8.dp)) {
                            Text(row.sectionOrClause, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Amber500)
                        }

                        // Col 1: Provision Name
                        Box(modifier = Modifier.width(190.dp).padding(horizontal = 8.dp)) {
                            Text(row.provisionName, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }

                        // Col 2: Threshold / Limit
                        if (expandLevel >= 1 || isCellPeeked) {
                            Box(modifier = Modifier.width(180.dp).padding(horizontal = 8.dp)) {
                                Text(row.thresholdOrLimit, fontSize = 11.sp, color = Sky500)
                            }
                        } else if (expandLevel == 0) {
                            Box(
                                modifier = Modifier
                                    .width(180.dp)
                                    .padding(horizontal = 8.dp)
                                    .clickable { revealedCells[cellKey] = true }
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Sky500.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "🔒 Peek Limit",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Sky500,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        // Col 3 & 4 (Full Sheet)
                        if (expandLevel >= 2) {
                            Box(modifier = Modifier.width(220.dp).padding(horizontal = 8.dp)) {
                                Text(row.approvalOrRate, fontSize = 11.sp)
                            }
                            Box(modifier = Modifier.width(260.dp).padding(horizontal = 8.dp)) {
                                Text(row.penaltyOrException, fontSize = 11.sp, color = ErrorRed)
                            }
                        }
                    }
                }

                if (filteredRows.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No rows match '$filterQuery'", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

// =========================================================================
// SUB-MODE B: TINDER-STYLE CARD SWIPE DRILL
// =========================================================================
data class SwipeDeckCard(
    val id: String,
    val topicId: String,
    val title: String,
    val subtitle: String,
    val keyRule: String,
    val pitfallOrException: String,
    val paperName: String
)

@Composable
fun TinderSwipeDrillSection(
    paper: PaperCode,
    topics: List<SyllabusTopic>,
    matrixRows: List<StatutoryMatrixRow>,
    progressMap: Map<String, com.example.data.local.TopicProgressEntity>,
    onSwipeMastered: (String) -> Unit,
    onSwipeNeedPractice: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // Build the drill card deck
    val cards = remember(paper, topics, matrixRows) {
        val topicCards = topics.map { topic ->
            SwipeDeckCard(
                id = "topic_${topic.id}",
                topicId = topic.id,
                title = topic.topicName,
                subtitle = "${topic.chapter} • ${topic.priorityTier.label.split("•")[0].trim()}",
                keyRule = topic.keyProvisions.firstOrNull() ?: topic.blueprint?.recognitionAndMeasurement?.take(160) ?: "Standard provisions under ICAI Material.",
                pitfallOrException = if (topic.commonExaminerPitfalls.isNotBlank()) "🚨 Trap: ${topic.commonExaminerPitfalls}" else "Ensure condition compliance.",
                paperName = paper.shortName
            )
        }
        val rowCards = matrixRows.take(10).mapIndexed { idx, row ->
            SwipeDeckCard(
                id = "row_${row.sectionOrClause}_$idx",
                topicId = topics.firstOrNull()?.id ?: "general_topic",
                title = "${row.sectionOrClause}: ${row.provisionName}",
                subtitle = "Statutory Matrix Rule • ${row.thresholdOrLimit}",
                keyRule = "Threshold: ${row.thresholdOrLimit} | Approval: ${row.approvalOrRate}",
                pitfallOrException = "🚨 Exception / Penalty: ${row.penaltyOrException}",
                paperName = paper.shortName
            )
        }
        (topicCards + rowCards).shuffled()
    }

    var currentIndex by remember(paper) { mutableIntStateOf(0) }
    val historyStack = remember { mutableListOf<Pair<Int, Int>>() } // (cardIndex, status: 1 or 2)
    var masteredCount by remember(paper) { mutableIntStateOf(0) }
    var needPracticeCount by remember(paper) { mutableIntStateOf(0) }

    val offsetX = remember { Animatable(0f) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Amber500.copy(alpha = 0.4f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("tinder_swipe_drill_container")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Deck Progress Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Whatshot, contentDescription = null, tint = Amber500, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Tinder Swipe Recall Drill", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = if (currentIndex < cards.size) "Card ${currentIndex + 1} of ${cards.size}" else "Complete!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar
            val progressFraction = if (cards.isNotEmpty()) (currentIndex.toFloat() / cards.size).coerceIn(0f, 1f) else 1f
            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Amber500,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (currentIndex < cards.size) {
                val currentCard = cards[currentIndex]
                val currentTopicProgress = progressMap[currentCard.topicId]
                val previousStatus = currentTopicProgress?.swipeDrillStatus ?: 0

                // Swipable Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .graphicsLayer {
                            translationX = offsetX.value
                            rotationZ = (offsetX.value / 40f).coerceIn(-15f, 15f)
                            alpha = (1f - (kotlin.math.abs(offsetX.value) / 800f)).coerceIn(0.5f, 1f)
                        }
                        .pointerInput(currentIndex) {
                            detectHorizontalDragGestures(
                                onDragEnd = {
                                    if (offsetX.value > 220f) {
                                        // Swipe Right (Mastered)
                                        coroutineScope.launch {
                                            offsetX.animateTo(1000f, tween(200))
                                            onSwipeMastered(currentCard.topicId)
                                            masteredCount++
                                            historyStack.add(currentIndex to 2)
                                            currentIndex++
                                            offsetX.snapTo(0f)
                                        }
                                    } else if (offsetX.value < -220f) {
                                        // Swipe Left (Need Practice)
                                        coroutineScope.launch {
                                            offsetX.animateTo(-1000f, tween(200))
                                            onSwipeNeedPractice(currentCard.topicId)
                                            needPracticeCount++
                                            historyStack.add(currentIndex to 1)
                                            currentIndex++
                                            offsetX.snapTo(0f)
                                        }
                                    } else {
                                        coroutineScope.launch { offsetX.animateTo(0f, spring()) }
                                    }
                                },
                                onHorizontalDrag = { change, dragAmount ->
                                    change.consume()
                                    coroutineScope.launch { offsetX.snapTo(offsetX.value + dragAmount) }
                                }
                            )
                        }
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("active_swipe_card_$currentIndex"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer
                                    ) {
                                        Text(
                                            text = currentCard.paperName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }

                                    // Previous Status Badge
                                    if (previousStatus == 2) {
                                        Surface(shape = RoundedCornerShape(6.dp), color = SuccessGreen.copy(alpha = 0.2f)) {
                                            Text("Already Mastered ✓", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SuccessGreen, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    } else if (previousStatus == 1) {
                                        Surface(shape = RoundedCornerShape(6.dp), color = Amber500.copy(alpha = 0.2f)) {
                                            Text("Flagged for Practice 🔁", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Amber500, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = currentCard.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    maxLines = 2
                                )
                                Text(
                                    text = currentCard.subtitle,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text("📌 Key Statutory Rule:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Sky500)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(currentCard.keyRule, fontSize = 11.sp, lineHeight = 16.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = ErrorRed.copy(alpha = 0.08f),
                                    border = BorderStroke(1.dp, ErrorRed.copy(alpha = 0.25f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = currentCard.pitfallOrException,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ErrorRed,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("← Swipe Left: Need Practice", fontSize = 10.sp, color = ErrorRed, fontWeight = FontWeight.Bold)
                                Text("Swipe Right: Mastered →", fontSize = 10.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Swipe Overlay Badges
                    if (offsetX.value > 60f) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SuccessGreen,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "MASTERED ✓",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    } else if (offsetX.value < -60f) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ErrorRed,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "NEED PRACTICE ↻",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Action Buttons: Left (Need Practice), Center (Undo), Right (Mastered)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Button: Need Practice
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                offsetX.animateTo(-1000f, tween(180))
                                onSwipeNeedPractice(currentCard.topicId)
                                needPracticeCount++
                                historyStack.add(currentIndex to 1)
                                currentIndex++
                                offsetX.snapTo(0f)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("swipe_button_need_practice")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Need Practice", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }

                    // Center Button: Undo
                    OutlinedButton(
                        onClick = {
                            if (historyStack.isNotEmpty()) {
                                val last = historyStack.removeAt(historyStack.lastIndex)
                                currentIndex = last.first
                                if (last.second == 2 && masteredCount > 0) masteredCount--
                                if (last.second == 1 && needPracticeCount > 0) needPracticeCount--
                            }
                        },
                        enabled = historyStack.isNotEmpty(),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("swipe_button_undo")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = "Undo", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Undo", fontSize = 11.sp)
                    }

                    // Right Button: Mastered
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                offsetX.animateTo(1000f, tween(180))
                                onSwipeMastered(currentCard.topicId)
                                masteredCount++
                                historyStack.add(currentIndex to 2)
                                currentIndex++
                                offsetX.snapTo(0f)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("swipe_button_mastered")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Mastered", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            } else {
                // Deck Completion State
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(SuccessGreen.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(32.dp))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Drill Complete!", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Mastered: $masteredCount • Need Practice: $needPracticeCount",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Chapters marked as 'Need Practice' are automatically pinned in your Progress Ledger for quick revision.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            currentIndex = 0
                            masteredCount = 0
                            needPracticeCount = 0
                            historyStack.clear()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Amber500, contentColor = Color.Black),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Restart Deck Drill", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// Dropdown picker helper
@Composable
fun TopicDropdownPicker(
    label: String,
    topics: List<SyllabusTopic>,
    selectedTopic: SyllabusTopic?,
    onTopicSelected: (SyllabusTopic) -> Unit,
    testTag: String
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = selectedTopic?.topicName ?: "Select Topic",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )
                }
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            topics.forEach { topic ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(topic.topicName, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                            Text(topic.chapter, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    },
                    onClick = {
                        onTopicSelected(topic)
                        expanded = false
                    }
                )
            }
        }
    }
}
