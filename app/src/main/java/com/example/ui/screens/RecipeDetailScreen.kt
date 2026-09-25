package com.example.ui.screens

import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MealType
import com.example.data.model.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipe: Recipe,
    currentServings: Int,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onServingsChange: (Int) -> Unit,
    onToggleFavorite: (Recipe) -> Unit,
    onAddMissingToShoppingList: (Recipe) -> Unit,
    onScheduleMealPlan: (recipeTitle: String, day: String, mealType: MealType) -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    val checkedInstructions = remember { mutableStateListOf<Int>() }
    var showScheduleDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("recipe_detail_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // --- 1. Top Navigation & Action Bar ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("btn_back_recipe_detail")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row {
                    IconButton(
                        onClick = { shareRecipe(context, recipe, currentServings) },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("btn_share_recipe")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartilhar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = { onToggleFavorite(recipe) },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("btn_fav_recipe_detail")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = if (isFavorite) "Remover dos favoritos" else "Adicionar aos favoritos",
                            tint = if (isFavorite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // --- 2. Title & Description ---
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        // --- 3. Custo-Benefício & Waste Metrics Card ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Paid,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Impacto Econômico",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "${recipe.wasteScore}% Zero Desperdício",
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Economia Gerada", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                "R$ ${String.format("%.2f", recipe.savingsEstimate)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        Column {
                            Text("Custo por Pessoa", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                "R$ ${String.format("%.2f", recipe.costPerServing)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Column {
                            Text("Tempo Total", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                "${recipe.prepTimeMinutes} min",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // --- 4. Interactive Servings Adjuster ---
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Porções na Refeição",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Ingredientes ajustados automaticamente",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = { if (currentServings > 1) onServingsChange(currentServings - 1) },
                            enabled = currentServings > 1,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .testTag("btn_decrease_servings")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Diminuir porções",
                                tint = if (currentServings > 1) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                            )
                        }

                        Text(
                            text = "$currentServings ${if (currentServings == 1) "pessoa" else "pessoas"}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )

                        IconButton(
                            onClick = { if (currentServings < 12) onServingsChange(currentServings + 1) },
                            enabled = currentServings < 12,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .testTag("btn_increase_servings")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Aumentar porções",
                                tint = if (currentServings < 12) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                            )
                        }
                    }
                }
            }
        }

        // --- 5. Ingredients Section ---
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ingredientes Necessários",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (recipe.missingItems.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.errorContainer,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = "${recipe.missingItems.size} faltantes",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        itemsIndexed(recipe.ingredients) { _, ing ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (ing.isAvailable) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier.size(22.dp)
                ) {
                    Icon(
                        imageVector = if (ing.isAvailable) Icons.Default.Check else Icons.Default.WarningAmber,
                        contentDescription = null,
                        tint = if (ing.isAvailable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "${if (ing.amount % 1.0 == 0.0) ing.amount.toInt().toString() else ing.amount.toString()} ${ing.unit} de ${ing.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (ing.isAvailable) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = if (ing.isAvailable) FontWeight.Medium else FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                if (!ing.isAvailable) {
                    Text(
                        text = "Falta comprar",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // --- 6. Missing Ingredients to Shopping List CTA ---
        if (recipe.missingItems.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = { onAddMissingToShoppingList(recipe) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .testTag("btn_add_missing_to_shopping"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Adicionar ${recipe.missingItems.size} faltantes à Lista de Compras",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        // --- 7. Instructions / Step by step ---
        item {
            Spacer(modifier = Modifier.height(26.dp))
            Text(
                text = "Modo de Preparo Passo a Passo",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        itemsIndexed(recipe.instructions) { index, step ->
            val isChecked = checkedInstructions.contains(index)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(14.dp))
                    .clickable {
                        if (isChecked) checkedInstructions.remove(index) else checkedInstructions.add(index)
                    }
                    .background(if (isChecked) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface)
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { checked ->
                        if (checked) checkedInstructions.add(index) else checkedInstructions.remove(index)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        checkmarkColor = MaterialTheme.colorScheme.onPrimary
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = "Passo ${index + 1}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodyMedium,
                        textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (isChecked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // --- 8. Zero Waste Chef Tip ---
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Segredo SobraChef",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = recipe.tips,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        // --- 9. Calendar Scheduling Action ---
        item {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = { showScheduleDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("btn_schedule_meal"),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Agendar no Cardápio Semanal",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }

    if (showScheduleDialog) {
        var selectedDay by remember { mutableStateOf("Hoje") }
        var selectedMeal by remember { mutableStateOf(MealType.ALMOCO) }

        AlertDialog(
            onDismissRequest = { showScheduleDialog = false },
            title = {
                Text(
                    "Agendar ${recipe.title}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Column {
                    Text(
                        "Escolha o dia da refeição:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Hoje", "Amanhã", "Sábado", "Domingo").forEach { day ->
                            val isSelected = selectedDay == day
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedDay = day }
                            ) {
                                Text(
                                    text = day,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Escolha o tipo:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        MealType.values().forEach { meal ->
                            val isSelected = selectedMeal == meal
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.secondary) else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedMeal = meal }
                            ) {
                                Text(
                                    text = meal.displayName,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onScheduleMealPlan(recipe.title, selectedDay, selectedMeal)
                        showScheduleDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("btn_confirm_schedule_dialog")
                ) {
                    Text("Salvar no Cardápio", style = MaterialTheme.typography.labelLarge)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showScheduleDialog = false },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        )
    }
}

private fun shareRecipe(context: Context, recipe: Recipe, servings: Int) {
    val ingredientsText = recipe.ingredients.joinToString("\n") { ing ->
        "• ${if (ing.amount % 1.0 == 0.0) ing.amount.toInt().toString() else ing.amount.toString()} ${ing.unit} de ${ing.name}"
    }

    val instructionsText = recipe.instructions.mapIndexed { idx, step ->
        "${idx + 1}. $step"
    }.joinToString("\n")

    val shareContent = """
        🥗 *${recipe.title}* (SobraChef - Desperdício Zero)
        
        ${recipe.description}
        
        ⏱ *Tempo:* ${recipe.prepTimeMinutes} min | 👥 *Porções:* $servings pessoas
        💰 *Economia estimada:* R$ ${String.format("%.2f", recipe.savingsEstimate)} em sobras aproveitadas!
        
        🛒 *Ingredientes:*
        $ingredientsText
        
        👩‍🍳 *Modo de Preparo:*
        $instructionsText
        
        💡 *Dica:* ${recipe.tips}
        
        Feito com o app SobraChef — Evite desperdício e economize!
    """.trimIndent()

    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareContent)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Compartilhar Receita")
    context.startActivity(shareIntent)
}
