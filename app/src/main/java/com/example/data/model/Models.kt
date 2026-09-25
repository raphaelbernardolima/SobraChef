package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class StorageLocation(val label: String) {
    GELADEIRA("Geladeira"),
    DISPENSA("Dispensa")
}

enum class MealType(val displayName: String) {
    CAFE("Café da Manhã"),
    ALMOCO("Almoço"),
    JANTAR("Jantar"),
    LANCHE("Lanche")
}

@Entity(tableName = "ingredients")
data class IngredientItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val location: StorageLocation = StorageLocation.GELADEIRA,
    val quantity: String = "1",
    val unit: String = "un",
    val isUrgent: Boolean = false,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_recipes")
data class SavedRecipe(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val prepTimeMinutes: Int,
    val difficulty: String,
    val baseServings: Int = 2,
    val wasteScore: Int = 90,
    val costPerServing: Double = 4.50,
    val savingsEstimate: Double = 22.00,
    val ingredientsJson: String,
    val instructionsJson: String,
    val missingItemsJson: String = "[]",
    val isFavorite: Boolean = true,
    val tips: String = "",
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "shopping_items")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val quantity: String = "1 un",
    val category: String = "Geral",
    val isChecked: Boolean = false,
    val recipeOrigin: String = ""
)

@Entity(tableName = "meal_plans")
data class MealPlanItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recipeTitle: String,
    val dayOfWeek: String,
    val mealType: MealType,
    val recipeId: Long = 0,
    val notes: String = ""
)

data class RecipeIngredient(
    val name: String,
    val amount: Double,
    val unit: String,
    val isAvailable: Boolean = true,
    val isPantryItem: Boolean = false
)

data class Recipe(
    val id: Long = 0,
    val title: String,
    val description: String,
    val prepTimeMinutes: Int,
    val difficulty: String,
    val baseServings: Int = 2,
    val wasteScore: Int = 90,
    val costPerServing: Double = 4.50,
    val savingsEstimate: Double = 25.00,
    val ingredients: List<RecipeIngredient>,
    val instructions: List<String>,
    val missingItems: List<String> = emptyList(),
    val tips: String = "Dica Zero Desperdício: aproveite talos e cascas bem higienizados.",
    val isFavorite: Boolean = false
)
