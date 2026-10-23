package com.example.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.TextUnit
import java.util.regex.Pattern

/**
 * Parses basic markdown syntax:
 * - **bold** into bold SpanStyle
 * - *italic* into italic SpanStyle
 * - `code` into monospace SpanStyle
 * - Normalizes leading "- " and "* " into clean bullet dots ("• ")
 * - Strips leading markdown header tokens (###, ##, #)
 */
@Composable
fun FormattedText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = Color.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified
) {
    val annotated = remember(text) {
        parseMarkdownToAnnotatedString(text)
    }

    Text(
        text = annotated,
        modifier = modifier,
        style = style,
        color = color,
        lineHeight = lineHeight
    )
}

fun parseMarkdownToAnnotatedString(rawText: String): AnnotatedString {
    // Normalize bullet lines and header tokens
    val normalizedLines = rawText.lines().map { line ->
        val trimmed = line.trimStart()
        when {
            trimmed.startsWith("- ") -> "• " + trimmed.substring(2)
            trimmed.startsWith("* ") -> "• " + trimmed.substring(2)
            trimmed.startsWith("### ") -> trimmed.substring(4)
            trimmed.startsWith("## ") -> trimmed.substring(3)
            trimmed.startsWith("# ") -> trimmed.substring(2)
            else -> line
        }
    }
    val processedText = normalizedLines.joinToString("\n")

    return buildAnnotatedString {
        val pattern = Pattern.compile("(\\*\\*.*?\\*\\*|\\*.*?\\*|`.*?`)")
        val matcher = pattern.matcher(processedText)
        var lastIndex = 0

        while (matcher.find()) {
            val start = matcher.start()
            val end = matcher.end()

            if (start > lastIndex) {
                append(processedText.substring(lastIndex, start))
            }

            val token = matcher.group()
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
}
