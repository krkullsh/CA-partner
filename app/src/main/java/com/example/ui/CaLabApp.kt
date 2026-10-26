package com.example.ui

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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
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
import com.example.data.model.LabMode
import com.example.data.model.PaperCode
import com.example.data.model.PriorityTier
import com.example.data.model.SyllabusTopic
import com.example.ui.components.SettingsDialog
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

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Amber500),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "CA",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 11.sp,
                                    color = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "CA-Lab",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer
                                    ) {
                                        Text(
                                            text = currentMode.badge,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "ICAI Intermediate (New Scheme)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    actions = {
                        // Compact Status Dot indicator (Amber = Offline, Green = Cloud AI)
                        Box(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .clickable { viewModel.toggleSettingsDialog(true) }
                                .testTag("offline_status_dot"),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (isOffline) Amber500 else SuccessGreen)
                            )
                        }

                        IconButton(
                            onClick = { viewModel.toggleSettingsDialog(true) },
                            modifier = Modifier.testTag("open_settings_button")
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )

                // Sticky Study Context Bar (immediately below TopAppBar)
                StickyStudyContextBar(
                    activePaper = activePaper,
                    activeTopic = activeTopic,
                    onClick = { showContextSheet = true }
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f),
                    thickness = 0.8.dp
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.testTag("bottom_nav_bar")
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
                        icon = { Icon(icon, contentDescription = label) },
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
            onDismiss = { showContextSheet = false }
        )
    }
}

@Composable
fun StickyStudyContextBar(
    activePaper: PaperCode,
    activeTopic: SyllabusTopic,
    onClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("sticky_study_context_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Paper Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(activePaper.colorHex).copy(alpha = 0.18f),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = activePaper.shortName,
                        color = Color(activePaper.colorHex),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                    )
                }

                // Topic Title
                Text(
                    text = activeTopic.topicName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                // 🆕 Updated Badge if recent amendment
                if (activeTopic.hasRecentAmendment) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Amber500.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "🆕 Updated",
                            color = Amber500,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                        )
                    }
                }

                // Quiet [T1] badge if Tier 1
                if (activeTopic.priorityTier == PriorityTier.TIER_1) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFEF4444).copy(alpha = 0.16f)
                    ) {
                        Text(
                            text = "T1",
                            color = Color(0xFFEF4444),
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Default.UnfoldMore,
                contentDescription = "Switch Study Topic",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyContextBottomSheet(
    viewModel: CaLabViewModel,
    currentPaper: PaperCode,
    currentTopic: SyllabusTopic,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedPaper by remember(currentPaper) { mutableStateOf(currentPaper) }
    var searchQuery by remember { mutableStateOf("") }

    val sortedTopics = remember(selectedPaper, viewModel.catalogSortOrder.collectAsState().value) {
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
        sheetState = sheetState
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
                Text(
                    text = "Select Study Context",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search chapter, standard, or section...", fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Horizontal Paper Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                PaperCode.entries.forEach { paper ->
                    FilterChip(
                        selected = selectedPaper == paper,
                        onClick = { selectedPaper = paper },
                        label = {
                            Text(
                                text = paper.shortName,
                                fontSize = 11.sp,
                                fontWeight = if (selectedPaper == paper) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Topics List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(filteredTopics, key = { it.id }) { topic ->
                    val isCurrent = topic.id == currentTopic.id
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrent)
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            else
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
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
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Ch ${topic.icaiChapterNumber} • ${topic.chapter}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = topic.topicName,
                                    fontSize = 13.sp,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (topic.hasRecentAmendment) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Amber500.copy(alpha = 0.2f),
                                        modifier = Modifier.padding(end = 4.dp)
                                    ) {
                                        Text(
                                            text = "🆕 Updated",
                                            color = Amber500,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(topic.priorityTier.badgeColorHex).copy(alpha = 0.16f)
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
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
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
