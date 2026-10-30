package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.ExperienceLevel
import com.example.data.model.LabMode
import com.example.data.model.PaperCode
import com.example.data.model.SyllabusTopic
import com.example.ui.components.SettingsDialog
import com.example.ui.components.StrategyBlueprintBottomSheet
import com.example.ui.screens.AskLabScreen
import com.example.ui.screens.CatchLabOutScreen
import com.example.ui.screens.SpotDifferenceScreen
import com.example.ui.screens.TeachLabScreen
import com.example.ui.screens.VerdictsHistoryScreen
import com.example.ui.theme.Amber500
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.CaLabViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaLabApp(viewModel: CaLabViewModel = viewModel()) {
    val currentMode by viewModel.currentMode.collectAsState()
    val isOffline by viewModel.isOffline.collectAsState()
    val showSettings by viewModel.showSettingsDialog.collectAsState()

    val activePaper by viewModel.activePaper.collectAsState()
    val activeTopic by viewModel.activeTopic.collectAsState()

    var showContextSheet by remember { mutableStateOf(false) }
    var showBlueprintSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
                // Single unified 36.dp Top Reader Bar: [Book • Chapter] + [Filter ▾] + [Settings ⚙]
                MasterChapterHeader(
                    activePaper = activePaper,
                    activeTopic = activeTopic,
                    isOffline = isOffline,
                    onOpenFilter = { showContextSheet = true },
                    onOpenSettings = { viewModel.toggleSettingsDialog(true) }
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                    thickness = 0.5.dp
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp,
                modifier = Modifier
                    .height(54.dp)
                    .testTag("bottom_nav_bar")
            ) {
                LabMode.entries.forEach { mode ->
                    val isSelected = currentMode == mode
                    val icon: ImageVector = when (mode) {
                        LabMode.TEACH_LAB -> Icons.Default.School
                        LabMode.CATCH_LAB_OUT -> Icons.Default.BugReport
                        LabMode.ASK_LAB -> Icons.Default.Psychology
                        LabMode.SPOT_DIFFERENCE -> Icons.AutoMirrored.Filled.CompareArrows
                        LabMode.VERDICTS -> Icons.Default.Assessment
                    }

                    val label = when (mode) {
                        LabMode.TEACH_LAB -> "Teach"
                        LabMode.CATCH_LAB_OUT -> "Traps"
                        LabMode.ASK_LAB -> "Study"
                        LabMode.SPOT_DIFFERENCE -> "Compare"
                        LabMode.VERDICTS -> "Progress"
                    }

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setMode(mode) },
                        icon = { Icon(icon, contentDescription = label, modifier = Modifier.size(17.dp)) },
                        label = {
                            Text(
                                text = label,
                                fontSize = 9.5.sp,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.testTag("nav_item_${mode.name}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentMode) {
                LabMode.TEACH_LAB -> TeachLabScreen(viewModel = viewModel)
                LabMode.CATCH_LAB_OUT -> CatchLabOutScreen(viewModel = viewModel)
                LabMode.ASK_LAB -> AskLabScreen(viewModel = viewModel)
                LabMode.SPOT_DIFFERENCE -> SpotDifferenceScreen(viewModel = viewModel)
                LabMode.VERDICTS -> VerdictsHistoryScreen(viewModel = viewModel)
            }
        }
    }

    if (showSettings) {
        SettingsDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.toggleSettingsDialog(false) }
        )
    }

    if (showContextSheet) {
        StudyContextBottomSheet(
            viewModel = viewModel,
            currentPaper = activePaper,
            currentTopic = activeTopic,
            onOpenBlueprint = {
                showContextSheet = false
                showBlueprintSheet = true
            },
            onDismiss = { showContextSheet = false }
        )
    }

    if (showBlueprintSheet) {
        StrategyBlueprintBottomSheet(
            paper = activePaper,
            onDismiss = { showBlueprintSheet = false }
        )
    }
}

/**
 * Single-Line 36.dp Master Header: Shows ONLY [Book Short Name] • [Ch X: Topic Name],
 * [Filter ▾], and the Settings gear—giving 92% of the screen to the reader content.
 */
@Composable
fun MasterChapterHeader(
    activePaper: PaperCode,
    activeTopic: SyllabusTopic,
    isOffline: Boolean = true,
    onOpenFilter: () -> Unit,
    onOpenSettings: () -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .height(38.dp)
            .testTag("master_chapter_header")
            .testTag("sticky_study_context_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Book Tag + Ch X: Topic Title (Clickable to switch chapter)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onOpenFilter)
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(activePaper.colorHex).copy(alpha = 0.14f)
                ) {
                    Text(
                        text = activePaper.shortName,
                        color = Color(activePaper.colorHex),
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.5.sp,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Ch ${activeTopic.icaiChapterNumber}: ${activeTopic.topicName}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Right: Compact 24.dp [Filter ▾] + Offline Dot + Settings Gear
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(5.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .height(24.dp)
                        .clickable(onClick = onOpenFilter)
                        .testTag("filter_dropdown_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 7.dp)
                    ) {
                        Text(
                            text = "Chapter",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Switch Chapter",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onOpenSettings)
                        .testTag("offline_status_dot"),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(if (isOffline) Amber500 else SuccessGreen)
                    )
                }

                IconButton(
                    onClick = onOpenSettings,
                    modifier = Modifier
                        .size(28.dp)
                        .testTag("open_settings_button")
                ) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun StickyStudyContextBar(
    activePaper: PaperCode,
    activeTopic: SyllabusTopic,
    onClick: () -> Unit
) {
    MasterChapterHeader(
        activePaper = activePaper,
        activeTopic = activeTopic,
        onOpenFilter = onClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyContextBottomSheet(
    viewModel: CaLabViewModel,
    currentPaper: PaperCode,
    currentTopic: SyllabusTopic,
    onOpenBlueprint: () -> Unit = {},
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedPaper by remember(currentPaper) { mutableStateOf(currentPaper) }
    var searchQuery by remember { mutableStateOf("") }
    val experienceLevel by viewModel.experienceLevel.collectAsState()

    val sortedTopics = remember(selectedPaper) {
        viewModel.getSortedTopicsForPaper(selectedPaper)
    }

    val filteredTopics = remember(sortedTopics, searchQuery) {
        if (searchQuery.isBlank()) sortedTopics
        else sortedTopics.filter {
            it.topicName.contains(searchQuery, ignoreCase = true) ||
            it.chapter.contains(searchQuery, ignoreCase = true)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .padding(bottom = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select Book & Chapter",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(5.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier
                            .height(24.dp)
                            .clickable(onClick = onOpenBlueprint)
                            .testTag("filter_open_blueprint_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 7.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${selectedPaper.shortName} Blueprint",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Compact Book / Paper Selector Pills (26.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                PaperCode.entries.forEach { paper ->
                    val isSelected = selectedPaper == paper
                    Surface(
                        shape = RoundedCornerShape(5.dp),
                        color = if (isSelected) Color(paper.colorHex).copy(alpha = 0.16f) else MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(0.5.dp, if (isSelected) Color(paper.colorHex) else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                        modifier = Modifier
                            .height(26.dp)
                            .clickable { selectedPaper = paper }
                    ) {
                        Text(
                            text = paper.shortName,
                            fontSize = 10.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color(paper.colorHex) else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Search + Persona Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search chapter or standard...", fontSize = 11.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(6.dp),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(15.dp)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )

                // Compact Persona Switcher
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ExperienceLevel.entries.forEach { exp ->
                        val isExp = experienceLevel == exp
                        val shortLabel = when (exp) {
                            ExperienceLevel.NOVICE -> "Nov"
                            ExperienceLevel.KNOWLEDGED -> "Int"
                            ExperienceLevel.EXAM_READY -> "Exam"
                        }
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (isExp) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .height(26.dp)
                                .padding(start = 2.dp)
                                .clickable { viewModel.setExperienceLevel(exp) }
                        ) {
                            Text(
                                text = shortLabel,
                                fontSize = 9.5.sp,
                                fontWeight = if (isExp) FontWeight.Bold else FontWeight.Normal,
                                color = if (isExp) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Clean Chapter List (Showing only Book/Ch # + Title + Past Paper Frequency)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 380.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(filteredTopics, key = { it.id }) { topic ->
                    val isCurrent = topic.id == currentTopic.id
                    Card(
                        shape = RoundedCornerShape(6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrent)
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            else
                                MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(
                            0.5.dp,
                            if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.22f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.setActiveTopic(topic)
                                onDismiss()
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Ch ${topic.icaiChapterNumber} • ${topic.topicName}",
                                fontSize = 11.5.sp,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = "${topic.averageMarks.toInt()}M",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Amber500
                            )
                        }
                    }
                }
            }
        }
    }
}
