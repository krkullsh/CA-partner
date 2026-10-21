package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.LabMode
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

    Scaffold(
        topBar = {
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
                    // Online / Offline Status Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isOffline) Amber500.copy(alpha = 0.15f) else SuccessGreen.copy(alpha = 0.15f),
                        modifier = Modifier
                            .clickable { viewModel.toggleOfflineMode() }
                            .padding(end = 4.dp)
                            .testTag("offline_status_badge")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (isOffline) Icons.Default.WifiOff else Icons.Default.Wifi,
                                contentDescription = null,
                                tint = if (isOffline) Amber500 else SuccessGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isOffline) "Offline" else "Cloud AI",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isOffline) Amber500 else SuccessGreen
                            )
                        }
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
                        LabMode.SPOT_DIFFERENCE -> Icons.Default.CompareArrows
                        LabMode.VERDICTS -> Icons.Default.Assessment
                    }

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setMode(mode) },
                        icon = { Icon(icon, contentDescription = mode.displayName) },
                        label = {
                            Text(
                                text = when (mode) {
                                    LabMode.TEACH_LAB -> "Teach"
                                    LabMode.CATCH_LAB_OUT -> "Trick 🎭"
                                    LabMode.ASK_LAB -> "Ask Lab"
                                    LabMode.SPOT_DIFFERENCE -> "Differences"
                                    LabMode.VERDICTS -> "Verdicts"
                                },
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
}
