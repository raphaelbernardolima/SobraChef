package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.SobraChefBottomNav
import com.example.ui.components.SobraChefTopAppBar
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MealCalendarScreen
import com.example.ui.screens.PhotoScanDialog
import com.example.ui.screens.RecipeDetailScreen
import com.example.ui.screens.RecipeListScreen
import com.example.ui.screens.ShoppingListScreen
import com.example.ui.theme.SobraChefTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.KitchenViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SobraChefTheme {
                SobraChefApp()
            }
        }
    }
}

@Composable
fun SobraChefApp(kitchenViewModel: KitchenViewModel = viewModel()) {
    val uiState by kitchenViewModel.uiState.collectAsStateWithLifecycle()
    val allIngredients by kitchenViewModel.allIngredients.collectAsStateWithLifecycle()
    val fridgeItems by kitchenViewModel.fridgeIngredients.collectAsStateWithLifecycle()
    val pantryItems by kitchenViewModel.pantryIngredients.collectAsStateWithLifecycle()
    val shoppingItems by kitchenViewModel.shoppingItems.collectAsStateWithLifecycle()
    val mealPlans by kitchenViewModel.mealPlans.collectAsStateWithLifecycle()
    val suggestedRecipes by kitchenViewModel.suggestedRecipes.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    var showScanDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.userFeedbackMessage) {
        uiState.userFeedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            kitchenViewModel.clearFeedbackMessage()
        }
    }

    // Handle back button for sub-screens
    if (uiState.currentScreen != AppScreen.HOME) {
        BackHandler {
            if (uiState.currentScreen == AppScreen.RECIPE_DETAIL) {
                kitchenViewModel.navigateTo(AppScreen.RECIPES)
            } else {
                kitchenViewModel.navigateTo(AppScreen.HOME)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (uiState.currentScreen != AppScreen.RECIPE_DETAIL) {
                val title = when (uiState.currentScreen) {
                    AppScreen.HOME -> "SobraChef"
                    AppScreen.RECIPES -> "Receitas Zero Desperdício"
                    AppScreen.MEAL_PLAN -> "Cardápio da Semana"
                    AppScreen.SHOPPING -> "Lista de Compras"
                    else -> "SobraChef"
                }
                SobraChefTopAppBar(title = title)
            }
        },
        bottomBar = {
            if (uiState.currentScreen != AppScreen.RECIPE_DETAIL) {
                SobraChefBottomNav(
                    currentScreen = uiState.currentScreen,
                    onNavigate = { screen -> kitchenViewModel.navigateTo(screen) },
                    shoppingCount = shoppingItems.count { !it.isChecked }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentScreen) {
                AppScreen.HOME -> {
                    HomeScreen(
                        fridgeItems = fridgeItems,
                        pantryItems = pantryItems,
                        suggestedRecipes = suggestedRecipes,
                        onAddIngredient = { name, location, qty, unit, urgent ->
                            kitchenViewModel.addIngredient(name, location, qty, unit, urgent)
                        },
                        onDeleteIngredient = { id -> kitchenViewModel.deleteIngredient(id) },
                        onToggleUrgency = { item -> kitchenViewModel.toggleUrgency(item) },
                        onOpenPhotoScanner = { showScanDialog = true },
                        onRecipeSelected = { recipe -> kitchenViewModel.openRecipeDetail(recipe) },
                        onViewAllRecipes = { kitchenViewModel.navigateTo(AppScreen.RECIPES) },
                        onGenerateAiRecipe = { kitchenViewModel.generateAiRecipeWithLeftovers() },
                        isGeneratingAi = uiState.isAiGenerating
                    )
                }

                AppScreen.RECIPES -> {
                    RecipeListScreen(
                        recipes = suggestedRecipes,
                        selectedMaxTime = uiState.maxPrepTime,
                        selectedDifficulty = uiState.selectedDifficulty,
                        onlyComplete = uiState.onlyCompleteLeftovers,
                        onTimeFilterSelected = { time -> kitchenViewModel.setPrepTimeFilter(time) },
                        onDifficultySelected = { diff -> kitchenViewModel.setDifficultyFilter(diff) },
                        onOnlyCompleteToggle = { complete -> kitchenViewModel.setOnlyComplete(complete) },
                        onRecipeSelected = { recipe -> kitchenViewModel.openRecipeDetail(recipe) },
                        onToggleFavorite = { recipe -> kitchenViewModel.toggleFavorite(recipe) }
                    )
                }

                AppScreen.RECIPE_DETAIL -> {
                    uiState.selectedRecipe?.let { recipe ->
                        RecipeDetailScreen(
                            recipe = recipe,
                            currentServings = uiState.selectedRecipeServings,
                            isFavorite = uiState.isRecipeFavorite,
                            onBack = { kitchenViewModel.navigateTo(AppScreen.RECIPES) },
                            onServingsChange = { newServings -> kitchenViewModel.setServings(newServings) },
                            onToggleFavorite = { r -> kitchenViewModel.toggleFavorite(r) },
                            onAddMissingToShoppingList = { r -> kitchenViewModel.addMissingToShoppingList(r) },
                            onScheduleMealPlan = { rTitle, day, mealType ->
                                kitchenViewModel.addMealPlan(rTitle, day, mealType)
                            }
                        )
                    }
                }

                AppScreen.MEAL_PLAN -> {
                    MealCalendarScreen(
                        mealPlans = mealPlans,
                        onAddMealPlan = { title, day, type, notes ->
                            kitchenViewModel.addMealPlan(title, day, type, notes)
                        },
                        onDeleteMealPlan = { id -> kitchenViewModel.deleteMealPlan(id) }
                    )
                }

                AppScreen.SHOPPING -> {
                    ShoppingListScreen(
                        items = shoppingItems,
                        onAddItem = { name, qty -> kitchenViewModel.addShoppingItem(name, qty) },
                        onToggleItem = { item -> kitchenViewModel.toggleShoppingItem(item) },
                        onDeleteItem = { item -> kitchenViewModel.deleteShoppingItem(item) },
                        onClearCompleted = { kitchenViewModel.clearCompletedShoppingItems() }
                    )
                }

                else -> {}
            }

            if (showScanDialog) {
                PhotoScanDialog(
                    isScanning = uiState.isScanningPhoto,
                    detectedItems = uiState.scannedIngredients,
                    onDismiss = {
                        showScanDialog = false
                        kitchenViewModel.clearScannedIngredients()
                    },
                    onPhotoCaptured = { bitmap ->
                        kitchenViewModel.scanPhotoForIngredients(bitmap)
                    },
                    onAddItemsToFridge = { items ->
                        kitchenViewModel.addScannedIngredientsToFridge(items)
                    }
                )
            }
        }
    }
}
