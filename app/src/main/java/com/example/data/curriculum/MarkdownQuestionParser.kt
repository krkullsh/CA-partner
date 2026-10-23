package com.example.data.curriculum

import android.content.Context
import com.example.data.model.PaperCode
import com.example.data.model.SyllabusTopic
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Pattern

object MarkdownQuestionParser {

    private val questionsCache = ConcurrentHashMap<PaperCode, List<PracticeQuestion>>()

    private val paperAssetFileMap = mapOf(
        PaperCode.PAPER_1 to "Advance accounting.md",
        PaperCode.PAPER_2 to "corporate other law_.md",
        PaperCode.PAPER_4 to "Cost and Management Accounting..md",
        PaperCode.PAPER_5 to "Auditing and Ethics.md",
        PaperCode.PAPER_6 to "FM and SM.md"
    )

    fun getQuestionsForPaper(context: Context, paper: PaperCode): List<PracticeQuestion> {
        val cached = questionsCache[paper]
        if (cached != null && cached.isNotEmpty()) {
            return cached
        }

        val assetName = paperAssetFileMap[paper]
        val parsedFromAsset = if (assetName != null) {
            parseFromAsset(context, assetName, paper)
        } else {
            emptyList()
        }

        val curated = CuratedQuestionBank.getQuestionsForPaper(paper)
        // Combine: put curated (which include compulsory Q1 fused problems) first, followed by parsed questions
        val combined = (curated + parsedFromAsset).distinctBy { it.id }
        questionsCache[paper] = combined
        return combined
    }

    fun getQuestionsForTopic(context: Context, topic: SyllabusTopic): List<PracticeQuestion> {
        val allForPaper = getQuestionsForPaper(context, topic.paper)
        val topicNameLower = topic.topicName.lowercase()
        val chapterLower = topic.chapter.lowercase()
        val moduleLower = topic.moduleName.lowercase()
        val fusedKeywords = topic.fusedWithTopics.map { it.lowercase() }

        // Find matches by topic name, chapter name, or fused topics
        val matched = allForPaper.filter { q ->
            val qChap = q.chapterName.lowercase()
            val qText = q.questionText.lowercase()
            val qTags = q.tags.joinToString(" ").lowercase()

            val matchesTopic = topicNameLower.isNotBlank() && (
                qChap.contains(topicNameLower) ||
                qText.contains(topicNameLower) ||
                (topic.id.startsWith("p1_as") && qText.contains(topic.topicName.split(":")[0].trim().lowercase()))
            )
            val matchesChapter = chapterLower.isNotBlank() && (qChap.contains(chapterLower) || qText.contains(chapterLower))
            val matchesFused = fusedKeywords.any { f -> qChap.contains(f) || qText.contains(f) || qTags.contains(f) }

            matchesTopic || matchesChapter || matchesFused
        }

        return if (matched.isNotEmpty()) {
            matched
        } else {
            // Fallback to top curated questions for this paper so student always has high-chance practice
            allForPaper.take(4)
        }
    }

    private enum class ParsingSection { NONE, QUESTION, ANSWER, APPROACH, OBSERVATIONS }

    fun parseFromAsset(context: Context, filename: String, paper: PaperCode): List<PracticeQuestion> {
        val parsedList = mutableListOf<PracticeQuestion>()
        try {
            val assetManager = context.assets
            val filesInAssets = assetManager.list("") ?: emptyArray()
            val targetFile = filesInAssets.firstOrNull { it.equals(filename, ignoreCase = true) } ?: filename

            val inputStream = try {
                assetManager.open(targetFile)
            } catch (e: Exception) {
                return emptyList()
            }

            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var currentChapter = "Chapter 1"
                var currentQNum = ""
                val currentQText = StringBuilder()
                var currentMetaRaw = ""
                val currentAnswer = StringBuilder()
                val currentApproach = StringBuilder()
                val currentObservations = StringBuilder()

                val questionPattern = Pattern.compile("^(?:PART\\s+[A-Z]\\s+)?(?:Question|Q)\\.?\\s*(\\d+[a-zA-Z]?)[:.]?\\s*(.*)", Pattern.CASE_INSENSITIVE)
                val answerPattern = Pattern.compile("^(?:Answer|Ans|Solution)\\.?\\s*(\\d+[a-zA-Z]?)?[:.]?\\s*(.*)", Pattern.CASE_INSENSITIVE)
                val approachPattern = Pattern.compile("^(?:Approach\\s*(?:&|and)?\\s*Time-?Saving\\s*Tips?|Approach\\s*:)(.*)", Pattern.CASE_INSENSITIVE)
                val observationPattern = Pattern.compile("^(?:Examiner\\s*Observations?|Examiner\\s*Comments?)(.*)", Pattern.CASE_INSENSITIVE)
                val chapterPattern = Pattern.compile("^(?:Chapter|Unit|#+)\\s*[-:]?\\s*(\\d+.*)", Pattern.CASE_INSENSITIVE)

                var section = ParsingSection.NONE

                fun flushCurrentQuestion() {
                    if (currentQNum.isNotBlank() && (currentQText.isNotBlank() || currentAnswer.isNotBlank())) {
                        val (marks, minutes, tags) = parseMetadata(currentMetaRaw)
                        val question = PracticeQuestion(
                            id = "${paper.code}_${currentChapter.take(15).trim().replace(" ", "_")}_q$currentQNum".lowercase(),
                            paper = paper,
                            chapterName = currentChapter,
                            questionNumber = currentQNum,
                            questionText = currentQText.toString().trim(),
                            marks = marks,
                            minutesAllowed = minutes,
                            tags = tags,
                            officialAnswer = if (currentAnswer.isNotBlank()) currentAnswer.toString().trim() else "Refer to standard ICAI suggested answer.",
                            approachTips = if (currentApproach.isNotBlank()) currentApproach.toString().trim() else "Structure answer with Legal Provision, Analysis, and clear Conclusion.",
                            examinerObservations = if (currentObservations.isNotBlank()) currentObservations.toString().trim() else "Quote specific Section / AS and avoid vague generalizations."
                        )
                        parsedList.add(question)
                    }
                    currentQNum = ""
                    currentQText.clear()
                    currentMetaRaw = ""
                    currentAnswer.clear()
                    currentApproach.clear()
                    currentObservations.clear()
                }

                var line: String? = reader.readLine()
                var count = 0
                // Parse up to 200 questions to keep memory and performance optimal
                while (line != null && count < 200) {
                    val trimmed = line.trim()

                    // Skip footer copyright lines
                    if (trimmed.contains("Catestseries.org", ignoreCase = true) || trimmed.startsWith("©")) {
                        line = reader.readLine()
                        continue
                    }

                    // Check for chapter header
                    val chapterMatcher = chapterPattern.matcher(trimmed)
                    if (chapterMatcher.find() && trimmed.length < 90) {
                        currentChapter = trimmed.replace("#", "").trim()
                        line = reader.readLine()
                        continue
                    }

                    // Check for Question start
                    val qMatcher = questionPattern.matcher(trimmed)
                    if (qMatcher.find()) {
                        flushCurrentQuestion()
                        count++
                        currentQNum = qMatcher.group(1) ?: count.toString()
                        val remainder = qMatcher.group(2) ?: ""
                        if (remainder.isNotBlank()) {
                            currentQText.append(remainder).append("\n")
                        }
                        section = ParsingSection.QUESTION
                        line = reader.readLine()
                        continue
                    }

                    // Check for Answer start
                    val ansMatcher = answerPattern.matcher(trimmed)
                    if (ansMatcher.find()) {
                        section = ParsingSection.ANSWER
                        val remainder = ansMatcher.group(2) ?: ""
                        if (remainder.isNotBlank()) {
                            currentAnswer.append(remainder).append("\n")
                        }
                        line = reader.readLine()
                        continue
                    }

                    // Check for Approach & Time-saving tips
                    val appMatcher = approachPattern.matcher(trimmed)
                    if (appMatcher.find()) {
                        section = ParsingSection.APPROACH
                        val remainder = appMatcher.group(1) ?: ""
                        if (remainder.isNotBlank()) {
                            currentApproach.append(remainder).append("\n")
                        }
                        line = reader.readLine()
                        continue
                    }

                    // Check for Examiner observations
                    val obsMatcher = observationPattern.matcher(trimmed)
                    if (obsMatcher.find()) {
                        section = ParsingSection.OBSERVATIONS
                        val remainder = obsMatcher.group(1) ?: ""
                        if (remainder.isNotBlank()) {
                            currentObservations.append(remainder).append("\n")
                        }
                        line = reader.readLine()
                        continue
                    }

                    // Metadata bracket e.g. [5 Marks | 6 Minutes | ...] or (5 marks|4 minutes)...
                    if (section == ParsingSection.QUESTION && currentMetaRaw.isBlank() &&
                        (trimmed.startsWith("[") || (trimmed.startsWith("(") && (trimmed.contains("mark", ignoreCase = true) || trimmed.contains("min", ignoreCase = true))))
                    ) {
                        currentMetaRaw = trimmed
                        line = reader.readLine()
                        continue
                    }

                    // Append to current active section
                    when (section) {
                        ParsingSection.QUESTION -> currentQText.append(trimmed).append("\n")
                        ParsingSection.ANSWER -> currentAnswer.append(trimmed).append("\n")
                        ParsingSection.APPROACH -> currentApproach.append(trimmed).append("\n")
                        ParsingSection.OBSERVATIONS -> currentObservations.append(trimmed).append("\n")
                        ParsingSection.NONE -> {}
                    }

                    line = reader.readLine()
                }

                flushCurrentQuestion()
            }
        } catch (e: Exception) {
            // Non-fatal; falls back to curated questions
        }
        return parsedList
    }

    private fun parseMetadata(metaRaw: String): Triple<Int, Int, List<String>> {
        var marks = 5
        var minutes = 8
        val tags = mutableListOf<String>()

        if (metaRaw.isBlank()) {
            return Triple(marks, minutes, listOf("ICAI FAV", "High Chance"))
        }

        // Clean surrounding braces/brackets
        val cleaned = metaRaw.removePrefix("[").removeSuffix("]").removePrefix("(").removeSuffix(")")

        // Extract marks
        val marksMatcher = Pattern.compile("(\\d+)\\s*(?:Marks|marks|M\\b)", Pattern.CASE_INSENSITIVE).matcher(cleaned)
        if (marksMatcher.find()) {
            marks = marksMatcher.group(1)?.toIntOrNull() ?: 5
        }

        // Extract minutes
        val minsMatcher = Pattern.compile("(\\d+)\\s*(?:Minutes|minutes|mins|Mins|min)", Pattern.CASE_INSENSITIVE).matcher(cleaned)
        if (minsMatcher.find()) {
            minutes = minsMatcher.group(1)?.toIntOrNull() ?: kotlin.math.max(4, (marks * 1.8).toInt())
        } else {
            minutes = kotlin.math.max(4, (marks * 1.8).toInt())
        }

        // Extract tags
        val rawTokens = cleaned.split("|", "I", "•", "·", ")(")
        rawTokens.forEach { token ->
            val t = token.trim()
                .removePrefix("(").removeSuffix(")")
                .removePrefix("[").removeSuffix("]")
                .trim()
            if (t.isNotBlank() &&
                !t.contains("mark", ignoreCase = true) &&
                !t.contains("min", ignoreCase = true) &&
                t.length in 2..40
            ) {
                tags.add(t)
            }
        }

        if (tags.isEmpty()) {
            tags.add("ICAI FAV")
            tags.add("High Chance")
        }

        return Triple(marks, minutes, tags.distinct())
    }
}
