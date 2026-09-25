package com.example.data.repository

import android.graphics.Bitmap
import com.example.data.local.AppDatabase
import com.example.data.model.IngredientItem
import com.example.data.model.MealPlanItem
import com.example.data.model.MealType
import com.example.data.model.Recipe
import com.example.data.model.RecipeIngredient
import com.example.data.model.SavedRecipe
import com.example.data.model.ShoppingItem
import com.example.data.model.StorageLocation
import com.example.data.remote.GeminiService
import com.example.data.remote.RecipeCatalog
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject

class KitchenRepository(private val database: AppDatabase) {
    private val ingredientDao = database.ingredientDao()
    private val recipeDao = database.recipeDao()
    private val shoppingDao = database.shoppingDao()
    private val mealPlanDao = database.mealPlanDao()

    val allIngredients: Flow<List<IngredientItem>> = ingredientDao.getAllIngredients()
    val fridgeIngredients: Flow<List<IngredientItem>> = ingredientDao.getIngredientsByLocation(StorageLocation.GELADEIRA)
    val pantryIngredients: Flow<List<IngredientItem>> = ingredientDao.getIngredientsByLocation(StorageLocation.DISPENSA)
    val savedRecipes: Flow<List<SavedRecipe>> = recipeDao.getAllSavedRecipes()
    val shoppingItems: Flow<List<ShoppingItem>> = shoppingDao.getAllShoppingItems()
    val mealPlans: Flow<List<MealPlanItem>> = mealPlanDao.getAllMealPlans()

    suspend fun populateInitialDataIfEmpty() {
        val initialItems = listOf(
            IngredientItem(name = "Arroz cozido (sobra)", location = StorageLocation.GELADEIRA, quantity = "2", unit = "xícaras", isUrgent = true),
            IngredientItem(name = "Ovos", location = StorageLocation.GELADEIRA, quantity = "4", unit = "un", isUrgent = false),
            IngredientItem(name = "Tomate maduro", location = StorageLocation.GELADEIRA, quantity = "2", unit = "un", isUrgent = true),
            IngredientItem(name = "Cenoura", location = StorageLocation.GELADEIRA, quantity = "1", unit = "un", isUrgent = false),
            IngredientItem(name = "Queijo mussarela", location = StorageLocation.GELADEIRA, quantity = "100", unit = "g", isUrgent = true),
            IngredientItem(name = "Cebola", location = StorageLocation.DISPENSA, quantity = "3", unit = "un", isUrgent = false),
            IngredientItem(name = "Alho", location = StorageLocation.DISPENSA, quantity = "1", unit = "cabeça", isUrgent = false),
            IngredientItem(name = "Farinha de trigo", location = StorageLocation.DISPENSA, quantity = "500", unit = "g", isUrgent = false),
            IngredientItem(name = "Macarrão", location = StorageLocation.DISPENSA, quantity = "1", unit = "pacote", isUrgent = false)
        )
        ingredientDao.insertIngredients(initialItems)

        // Initial sample meal plan item
        mealPlanDao.insertMealPlan(
            MealPlanItem(
                recipeTitle = "Arroz de Forno Cremoso da Geladeira",
                dayOfWeek = "Hoje",
                mealType = MealType.JANTAR,
                notes = "Aproveitar o arroz de ontem antes de estragar!"
            )
        )
    }

    suspend fun addIngredient(name: String, location: StorageLocation, quantity: String = "1", unit: String = "un", isUrgent: Boolean = false) {
        val trimmed = name.trim()
        if (trimmed.isNotBlank()) {
            ingredientDao.insertIngredient(
                IngredientItem(name = trimmed, location = location, quantity = quantity, unit = unit, isUrgent = isUrgent)
            )
        }
    }

    suspend fun deleteIngredient(id: Long) {
        ingredientDao.deleteById(id)
    }

    suspend fun toggleUrgency(item: IngredientItem) {
        ingredientDao.updateIngredient(item.copy(isUrgent = !item.isUrgent))
    }

    // Recipe Matching & Generation
    suspend fun findMatchingRecipes(
        availableItems: List<String>,
        maxTimeMinutes: Int? = null,
        difficulty: String? = null,
        onlyComplete: Boolean = false
    ): List<Recipe> {
        val localMatches = RecipeCatalog.matchRecipes(
            availableIngredientNames = availableItems,
            maxTimeMinutes = maxTimeMinutes,
            difficultyFilter = difficulty,
            onlyComplete = onlyComplete
        )
        return localMatches
    }

    suspend fun generateAiRecipe(
        fridgeItems: List<String>,
        pantryItems: List<String>,
        maxTime: Int? = null,
        difficulty: String? = null
    ): Recipe? {
        return GeminiService.generateZeroWasteRecipe(
            fridgeIngredients = fridgeItems,
            pantryIngredients = pantryItems,
            prepTimeLimit = maxTime,
            difficulty = difficulty
        )
    }

    suspend fun scanIngredientsFromPhoto(bitmap: Bitmap): List<String> {
        return GeminiService.analyzeFridgePhoto(bitmap)
    }

    // Save/Favorite Recipes
    suspend fun toggleFavoriteRecipe(recipe: Recipe): Boolean {
        val isAlreadySaved = recipeDao.isRecipeSaved(recipe.title)
        if (isAlreadySaved) {
            recipeDao.deleteRecipeByTitle(recipe.title)
            return false
        } else {
            val ingsJson = JSONArray().apply {
                recipe.ingredients.forEach { ing ->
                    put(JSONObject().apply {
                        put("name", ing.name)
                        put("amount", ing.amount)
                        put("unit", ing.unit)
                        put("isAvailable", ing.isAvailable)
                        put("isPantryItem", ing.isPantryItem)
                    })
                }
            }.toString()

            val instJson = JSONArray(recipe.instructions).toString()
            val missJson = JSONArray(recipe.missingItems).toString()

            recipeDao.insertRecipe(
                SavedRecipe(
                    title = recipe.title,
                    description = recipe.description,
                    prepTimeMinutes = recipe.prepTimeMinutes,
                    difficulty = recipe.difficulty,
                    baseServings = recipe.baseServings,
                    wasteScore = recipe.wasteScore,
                    costPerServing = recipe.costPerServing,
                    savingsEstimate = recipe.savingsEstimate,
                    ingredientsJson = ingsJson,
                    instructionsJson = instJson,
                    missingItemsJson = missJson,
                    isFavorite = true,
                    tips = recipe.tips
                )
            )
            return true
        }
    }

    suspend fun isRecipeSaved(title: String): Boolean {
        return recipeDao.isRecipeSaved(title)
    }

    suspend fun deleteSavedRecipe(id: Long) {
        recipeDao.deleteRecipeById(id)
    }

    // Shopping List
    suspend fun addMissingItemsToShoppingList(items: List<String>, recipeTitle: String) {
        val shoppingItems = items.map { name ->
            ShoppingItem(
                name = name,
                quantity = "1 un",
                category = "Ingredientes Faltantes",
                isChecked = false,
                recipeOrigin = recipeTitle
            )
        }
        shoppingDao.insertShoppingItems(shoppingItems)
    }

    suspend fun addShoppingItem(name: String, quantity: String = "1 un", category: String = "Geral") {
        if (name.isNotBlank()) {
            shoppingDao.insertShoppingItem(
                ShoppingItem(name = name.trim(), quantity = quantity, category = category)
            )
        }
    }

    suspend fun toggleShoppingItem(item: ShoppingItem) {
        shoppingDao.updateShoppingItem(item.copy(isChecked = !item.isChecked))
    }

    suspend fun deleteShoppingItem(item: ShoppingItem) {
        shoppingDao.deleteShoppingItem(item)
    }

    suspend fun clearCompletedShoppingItems() {
        shoppingDao.clearCompleted()
    }

    // Meal Plan
    suspend fun addMealPlan(recipeTitle: String, day: String, mealType: MealType, notes: String = "") {
        mealPlanDao.insertMealPlan(
            MealPlanItem(recipeTitle = recipeTitle, dayOfWeek = day, mealType = mealType, notes = notes)
        )
    }

    suspend fun deleteMealPlan(id: Long) {
        mealPlanDao.deleteMealPlanById(id)
    }

    // Portion Scaling
    fun scaleRecipe(recipe: Recipe, targetServings: Int): Recipe {
        if (targetServings == recipe.baseServings || recipe.baseServings <= 0) return recipe
        val factor = targetServings.toDouble() / recipe.baseServings
        val scaledIngredients = recipe.ingredients.map { ing ->
            ing.copy(amount = Math.round(ing.amount * factor * 10.0) / 10.0)
        }
        val scaledSavings = Math.round(recipe.savingsEstimate * factor * 10.0) / 10.0
        return recipe.copy(
            ingredients = scaledIngredients,
            savingsEstimate = scaledSavings
        )
    }
}
