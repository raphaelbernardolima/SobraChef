package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.IngredientItem
import com.example.data.model.MealPlanItem
import com.example.data.model.MealType
import com.example.data.model.Recipe
import com.example.data.model.SavedRecipe
import com.example.data.model.ShoppingItem
import com.example.data.model.StorageLocation
import com.example.data.remote.RecipeCatalog
import com.example.data.repository.KitchenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray

enum class AppScreen {
    HOME,
    RECIPES,
    RECIPE_DETAIL,
    MEAL_PLAN,
    SHOPPING,
    PANTRY
}

data class KitchenUiState(
    val currentScreen: AppScreen = AppScreen.HOME,
    val selectedRecipe: Recipe? = null,
    val selectedRecipeServings: Int = 2,
    val isRecipeFavorite: Boolean = false,
    val maxPrepTime: Int? = null, // null = qualquer
    val selectedDifficulty: String = "Todos",
    val onlyCompleteLeftovers: Boolean = false,
    val activeInventoryTab: StorageLocation = StorageLocation.GELADEIRA,
    val isScanningPhoto: Boolean = false,
    val scannedIngredients: List<String> = emptyList(),
    val isAiGenerating: Boolean = false,
    val userFeedbackMessage: String? = null
)

class KitchenViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: KitchenRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = KitchenRepository(db)
        viewModelScope.launch {
            // Check if db is empty and populate starter items
            val existing = db.ingredientDao().getAllIngredients()
            // We launch a small job to populate if empty
            launch {
                repository.populateInitialDataIfEmpty()
            }
        }
    }

    private val _uiState = MutableStateFlow(KitchenUiState())
    val uiState: StateFlow<KitchenUiState> = _uiState.asStateFlow()

    val allIngredients: StateFlow<List<IngredientItem>> = repository.allIngredients
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val fridgeIngredients: StateFlow<List<IngredientItem>> = repository.fridgeIngredients
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pantryIngredients: StateFlow<List<IngredientItem>> = repository.pantryIngredients
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedRecipes: StateFlow<List<SavedRecipe>> = repository.savedRecipes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val shoppingItems: StateFlow<List<ShoppingItem>> = repository.shoppingItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mealPlans: StateFlow<List<MealPlanItem>> = repository.mealPlans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Matched Recipes combining ingredients with user filter state
    val suggestedRecipes: StateFlow<List<Recipe>> = combine(
        allIngredients,
        _uiState
    ) { ingredients, state ->
        val names = ingredients.map { it.name }
        repository.findMatchingRecipes(
            availableItems = names,
            maxTimeMinutes = state.maxPrepTime,
            difficulty = state.selectedDifficulty,
            onlyComplete = state.onlyCompleteLeftovers
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RecipeCatalog.defaultRecipes)

    fun navigateTo(screen: AppScreen) {
        _uiState.value = _uiState.value.copy(currentScreen = screen)
    }

    fun openRecipeDetail(recipe: Recipe) {
        viewModelScope.launch {
            val isSaved = repository.isRecipeSaved(recipe.title)
            _uiState.value = _uiState.value.copy(
                selectedRecipe = recipe,
                selectedRecipeServings = recipe.baseServings,
                isRecipeFavorite = isSaved,
                currentScreen = AppScreen.RECIPE_DETAIL
            )
        }
    }

    fun setServings(newServings: Int) {
        val clamped = newServings.coerceIn(1, 12)
        val currentRecipe = _uiState.value.selectedRecipe ?: return
        val scaled = repository.scaleRecipe(currentRecipe, clamped)
        _uiState.value = _uiState.value.copy(
            selectedRecipe = scaled,
            selectedRecipeServings = clamped
        )
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            val isNowSaved = repository.toggleFavoriteRecipe(recipe)
            _uiState.value = _uiState.value.copy(
                isRecipeFavorite = isNowSaved,
                userFeedbackMessage = if (isNowSaved) "Receita salva nos favoritos!" else "Removida dos favoritos."
            )
        }
    }

    fun setPrepTimeFilter(minutes: Int?) {
        _uiState.value = _uiState.value.copy(maxPrepTime = minutes)
    }

    fun setDifficultyFilter(difficulty: String) {
        _uiState.value = _uiState.value.copy(selectedDifficulty = difficulty)
    }

    fun setOnlyComplete(onlyComplete: Boolean) {
        _uiState.value = _uiState.value.copy(onlyCompleteLeftovers = onlyComplete)
    }

    fun setInventoryTab(location: StorageLocation) {
        _uiState.value = _uiState.value.copy(activeInventoryTab = location)
    }

    fun addIngredient(name: String, location: StorageLocation, quantity: String = "1", unit: String = "un", isUrgent: Boolean = false) {
        viewModelScope.launch {
            repository.addIngredient(name, location, quantity, unit, isUrgent)
            _uiState.value = _uiState.value.copy(userFeedbackMessage = "'$name' adicionado à ${location.label}!")
        }
    }

    fun deleteIngredient(id: Long) {
        viewModelScope.launch {
            repository.deleteIngredient(id)
        }
    }

    fun toggleUrgency(item: IngredientItem) {
        viewModelScope.launch {
            repository.toggleUrgency(item)
        }
    }

    fun addMissingToShoppingList(recipe: Recipe) {
        viewModelScope.launch {
            val missing = recipe.missingItems
            if (missing.isNotEmpty()) {
                repository.addMissingItemsToShoppingList(missing, recipe.title)
                _uiState.value = _uiState.value.copy(
                    userFeedbackMessage = "${missing.size} itens faltantes adicionados à Lista de Compras!"
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    userFeedbackMessage = "Você já tem todos os ingredientes desta receita!"
                )
            }
        }
    }

    fun addShoppingItem(name: String, quantity: String = "1 un", category: String = "Geral") {
        viewModelScope.launch {
            repository.addShoppingItem(name, quantity, category)
        }
    }

    fun toggleShoppingItem(item: ShoppingItem) {
        viewModelScope.launch {
            repository.toggleShoppingItem(item)
        }
    }

    fun deleteShoppingItem(item: ShoppingItem) {
        viewModelScope.launch {
            repository.deleteShoppingItem(item)
        }
    }

    fun clearCompletedShoppingItems() {
        viewModelScope.launch {
            repository.clearCompletedShoppingItems()
        }
    }

    fun addMealPlan(recipeTitle: String, day: String, mealType: MealType, notes: String = "") {
        viewModelScope.launch {
            repository.addMealPlan(recipeTitle, day, mealType, notes)
            _uiState.value = _uiState.value.copy(
                userFeedbackMessage = "Prato agendado para $day no $mealType!"
            )
        }
    }

    fun deleteMealPlan(id: Long) {
        viewModelScope.launch {
            repository.deleteMealPlan(id)
        }
    }

    fun scanPhotoForIngredients(bitmap: Bitmap) {
        _uiState.value = _uiState.value.copy(isScanningPhoto = true)
        viewModelScope.launch {
            val detected = repository.scanIngredientsFromPhoto(bitmap)
            _uiState.value = _uiState.value.copy(
                isScanningPhoto = false,
                scannedIngredients = detected,
                userFeedbackMessage = "${detected.size} ingredientes detectados na foto!"
            )
        }
    }

    fun addScannedIngredientsToFridge(items: List<String>) {
        viewModelScope.launch {
            items.forEach { name ->
                repository.addIngredient(name, StorageLocation.GELADEIRA, "1", "un", false)
            }
            _uiState.value = _uiState.value.copy(
                scannedIngredients = emptyList(),
                userFeedbackMessage = "Ingredientes adicionados à Geladeira!"
            )
        }
    }

    fun clearScannedIngredients() {
        _uiState.value = _uiState.value.copy(scannedIngredients = emptyList())
    }

    fun generateAiRecipeWithLeftovers() {
        _uiState.value = _uiState.value.copy(isAiGenerating = true)
        viewModelScope.launch {
            val fridgeNames = fridgeIngredients.value.map { it.name }
            val pantryNames = pantryIngredients.value.map { it.name }
            val aiRecipe = repository.generateAiRecipe(
                fridgeItems = fridgeNames,
                pantryItems = pantryNames,
                maxTime = _uiState.value.maxPrepTime,
                difficulty = _uiState.value.selectedDifficulty
            )
            _uiState.value = _uiState.value.copy(isAiGenerating = false)
            if (aiRecipe != null) {
                openRecipeDetail(aiRecipe)
            } else {
                _uiState.value = _uiState.value.copy(
                    userFeedbackMessage = "Mostrando as melhores receitas do nosso catálogo inteligente para suas sobras!"
                )
                navigateTo(AppScreen.RECIPES)
            }
        }
    }

    fun clearFeedbackMessage() {
        _uiState.value = _uiState.value.copy(userFeedbackMessage = null)
    }
}
