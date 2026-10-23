package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.curriculum.CuratedDifferences
import com.example.data.model.ComparisonRow
import com.example.data.model.ConceptDifference
import com.example.data.model.PaperCode
import com.example.ui.theme.Amber500
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.Sky500
import com.example.ui.viewmodel.CaLabViewModel

@Composable
fun SpotDifferenceScreen(viewModel: CaLabViewModel) {
    val state by viewModel.diffState.collectAsState()
    val bookmarkedIds by viewModel.bookmarkedIds.collectAsState()
    val allDifferences = androidx.compose.runtime.remember { CuratedDifferences.differences }
    val filteredDifferences = androidx.compose.runtime.remember(state.selectedPaper, state.searchQuery, state.showBookmarkedOnly, bookmarkedIds, allDifferences) {
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("spot_difference_screen"),
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
                        label = { Text("All Papers", fontSize = 12.sp) },
                        modifier = Modifier.testTag("diff_paper_all")
                    )
                }
                items(PaperCode.entries, key = { it.name }) { paper ->
                    val isSelected = state.selectedPaper == paper
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectDiffPaper(paper) },
                        label = { Text(paper.shortName, fontSize = 12.sp) },
                        modifier = Modifier.testTag("diff_paper_${paper.name}")
                    )
                }
            }
        }

        // Search bar with Compare with AI button
        item {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.setDiffSearch(it) },
                placeholder = { Text("Search or type concepts (e.g. AS 7 vs AS 9)...", fontSize = 12.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("diff_search_field"),
                shape = RoundedCornerShape(20.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (state.searchQuery.isNotBlank()) {
                        Button(
                            onClick = { viewModel.generateAiDifference(state.searchQuery) },
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .height(36.dp)
                                .testTag("compare_with_ai_button"),
                            contentPadding = PaddingValues(horizontal = 8.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            if (state.isGeneratingAiDifference) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(14.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Compare with AI", fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                        Text(
                            text = state.aiErrorMessage!!,
                            color = ErrorRed,
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { viewModel.dismissDiffAiError() }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = ErrorRed, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        if (state.isGeneratingAiDifference) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Gemini AI is analyzing ICAI provisions and building comparison matrix...", fontSize = 12.sp)
                    }
                }
            }
        }

        // Available comparison cards row
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
                                text = diff.paper.shortName,
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
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
                                text = currentDiff.title,
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
                                modifier = Modifier
                                    .size(24.dp)
                                    .testTag("bookmark_diff_button")
                            ) {
                                Icon(
                                    imageVector = if (isDiffBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark Difference for LDR",
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
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
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
}

@Composable
fun ComparisonRowCard(
    row: ComparisonRow,
    isQuizMode: Boolean,
    isRevealed: Boolean,
    onReveal: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("diff_row_${row.basisOfDifference.take(6)}"),
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
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
