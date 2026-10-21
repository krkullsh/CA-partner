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
import java.util.concurrent.TimeUnit

object GeminiApiClient {
    private const val TAG = "GeminiApiClient"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.8-flash:generateContent"

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectionPool(okhttp3.ConnectionPool(2, 15, TimeUnit.SECONDS))
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
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
        } catch (e: Exception) {
            Log.e(TAG, "Exception calling Gemini API", e)
            Result.failure(e)
        }
    }
}
