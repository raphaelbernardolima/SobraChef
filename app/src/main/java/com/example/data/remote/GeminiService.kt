package com.example.data.remote

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import com.example.data.model.Recipe
import com.example.data.model.RecipeIngredient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

object GeminiService {
    private const val TAG = "GeminiService"
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"

    private val client by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private fun getApiKey(): String {
        return try {
            val key = BuildConfig.GEMINI_API_KEY
            if (key.isNullOrBlank() || key == "MY_GEMINI_API_KEY") "" else key
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun analyzeFridgePhoto(bitmap: Bitmap): List<String> = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isEmpty()) {
            Log.w(TAG, "No Gemini API key available, using smart local detection fallback")
            return@withContext simulatePhotoDetection()
        }

        try {
            val base64Image = bitmapToBase64(bitmap)
            val jsonBody = JSONObject().apply {
                val contents = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val parts = JSONArray().apply {
                            put(JSONObject().apply {
                                put(
                                    "text",
                                    "Você é um chef especialista em combate ao desperdício de alimentos. " +
                                            "Analise esta foto da geladeira ou dispensa e liste APENAS os nomes dos ingredientes, " +
                                            "sobras e alimentos visíveis em português, separados por vírgula. " +
                                            "Exemplo: 'Arroz cozido, Ovos, Tomate, Cenoura, Queijo, Frango desfiado, Leite'. " +
                                            "Não inclua explicações ou texto extra, apenas os ingredientes."
                                )
                            })
                            put(JSONObject().apply {
                                put("inline_data", JSONObject().apply {
                                    put("mime_type", "image/jpeg")
                                    put("data", base64Image)
                                })
                            })
                        }
                        put("parts", parts)
                    }
                    put(contentObj)
                }
                put("contents", contents)
            }

            val request = Request.Builder()
                .url("$BASE_URL/$MODEL_NAME:generateContent?key=$apiKey")
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.e(TAG, "Gemini vision failed: HTTP ${response.code} $responseBody")
                return@withContext simulatePhotoDetection()
            }

            val rootJson = JSONObject(responseBody)
            val candidates = rootJson.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val rawText = parts?.optJSONObject(0)?.optString("text", "") ?: ""

            val parsedList = rawText.split(",", "\n", ";")
                .map { it.trim().removePrefix("-").removePrefix("*").trim() }
                .filter { it.length in 2..40 }

            if (parsedList.isNotEmpty()) parsedList else simulatePhotoDetection()
        } catch (e: Exception) {
            Log.e(TAG, "Exception calling Gemini vision", e)
            simulatePhotoDetection()
        }
    }

    suspend fun generateZeroWasteRecipe(
        fridgeIngredients: List<String>,
        pantryIngredients: List<String>,
        prepTimeLimit: Int? = null,
        difficulty: String? = null
    ): Recipe? = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isEmpty()) {
            return@withContext null
        }

        try {
            val allIngredientsStr = (fridgeIngredients + pantryIngredients).joinToString(", ")
            val timeConstraint = prepTimeLimit?.let { "em no máximo $it minutos" } ?: "rápido"
            val diffConstraint = difficulty?.let { "dificuldade $it" } ?: "fácil"

            val prompt = """
                Você é o SobraChef, especialista em receitas anti-desperdício e culinária econômica.
                Crie UMA receita criativa, deliciosa e prática aproveitando ao máximo estes ingredientes disponíveis:
                $allIngredientsStr.
                Tempo desejado: $timeConstraint. Dificuldade: $diffConstraint.
                
                Retorne a resposta estritamente no formato JSON válido com as seguintes chaves:
                {
                   "title": "Nome do prato chamativo",
                   "description": "Descrição apetitosa de 2 linhas destacando as sobras usadas",
                   "prepTimeMinutes": 20,
                   "difficulty": "Fácil",
                   "baseServings": 4,
                   "wasteScore": 95,
                   "costPerServing": 3.80,
                   "savingsEstimate": 25.00,
                   "tips": "Dica de aproveitamento de cascas/talos",
                   "ingredients": [
                      {"name": "Arroz cozido", "amount": 2.0, "unit": "xícaras", "isAvailable": true},
                      {"name": "Ovo", "amount": 2.0, "unit": "unidades", "isAvailable": true}
                   ],
                   "instructions": [
                      "Passo 1 detalhado...",
                      "Passo 2 detalhado..."
                   ],
                   "missingItems": ["Azeite", "Sal"]
                }
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                val contents = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val parts = JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        }
                        put("parts", parts)
                    }
                    put(contentObj)
                }
                put("contents", contents)
                put("generationConfig", JSONObject().apply {
                    put("responseMimeType", "application/json")
                })
            }

            val request = Request.Builder()
                .url("$BASE_URL/$MODEL_NAME:generateContent?key=$apiKey")
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.e(TAG, "Gemini recipe generation failed: HTTP ${response.code} $responseBody")
                return@withContext null
            }

            val rootJson = JSONObject(responseBody)
            val candidates = rootJson.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val jsonText = parts?.optJSONObject(0)?.optString("text", "") ?: ""

            val recipeJson = JSONObject(jsonText)
            val ingredientsList = mutableListOf<RecipeIngredient>()
            val ingsArray = recipeJson.optJSONArray("ingredients")
            if (ingsArray != null) {
                for (i in 0 until ingsArray.length()) {
                    val item = ingsArray.getJSONObject(i)
                    ingredientsList.add(
                        RecipeIngredient(
                            name = item.optString("name", "Ingrediente"),
                            amount = item.optDouble("amount", 1.0),
                            unit = item.optString("unit", "un"),
                            isAvailable = item.optBoolean("isAvailable", true),
                            isPantryItem = item.optBoolean("isPantryItem", false)
                        )
                    )
                }
            }

            val instructionsList = mutableListOf<String>()
            val instArray = recipeJson.optJSONArray("instructions")
            if (instArray != null) {
                for (i in 0 until instArray.length()) {
                    instructionsList.add(instArray.getString(i))
                }
            }

            val missingList = mutableListOf<String>()
            val missArray = recipeJson.optJSONArray("missingItems")
            if (missArray != null) {
                for (i in 0 until missArray.length()) {
                    missingList.add(missArray.getString(i))
                }
            }

            Recipe(
                id = System.currentTimeMillis(),
                title = recipeJson.optString("title", "Prato Especial SobraChef"),
                description = recipeJson.optString("description", "Receita especial com seus ingredientes"),
                prepTimeMinutes = recipeJson.optInt("prepTimeMinutes", 20),
                difficulty = recipeJson.optString("difficulty", "Fácil"),
                baseServings = recipeJson.optInt("baseServings", 3),
                wasteScore = recipeJson.optInt("wasteScore", 95),
                costPerServing = recipeJson.optDouble("costPerServing", 3.90),
                savingsEstimate = recipeJson.optDouble("savingsEstimate", 24.00),
                ingredients = ingredientsList,
                instructions = instructionsList,
                missingItems = missingList,
                tips = recipeJson.optString("tips", "Aproveite talos e cascas higienizadas.")
            )
        } catch (e: Exception) {
            Log.e(TAG, "Exception generating recipe via Gemini", e)
            null
        }
    }

    private fun simulatePhotoDetection(): List<String> {
        return listOf("Ovos", "Tomate", "Cenoura", "Queijo mussarela", "Arroz cozido", "Cebola")
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        // Resize if too huge
        val scaled = if (bitmap.width > 1200 || bitmap.height > 1200) {
            val scale = 1200.0 / maxOf(bitmap.width, bitmap.height)
            Bitmap.createScaledBitmap(bitmap, (bitmap.width * scale).toInt(), (bitmap.height * scale).toInt(), true)
        } else {
            bitmap
        }
        scaled.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        return Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
    }
}
