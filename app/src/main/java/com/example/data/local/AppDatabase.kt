package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.IngredientItem
import com.example.data.model.MealPlanItem
import com.example.data.model.SavedRecipe
import com.example.data.model.ShoppingItem

@Database(
    entities = [
        IngredientItem::class,
        SavedRecipe::class,
        ShoppingItem::class,
        MealPlanItem::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ingredientDao(): IngredientDao
    abstract fun recipeDao(): RecipeDao
    abstract fun shoppingDao(): ShoppingDao
    abstract fun mealPlanDao(): MealPlanDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sobrachef_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
