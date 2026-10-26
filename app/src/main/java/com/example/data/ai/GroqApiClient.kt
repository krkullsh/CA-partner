package com.example.data.ai

import com.example.data.model.CatchLabScenario
import com.example.data.model.ChatMessage
import com.example.data.model.ComparisonRow
import com.example.data.model.ConceptDifference
import com.example.data.model.LabVerdict
import com.example.data.model.McqQuestion
import com.example.data.model.PaperCode
import com.example.data.model.PriorityTier
import com.example.data.model.SubtleError
import com.example.data.model.SyllabusTopic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object GroqApiClient {
    private const val GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions"
    private const val MODEL_NAME = "llama-3.3-70b-versatile"

    /**
     * Existing plain-text generation method for simple chat responses.
     */
    suspend fun generateResponse(prompt: String, apiKey: String): String = withContext(Dispatchers.IO) {
        val url = URL(GROQ_API_URL)
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Authorization", "Bearer $apiKey")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true
            connection.connectTimeout = 30000
            connection.readTimeout = 60000

            val messageObj = JSONObject().apply {
                put("role", "user")
                put("content", prompt)
            }
            val messagesArray = JSONArray().put(messageObj)

            val payload = JSONObject().apply {
                put("model", MODEL_NAME)
                put("messages", messagesArray)
                put("temperature", 0.3)
            }

            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(payload.toString())
                writer.flush()
            }

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonResponse = JSONObject(response)
                val choices = jsonResponse.getJSONArray("choices")
                if (choices.length() > 0) {
                    val firstChoice = choices.getJSONObject(0)
                    val message = firstChoice.getJSONObject("message")
                    return@withContext message.getString("content")
                } else {
                    throw Exception("Empty choices array returned from Groq.")
                }
            } else {
                val errorResponse = connection.errorStream?.bufferedReader()?.use { it.readText() }
                throw Exception("Groq API Error ($responseCode): $errorResponse")
            }
        } finally {
            connection.disconnect()
        }
    }

    /**
     * Generic JSON completion calling Groq with llama-3.3-70b-versatile and response_format type json_object.
     */
    suspend fun generateJsonCompletion(
        apiKey: String,
        systemPrompt: String,
        userPrompt: String
    ): Result<String> = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) {
            return@withContext Result.failure(IllegalStateException("No Groq API key available"))
        }

        val url = URL(GROQ_API_URL)
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Authorization", "Bearer $apiKey")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true
            connection.connectTimeout = 30000
            connection.readTimeout = 60000

            val messagesArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "system")
                    put("content", systemPrompt)
                })
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", userPrompt)
                })
            }

            val responseFormatObj = JSONObject().apply {
                put("type", "json_object")
            }

            val payload = JSONObject().apply {
                put("model", MODEL_NAME)
                put("messages", messagesArray)
                put("response_format", responseFormatObj)
                put("temperature", 0.3)
            }

            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(payload.toString())
                writer.flush()
            }

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonResponse = JSONObject(response)
                val choices = jsonResponse.getJSONArray("choices")
                if (choices.length() > 0) {
                    val firstChoice = choices.getJSONObject(0)
                    val message = firstChoice.getJSONObject("message")
                    val content = message.getString("content")
                    return@withContext Result.success(content)
                } else {
                    return@withContext Result.failure(Exception("Empty choices array returned from Groq."))
                }
            } else {
                val errorResponse = connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
                return@withContext Result.failure(Exception("Groq API Error ($responseCode): $errorResponse"))
            }
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        } finally {
            connection.disconnect()
        }
    }

    // -------------------------------------------------------------
    // JSON PARSER HELPER METHODS
    // -------------------------------------------------------------

    suspend fun generateVerdict(
        apiKey: String,
        topic: SyllabusTopic,
        transcript: List<ChatMessage>
    ): Result<LabVerdict> {
        val conversationText = transcript.joinToString("\n") { "${it.sender.name}: ${it.text}" }
        return generateVerdict(topic, conversationText, apiKey)
    }

    suspend fun generateVerdict(
        topic: SyllabusTopic,
        conversationText: String,
        apiKey: String
    ): Result<LabVerdict> {
        val systemPrompt = "You are CA-Lab's verdict evaluator for ICAI CA Intermediate study companion. Always return strictly valid JSON matching the requested schema."
        val userPrompt = """
You are acting in [LAB'S VERDICT] mode for the ICAI CA Intermediate study companion "CA-Lab".
The student explained the following topic to you in [TEACH LAB] mode:
Paper: ${topic.paper.title}
Topic: ${topic.topicName}
Key Statutory Provisions / Conditions:
${topic.keyProvisions.joinToString("\n") { "- $it" }}
ICAI Examiner Pitfall: ${topic.commonExaminerPitfalls}

Student's Explanation Transcript:
\"\"\"
$conversationText
\"\"\"

Evaluate the student's explanation against the required statutory provisions and common pitfalls.
Keep bullet points in whatYouNailed and whatSlipped concise (max 20 words each). Keep icaiExaminerTip and nextAction concise.
Output strictly a JSON object with this schema:
{
  "masteryScore": <integer between 0 and 100>,
  "whatYouNailed": [<array of string bullet points of strong provisions/rules correctly cited>],
  "whatSlipped": [<array of string bullet points of missed provisos, limits, keywords, or conditions>],
  "icaiExaminerTip": "<specific examiner comment for this topic>",
  "nextAction": "<one actionable revision recommendation>"
}
""".trimIndent()

        val rawResult = generateJsonCompletion(apiKey, systemPrompt, userPrompt)
        if (rawResult.isFailure) return Result.failure(rawResult.exceptionOrNull()!!)

        return try {
            val text = cleanJsonString(rawResult.getOrNull().orEmpty())
            val parsedObj = JSONObject(text)
            val score = parsedObj.optInt("masteryScore", 70).coerceIn(0, 100)
            val nailedArr = parsedObj.optJSONArray("whatYouNailed") ?: JSONArray()
            val slippedArr = parsedObj.optJSONArray("whatSlipped") ?: JSONArray()
            val nailedList = mutableListOf<String>()
            for (i in 0 until nailedArr.length()) nailedList.add(nailedArr.getString(i))
            val slippedList = mutableListOf<String>()
            for (i in 0 until slippedArr.length()) slippedList.add(slippedArr.getString(i))

            val tip = parsedObj.optString("icaiExaminerTip", topic.commonExaminerPitfalls)
            val action = parsedObj.optString("nextAction", "Revise the study material and statutory provisions for ${topic.topicName}.")

            val verdict = LabVerdict(
                masteryScore = score,
                paperName = topic.paper.title,
                topicName = topic.topicName,
                whatYouNailed = if (nailedList.isNotEmpty()) nailedList else listOf("Demonstrated conceptual understanding"),
                whatSlipped = if (slippedList.isNotEmpty()) slippedList else listOf("Check exact ICAI legal wording"),
                icaiExaminerTip = if (tip.isNotBlank()) tip else topic.commonExaminerPitfalls,
                nextAction = action
            )
            Result.success(verdict)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun generateCatchLabScenario(
        apiKey: String,
        topic: SyllabusTopic
    ): Result<CatchLabScenario> {
        return generateAiTrickScenario(topic.paper, topic.topicName, apiKey)
    }

    suspend fun generateAiTrickScenario(
        paper: PaperCode,
        topicName: String,
        apiKey: String
    ): Result<CatchLabScenario> {
        val systemPrompt = "You are CA-Lab's Trick Mode scenario generator. Always output strictly valid JSON conforming to the requested schema with exactly 3 subtleErrors."
        val userPrompt = """
You are acting in [CATCH LAB OUT] mode for the ICAI CA Intermediate study companion "CA-Lab".
Paper: ${paper.title}
Topic: $topicName

Draft a short case memo (3-4 sentences, max 100 words) on this topic containing EXACTLY 3 subtle, realistic mistakes or traps (e.g. incorrect threshold limit, mixing up section numbers, wrong ITC blocked credit condition, treating an adjusting event as non-adjusting, or wrong variance formula multiplier).
Keep each subtle error explanation and correctRuleOrLimit strictly under 35 words. Keep correctIcaiProvision under 40 words.
Output strictly a JSON object with this schema:
{
  "title": "<catchy case study title>",
  "caseSnippet": "<paragraph with the 3 subtle errors numbered 1, 2, 3 in realistic audit/tax/law context>",
  "subtleErrors": [
    {
      "wrongStatement": "<what statement is incorrect>",
      "errorExplanation": "<why this is an error according to ICAI law or standard>",
      "correctRuleOrLimit": "<the correct statutory provision, section, standard, or limit>"
    }
  ],
  "correctIcaiProvision": "<concise 2-3 sentence summary of the correct governing provision>"
}
""".trimIndent()

        val rawResult = generateJsonCompletion(apiKey, systemPrompt, userPrompt)
        if (rawResult.isFailure) return Result.failure(rawResult.exceptionOrNull()!!)

        return try {
            val text = cleanJsonString(rawResult.getOrNull().orEmpty())
            val parsedObj = JSONObject(text)
            val title = parsedObj.optString("title", "AI Generated Trick Case: $topicName")
            val caseSnippet = parsedObj.optString("caseSnippet", "Review the following case notes and find the subtle traps.")
            val correctIcaiProvision = parsedObj.optString("correctIcaiProvision", "Always verify statutory provisions in ICAI Study Material.")
            val subtleErrorsArr = parsedObj.optJSONArray("subtleErrors") ?: JSONArray()
            val subtleErrorsList = mutableListOf<SubtleError>()

            for (i in 0 until subtleErrorsArr.length()) {
                val errObj = subtleErrorsArr.getJSONObject(i)
                subtleErrorsList.add(
                    SubtleError(
                        wrongStatement = errObj.optString("wrongStatement", "Subtle Trap #${i + 1}"),
                        errorExplanation = errObj.optString("errorExplanation", "Incorrect statutory treatment."),
                        correctRuleOrLimit = errObj.optString("correctRuleOrLimit", "Check standard rule.")
                    )
                )
            }

            val scenario = CatchLabScenario(
                id = "groq_scenario_" + System.currentTimeMillis(),
                paper = paper,
                topic = topicName,
                title = title,
                caseSnippet = caseSnippet,
                errorCount = subtleErrorsList.size.coerceAtLeast(1),
                subtleErrors = subtleErrorsList,
                correctIcaiProvision = correctIcaiProvision
            )
            Result.success(scenario)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun generateConceptDifference(
        apiKey: String,
        paper: PaperCode,
        conceptA: String,
        conceptB: String
    ): Result<ConceptDifference> {
        return generateAiConceptDifference("$conceptA vs $conceptB", paper, apiKey)
    }

    suspend fun generateAiConceptDifference(
        query: String,
        paper: PaperCode?,
        apiKey: String
    ): Result<ConceptDifference> {
        val systemPrompt = "You are CA-Lab's Spot the Difference comparison generator. Always return strictly valid JSON matching the requested schema."
        val userPrompt = """
You are acting in [SPOT THE DIFFERENCE] mode for the ICAI CA Intermediate study companion "CA-Lab".
The student wants a side-by-side comparison for: "$query"

Determine the two contrasting concepts (Concept A and Concept B) in CA Intermediate (New Scheme) syllabus.
Generate a structured side-by-side comparison table with 4 to 5 distinct bases of difference, plus an unforgettable ICAI Exam Memory Hook.
Keep conceptAValue and conceptBValue concise (max 25 words each). Keep memoryHook punchy (1 sentence).
Output strictly a JSON object with this schema:
{
  "title": "<e.g. $query>",
  "conceptA": "<Concept A name>",
  "conceptB": "<Concept B name>",
  "comparisonPoints": [
    {
      "basisOfDifference": "<e.g. Nature / Governing Section / Statutory Limit / Disclosure>",
      "conceptAValue": "<treatment / rule under Concept A>",
      "conceptBValue": "<treatment / rule under Concept B>"
    }
  ],
  "memoryHook": "<punchy 1-sentence exam memory hook>"
}
""".trimIndent()

        val rawResult = generateJsonCompletion(apiKey, systemPrompt, userPrompt)
        if (rawResult.isFailure) return Result.failure(rawResult.exceptionOrNull()!!)

        return try {
            val text = cleanJsonString(rawResult.getOrNull().orEmpty())
            val parsedObj = JSONObject(text)
            val title = parsedObj.optString("title", query)
            val conceptA = parsedObj.optString("conceptA", "Concept A")
            val conceptB = parsedObj.optString("conceptB", "Concept B")
            val memoryHook = parsedObj.optString("memoryHook", "Contrast the statutory bases carefully.")
            val rowsArr = parsedObj.optJSONArray("comparisonPoints") ?: JSONArray()
            val rowsList = mutableListOf<ComparisonRow>()

            for (i in 0 until rowsArr.length()) {
                val r = rowsArr.getJSONObject(i)
                rowsList.add(
                    ComparisonRow(
                        basisOfDifference = r.optString("basisOfDifference", "Point ${i + 1}"),
                        conceptAValue = r.optString("conceptAValue", "-"),
                        conceptBValue = r.optString("conceptBValue", "-")
                    )
                )
            }

            val difference = ConceptDifference(
                id = "groq_diff_" + System.currentTimeMillis(),
                paper = paper ?: PaperCode.PAPER_1,
                title = title,
                conceptA = conceptA,
                conceptB = conceptB,
                comparisonPoints = if (rowsList.isNotEmpty()) rowsList else listOf(
                    ComparisonRow("Primary Distinction", "Check ICAI Study Material", "Check ICAI Study Material")
                ),
                memoryHook = memoryHook
            )
            Result.success(difference)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun generateMcqQuestions(
        apiKey: String,
        topic: SyllabusTopic,
        count: Int = 3
    ): Result<List<McqQuestion>> {
        return generateAiMcqs(topic.paper, topic.topicName, apiKey, count)
    }

    suspend fun generateAiMcqs(
        paper: PaperCode,
        topicName: String,
        apiKey: String,
        count: Int = 3
    ): Result<List<McqQuestion>> {
        val systemPrompt = "You are an expert ICAI item writer. Always output strictly valid JSON array of MCQs conforming to the requested schema."
        val userPrompt = """
You are acting as an ICAI CA Intermediate Exam Item Writer for Paper ${paper.code}: ${paper.title}.
Generate $count realistic ICAI 2-mark MCQs on the topic: "$topicName".
Include 1 Integrated Case Scenario MCQ and ${count - 1} standalone MCQs with strict options A, B, C, D.
Output strictly a JSON array of objects with this schema:
[
  {
    "id": "groq_mcq_${System.currentTimeMillis()}_1",
    "topicId": "custom_topic",
    "topicName": "$topicName",
    "caseScenarioText": "<Optional case paragraph for scenario MCQ, or null for standalone>",
    "questionStem": "<Clear stem asking for 2-mark calculation or legal provision application>",
    "options": [
      "A. Option 1",
      "B. Option 2",
      "C. Option 3",
      "D. Option 4"
    ],
    "correctOptionIndex": <0, 1, 2, or 3>,
    "explanation": "<Step-by-step working note or legal explanation for the correct option>",
    "icaiProvisionRef": "<Exact Section, Accounting Standard, or SA reference>"
  }
]
""".trimIndent()

        val rawResult = generateJsonCompletion(apiKey, systemPrompt, userPrompt)
        if (rawResult.isFailure) return Result.failure(rawResult.exceptionOrNull()!!)

        return try {
            val text = cleanJsonString(rawResult.getOrNull().orEmpty())
            val mcqsList = mutableListOf<McqQuestion>()
            val jsonArr = if (text.startsWith("[")) JSONArray(text) else JSONArray().put(JSONObject(text))

            for (i in 0 until jsonArr.length()) {
                val obj = jsonArr.getJSONObject(i)
                val optionsArr = obj.optJSONArray("options") ?: JSONArray()
                val opts = mutableListOf<String>()
                for (j in 0 until optionsArr.length()) {
                    opts.add(optionsArr.getString(j))
                }

                mcqsList.add(
                    McqQuestion(
                        id = obj.optString("id", "groq_mcq_${System.currentTimeMillis()}_$i"),
                        paper = paper,
                        topicId = obj.optString("topicId", "custom_topic"),
                        topicName = obj.optString("topicName", topicName),
                        priorityTier = PriorityTier.TIER_1,
                        caseScenarioText = if (obj.has("caseScenarioText") && !obj.isNull("caseScenarioText")) obj.getString("caseScenarioText") else null,
                        questionStem = obj.optString("questionStem", "Which of the following is correct?"),
                        options = if (opts.size >= 4) opts else listOf("A. Option A", "B. Option B", "C. Option C", "D. Option D"),
                        correctOptionIndex = obj.optInt("correctOptionIndex", 0).coerceIn(0, 3),
                        explanation = obj.optString("explanation", "Refer to standard ICAI study material for detailed working."),
                        icaiProvisionRef = obj.optString("icaiProvisionRef", "ICAI New Scheme Provision")
                    )
                )
            }
            Result.success(mcqsList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun cleanJsonString(raw: String): String {
        var text = raw.trim()
        if (text.startsWith("```json", ignoreCase = true)) {
            text = text.substring(7).trim()
        } else if (text.startsWith("```")) {
            text = text.substring(3).trim()
        }
        if (text.endsWith("```")) {
            text = text.substring(0, text.length - 3).trim()
        }
        val firstObj = text.indexOf('{')
        val firstArr = text.indexOf('[')
        val start = when {
            firstObj != -1 && firstArr != -1 -> minOf(firstObj, firstArr)
            firstObj != -1 -> firstObj
            firstArr != -1 -> firstArr
            else -> -1
        }
        val lastObj = text.lastIndexOf('}')
        val lastArr = text.lastIndexOf(']')
        val end = maxOf(lastObj, lastArr)
        if (start != -1 && end != -1 && end >= start) {
            text = text.substring(start, end + 1).trim()
        }
        return text
    }
}
