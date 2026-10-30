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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.curriculum.CuratedCatchLabScenarios
import com.example.data.curriculum.CuratedQuestionBank
import com.example.data.curriculum.IntegratedNotesRepository
import com.example.data.local.TopicProgressEntity
import com.example.data.model.CatchLabScenario
import com.example.data.model.ExperienceLevel
import com.example.data.model.SubjectArchetype
import com.example.data.model.SyllabusTopic
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
    TEACHER_PYQ_RADAR("Teacher (PYQ)", Icons.Default.Psychology),
    CHAPTER_NOTES("Chapter Notes", Icons.AutoMirrored.Filled.MenuBook),
    PRACTICE_QBANK("Practice Qs", Icons.Default.Timer)
}

fun SubjectArchetype.displayName(): String = when (this) {
    SubjectArchetype.STANDARDS_AND_ACCOUNTS -> "Standards & Accounts"
    SubjectArchetype.STATUTE_AND_CASE_LAW -> "Statute & Case Law"
    SubjectArchetype.TAX_AMENDMENTS_AND_COMPUTATION -> "Tax & Computation"
    SubjectArchetype.FORMULAS_AND_PROFORMA -> "Formulas & Proforma"
    SubjectArchetype.AUDIT_PROCEDURES_AND_CLAUSES -> "Audit & Clauses"
    SubjectArchetype.STRATEGIC_FRAMEWORKS -> "Strategic Frameworks"
}

/**
 * Modern Notion-Style borderless left-accent callout strip for dark mode safety & clean reading.
 */
@Composable
fun NotionCallout(
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

/**
 * Tap-to-Reveal inline masking helper for numbers, limits, and sections.
 */
@Composable
fun MaskableNoteText(
    text: String,
    isMasked: Boolean,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodySmall,
    lineHeight: androidx.compose.ui.unit.TextUnit = 18.sp
) {
    if (!isMasked) {
        FormattedText(text = text, style = style, lineHeight = lineHeight, modifier = modifier)
        return
    }

    var isRevealed by remember(text, isMasked) { mutableStateOf(false) }
    val regex = remember {
        Regex("""(₹\s?[\d,]+(?:\.\d+)?|\b\d+(?:\.\d+)?%|\bSec(?:tion)?\s+\d+[A-Za-z0-9()]*|\bPara(?:graph)?\s+\d+[A-Za-z0-9()]*|\bAS\s+\d+|\b\d{1,3}(?:,\d{3})+(?:\.\d+)?\b)""")
    }

    val maskedText = remember(text) {
        regex.replace(text) { "[ 🔒 ??? ]" }
    }

    Column(modifier = modifier) {
        FormattedText(
            text = if (isRevealed) text else maskedText,
            style = style,
            lineHeight = lineHeight
        )
        Spacer(modifier = Modifier.height(3.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = if (isRevealed) Amber500.copy(alpha = 0.14f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                modifier = Modifier
                    .clickable { isRevealed = !isRevealed }
                    .padding(vertical = 1.dp)
            ) {
                Text(
                    text = if (isRevealed) "🔒 Mask Limits" else "👁 Tap to Reveal Numbers",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isRevealed) Amber500 else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
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
    var showToolsDropdown by remember { mutableStateOf(false) }
    var showSearchBox by remember { mutableStateOf(false) }
    var maskNumbersAndLimits by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    // 4 Rich offline teacher sections from Repository
    val teacherSections = remember(activeTopic.id, activePaper) {
        viewModel.repository.getOfflineTeacherArticle(activeTopic, activePaper.archetype)
    }

    // Curated Notes from IntegratedNotesRepository (Uploaded Notes)
    val curatedRadarNotes = remember(activePaper, activeTopic.id) {
        val paperList = IntegratedNotesRepository.teacherRadarByPaper[activePaper].orEmpty()
        val matched = paperList.filter {
            activeTopic.id.contains(it.topicId, ignoreCase = true) ||
            it.chapterTitle.contains(activeTopic.topicName, ignoreCase = true) ||
            activeTopic.topicName.contains(it.chapterTitle.substringBefore(":"), ignoreCase = true)
        }
        matched.ifEmpty { paperList }
    }

    val curatedLiteralSections = remember(activePaper, activeTopic.id) {
        val paperNotes = IntegratedNotesRepository.literalChapterNotesByPaper[activePaper].orEmpty()
        val matched = paperNotes.filter {
            activeTopic.id.contains(it.topicId, ignoreCase = true)
        }
        matched.ifEmpty { paperNotes }
    }

    val outlinePills = listOf("1. PYQ Map", "2. Logic", "3. Blueprint", "4. Traps", "5. Format")
    val firstVisible = listState.firstVisibleItemIndex
    val activePillIndex = (firstVisible - 2).coerceIn(0, 4)

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .testTag("ask_lab_screen"),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // =========================================================================
        // ITEM 0: ULTRA-COMPACT 28.DP HEADER ROW (NO DUPLICATE TOPIC TITLE)
        // Left (weight 1f): 3-tab segmented switcher [Teacher (PYQ)] | [Chapter Notes] | [Practice Qs]
        // Right: Compact 26.dp [Tools ▾] dropdown button
        // =========================================================================
        item {
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left 3-tab Segmented Switcher
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.22f)),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .testTag("study_segmented_modes")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StudyAskSubTab.entries.forEach { tab ->
                            val isSelected = activeSubTab == tab
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
                                shadowElevation = if (isSelected) 0.5.dp else 0.dp,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
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
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = tab.label,
                                        fontSize = 10.5.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Right: Compact 26.dp [Tools ▾] Dropdown Button
                Box {
                    Surface(
                        shape = RoundedCornerShape(5.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .height(26.dp)
                            .clickable { showToolsDropdown = true }
                            .testTag("header_open_blueprint_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 7.dp)
                        ) {
                            Text(
                                text = "Tools",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Tools & Filters",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showToolsDropdown,
                        onDismissRequest = { showToolsDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = if (maskNumbersAndLimits) "✓ Mask Numbers & Limits: ON" else "Mask Numbers & Limits: OFF",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    if (maskNumbersAndLimits) Icons.Default.Lock else Icons.Default.Visibility,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp)
                                )
                            },
                            onClick = {
                                maskNumbersAndLimits = !maskNumbersAndLimits
                                showToolsDropdown = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = if (showSearchBox) "Hide Doubt Search" else "Ask Doubt / Search Concept",
                                    fontSize = 11.5.sp
                                )
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(15.dp))
                            },
                            onClick = {
                                showSearchBox = !showSearchBox
                                showToolsDropdown = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Strategy Blueprint", fontSize = 11.5.sp) },
                            leadingIcon = {
                                Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, modifier = Modifier.size(15.dp))
                            },
                            onClick = {
                                showBlueprintDialog = true
                                showToolsDropdown = false
                            }
                        )
                        val isBookmarked = bookmarkedIds.contains(activeTopic.id)
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = if (isBookmarked) "Remove Bookmark" else "Bookmark Topic",
                                    fontSize = 11.5.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp),
                                    tint = if (isBookmarked) Amber500 else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            onClick = {
                                viewModel.toggleBookmark(
                                    activeTopic.id,
                                    "TOPIC",
                                    activeTopic.topicName,
                                    activeTopic.paper.title
                                )
                                showToolsDropdown = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Copy Chapter Notes", fontSize = 11.5.sp) },
                            leadingIcon = {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
                            },
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(
                                    ClipData.newPlainText(
                                        "CA-Lab Note",
                                        "${activeTopic.topicName}\n\n${activeTopic.keyProvisions.joinToString("\n• ", prefix = "• ")}"
                                    )
                                )
                                Toast.makeText(context, "Copied notes to clipboard!", Toast.LENGTH_SHORT).show()
                                showToolsDropdown = false
                            }
                        )
                    }
                }
            }
        }

        // Inline Search Box (Toggled from Tools Dropdown)
        if (showSearchBox) {
            item {
                OutlinedTextField(
                    value = state.customQuery,
                    onValueChange = { viewModel.updateAskCustomQuery(it) },
                    placeholder = { Text("Search concept or ask specific doubt...", fontSize = 11.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("ask_search_field"),
                    shape = RoundedCornerShape(6.dp),
                    trailingIcon = {
                        IconButton(
                            onClick = { viewModel.searchOrGenerateExplanation() },
                            modifier = Modifier
                                .size(28.dp)
                                .testTag("ask_search_button")
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                modifier = Modifier.size(15.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )
            }
        }

        // =========================================================================
        // TAB 1: TEACHER (PYQ RADAR — WHAT TOPICS EXIST & WHAT CAME IN PAST PAPERS)
        // =========================================================================
        if (activeSubTab == StudyAskSubTab.TEACHER_PYQ_RADAR) {
            // Compact Reading Outline Scrub Bar (23dp pills)
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reading_outline_scrub_bar"),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    outlinePills.forEachIndexed { index, pillLabel ->
                        item {
                            val isPillActive = activePillIndex == index
                            Surface(
                                shape = RoundedCornerShape(5.dp),
                                color = if (isPillActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                                modifier = Modifier
                                    .height(23.dp)
                                    .clickable {
                                        coroutineScope.launch {
                                            val targetIndex = (2 + index).coerceAtMost(6)
                                            listState.animateScrollToItem(targetIndex)
                                        }
                                    }
                                    .testTag("outline_pill_$index")
                            ) {
                                Text(
                                    text = pillLabel,
                                    fontSize = 10.sp,
                                    fontWeight = if (isPillActive) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isPillActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }

            // PYQ RADAR & CHAPTER SUB-TOPICS MAP (FROM CURATED EXAM NOTES)
            item {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.28f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
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
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Chapter Topic Map & Past Paper Radar",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = "Freq: ${activeTopic.pastPaperFrequency}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Amber500
                            )
                        }

                        curatedRadarNotes.forEach { radar ->
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "${radar.chapterTitle} (${radar.examProbability})",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "TOPICS IN THIS CHAPTER:",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            radar.subTopicsInChapter.forEach { sub ->
                                Text(
                                    text = sub,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "WHAT CAME IN PAST PAPERS (PYQ / RTP):",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Sky500
                            )
                            radar.whatCameInPastPapers.forEach { pyq ->
                                Text(
                                    text = "• $pyq",
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            // Notion-Style Examiner Loss Point Strip
                            NotionCallout(accentColor = ErrorRed) {
                                Text(
                                    text = "⚠️ HOW STUDENTS LOSE MARKS (EXAMINER OBSERVATIONS):",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ErrorRed
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                radar.howStudentsLoseMarks.forEach { loss ->
                                    Text(
                                        text = "• $loss",
                                        fontSize = 10.5.sp,
                                        lineHeight = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // SECTION 1: Core Logic & Analogy
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

            // SECTION 2: Official Blueprint Breakdown
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

            // SECTION 3: Examiner Traps & Spot the Trap Mini-Card
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
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.28f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("teacher_section_3")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(Amber500.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("3", color = Amber500, fontWeight = FontWeight.Bold, fontSize = 9.sp)
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = sec3?.heading ?: "3. Examiner Traps & Student Mark Loss Points",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Amber500
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        FormattedText(
                            text = sec3?.bodyMarkdown ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        SpotTheTrapMiniCard(scenario = matchingScenario)

                        Spacer(modifier = Modifier.height(8.dp))

                        // Compact [Mark Lesson Read ✓] Button (26.dp height)
                        val isTeacherRead = progressMap[activeTopic.id]?.teacherReadCompleted == 1
                        Surface(
                            shape = RoundedCornerShape(5.dp),
                            color = if (isTeacherRead) SuccessGreen.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(
                                0.5.dp,
                                if (isTeacherRead) SuccessGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(26.dp)
                                .clickable {
                                    val newStatus = !isTeacherRead
                                    viewModel.markTeacherRead(activeTopic.id, newStatus)
                                    Toast.makeText(
                                        context,
                                        if (newStatus) "✓ Marked '${activeTopic.topicName}' as Read!" else "Marked unread.",
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
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (isTeacherRead) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = if (isTeacherRead) "Completed ✓" else "Mark Teacher Radar Read ✓",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 10.5.sp,
                                    color = if (isTeacherRead) SuccessGreen else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // SECTION 4: Exam Answer Presentation
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

        // =========================================================================
        // TAB 2: CHAPTER NOTES (LITERAL CHAPTER SUBSTANCE + MASK NUMBERS & LIMITS)
        // =========================================================================
        if (activeSubTab == StudyAskSubTab.CHAPTER_NOTES) {
            item {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
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
                                text = "Chapter Reference Notes — ${activeTopic.topicName}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (maskNumbersAndLimits) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Amber500.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "🔒 MASKED",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Amber500,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))

                        // 1. Exact Statutory Provisions & ICAI Criteria
                        Text(
                            text = "CORE STATUTORY / ACCOUNTING STANDARD RULES",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        MaskableNoteText(
                            text = activeTopic.keyProvisions.joinToString("\n• ", prefix = "• "),
                            isMasked = maskNumbersAndLimits,
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 19.sp
                        )

                        // 2. Integrated Notes Sections (Formula Logic & Common Student Confusions)
                        curatedLiteralSections.forEach { section ->
                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = section.sectionHeading,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            MaskableNoteText(
                                text = section.coreRuleOrProvision,
                                isMasked = maskNumbersAndLimits,
                                style = MaterialTheme.typography.bodySmall,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))
                            NotionCallout(accentColor = Sky500) {
                                Text(
                                    text = section.formulaOrAccountingLogic,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.5.sp,
                                    lineHeight = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            NotionCallout(accentColor = Amber500) {
                                Text(
                                    text = "💡 Common Student Confusion: ${section.commonStudentConfusion}",
                                    fontSize = 10.5.sp,
                                    lineHeight = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // 3. AI / Search Notes content if queried
                        if (state.displayedContent.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "DOUBT / SEARCH EXPLANATION",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            if (state.isLoading) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
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

        // =========================================================================
        // TAB 3: PRACTICE Q-BANK
        // =========================================================================
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
                            listState.animateScrollToItem(2)
                        }
                    },
                    hintText = viewModel.repository.getHintForTopic(activeTopic)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(14.dp))
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
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
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
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = sectionNumber,
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
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

    NotionCallout(
        accentColor = Amber500,
        modifier = modifier.testTag("spot_trap_mini_card")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "⚠️ Case Trap: ${scenario.title}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(0.5.dp, Amber500.copy(alpha = 0.5f)),
                modifier = Modifier
                    .height(22.dp)
                    .clickable { isRevealed = !isRevealed }
                    .testTag("toggle_trap_reveal_button")
            ) {
                Text(
                    text = if (isRevealed) "Hide ▲" else "Check ICAI Fix ▼",
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Amber500,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = scenario.caseSnippet,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 16.sp
        )

        if (isRevealed) {
            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Amber500.copy(alpha = 0.25f))
            Spacer(modifier = Modifier.height(6.dp))
            FormattedText(
                text = scenario.correctIcaiProvision,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 17.sp
            )
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
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.28f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("collapsible_practice_questions_card")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            if (currentQuestion == null) {
                Text(
                    text = "No questions found for this topic.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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

                val minutesLeft = remainingSeconds / 60
                val secondsLeft = remainingSeconds % 60
                val timeString = String.format("%02d:%02d", minutesLeft, secondsLeft)

                // Compact 1-Line Header: Question Index + Marks + Timer + Nav Arrows
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "Q${activeQuestionIndex + 1}/${questions.size}",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = ErrorRed.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "${currentQuestion.marks}M",
                                color = ErrorRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.5.sp,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .height(22.dp)
                                .clickable { isTimerRunning = !isTimerRunning }
                                .testTag("practice_timer_toggle_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp)
                            ) {
                                Icon(
                                    imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = timeString,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.testTag("practice_timer_text")
                                )
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Amber500.copy(alpha = 0.12f),
                            modifier = Modifier
                                .height(22.dp)
                                .clickable { isHintVisible = !isHintVisible }
                                .testTag("provision_hint_chip")
                        ) {
                            Text(
                                text = "Hint",
                                color = Amber500,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.5.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
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
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous", modifier = Modifier.size(14.dp))
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
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next", modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }

                if (isHintVisible) {
                    Spacer(modifier = Modifier.height(6.dp))
                    NotionCallout(
                        accentColor = Amber500,
                        modifier = Modifier.testTag("active_provision_hint_banner")
                    ) {
                        Text(
                            text = hintText ?: "💡 Look up key thresholds and statutory conditions.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentQuestion.questionText,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = userDraftInput,
                    onValueChange = { userDraftInput = it },
                    placeholder = { Text("Draft working notes / key provisions...", fontSize = 11.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .testTag("practice_draft_input"),
                    shape = RoundedCornerShape(6.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Compact 28.dp Reveal Button
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isAnswerRevealed) SuccessGreen else MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                        .clickable {
                            if (!isAnswerRevealed) {
                                isAnswerRevealed = true
                                onWrittenSolved()
                            } else {
                                isAnswerRevealed = false
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
                        Text(
                            text = if (isAnswerRevealed) "Hide ICAI Answer ▲" else "Check ICAI Suggested Answer & Examiner Notes ▼",
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.5.sp,
                            color = Color.White
                        )
                    }
                }

                if (isAnswerRevealed) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                        border = BorderStroke(0.5.dp, SuccessGreen.copy(alpha = 0.35f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("practice_official_answer_card")
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                "OFFICIAL ICAI SUGGESTED ANSWER",
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen,
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            FormattedText(
                                text = currentQuestion.officialAnswer,
                                style = MaterialTheme.typography.bodySmall,
                                lineHeight = 18.sp
                            )
                            if (currentQuestion.examinerObservations.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                NotionCallout(accentColor = ErrorRed) {
                                    Text(
                                        text = "⚠️ Examiner Observation: ${currentQuestion.examinerObservations}",
                                        fontSize = 10.5.sp,
                                        lineHeight = 16.sp,
                                        color = ErrorRed
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
    val writtenSolved = progress?.writtenSolved ?: 0
    val mcqAttempted = progress?.mcqAttempted ?: 0
    val mcqCorrect = progress?.mcqCorrect ?: 0
    val oralRecalls = progress?.oralRecallsCompleted ?: 0

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${topic.topicName} • W:$writtenSolved/${topic.writtenTarget} • M:$mcqCorrect/$mcqAttempted • R:$oralRecalls/${topic.oralRecallTarget}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onToggleBookmark, modifier = Modifier.size(22.dp)) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = if (isBookmarked) Amber500 else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}
