package com.example.data.ai

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import com.example.data.model.ComparisonRow
import com.example.data.model.ConceptDifference
import com.example.data.model.CatchLabScenario
import com.example.data.model.LabVerdict
import com.example.data.model.PaperCode
import com.example.data.model.SubtleError
import com.example.data.model.SyllabusTopic
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

object GeminiApiClient {
    private const val TAG = "GeminiApiClient"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.8-flash:generateContent"

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectionPool(okhttp3.ConnectionPool(2, 30, TimeUnit.SECONDS))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .callTimeout(90, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    const val MASTER_SYSTEM_PROMPT = """
You are "CA-Lab", an AI Study Companion modeled after AeraWise's "Lab", built specifically for ICAI CA Intermediate (New Scheme) students in India.
Your core philosophy is: "Knowing isn't understanding—you understand a provision or standard best when you can teach it."

Syllabus Scope (ICAI New Scheme - 6 Papers):
- Paper 1: Advanced Accounting (Accounting Standards AS 1 to AS 29)
- Paper 2: Corporate and Other Laws (Companies Act 2013, LLP Act, General Clauses Act, Interpretation of Statutes, FEMA)
- Paper 3: Taxation (Section A: Income Tax Law; Section B: GST)
- Paper 4: Cost and Management Accounting (CMA)
- Paper 5: Auditing and Ethics (Standards on Auditing - SAs, CARO 2020, Ethics)
- Paper 6: Financial Management (FM) & Strategic Management (SM)

Operating Modes:
1. [TEACH LAB] Mode (Feynman Technique - Default)
- Act as a curious, slightly confused junior article assistant or fellow CA student.
- Ask the user to pick a Paper and Topic and explain it to you in their own words.
- Do NOT lecture them. Listen to their explanation and ask targeted follow-up doubts probing for what they left out:
  - In Law/Tax: Ask about threshold limits, effective dates, exceptions, "Provided that..." (provisos), or whether an "AND" vs "OR" condition applies.
  - In Accounts/Costing/FM: Ask about treatment of abnormal losses, disclosure requirements, or why a specific formula/adjustment is used.
  - In Audit: Ask what specific audit procedure (inspection, inquiry, external confirmation) or SA requirement applies if management refuses.
- Only give "Lab's Verdict" when the user says "Give Verdict" or finishes teaching.

2. [CATCH LAB OUT] Mode (Trick Mode 🎭)
- Present a short paragraph or mini-case on the selected CA Inter topic containing 2 to 3 subtle, realistic mistakes (e.g., citing a 20% limit instead of 10%, mixing up Section 185 and 186, wrong ITC blocked credit rule under Sec 17(5), treating adjusting event under AS 4 as non-adjusting).
- Challenge the user to spot every error and cite the correct provision.

3. [ASK LAB] Mode (Depth-Controlled Explanations)
When the user asks you to explain a topic, check their preferred Depth:
- Depth 1 (Simple): Explain like they are a first-day commerce student using real-world Indian business analogies (Kirana store, Chai stall, Dosa corner).
- Depth 2 (Standard): Structured notes, flowcharts, and formula breakdowns.
- Depth 3 (ICAI Exam Level): Write exact answer format rewarded by ICAI examiners (1. Provision of Law/Standard, 2. Facts & Analysis of Case, 3. Conclusion; highlight mandatory ICAI keywords in bold; show structured Working Notes first for practical papers).

4. [SPOT THE DIFFERENCE] Mode
- Compare two easily confused CA Inter concepts side-by-side in a table with Memory Hook.

5. [LAB'S VERDICT] (Grading & Gap Analysis)
When requested after a teaching session or practice question, output a structured report:
- Mastery Score: X/100
- What You Nailed: Strong conceptual points.
- What Slipped (Gaps): Missed provisos, thresholds, working note steps, or ICAI keywords.
- ICAI Examiner Tip: Common mistake students make in this topic according to ICAI Examiner Comments.
- Next Action: Suggest 1 specific sub-concept to revise next.
"""

    fun getApiKey(customKey: String?): String {
        if (!customKey.isNullOrBlank()) return customKey
        return try {
            val field = BuildConfig::class.java.getField("GEMINI_API_KEY")
            val key = field.get(null) as? String
            if (key != null && key != "MY_GEMINI_API_KEY" && key.isNotBlank()) key else ""
        } catch (_: Exception) {
            ""
        }
    }

    suspend fun generateContent(
        prompt: String,
        apiKey: String,
        conversationHistory: List<Pair<String, String>> = emptyList()
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            if (apiKey.isBlank()) {
                return@withContext Result.failure(IllegalStateException("No Gemini API key available"))
            }

            val requestBodyJson = JSONObject()

            // System instruction
            val systemInstruction = JSONObject().apply {
                put("parts", JSONArray().put(JSONObject().put("text", MASTER_SYSTEM_PROMPT)))
            }
            requestBodyJson.put("system_instruction", systemInstruction)

            // Contents array
            val contentsArray = JSONArray()

            // Prior turns
            conversationHistory.takeLast(8).forEach { (role, content) ->
                val turn = JSONObject()
                turn.put("role", if (role.equals("user", true)) "user" else "model")
                turn.put("parts", JSONArray().put(JSONObject().put("text", content)))
                contentsArray.put(turn)
            }

            // Current prompt
            val currentTurn = JSONObject()
            currentTurn.put("role", "user")
            currentTurn.put("parts", JSONArray().put(JSONObject().put("text", prompt)))
            contentsArray.put(currentTurn)

            requestBodyJson.put("contents", contentsArray)

            // Generation config
            val genConfig = JSONObject().apply {
                put("temperature", 0.7)
                put("maxOutputTokens", 2048)
            }
            requestBodyJson.put("generationConfig", genConfig)

            val urlWithKey = "$BASE_URL?key=$apiKey"
            val body = requestBodyJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url(urlWithKey)
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errorBody = response.body?.string().orEmpty()
                    Log.e(TAG, "Gemini API HTTP ${response.code}: $errorBody")
                    return@withContext Result.failure(Exception("Gemini API error (${response.code})"))
                }

                val responseStr = response.body?.string() ?: ""
                val json = JSONObject(responseStr)
                val candidates = json.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val contentObj = candidate.optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val text = parts.getJSONObject(0).optString("text")
                        return@withContext Result.success(text)
                    }
                }
                Result.failure(Exception("Empty response from Gemini"))
            }
        } catch (e: SocketTimeoutException) {
            Log.w(TAG, "Gemini API content generation timed out: ${e.message}")
            Result.failure(Exception("Request timed out. Please try again."))
        } catch (e: Exception) {
            Log.e(TAG, "Exception calling Gemini API", e)
            Result.failure(e)
        }
    }

    suspend fun generateJsonVerdict(
        topic: SyllabusTopic,
        conversationText: String,
        apiKey: String
    ): Result<LabVerdict> = withContext(Dispatchers.IO) {
        try {
            if (apiKey.isBlank()) {
                return@withContext Result.failure(IllegalStateException("No Gemini API key available"))
            }

            val verdictPrompt = """
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

            val requestBodyJson = JSONObject()
            val systemInstruction = JSONObject().apply {
                put("parts", JSONArray().put(JSONObject().put("text", "You are CA-Lab's verdict evaluator. Always return strictly valid JSON matching the requested schema.")))
            }
            requestBodyJson.put("system_instruction", systemInstruction)

            val contentsArray = JSONArray()
            val currentTurn = JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().put(JSONObject().put("text", verdictPrompt)))
            }
            contentsArray.put(currentTurn)
            requestBodyJson.put("contents", contentsArray)

            val genConfig = JSONObject().apply {
                put("temperature", 0.2)
                put("maxOutputTokens", 750)
                put("responseMimeType", "application/json")
            }
            requestBodyJson.put("generationConfig", genConfig)

            val urlWithKey = "$BASE_URL?key=$apiKey"
            val body = requestBodyJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url(urlWithKey)
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errorBody = response.body?.string().orEmpty()
                    Log.e(TAG, "Gemini API HTTP ${response.code}: $errorBody")
                    return@withContext Result.failure(Exception("Gemini API error (${response.code})"))
                }

                val responseStr = response.body?.string() ?: ""
                val json = JSONObject(responseStr)
                val candidates = json.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val contentObj = candidate.optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val rawText = parts.getJSONObject(0).optString("text")
                        val text = cleanJsonString(rawText)

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
                        return@withContext Result.success(verdict)
                    }
                }
                Result.failure(Exception("Empty or invalid verdict response from Gemini"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception parsing Gemini verdict", e)
            Result.failure(e)
        }
    }

    suspend fun generateAiTrickScenario(
        paper: PaperCode,
        topicName: String,
        apiKey: String
    ): Result<CatchLabScenario> = withContext(Dispatchers.IO) {
        try {
            if (apiKey.isBlank()) {
                return@withContext Result.failure(IllegalStateException("No Gemini API key available"))
            }

            val prompt = """
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

            val requestBodyJson = JSONObject()
            val systemInstruction = JSONObject().apply {
                put("parts", JSONArray().put(JSONObject().put("text", "You are CA-Lab's Trick Mode scenario generator. Always output strictly valid JSON conforming to the requested schema with exactly 3 subtleErrors.")))
            }
            requestBodyJson.put("system_instruction", systemInstruction)

            val contentsArray = JSONArray()
            val currentTurn = JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().put(JSONObject().put("text", prompt)))
            }
            contentsArray.put(currentTurn)
            requestBodyJson.put("contents", contentsArray)

            val genConfig = JSONObject().apply {
                put("temperature", 0.4)
                put("maxOutputTokens", 750)
                put("responseMimeType", "application/json")
            }
            requestBodyJson.put("generationConfig", genConfig)

            val urlWithKey = "$BASE_URL?key=$apiKey"
            val body = requestBodyJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder().url(urlWithKey).post(body).build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errorBody = response.body?.string().orEmpty()
                    Log.e(TAG, "Gemini API HTTP ${response.code}: $errorBody")
                    return@withContext Result.failure(Exception("Gemini API error (${response.code})"))
                }

                val responseStr = response.body?.string() ?: ""
                val json = JSONObject(responseStr)
                val candidates = json.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val contentObj = candidate.optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val rawText = parts.getJSONObject(0).optString("text")
                        val text = cleanJsonString(rawText)

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
                            id = "ai_scenario_" + System.currentTimeMillis(),
                            paper = paper,
                            topic = topicName,
                            title = title,
                            caseSnippet = caseSnippet,
                            errorCount = subtleErrorsList.size.coerceAtLeast(1),
                            subtleErrors = subtleErrorsList,
                            correctIcaiProvision = correctIcaiProvision
                        )
                        return@withContext Result.success(scenario)
                    }
                }
                Result.failure(Exception("Empty response from Gemini"))
            }
        } catch (e: SocketTimeoutException) {
            Log.w(TAG, "Gemini API trick scenario request timed out: ${e.message}")
            Result.failure(Exception("Generation timed out. Switching to offline engine..."))
        } catch (e: Exception) {
            Log.e(TAG, "Exception creating AI trick scenario", e)
            Result.failure(e)
        }
    }

    suspend fun generateAiConceptDifference(
        query: String,
        paper: PaperCode?,
        apiKey: String
    ): Result<ConceptDifference> = withContext(Dispatchers.IO) {
        try {
            if (apiKey.isBlank()) {
                return@withContext Result.failure(IllegalStateException("No Gemini API key available"))
            }

            val prompt = """
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

            val requestBodyJson = JSONObject()
            val systemInstruction = JSONObject().apply {
                put("parts", JSONArray().put(JSONObject().put("text", "You are CA-Lab's Spot the Difference comparison generator. Always return strictly valid JSON matching the requested schema.")))
            }
            requestBodyJson.put("system_instruction", systemInstruction)

            val contentsArray = JSONArray()
            val currentTurn = JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().put(JSONObject().put("text", prompt)))
            }
            contentsArray.put(currentTurn)
            requestBodyJson.put("contents", contentsArray)

            val genConfig = JSONObject().apply {
                put("temperature", 0.3)
                put("maxOutputTokens", 750)
                put("responseMimeType", "application/json")
            }
            requestBodyJson.put("generationConfig", genConfig)

            val urlWithKey = "$BASE_URL?key=$apiKey"
            val body = requestBodyJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder().url(urlWithKey).post(body).build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errorBody = response.body?.string().orEmpty()
                    Log.e(TAG, "Gemini API HTTP ${response.code}: $errorBody")
                    return@withContext Result.failure(Exception("Gemini API error (${response.code})"))
                }

                val responseStr = response.body?.string() ?: ""
                val json = JSONObject(responseStr)
                val candidates = json.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val contentObj = candidate.optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val rawText = parts.getJSONObject(0).optString("text")
                        val text = cleanJsonString(rawText)

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
                            id = "ai_diff_" + System.currentTimeMillis(),
                            paper = paper ?: PaperCode.PAPER_1,
                            title = title,
                            conceptA = conceptA,
                            conceptB = conceptB,
                            comparisonPoints = if (rowsList.isNotEmpty()) rowsList else listOf(
                                ComparisonRow("Primary Distinction", "Check ICAI Study Material", "Check ICAI Study Material")
                            ),
                            memoryHook = memoryHook
                        )
                        return@withContext Result.success(difference)
                    }
                }
                Result.failure(Exception("Empty response from Gemini"))
            }
        } catch (e: SocketTimeoutException) {
            Log.w(TAG, "Gemini API concept difference request timed out: ${e.message}")
            Result.failure(Exception("Comparison timed out. Switching to offline engine..."))
        } catch (e: Exception) {
            Log.e(TAG, "Exception creating AI concept difference", e)
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
