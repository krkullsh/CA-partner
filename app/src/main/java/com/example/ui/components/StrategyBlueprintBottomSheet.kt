package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.data.curriculum.StrategyBlueprintReader
import com.example.data.model.PaperCode
import com.example.ui.theme.Amber500
import com.example.ui.theme.Sky500
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StrategyBlueprintBottomSheet(
    paper: PaperCode,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    val rawMarkdown = remember(paper) {
        StrategyBlueprintReader.getBlueprintMarkdown(context, paper)
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedQuickSection by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()

    // Pre-extract major section headings from the markdown
    val extractedSections = remember(rawMarkdown) {
        val lines = rawMarkdown.lines()
        val sections = mutableListOf<String>()
        lines.forEach { line ->
            val trimmed = line.trim()
            if ((trimmed.startsWith("#") || trimmed.startsWith("Section ") || trimmed.startsWith("Chapter ")) &&
                trimmed.length in 5..75 &&
                !trimmed.contains("CATestSeries.org", ignoreCase = true)
            ) {
                val clean = trimmed.replace(Regex("^#+\\s*"), "").trim()
                if (clean.isNotBlank() && clean !in sections) {
                    sections.add(clean)
                }
            }
        }
        if (sections.isEmpty()) {
            listOf("Exam Overview", "Priority Matrix", "Compulsory Q1 Strategy", "Exam-Day Execution")
        } else {
            sections.take(12)
        }
    }

    // Split markdown into searchable segments (by double-newlines or sections)
    val displayContent by remember(rawMarkdown, searchQuery, selectedQuickSection) {
        derivedStateOf {
            if (searchQuery.isNotBlank()) {
                val queryLower = searchQuery.trim().lowercase()
                val paragraphs = rawMarkdown.split("\n\n")
                val matching = paragraphs.filter { it.lowercase().contains(queryLower) }
                if (matching.isNotEmpty()) {
                    "### 🔍 Search Results for \"$searchQuery\" (${matching.size} matching sections):\n\n" +
                        matching.joinToString("\n\n---\n\n")
                } else {
                    "No sections matched \"$searchQuery\". Try searching for \"Compulsory Q1\", \"Tier 1\", \"Schedule III\", \"FEMA\", \"CARO\", or \"Working Capital\"."
                }
            } else if (selectedQuickSection != null) {
                val sectionLower = selectedQuickSection!!.lowercase()
                val paragraphs = rawMarkdown.split("\n\n")
                val startIndex = paragraphs.indexOfFirst { it.lowercase().contains(sectionLower) }
                if (startIndex != -1) {
                    // Show from this section forward
                    val takeSections = paragraphs.drop(startIndex).take(10)
                    takeSections.joinToString("\n\n")
                } else {
                    rawMarkdown
                }
            } else {
                rawMarkdown
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.95f)
            .testTag("strategy_blueprint_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header: Paper title, close button, and metadata
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "${paper.shortName} Master Strategy Blueprint",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "12-Attempt Exam Trend Analysis • Open-Timeline Mastery",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            sheetState.hide()
                            onDismiss()
                        }
                    },
                    modifier = Modifier.testTag("close_blueprint_sheet")
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close Sheet")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar for Sections and Keywords
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    selectedQuickSection = null
                },
                placeholder = {
                    Text("Search Blueprint (e.g. Compulsory Q1, 3-Hour Clock, Tier 1, Traps)...", fontSize = 12.sp)
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search", modifier = Modifier.size(18.dp))
                        }
                    }
                },
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("blueprint_search_field"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Section Jump Chips
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item {
                    FilterChip(
                        selected = searchQuery.isBlank() && selectedQuickSection == null,
                        onClick = {
                            searchQuery = ""
                            selectedQuickSection = null
                        },
                        label = { Text("Full Blueprint", fontSize = 11.sp) },
                        modifier = Modifier.testTag("chip_full_blueprint"),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
                items(extractedSections, key = { it }) { sectionTitle ->
                    val isSelected = selectedQuickSection == sectionTitle
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            searchQuery = ""
                            selectedQuickSection = if (isSelected) null else sectionTitle
                        },
                        label = {
                            Text(
                                text = sectionTitle.take(30) + if (sectionTitle.length > 30) "..." else "",
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.testTag("section_chip_${sectionTitle.take(10).replace(" ", "_")}"),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Sky500.copy(alpha = 0.15f),
                            selectedLabelColor = Sky500
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Blueprint Markdown Content Reader
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val contentSections = remember(displayContent) {
                    displayContent.split("\n\n").filter { it.isNotBlank() }
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(contentSections, key = { it.hashCode() }) { paragraph ->
                        FormattedText(
                            text = paragraph,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom action: Dismiss button
            Button(
                onClick = {
                    coroutineScope.launch {
                        sheetState.hide()
                        onDismiss()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dismiss_blueprint_bottom_button")
            ) {
                Text("Return to Lab & Practice")
            }
        }
    }
}
