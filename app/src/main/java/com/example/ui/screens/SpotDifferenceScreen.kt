package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.example.ui.theme.Sky500
import com.example.ui.viewmodel.CaLabViewModel

@Composable
fun SpotDifferenceScreen(viewModel: CaLabViewModel) {
    val state by viewModel.diffState.collectAsState()
    val allDifferences = androidx.compose.runtime.remember { CuratedDifferences.differences }
    val filteredDifferences = androidx.compose.runtime.remember(state.selectedPaper, state.searchQuery, allDifferences) {
        allDifferences.filter { diff ->
            (state.selectedPaper == null || diff.paper == state.selectedPaper) &&
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
        // Paper selection chips
        item {
            Spacer(modifier = Modifier.height(4.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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

        // Search bar
        item {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.setDiffSearch(it) },
                placeholder = { Text("Search comparison (e.g. 194C vs 194J, AS 5, Void vs Illegal)...", fontSize = 12.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("diff_search_field"),
                shape = RoundedCornerShape(20.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
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
