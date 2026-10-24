package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.regex.Pattern

/**
 * Parses markdown syntax safely:
 * - **bold** into bold SpanStyle
 * - *italic* into italic SpanStyle
 * - `code` into monospace SpanStyle
 * - Markdown tables (containing `|`) rendered in a horizontal scroll container
 * - Normalizes leading "- " and "* " into clean bullet dots ("• ")
 * - Strips leading markdown header tokens (###, ##, #)
 * - Safe against IndexOutOfBoundsException and nested vertical scroll crashes.
 */
@Composable
fun FormattedText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = Color.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified
) {
    if (text.isBlank()) return

    // Detect markdown table blocks
    val isTable = remember(text) {
        val lines = text.lines()
        lines.size >= 2 && lines.any { it.trim().startsWith("|") && it.contains("-") }
    }

    if (isTable) {
        MarkdownTableLayout(text = text, modifier = modifier)
    } else {
        val annotated = remember(text) {
            runCatching {
                parseMarkdownToAnnotatedString(text)
            }.getOrElse {
                AnnotatedString(text)
            }
        }

        Text(
            text = annotated,
            modifier = modifier,
            style = style,
            color = color,
            lineHeight = lineHeight
        )
    }
}

@Composable
fun MarkdownTableLayout(
    text: String,
    modifier: Modifier = Modifier
) {
    val tableData = remember(text) {
        parseMarkdownTable(text)
    }

    if (tableData.headers.isEmpty() && tableData.rows.isEmpty()) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
        return
    }

    val horizontalScrollState = rememberScrollState()

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .horizontalScroll(horizontalScrollState)
                .padding(8.dp)
        ) {
            // Header Row
            if (tableData.headers.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(vertical = 6.dp, horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    tableData.headers.forEach { header ->
                        Box(
                            modifier = Modifier
                                .widthIn(min = 100.dp, max = 220.dp)
                                .padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = header.trim(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            // Data Rows
            tableData.rows.forEachIndexed { rowIndex, rowCells ->
                val rowBg = if (rowIndex % 2 == 0) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                Row(
                    modifier = Modifier
                        .background(rowBg)
                        .padding(vertical = 6.dp, horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val columnCount = maxOf(tableData.headers.size, rowCells.size)
                    for (colIndex in 0 until columnCount) {
                        val cellText = rowCells.getOrNull(colIndex)?.trim() ?: ""
                        Box(
                            modifier = Modifier
                                .widthIn(min = 100.dp, max = 220.dp)
                                .padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = cellText,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class ParsedTable(
    val headers: List<String>,
    val rows: List<List<String>>
)

private fun parseMarkdownTable(text: String): ParsedTable {
    val headers = mutableListOf<String>()
    val rows = mutableListOf<List<String>>()

    runCatching {
        val lines = text.lines().map { it.trim() }.filter { it.isNotBlank() && it.contains("|") }
        if (lines.isEmpty()) return ParsedTable(headers, rows)

        var headerFound = false

        lines.forEach { line ->
            val cells = line.split("|")
                .map { it.trim() }
                .filterIndexed { index, _ -> index != 0 && index != line.split("|").lastIndex }

            if (cells.isNotEmpty()) {
                // Check if this is separator line like |---|---|
                if (cells.all { cell -> cell.all { char -> char == '-' || char == ':' || char == ' ' } }) {
                    headerFound = true
                } else if (!headerFound && headers.isEmpty()) {
                    headers.addAll(cells)
                } else {
                    rows.add(cells)
                }
            }
        }
    }.onFailure {
        headers.clear()
        rows.clear()
    }

    return ParsedTable(headers, rows)
}

fun parseMarkdownToAnnotatedString(rawText: String): AnnotatedString {
    return runCatching {
        // Normalize bullet lines and header tokens safely
        val normalizedLines = rawText.lines().map { line ->
            val trimmed = line.trimStart()
            when {
                trimmed.startsWith("- ") && trimmed.length >= 2 -> "• " + trimmed.substring(2)
                trimmed.startsWith("* ") && trimmed.length >= 2 -> "• " + trimmed.substring(2)
                trimmed.startsWith("### ") && trimmed.length >= 4 -> trimmed.substring(4)
                trimmed.startsWith("## ") && trimmed.length >= 3 -> trimmed.substring(3)
                trimmed.startsWith("# ") && trimmed.length >= 2 -> trimmed.substring(2)
                else -> line
            }
        }
        val processedText = normalizedLines.joinToString("\n")

        buildAnnotatedString {
            val pattern = Pattern.compile("(\\*\\*.*?\\*\\*|\\*.*?\\*|`.*?`)")
            val matcher = pattern.matcher(processedText)
            var lastIndex = 0

            while (matcher.find()) {
                val start = matcher.start()
                val end = matcher.end()

                if (start > lastIndex && start <= processedText.length) {
                    append(processedText.substring(lastIndex, start))
                }

                val token = matcher.group() ?: ""
                when {
                    token.startsWith("**") && token.endsWith("**") && token.length >= 4 -> {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(token.substring(2, token.length - 2))
                        }
                    }
                    token.startsWith("`") && token.endsWith("`") && token.length >= 2 -> {
                        withStyle(SpanStyle(fontFamily = FontFamily.Monospace)) {
                            append(token.substring(1, token.length - 1))
                        }
                    }
                    token.startsWith("*") && token.endsWith("*") && token.length >= 2 -> {
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                            append(token.substring(1, token.length - 1))
                        }
                    }
                    else -> {
                        append(token)
                    }
                }
                lastIndex = end
            }

            if (lastIndex < processedText.length) {
                append(processedText.substring(lastIndex))
            }
        }
    }.getOrElse {
        AnnotatedString(rawText)
    }
}
