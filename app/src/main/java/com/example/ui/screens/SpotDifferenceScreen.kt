package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
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
import com.example.data.curriculum.CuratedDifferences
import com.example.data.curriculum.StrategyBlueprintReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.data.model.ComparisonRow
import com.example.data.model.ConceptDifference
import com.example.data.model.PaperCode
import com.example.data.model.StatutoryCheatMatrix
import com.example.data.model.SyllabusTopic
import com.example.ui.theme.Amber500
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.Sky500
import com.example.ui.viewmodel.CaLabViewModel

@Composable
fun SpotDifferenceScreen(viewModel: CaLabViewModel) {
    val state by viewModel.diffState.collectAsState()
    val bookmarkedIds by viewModel.bookmarkedIds.collectAsState()
    val customDiffs by viewModel.customDifferences.collectAsState()

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

    var modeSubTab by remember { mutableIntStateOf(0) } // 0 = Concept vs Concept (30+), 1 = Statutory Cheat Matrices
    var activeMatrixPaper by remember { mutableStateOf(PaperCode.PAPER_1) }
    var matrixSearchQuery by remember { mutableStateOf("") }

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

        // Mode Selector: Concept vs Concept (30+) vs Statutory Cheat Matrices
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
                            Icon(Icons.Default.CompareArrows, contentDescription = null, modifier = Modifier.size(16.dp))
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
                            Text("Excel Cheat Matrices", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (modeSubTab == 1) Amber500 else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    },
                    modifier = Modifier.testTag("subtab_statutory_matrices")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (modeSubTab == 0) {
            // MODE 0: CONCEPT VS CONCEPT (30+ HIGH-YIELD COMPARISONS)
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
                                        Text("Pick two chapters or standards for an instant 3-column matrix", fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
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
                                        // Topic A Picker
                                        Box(modifier = Modifier.weight(1f)) {
                                            TopicDropdownPicker(
                                                label = "Topic A",
                                                topics = availableTopics,
                                                selectedTopic = selectedTopicA,
                                                onTopicSelected = { selectedTopicA = it },
                                                testTag = "topic_a_picker"
                                            )
                                        }

                                        // Topic B Picker
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
                                            Icon(Icons.Default.CompareArrows, contentDescription = null, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Compare & Save to List (AI)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Search bar with Compare with AI button
                item {
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = { viewModel.setDiffSearch(it) },
                        placeholder = { Text("Search comparisons or type custom concepts...", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("diff_search_field"),
                        shape = RoundedCornerShape(20.dp),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (state.searchQuery.isNotBlank()) {
                                Button(
                                    onClick = { viewModel.generateAiDifference(state.searchQuery) },
                                    modifier = Modifier.padding(end = 4.dp).height(36.dp).testTag("compare_with_ai_button"),
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

                // Horizontal scrollable comparison cards row
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

                // Self-quiz toggle card
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Quiz, contentDescription = null, tint = Sky500, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Self-Quiz Mode", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Hide differences to test yourself first", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Switch(
                                checked = state.isSelfQuizMode,
                                onCheckedChange = { viewModel.toggleSelfQuizMode() },
                                modifier = Modifier.testTag("self_quiz_switch")
                            )
                        }
                    }
                }

                // Main Comparison Matrix
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

                    // Comparison Header Card
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
                                        modifier = Modifier.size(24.dp).testTag("bookmark_diff_button")
                                    ) {
                                        Icon(
                                            imageVector = if (isDiffBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                            contentDescription = "Bookmark Difference",
                                            tint = if (isDiffBookmarked) Amber500 else MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = currentDiff.conceptA,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Box(
                                        modifier = Modifier.size(28.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("VS", fontWeight = FontWeight.ExtraBold, fontSize = 10.sp, color = MaterialTheme.colorScheme.onPrimary)
                                    }
                                    Text(
                                        text = currentDiff.conceptB,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.weight(1f),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.End
                                    )
                                }
                            }
                        }
                    }

                    // Excel-Style 3-Column Table Header
                    item {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Basis of Difference", fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onPrimaryContainer)
                                Text(currentDiff.conceptA, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, modifier = Modifier.weight(1.2f), color = MaterialTheme.colorScheme.onPrimaryContainer)
                                Text(currentDiff.conceptB, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, modifier = Modifier.weight(1.2f), color = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                        }
                    }

                    // Comparison Rows
                    items(currentDiff.comparisonPoints.indices.toList(), key = { it }) { index ->
                        val row = currentDiff.comparisonPoints[index]
                        val isRevealed = !state.isSelfQuizMode || state.revealedColumns.contains(index)
                        ComparisonRowCard(
                            row = row,
                            isQuizMode = state.isSelfQuizMode,
                            isRevealed = isRevealed,
                            onReveal = { viewModel.revealQuizRow(index) }
                        )
                    }
                }
            }
        } else {
            // MODE 1: STATUTORY CHEAT MATRICES (EXCEL-STYLE)
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
                // Paper Selection Chips
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(PaperCode.entries, key = { it.name }) { paper ->
                            val isSelected = activeMatrixPaper == paper
                            FilterChip(
                                selected = isSelected,
                                onClick = { activeMatrixPaper = paper },
                                label = { Text(paper.shortName, fontSize = 12.sp) },
                                modifier = Modifier.testTag("matrix_paper_${paper.name}"),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Amber500.copy(alpha = 0.2f),
                                    selectedLabelColor = Amber500
                                )
                            )
                        }
                    }
                }

                // Category Filter Chips (All Rows, Statutory Limits, Blueprint Tables, Syllabus & Quotas)
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

                // Matrix Search Filter
                item {
                    OutlinedTextField(
                        value = matrixSearchQuery,
                        onValueChange = { matrixSearchQuery = it },
                        placeholder = { Text("Filter matrix by Section, Limit, Penalty, or Rule...", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("matrix_search_field"),
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
                        StatutoryCheatMatrixCard(
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
            modifier = Modifier.fillMaxWidth().testTag(testTag),
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

@Composable
fun ComparisonRowCard(
    row: ComparisonRow,
    isQuizMode: Boolean,
    isRevealed: Boolean,
    onReveal: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().testTag("diff_row_${row.basisOfDifference.take(6)}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "📌 ${row.basisOfDifference}",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Concept A Box
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = row.conceptAValue,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                // Concept B Box
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(enabled = isQuizMode && !isRevealed) { onReveal() }
                ) {
                    if (isRevealed) {
                        Text(
                            text = row.conceptBValue,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Visibility, contentDescription = null, tint = Sky500, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Tap to Reveal", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Sky500)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatutoryCheatMatrixCard(
    matrix: StatutoryCheatMatrix,
    filterQuery: String = "",
    selectedCategory: String = "All Rows"
) {
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
        modifier = Modifier.fillMaxWidth().testTag("statutory_matrix_card_${matrix.paper.name}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
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
                            text = "Excel-Style Master Quick Reference • Category: $selectedCategory",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Amber500.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, Amber500.copy(alpha = 0.35f))
                ) {
                    Text(
                        text = "Total Entries: ${filteredRows.size}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Amber500,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Column widths helper
            fun getColumnWidth(index: Int) = when (index) {
                0 -> 160.dp
                1 -> 180.dp
                2 -> 170.dp
                3 -> 230.dp
                else -> 260.dp
            }

            // Horizontal Scrollable Matrix Table
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
                    matrix.columnHeaders.forEachIndexed { colIndex, header ->
                        Box(
                            modifier = Modifier
                                .width(getColumnWidth(colIndex))
                                .padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = header,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                // Table Data Rows
                filteredRows.forEachIndexed { rowIndex, row ->
                    val rowBg = if (rowIndex % 2 == 0) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    Row(
                        modifier = Modifier
                            .background(rowBg)
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.width(getColumnWidth(0)).padding(horizontal = 8.dp)) {
                            Text(row.sectionOrClause, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Amber500)
                        }
                        Box(modifier = Modifier.width(getColumnWidth(1)).padding(horizontal = 8.dp)) {
                            Text(row.provisionName, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Box(modifier = Modifier.width(getColumnWidth(2)).padding(horizontal = 8.dp)) {
                            Text(row.thresholdOrLimit, fontSize = 11.sp, color = Sky500)
                        }
                        Box(modifier = Modifier.width(getColumnWidth(3)).padding(horizontal = 8.dp)) {
                            Text(row.approvalOrRate, fontSize = 11.sp)
                        }
                        Box(modifier = Modifier.width(getColumnWidth(4)).padding(horizontal = 8.dp)) {
                            Text(row.penaltyOrException, fontSize = 11.sp, color = ErrorRed)
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
                        val emptyMsg = if (filterQuery.isNotBlank()) "No rows match '$filterQuery' in $selectedCategory" else "No rows found in category '$selectedCategory'"
                        Text(emptyMsg, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
