package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.IngredientItem
import com.example.data.model.MealPlanItem
import com.example.data.model.SavedRecipe
import com.example.data.model.ShoppingItem
import com.example.data.model.StorageLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredients ORDER BY isUrgent DESC, addedAt DESC")
    fun getAllIngredients(): Flow<List<IngredientItem>>

    @Query("SELECT * FROM ingredients WHERE location = :location ORDER BY isUrgent DESC, addedAt DESC")
    fun getIngredientsByLocation(location: StorageLocation): Flow<List<IngredientItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(item: IngredientItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(items: List<IngredientItem>)

    @Update
    suspend fun updateIngredient(item: IngredientItem)

    @Delete
    suspend fun deleteIngredient(item: IngredientItem)

    @Query("DELETE FROM ingredients WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM ingredients")
    suspend fun clearAll()
}

@Dao
interface RecipeDao {
    @Query("SELECT * FROM saved_recipes ORDER BY savedAt DESC")
    fun getAllSavedRecipes(): Flow<List<SavedRecipe>>

    @Query("SELECT * FROM saved_recipes WHERE isFavorite = 1 ORDER BY savedAt DESC")
    fun getFavoriteRecipes(): Flow<List<SavedRecipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: SavedRecipe): Long

    @Query("DELETE FROM saved_recipes WHERE id = :id")
    suspend fun deleteRecipeById(id: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_recipes WHERE title = :title)")
    suspend fun isRecipeSaved(title: String): Boolean

    @Query("DELETE FROM saved_recipes WHERE title = :title")
    suspend fun deleteRecipeByTitle(title: String)
}

@Dao
interface ShoppingDao {
    @Query("SELECT * FROM shopping_items ORDER BY isChecked ASC, id DESC")
    fun getAllShoppingItems(): Flow<List<ShoppingItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(item: ShoppingItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItems(items: List<ShoppingItem>)

    @Update
    suspend fun updateShoppingItem(item: ShoppingItem)

    @Delete
    suspend fun deleteShoppingItem(item: ShoppingItem)

    @Query("DELETE FROM shopping_items WHERE isChecked = 1")
    suspend fun clearCompleted()

    @Query("DELETE FROM shopping_items")
    suspend fun clearAll()
}

@Dao
interface MealPlanDao {
    @Query("SELECT * FROM meal_plans ORDER BY id ASC")
    fun getAllMealPlans(): Flow<List<MealPlanItem>>

    @Query("SELECT * FROM meal_plans WHERE dayOfWeek = :day")
    fun getMealPlansForDay(day: String): Flow<List<MealPlanItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealPlan(item: MealPlanItem): Long

    @Delete
    suspend fun deleteMealPlan(item: MealPlanItem)

    @Query("DELETE FROM meal_plans WHERE id = :id")
    suspend fun deleteMealPlanById(id: Long)
}
