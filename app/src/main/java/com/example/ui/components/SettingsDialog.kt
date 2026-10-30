package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.CatalogSortOrder
import com.example.data.model.ExperienceLevel
import com.example.data.model.ThemeMode
import com.example.ui.theme.Amber500
import com.example.ui.theme.Sky500
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.CaLabViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialog(
    viewModel: CaLabViewModel,
    onDismiss: () -> Unit
) {
    val isOffline by viewModel.isOffline.collectAsState()
    var customKey by remember { mutableStateOf(viewModel.repository.customApiKey) }
    val hasValidKey = viewModel.repository.hasValidGeminiKey()

    val groqApiKey by viewModel.groqApiKey.collectAsState()
    val selectedAiProvider by viewModel.selectedAiProvider.collectAsState()

    val experienceLevel by viewModel.experienceLevel.collectAsState()
    val catalogSortOrder by viewModel.catalogSortOrder.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()

    val clipboardManager = LocalClipboardManager.current
    var backupStatusMessage by remember { mutableStateOf<String?>(null) }
    var isRestoreInputVisible by remember { mutableStateOf(false) }
    var restoreJsonInput by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .testTag("settings_dialog_card"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CA-Lab Settings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_settings_button")) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Appearance & Theme Quick Toggle
                Text("Appearance & Theme", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ThemeMode.entries.forEach { mode ->
                        val isSelected = themeMode == mode
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setThemeMode(mode) },
                            leadingIcon = {
                                Icon(
                                    imageVector = when (mode) {
                                        ThemeMode.LIGHT -> Icons.Default.LightMode
                                        ThemeMode.DARK -> Icons.Default.DarkMode
                                        ThemeMode.SYSTEM -> Icons.Default.BrightnessAuto
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            label = {
                                Text(
                                    text = mode.title,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("theme_mode_${mode.name.lowercase()}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = when (themeMode) {
                            ThemeMode.SYSTEM -> "⚙️ System: Automatically matches your device's light or dark mode theme."
                            ThemeMode.LIGHT -> "☀️ Light Mode: Crisp editorial light palette for well-lit study environments."
                            ThemeMode.DARK -> "🌙 Dark Mode: High-contrast slate dark palette for night revisions."
                        },
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 15.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 1. Experience & Display Tone
                Text("Experience & Display Tone", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ExperienceLevel.entries.forEach { level ->
                        FilterChip(
                            selected = experienceLevel == level,
                            onClick = { viewModel.setExperienceLevel(level) },
                            label = {
                                Text(
                                    text = when (level) {
                                        ExperienceLevel.NOVICE -> "Novice"
                                        ExperienceLevel.KNOWLEDGED -> "Knowledged"
                                        ExperienceLevel.EXAM_READY -> "Exam Ready"
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = if (experienceLevel == level) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = when (experienceLevel) {
                            ExperienceLevel.NOVICE -> "☕ Novice: Everyday Kirana/Chai business analogies, gentle grading tone, and foundation concept building."
                            ExperienceLevel.KNOWLEDGED -> "📊 Knowledged: Standard revision notes, statutory thresholds, provisos, and practical computation formats."
                            ExperienceLevel.EXAM_READY -> "🏛️ Exam Ready: Strict ICAI provision-facts-conclusion format, aggressive trap detection, and exam marking rubrics."
                        },
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 15.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 2. Catalogue Organization Toggle
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Sort,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Catalogue Organization",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = if (catalogSortOrder == CatalogSortOrder.OFFICIAL_ICAI)
                                        "Official ICAI Chapter Chronology (Module 1, 2, 3)"
                                    else
                                        "Exam Priority Tier View (Tier 1 High-Yield first)",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Switch(
                            checked = catalogSortOrder == CatalogSortOrder.PRIORITY_TIER,
                            onCheckedChange = { isPriority ->
                                viewModel.setCatalogSortOrder(
                                    if (isPriority) CatalogSortOrder.PRIORITY_TIER else CatalogSortOrder.OFFICIAL_ICAI
                                )
                            },
                            modifier = Modifier.testTag("catalogue_sort_switch")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 3. Online / Offline Toggle
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isOffline) Icons.Default.WifiOff else Icons.Default.Wifi,
                                    contentDescription = null,
                                    tint = if (isOffline) Amber500 else SuccessGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isOffline) "Offline Mode (Active)" else "Cloud AI Mode (Active)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = if (isOffline) "Instant local heuristic engine" else "Live Cloud AI reasoning",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Switch(
                                checked = !isOffline,
                                onCheckedChange = { viewModel.toggleOfflineMode() },
                                modifier = Modifier.testTag("online_offline_switch")
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isOffline)
                                "⚡ Offline Mode works anywhere with zero internet! It uses preloaded ICAI syllabus provisions, trick cases, difference tables, and intelligent follow-up doubts."
                            else
                                "🌐 Cloud Mode connects to AI to answer any custom doubts, simulate realistic dynamic articleship dialogues, and grade any topic.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 4. Cloud AI Configuration
                Text("Cloud AI Configuration", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))

                // AI Provider Selection Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedAiProvider == "GEMINI",
                        onClick = { viewModel.setAiProvider("GEMINI") },
                        label = { Text("Gemini") }
                    )
                    FilterChip(
                        selected = selectedAiProvider == "GROQ",
                        onClick = { viewModel.setAiProvider("GROQ") },
                        label = { Text("Groq (Llama 3.3 70B)") }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Dynamic Input Fields based on selected Provider
                if (selectedAiProvider == "GEMINI") {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (hasValidKey) SuccessGreen.copy(alpha = 0.12f) else Amber500.copy(alpha = 0.12f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (hasValidKey) "✓ AI Key Active (via Secrets Panel / BuildConfig)" else "ℹ️ Optional Custom Gemini API Key",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (hasValidKey) SuccessGreen else Amber500,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = customKey,
                        onValueChange = {
                            customKey = it
                            viewModel.setCustomApiKey(it)
                        },
                        placeholder = { Text("Enter custom Gemini API key", fontSize = 12.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_key_field"),
                        shape = RoundedCornerShape(10.dp),
                        leadingIcon = { Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        singleLine = true
                    )
                } else {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Amber500.copy(alpha = 0.12f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "ℹ️ Groq API Key required for Llama 3.3 70B",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Amber500,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = groqApiKey,
                        onValueChange = { viewModel.setGroqApiKey(it) },
                        placeholder = { Text("gsk_...", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        leadingIcon = { Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                // Philosophy of CA-Lab
                Text("Philosophy of CA-Lab", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\"Knowing isn't understanding—you understand a provision or standard best when you can teach it.\"",
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Modeled after AeraWise's Feynman learning method, CA-Lab challenges you to explain concepts without reading from notes. By answering probing doubts on limits, exceptions, and provisos, you achieve true ICAI exam mastery.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                // 5. Data Safety & Backup (V6)
                Text("🛡️ Data Safety & Backup (V6)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("backup_section_card")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Export or restore your solved written counts, diagnostic errors, oral recall quotas, and bookmarks.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 15.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.exportProgress { jsonString ->
                                        if (jsonString.isNotBlank()) {
                                            clipboardManager.setText(AnnotatedString(jsonString))
                                            backupStatusMessage = "✓ Copied backup (${jsonString.length} bytes) to clipboard!"
                                        } else {
                                            backupStatusMessage = "⚠️ No study data to export yet."
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("export_backup_button"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("📤 Copy Backup", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    val clipText = clipboardManager.getText()?.text?.trim()
                                    if (!clipText.isNullOrBlank() && clipText.startsWith("{")) {
                                        viewModel.importProgress(clipText) { msg ->
                                            backupStatusMessage = msg
                                        }
                                    } else {
                                        isRestoreInputVisible = !isRestoreInputVisible
                                        if (isRestoreInputVisible && !clipText.isNullOrBlank()) {
                                            restoreJsonInput = clipText
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("restore_backup_button"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("📥 Restore", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        if (isRestoreInputVisible) {
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value = restoreJsonInput,
                                onValueChange = { restoreJsonInput = it },
                                placeholder = { Text("Paste backup JSON here...", fontSize = 11.sp) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("restore_json_field"),
                                shape = RoundedCornerShape(8.dp),
                                minLines = 2,
                                maxLines = 4
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Button(
                                onClick = {
                                    if (restoreJsonInput.isNotBlank()) {
                                        viewModel.importProgress(restoreJsonInput.trim()) { msg ->
                                            backupStatusMessage = msg
                                            isRestoreInputVisible = false
                                        }
                                    } else {
                                        backupStatusMessage = "⚠️ Please paste valid backup JSON first."
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("confirm_restore_button"),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Apply Restored Data", fontSize = 11.sp)
                            }
                        }

                        if (!backupStatusMessage.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (backupStatusMessage!!.startsWith("✓") || backupStatusMessage!!.contains("Successfully", ignoreCase = true))
                                    SuccessGreen.copy(alpha = 0.15f)
                                else
                                    Amber500.copy(alpha = 0.15f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = backupStatusMessage!!,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (backupStatusMessage!!.startsWith("✓") || backupStatusMessage!!.contains("Successfully", ignoreCase = true))
                                        SuccessGreen
                                    else
                                        Amber500,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_settings_button")
                ) {
                    Text("Done")
                }
            }
        }
    }
}
