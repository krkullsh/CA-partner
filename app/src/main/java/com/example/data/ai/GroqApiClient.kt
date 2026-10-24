package com.example.data.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.io.OutputStreamWriter

object GroqApiClient {
    private const val GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions"
    // Using Llama 3 8B - lightning fast and highly capable for text evaluation
    private const val MODEL_NAME = "llama3-8b-8192"

    suspend fun generateResponse(prompt: String, apiKey: String): String = withContext(Dispatchers.IO) {
        val url = URL(GROQ_API_URL)
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Authorization", "Bearer $apiKey")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            // Build OpenAI/Groq compatible JSON payload
            val messageObj = JSONObject().apply {
                put("role", "user")
                put("content", prompt)
            }
            val messagesArray = JSONArray().put(messageObj)
            
            val payload = JSONObject().apply {
                put("model", MODEL_NAME)
                put("messages", messagesArray)
                put("temperature", 0.3) // Low temp for factual accuracy
            }

            // Send Request
            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(payload.toString())
                writer.flush()
            }

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonResponse = JSONObject(response)
                
                // Parse the Groq response format
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
}
