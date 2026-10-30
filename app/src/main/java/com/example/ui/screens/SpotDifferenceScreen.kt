package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.curriculum.CaSyllabusData
import com.example.data.curriculum.CuratedDifferences
import com.example.data.curriculum.IntegratedNotesRepository
import com.example.data.curriculum.NoteExamComparison
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

/**
 * Modern Notion-Style borderless left-accent callout strip for dark mode safety & clean reading.
 */
@Composable
private fun DiffNotionCallout(
    accentColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(accentColor.copy(alpha = 0.08f))
            .drawBehind {
                drawLine(
                    color = accentColor,
                    start = Offset(1.5.dp.toPx(), 0f),
                    end = Offset(1.5.dp.toPx(), size.height),
                    strokeWidth = 3.dp.toPx()
                )
            }
            .padding(start = 11.dp, end = 9.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Column(content = content)
    }
}

@Composable
fun SpotDifferenceScreen(viewModel: CaLabViewModel) {
    val state by viewModel.diffState.collectAsState()
    val bookmarkedIds by viewModel.bookmarkedIds.collectAsState()
    val customDiffs by viewModel.customDifferences.collectAsState()
    val expLevel by viewModel.experienceLevel.collectAsState()
    val progressMap by viewModel.topicProgressMap.collectAsState()

    // Convert IntegratedNotesRepository.examComparisons into ConceptDifference objects so they lead the list
    val noteDerivedDifferences = remember {
        IntegratedNotesRepository.examComparisons.map { noteComp ->
            ConceptDifference(
                id = noteComp.id,
                paper = noteComp.paper,
                title = "${noteComp.conceptA} vs ${noteComp.conceptB} (${noteComp.chapterShortTag})",
                conceptA = noteComp.conceptA,
                conceptB = noteComp.conceptB,
                pastExamFrequency = noteComp.examProbabilityBadge,
                comparisonPoints = noteComp.basisRows.map { (basis, valA, valB) ->
                    ComparisonRow(
                        basisOfDifference = basis,
                        conceptAValue = valA,
                        conceptBValue = valB
                    )
                },
                examinerTrapNote = noteComp.examinerObservation,
                memoryHook = noteComp.pastPaperTrend
            )
        }
    }

    val noteComparisonMap = remember {
        IntegratedNotesRepository.examComparisons.associateBy { it.id }
    }

    // Combine note-derived PYQ comparisons + curated + custom differences
    val allDifferences = remember(customDiffs, noteDerivedDifferences) {
        (noteDerivedDifferences + CuratedDifferences.differences + customDiffs).distinctBy { it.id }
    }

    var modeSubTab by remember { mutableIntStateOf(0) } // 0 = Concept vs Concept, 1 = Excel Matrix & Drill
    var activeMatrixPaper by remember { mutableStateOf(state.selectedPaper ?: PaperCode.PAPER_1) }
    var selectedMatrixCategory by remember { mutableStateOf("All Rows") }
    var matrixSearchQuery by remember { mutableStateOf("") }

    var tapToRecallMode by remember {
        mutableStateOf(expLevel == ExperienceLevel.KNOWLEDGED || expLevel == ExperienceLevel.EXAM_READY)
    }

    var isSwipeDrillActive by remember { mutableStateOf(false) }
    var showFilterDropdown by remember { mutableStateOf(false) }
    var showSearchAndAiPanel by remember { mutableStateOf(false) }

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
    val currentDiff = state.selectedDifference?.takeIf { sel -> filteredDifferences.any { it.id == sel.id } }
        ?: filteredDifferences.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .testTag("spot_difference_screen")
    ) {
        Spacer(modifier = Modifier.height(2.dp))

        // ULTRA-COMPACT 26.DP TOP SWITCHER + SINGLE [FILTER ▾] DROPDOWN
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mode Switcher Pill (26.dp)
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (modeSubTab == 0) MaterialTheme.colorScheme.surface else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .height(24.dp)
                            .clickable { modeSubTab = 0 }
                            .testTag("subtab_30_comparisons")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Concept vs Concept (${filteredDifferences.size})",
                                fontSize = 10.5.sp,
                                fontWeight = if (modeSubTab == 0) FontWeight.Bold else FontWeight.Medium,
                                color = if (modeSubTab == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (modeSubTab == 1) MaterialTheme.colorScheme.surface else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .height(24.dp)
                            .clickable { modeSubTab = 1 }
                            .testTag("subtab_statutory_matrices")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Excel Matrix & Drill",
                                fontSize = 10.5.sp,
                                fontWeight = if (modeSubTab == 1) FontWeight.Bold else FontWeight.Medium,
                                color = if (modeSubTab == 1) Amber500 else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Single Compact [Filter ▾] Dropdown (Hides all secondary buttons & matrix categories)
            Box {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .height(26.dp)
                        .clickable { showFilterDropdown = true }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = state.selectedPaper?.shortName ?: "All Papers",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Filters & Tools",
                            modifier = Modifier.size(13.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                DropdownMenu(
                    expanded = showFilterDropdown,
                    onDismissRequest = { showFilterDropdown = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                if (tapToRecallMode) "✓ Tap-to-Recall Masking: ON" else "Tap-to-Recall Masking: OFF",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        leadingIcon = {
                            Icon(
                                if (tapToRecallMode) Icons.Default.Lock else Icons.Default.Visibility,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp)
                            )
                        },
                        onClick = {
                            tapToRecallMode = !tapToRecallMode
                            showFilterDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                if (showSearchAndAiPanel) "Hide Search & AI Compare" else "Search / Compare 2 Topics (AI)",
                                fontSize = 11.5.sp
                            )
                        },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(15.dp)) },
                        onClick = {
                            showSearchAndAiPanel = !showSearchAndAiPanel
                            showFilterDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                if (state.showBookmarkedOnly) "✓ Showing Bookmarked Only" else "Show Bookmarked Only",
                                fontSize = 11.5.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                if (state.showBookmarkedOnly) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp)
                            )
                        },
                        onClick = {
                            viewModel.toggleDiffBookmarkedOnly()
                            showFilterDropdown = false
                        }
                    )
                    HorizontalDivider(thickness = 0.5.dp)
                    // Matrix Categories inside filter dropdown
                    listOf("All Rows", "Statutory Limits", "Blueprint Tables").forEach { cat ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = if (selectedMatrixCategory == cat) "✓ Category: $cat" else "Category: $cat",
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedMatrixCategory == cat) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedMatrixCategory == cat) Amber500 else MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                selectedMatrixCategory = cat
                                showFilterDropdown = false
                            }
                        )
                    }
                    HorizontalDivider(thickness = 0.5.dp)
                    DropdownMenuItem(
                        text = { Text("All 6 Papers (${allDifferences.size})", fontSize = 11.sp) },
                        onClick = {
                            viewModel.selectDiffPaper(null)
                            showFilterDropdown = false
                        }
                    )
                    PaperCode.entries.forEach { paper ->
                        DropdownMenuItem(
                            text = { Text(paper.title, fontSize = 11.sp) },
                            onClick = {
                                viewModel.selectDiffPaper(paper)
                                activeMatrixPaper = paper
                                showFilterDropdown = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (modeSubTab == 0) {
            // =================================================================
            // SUB-MODE A: CONCEPT VS CONCEPT (PYQ PROBABILITY + EXAM QUESTIONS)
            // =================================================================
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Collapsible Search & AI Compare Panel (Hidden unless triggered from Filter menu)
                if (showSearchAndAiPanel) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("topic_compare_card"),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                OutlinedTextField(
                                    value = state.searchQuery,
                                    onValueChange = { viewModel.setDiffSearch(it) },
                                    placeholder = { Text("Search comparisons (e.g. Cum vs Ex, TDS vs TCS)...", fontSize = 11.sp) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(42.dp)
                                        .testTag("diff_search_field"),
                                    shape = RoundedCornerShape(6.dp),
                                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(15.dp)) },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                    )
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
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

                                Spacer(modifier = Modifier.height(6.dp))

                                Surface(
                                    shape = RoundedCornerShape(5.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(26.dp)
                                        .clickable(enabled = selectedTopicA != null && selectedTopicB != null && !state.isGeneratingAiDifference) {
                                            if (selectedTopicA != null && selectedTopicB != null) {
                                                viewModel.generateAiTopicComparison(selectedTopicA!!, selectedTopicB!!)
                                            }
                                        }
                                        .testTag("compare_topics_ai_button")
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = if (state.isGeneratingAiDifference) "Generating..." else "Generate PYQ Comparison (AI)",
                                            fontSize = 10.5.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Compact Single-Line Horizontal Topic Selector Strip (height 26.dp)
                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(26.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(filteredDifferences, key = { it.id }) { diff ->
                            val isSelected = currentDiff?.id == diff.id
                            val isFromNotes = noteComparisonMap.containsKey(diff.id)
                            Surface(
                                shape = RoundedCornerShape(5.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                border = BorderStroke(
                                    0.5.dp,
                                    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                ),
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .clickable { viewModel.selectDifference(diff) }
                                    .testTag("diff_item_${diff.id}")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 7.dp)
                                ) {
                                    if (isFromNotes) {
                                        Text(
                                            text = "★ ",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else Amber500
                                        )
                                    }
                                    Text(
                                        text = "${diff.conceptA} vs ${diff.conceptB}",
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }

                if (currentDiff != null) {
                    val linkedNoteComparison = noteComparisonMap[currentDiff.id]

                    // Compact Comparison Title & Exam Probability Header
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.28f))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Amber500.copy(alpha = 0.14f)
                                    ) {
                                        Text(
                                            text = "🔥 ${currentDiff.pastExamFrequency}",
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Amber500,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }

                                    val isDiffBookmarked = bookmarkedIds.contains(currentDiff.id)
                                    IconButton(
                                        onClick = {
                                            viewModel.toggleBookmark(currentDiff.id, "DIFFERENCE", currentDiff.title, currentDiff.paper.title)
                                        },
                                        modifier = Modifier
                                            .size(22.dp)
                                            .testTag("bookmark_diff_button")
                                    ) {
                                        Icon(
                                            imageVector = if (isDiffBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                            contentDescription = "Bookmark",
                                            tint = if (isDiffBookmarked) Amber500 else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = currentDiff.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                if (currentDiff.memoryHook.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = "📌 PYQ Focus: ${currentDiff.memoryHook}",
                                        fontSize = 10.5.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    // Basis-First Stacked Cards (with NotionCallout and auto-bookmark on review)
                    items(currentDiff.comparisonPoints.indices.toList(), key = { "basis_$it" }) { index ->
                        val row = currentDiff.comparisonPoints[index]
                        BasisFirstStackedCard(
                            index = index + 1,
                            row = row,
                            conceptAName = currentDiff.conceptA,
                            conceptBName = currentDiff.conceptB,
                            isTapToRecall = tapToRecallMode,
                            onReviewTapped = {
                                if (!bookmarkedIds.contains(currentDiff.id)) {
                                    viewModel.toggleBookmark(
                                        currentDiff.id,
                                        "DIFFERENCE",
                                        currentDiff.title,
                                        currentDiff.paper.title
                                    )
                                }
                            }
                        )
                    }

                    // EXAM-BASED QUESTION (PYQ / RTP APPLICATION) CARD
                    item {
                        ExamApplicationDifferenceCard(
                            diff = currentDiff,
                            noteComparison = linkedNoteComparison
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }
            }
        } else {
            // =================================================================
            // SUB-MODE B: PROGRESSIVE EXCEL MATRIX & TINDER SWIPE DRILL
            // =================================================================
            val context = LocalContext.current

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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isSwipeDrillActive) {
                    item {
                        val drillTopics = remember(activeMatrixPaper) {
                            CaSyllabusData.getTopicsForPaper(activeMatrixPaper)
                        }
                        val drillRows = remember(matrix) {
                            matrix?.rows?.filterNot { row ->
                                row.sectionOrClause.contains("Col 1", ignoreCase = true) ||
                                row.provisionName.contains("Col 2", ignoreCase = true) ||
                                row.sectionOrClause.contains("---") ||
                                row.sectionOrClause.startsWith("Statutory Test", ignoreCase = true)
                            }?.take(20) ?: emptyList()
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
                            },
                            onBackToTable = { isSwipeDrillActive = false }
                        )
                    }
                } else {
                    if (showSearchAndAiPanel) {
                        item {
                            OutlinedTextField(
                                value = matrixSearchQuery,
                                onValueChange = { matrixSearchQuery = it },
                                placeholder = { Text("Filter matrix by Section, Limit, or Rule...", fontSize = 11.sp) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                                    .testTag("matrix_search_field"),
                                shape = RoundedCornerShape(6.dp),
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(15.dp)) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                )
                            )
                        }
                    }

                    if (matrix != null) {
                        item {
                            ProgressiveMemoryMatrixCard(
                                matrix = matrix,
                                filterQuery = matrixSearchQuery,
                                selectedCategory = selectedMatrixCategory,
                                isSwipeDrillActive = isSwipeDrillActive,
                                onToggleSwipeDrill = { isSwipeDrillActive = !isSwipeDrillActive }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * EXAM-BASED QUESTION (PYQ / RTP APPLICATION) CARD
 * Uses exact question and solution without robotic repetition or "Reserve Reserve" phrasing.
 */
@Composable
fun ExamApplicationDifferenceCard(
    diff: ConceptDifference,
    noteComparison: NoteExamComparison? = null,
    modifier: Modifier = Modifier
) {
    var isSolutionRevealed by remember(diff.id) { mutableStateOf(false) }

    val questionText = noteComparison?.examBasedQuestion
        ?: "EXAM QUESTION (${diff.paper.shortName} • ${diff.pastExamFrequency}):\nExplain the key statutory and accounting distinctions between '${diff.conceptA}' and '${diff.conceptB}' with reference to ${diff.comparisonPoints.take(3).joinToString(", ") { it.basisOfDifference }}. How do these differences impact financial statement presentation or compliance?"

    val solutionText = noteComparison?.icaiExamSolution
        ?: buildString {
            diff.comparisonPoints.forEachIndexed { idx, pt ->
                append("${idx + 1}. Basis: ${pt.basisOfDifference}\n")
                append("   • ${diff.conceptA}: ${pt.conceptAValue}\n")
                append("   • ${diff.conceptB}: ${pt.conceptBValue}\n\n")
            }
        }.trimEnd()

    val examinerObservationText = noteComparison?.examinerObservation
        ?: diff.examinerTrapNote.ifBlank {
            "Always present the Basis of Difference in a dedicated column and cite exact statutory limits/AS numbers to secure full step marks."
        }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("diff_exam_application_card"),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, Amber500.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Quiz,
                        contentDescription = null,
                        tint = Amber500,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "EXAM-BASED QUESTION (PYQ / RTP APPLICATION)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Amber500
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                    modifier = Modifier
                        .height(24.dp)
                        .clickable { isSolutionRevealed = !isSolutionRevealed }
                        .testTag("toggle_solution_examiner_note_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 7.dp)
                    ) {
                        Text(
                            text = if (isSolutionRevealed) "Hide Solution ▲" else "ICAI Answer ▼",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = questionText,
                fontSize = 11.5.sp,
                lineHeight = 17.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (isSolutionRevealed) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "✓ ICAI STEP-BY-STEP SOLUTION:",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = SuccessGreen
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = solutionText,
                    fontSize = 11.sp,
                    lineHeight = 17.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))
                DiffNotionCallout(accentColor = ErrorRed) {
                    Text(
                        text = "⚠️ Examiner Observation: $examinerObservationText",
                        fontSize = 10.5.sp,
                        lineHeight = 15.sp,
                        color = ErrorRed
                    )
                }
            }
        }
    }
}

@Composable
fun BasisFirstStackedCard(
    index: Int,
    row: ComparisonRow,
    conceptAName: String,
    conceptBName: String,
    isTapToRecall: Boolean,
    onReviewTapped: () -> Unit = {}
) {
    var isRevealed by remember(row, isTapToRecall) { mutableStateOf(!isTapToRecall) }
    var reviewStatus by remember(row) { mutableStateOf<Boolean?>(null) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("diff_stacked_card_$index"),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.24f))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$index. ${row.basisOfDifference}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.5.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                reviewStatus?.let { gotIt ->
                    Text(
                        text = if (gotIt) "✓ Got it" else "🔁 Review",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (gotIt) SuccessGreen else Amber500
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Concept A (Notion-style Sky callout)
            DiffNotionCallout(accentColor = Sky500) {
                Text(
                    text = conceptAName,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Sky500
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = row.conceptAValue,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            // Concept B (Notion-style Amber callout)
            DiffNotionCallout(
                accentColor = Amber500,
                modifier = Modifier
                    .clickable(enabled = !isRevealed) { isRevealed = true }
                    .testTag("concept_b_strip_$index")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conceptBName,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Amber500
                    )
                    if (!isRevealed) {
                        Text(
                            text = "🔒 Tap to reveal",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Amber500
                        )
                    }
                }

                if (isRevealed) {
                    Spacer(modifier = Modifier.height(2.dp))
                    AnimatedVisibility(visible = true, enter = fadeIn(tween(180))) {
                        Column {
                            Text(
                                text = row.conceptBValue,
                                fontSize = 11.sp,
                                lineHeight = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (isTapToRecall) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = "✓ Got it",
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessGreen,
                                        modifier = Modifier
                                            .clickable { reviewStatus = true }
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "🔁 Review",
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Amber500,
                                        modifier = Modifier
                                            .clickable {
                                                reviewStatus = false
                                                onReviewTapped()
                                            }
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
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

/**
 * Responsive Full-Width Memory Matrix Card.
 * NO horizontal scroll clipping: Left col (32% weight) + Right col (68% weight).
 * Filters out raw markdown table headers ("Col 1", "Col 2", "---", etc.).
 */
@Composable
fun ProgressiveMemoryMatrixCard(
    matrix: StatutoryCheatMatrix,
    filterQuery: String = "",
    selectedCategory: String = "All Rows",
    isSwipeDrillActive: Boolean = false,
    onToggleSwipeDrill: () -> Unit = {}
) {
    var expandLevel by remember { mutableIntStateOf(0) }
    val revealedCells = remember { mutableStateMapOf<String, Boolean>() }

    val filteredRows = remember(matrix, filterQuery, selectedCategory) {
        // Filter out markdown headers and table divider noise
        val cleanRows = matrix.rows.filterNot { row ->
            row.sectionOrClause.contains("Col 1", ignoreCase = true) ||
            row.provisionName.contains("Col 2", ignoreCase = true) ||
            row.sectionOrClause.contains("---") ||
            row.sectionOrClause.startsWith("Statutory Test", ignoreCase = true)
        }

        val categoryFiltered = when (selectedCategory) {
            "Statutory Limits" -> cleanRows.filter {
                !it.penaltyOrException.contains("Quotas —") &&
                (it.sectionOrClause.startsWith("Sec") || it.sectionOrClause.startsWith("AS ") ||
                 it.sectionOrClause.startsWith("Clause") || it.sectionOrClause.startsWith("GST") ||
                 it.sectionOrClause.startsWith("FM") || it.sectionOrClause.startsWith("SM") ||
                 it.sectionOrClause.contains("Variance") || it.sectionOrClause.contains("Analysis"))
            }
            "Blueprint Tables" -> cleanRows.filter {
                it.sectionOrClause.startsWith("Blueprint") ||
                it.thresholdOrLimit.contains("As per Blueprint") ||
                it.penaltyOrException.contains("Exam Trap & Blueprint")
            }
            else -> cleanRows
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
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.28f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("statutory_matrix_card_${matrix.paper.name}")
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Header Row (26.dp): Title + [1 Col | 2 Col | Full] + [🔥 Swipe Drill]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${matrix.title} (${filteredRows.size})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    listOf("1 Col" to 0, "2 Col" to 1, "Full" to 2).forEach { (lbl, lvl) ->
                        val testTagName = when (lvl) {
                            0 -> "pill_1_col_test"
                            1 -> "pill_2_col_expand"
                            else -> "pill_3_col_full"
                        }
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (expandLevel == lvl) Amber500 else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .height(24.dp)
                                .clickable { expandLevel = lvl }
                                .testTag(testTagName)
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 5.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = lbl,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (expandLevel == lvl) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(3.dp))

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (isSwipeDrillActive) Amber500 else MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier
                            .height(24.dp)
                            .clickable { onToggleSwipeDrill() }
                            .testTag("toggle_swipe_drill_button")
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isSwipeDrillActive) "Table View" else "🔥 Swipe Drill",
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.5.sp,
                                color = if (isSwipeDrillActive) Color.Black else MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Responsive Full-Width Table (No horizontal scroll clipping)
            Column(modifier = Modifier.fillMaxWidth()) {
                filteredRows.forEachIndexed { rowIndex, row ->
                    val rowBg = if (rowIndex % 2 == 0) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                    val cellKey = "${row.sectionOrClause}_$rowIndex"
                    val isCellPeeked = revealedCells[cellKey] == true

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(rowBg)
                            .padding(horizontal = 6.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Left Column (32% weight): Section + Provision Name
                        Column(
                            modifier = Modifier
                                .weight(0.32f)
                                .padding(end = 6.dp)
                        ) {
                            Text(
                                text = row.sectionOrClause,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = Amber500
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = row.provisionName,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 14.sp
                            )
                        }

                        // Right Column (68% weight): Threshold / Limit / Rule / Exception
                        Column(
                            modifier = Modifier.weight(0.68f)
                        ) {
                            if (expandLevel == 0 && !isCellPeeked) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                    border = BorderStroke(0.5.dp, Sky500.copy(alpha = 0.4f)),
                                    modifier = Modifier
                                        .clickable { revealedCells[cellKey] = true }
                                        .padding(vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "🔒 Tap to Recall Rule & Limit",
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Sky500,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            } else {
                                Text(
                                    text = row.thresholdOrLimit,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Sky500,
                                    lineHeight = 15.sp
                                )
                                if (row.approvalOrRate.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = row.approvalOrRate,
                                        fontSize = 10.5.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 14.sp
                                    )
                                }
                                if (expandLevel >= 2 || isCellPeeked) {
                                    if (row.penaltyOrException.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = "⚠️ ${row.penaltyOrException}",
                                            fontSize = 10.sp,
                                            color = ErrorRed,
                                            lineHeight = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                    HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
                }
            }
        }
    }
}

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
    onSwipeNeedPractice: (String) -> Unit,
    onBackToTable: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()

    val cards = remember(paper, topics, matrixRows) {
        val topicCards = topics.map { topic ->
            SwipeDeckCard(
                id = "topic_${topic.id}",
                topicId = topic.id,
                title = topic.topicName,
                subtitle = topic.chapter,
                keyRule = topic.keyProvisions.firstOrNull() ?: "Standard ICAI provision.",
                pitfallOrException = if (topic.commonExaminerPitfalls.isNotBlank()) "⚠️ Trap: ${topic.commonExaminerPitfalls}" else "Verify statutory conditions.",
                paperName = paper.shortName
            )
        }
        val rowCards = matrixRows.take(10).mapIndexed { idx, row ->
            SwipeDeckCard(
                id = "row_${row.sectionOrClause}_$idx",
                topicId = topics.firstOrNull()?.id ?: "general_topic",
                title = "${row.sectionOrClause}: ${row.provisionName}",
                subtitle = row.thresholdOrLimit,
                keyRule = "Limit: ${row.thresholdOrLimit} | Rule: ${row.approvalOrRate}",
                pitfallOrException = "⚠️ Exception: ${row.penaltyOrException}",
                paperName = paper.shortName
            )
        }
        (topicCards + rowCards).shuffled()
    }

    var currentIndex by remember(paper) { mutableIntStateOf(0) }
    val historyStack = remember { mutableListOf<Pair<Int, Int>>() }
    var masteredCount by remember(paper) { mutableIntStateOf(0) }
    var needPracticeCount by remember(paper) { mutableIntStateOf(0) }

    val offsetX = remember { Animatable(0f) }

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, Amber500.copy(alpha = 0.35f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("tinder_swipe_drill_container")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rapid Recall Card Drill",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (currentIndex < cards.size) "${currentIndex + 1}/${cards.size}" else "Done",
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.5.sp,
                        color = Amber500
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .height(20.dp)
                            .clickable { onBackToTable() }
                    ) {
                        Text(
                            text = "Table ✕",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            val progressFraction = if (cards.isNotEmpty()) (currentIndex.toFloat() / cards.size).coerceIn(0f, 1f) else 1f
            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = Amber500,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (currentIndex < cards.size) {
                val currentCard = cards[currentIndex]

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .graphicsLayer {
                            translationX = offsetX.value
                            rotationZ = (offsetX.value / 40f).coerceIn(-15f, 15f)
                        }
                        .pointerInput(currentIndex) {
                            detectHorizontalDragGestures(
                                onDragEnd = {
                                    if (offsetX.value > 180f) {
                                        coroutineScope.launch {
                                            offsetX.animateTo(1000f, tween(160))
                                            onSwipeMastered(currentCard.topicId)
                                            masteredCount++
                                            historyStack.add(currentIndex to 2)
                                            currentIndex++
                                            offsetX.snapTo(0f)
                                        }
                                    } else if (offsetX.value < -180f) {
                                        coroutineScope.launch {
                                            offsetX.animateTo(-1000f, tween(160))
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
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = currentCard.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 2
                                )
                                Text(
                                    text = currentCard.subtitle,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = currentCard.keyRule,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = currentCard.pitfallOrException,
                                    fontSize = 10.5.sp,
                                    color = ErrorRed
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("← Need Practice", fontSize = 9.5.sp, color = ErrorRed, fontWeight = FontWeight.Bold)
                                Text("Mastered →", fontSize = 9.5.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Surface(
                        shape = RoundedCornerShape(5.dp),
                        color = ErrorRed,
                        modifier = Modifier
                            .height(26.dp)
                            .clickable {
                                coroutineScope.launch {
                                    onSwipeNeedPractice(currentCard.topicId)
                                    needPracticeCount++
                                    historyStack.add(currentIndex to 1)
                                    currentIndex++
                                }
                            }
                            .testTag("swipe_button_need_practice")
                    ) {
                        Text(
                            text = "Need Practice",
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(5.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .height(26.dp)
                            .clickable(enabled = historyStack.isNotEmpty()) {
                                if (historyStack.isNotEmpty()) {
                                    val last = historyStack.removeAt(historyStack.lastIndex)
                                    currentIndex = last.first
                                }
                            }
                            .testTag("swipe_button_undo")
                    ) {
                        Text(
                            text = "Undo",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(5.dp),
                        color = SuccessGreen,
                        modifier = Modifier
                            .height(26.dp)
                            .clickable {
                                coroutineScope.launch {
                                    onSwipeMastered(currentCard.topicId)
                                    masteredCount++
                                    historyStack.add(currentIndex to 2)
                                    currentIndex++
                                }
                            }
                            .testTag("swipe_button_mastered")
                    ) {
                        Text(
                            text = "Mastered ✓",
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Drill Complete! Mastered: $masteredCount • Review: $needPracticeCount",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(5.dp),
                        color = Amber500,
                        modifier = Modifier
                            .height(26.dp)
                            .clickable {
                                currentIndex = 0
                                masteredCount = 0
                                needPracticeCount = 0
                                historyStack.clear()
                            }
                    ) {
                        Text("Restart", fontWeight = FontWeight.Bold, fontSize = 10.5.sp, color = Color.Black, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
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
        Surface(
            shape = RoundedCornerShape(5.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .clickable { expanded = true }
                .testTag(testTag)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 7.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$label: ${selectedTopic?.topicName ?: "Select"}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(13.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            topics.forEach { topic ->
                DropdownMenuItem(
                    text = { Text("${topic.chapter} • ${topic.topicName}", fontSize = 11.sp) },
                    onClick = {
                        onTopicSelected(topic)
                        expanded = false
                    }
                )
            }
        }
    }
}
