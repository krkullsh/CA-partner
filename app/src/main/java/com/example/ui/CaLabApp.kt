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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.data.model.CatalogSortOrder
import com.example.data.model.ExperienceLevel
import com.example.data.model.LabMode
import com.example.data.model.PaperCode
import com.example.data.model.PriorityTier
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
                CenterAlignedTopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "CA",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 10.sp,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "CA-Lab",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = currentMode.badge,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                    },
                    actions = {
                        // Compact Status Dot indicator (Amber = Offline, Green = Cloud AI)
                        Box(
                            modifier = Modifier
                                .padding(end = 2.dp)
                                .size(32.dp)
                                .clip(CircleShape)
                                .clickable { viewModel.toggleSettingsDialog(true) }
                                .testTag("offline_status_dot"),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (isOffline) Amber500 else SuccessGreen)
                            )
                        }

                        IconButton(
                            onClick = { viewModel.toggleSettingsDialog(true) },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("open_settings_button")
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings", modifier = Modifier.size(18.dp))
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )

                // Ultra-Compact 34.dp Master Chapter Header
                MasterChapterHeader(
                    activePaper = activePaper,
                    activeTopic = activeTopic,
                    onOpenFilter = { showContextSheet = true }
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                    thickness = 0.5.dp
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .height(60.dp)
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
                        LabMode.TEACH_LAB -> "Teach Lab"
                        LabMode.CATCH_LAB_OUT -> "Catch Traps"
                        LabMode.ASK_LAB -> "Study & Ask"
                        LabMode.SPOT_DIFFERENCE -> "Differences"
                        LabMode.VERDICTS -> "My Progress"
                    }

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setMode(mode) },
                        icon = { Icon(icon, contentDescription = label, modifier = Modifier.size(18.dp)) },
                        label = {
                            Text(
                                text = label,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
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
 * Ultra-Compact 34.dp Master Chapter Header
 * Maximizes reading viewport to ~90% of screen height.
 */
@Composable
fun MasterChapterHeader(
    activePaper: PaperCode,
    activeTopic: SyllabusTopic,
    onOpenFilter: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .height(34.dp)
            .clickable(onClick = onOpenFilter)
            .testTag("master_chapter_header")
            .testTag("sticky_study_context_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left & Middle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Short subject code badge
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(activePaper.colorHex).copy(alpha = 0.14f),
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text(
                        text = activePaper.shortName,
                        color = Color(activePaper.colorHex),
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }

                // Middle: Ch X: Title
                Text(
                    text = "Ch ${activeTopic.icaiChapterNumber}: ${activeTopic.topicName}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                if (activeTopic.hasRecentAmendment) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Surface(
                        shape = RoundedCornerShape(3.dp),
                        color = Amber500.copy(alpha = 0.16f)
                    ) {
                        Text(
                            text = "🆕",
                            fontSize = 8.sp,
                            modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                        )
                    }
                }

                if (activeTopic.priorityTier == PriorityTier.TIER_1) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Surface(
                        shape = RoundedCornerShape(3.dp),
                        color = Color(0xFFEF4444).copy(alpha = 0.14f)
                    ) {
                        Text(
                            text = "T1",
                            color = Color(0xFFEF4444),
                            fontWeight = FontWeight.Bold,
                            fontSize = 8.sp,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Right: Sleek 28.dp height [Filter ▾] button
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                modifier = Modifier
                    .height(26.dp)
                    .clickable(onClick = onOpenFilter)
                    .testTag("filter_dropdown_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Filter",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Open Filters",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}

// Backward-compatible alias
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

/**
 * Filter Sheet housing Secondary Controls:
 * Paper selector, Topic search & list, Tier sort order, Experience level, Strategy Blueprint
 */
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
    val catalogSortOrder by viewModel.catalogSortOrder.collectAsState()
    val experienceLevel by viewModel.experienceLevel.collectAsState()

    val sortedTopics = remember(selectedPaper, catalogSortOrder) {
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
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Study Context & Filters",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Customize active subject, topic, and grading persona",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Strategy Blueprint Trigger
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onOpenBlueprint)
                    .testTag("filter_open_blueprint_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "View ${selectedPaper.shortName} Strategy Blueprint & 3-Column Tables",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "Open ▸",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Horizontal Paper Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                PaperCode.entries.forEach { paper ->
                    val isSelected = selectedPaper == paper
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isSelected) Color(paper.colorHex).copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceVariant,
                        border = if (isSelected) BorderStroke(1.dp, Color(paper.colorHex)) else BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .height(28.dp)
                            .clickable { selectedPaper = paper }
                    ) {
                        Text(
                            text = paper.shortName,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color(paper.colorHex) else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Secondary Controls: Sort Order & Experience Level in a compact strip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sort Order
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Sort: ", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (catalogSortOrder == CatalogSortOrder.OFFICIAL_ICAI) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .height(24.dp)
                            .clickable { viewModel.setCatalogSortOrder(CatalogSortOrder.OFFICIAL_ICAI) }
                    ) {
                        Text(
                            text = "ICAI Ch #",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (catalogSortOrder == CatalogSortOrder.PRIORITY_TIER) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .height(24.dp)
                            .clickable { viewModel.setCatalogSortOrder(CatalogSortOrder.PRIORITY_TIER) }
                    ) {
                        Text(
                            text = "Tier 1 First",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                // Experience Level
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Persona: ", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    ExperienceLevel.entries.forEach { exp ->
                        val isExp = experienceLevel == exp
                        val shortLabel = when (exp) {
                            ExperienceLevel.NOVICE -> "Novice"
                            ExperienceLevel.KNOWLEDGED -> "Inter"
                            ExperienceLevel.EXAM_READY -> "Exam"
                        }
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (isExp) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .height(24.dp)
                                .padding(start = 3.dp)
                                .clickable { viewModel.setExperienceLevel(exp) }
                        ) {
                            Text(
                                text = shortLabel,
                                fontSize = 10.sp,
                                fontWeight = if (isExp) FontWeight.Bold else FontWeight.Normal,
                                color = if (isExp) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar (Compact 36dp)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search chapter, standard, or section...", fontSize = 11.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(8.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp)) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Topics List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 380.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(filteredTopics, key = { it.id }) { topic ->
                    val isCurrent = topic.id == currentTopic.id
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrent)
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                            else
                                MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(
                            0.5.dp,
                            if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
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
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Ch ${topic.icaiChapterNumber} • ${topic.chapter}",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = topic.topicName,
                                    fontSize = 12.sp,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (topic.hasRecentAmendment) {
                                    Surface(
                                        shape = RoundedCornerShape(3.dp),
                                        color = Amber500.copy(alpha = 0.18f),
                                        modifier = Modifier.padding(end = 4.dp)
                                    ) {
                                        Text(
                                            text = "🆕 Updated",
                                            color = Amber500,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 8.sp,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(3.dp),
                                    color = Color(topic.priorityTier.badgeColorHex).copy(alpha = 0.14f)
                                ) {
                                    Text(
                                        text = when (topic.priorityTier) {
                                            PriorityTier.TIER_1 -> "Tier 1"
                                            PriorityTier.TIER_2 -> "Tier 2"
                                            PriorityTier.TIER_3 -> "Tier 3"
                                            PriorityTier.TIER_4 -> "Tier 4"
                                        },
                                        color = Color(topic.priorityTier.badgeColorHex),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
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
