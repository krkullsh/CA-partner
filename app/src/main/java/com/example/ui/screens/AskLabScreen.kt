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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import com.example.data.model.StandardBlueprint
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.curriculum.CaSyllabusData
import com.example.data.model.ExplanationDepth
import com.example.data.model.PaperCode
import com.example.ui.components.FormattedText
import com.example.ui.theme.Amber500
import com.example.ui.viewmodel.CaLabViewModel

@Composable
fun AskLabScreen(viewModel: CaLabViewModel) {
    val state by viewModel.askState.collectAsState()
    val bookmarkedIds by viewModel.bookmarkedIds.collectAsState()
    val context = LocalContext.current
    var selectedModule by remember(state.selectedPaper) { mutableStateOf("All") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("ask_lab_screen"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Paper selection chips & Bookmarked Only toggle
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

        // Custom search / doubt input
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

        // Topic quick chips & Module filtering
        item {
            val allTopics = androidx.compose.runtime.remember(state.selectedPaper) {
                CaSyllabusData.getTopicsForPaper(state.selectedPaper)
            }
            val availableModules = androidx.compose.runtime.remember(allTopics) {
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

                val topics = allTopics.filter {
                    (selectedModule == "All" || it.moduleName == selectedModule) &&
                    (!state.showBookmarkedOnly || bookmarkedIds.contains(it.id))
                }

                if (topics.isEmpty() && state.showBookmarkedOnly) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = "⭐ No bookmarked topics for ${state.selectedPaper.shortName} yet! Tap the bookmark icon on any topic to save for LDR.",
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
                        items(topics, key = { it.id }) { t ->
                            val isSelected = state.selectedTopic?.id == t.id
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clickable { viewModel.selectAskTopic(t) }
                                    .testTag("topic_chip_${t.id}")
                            ) {
                                Text(
                                    text = t.topicName.take(30) + if (t.topicName.length > 30) "..." else "",
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

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
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = when (depth) {
                                                ExplanationDepth.DEPTH_1 -> "Tier 1: Simple"
                                                ExplanationDepth.DEPTH_2 -> "Tier 2: Standard"
                                                ExplanationDepth.DEPTH_3 -> "Tier 3: ICAI Exam"
                                            },
                                            fontWeight = if (state.selectedDepth == depth) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 12.sp
                                        )
                                    }
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
    }
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
                        imageVector = Icons.Default.MenuBook,
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
                            icon = Icons.Default.FactCheck,
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
                            icon = Icons.Default.FactCheck,
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

