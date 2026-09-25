package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Schedule
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
import com.example.ui.theme.LeafGreen
import com.example.ui.theme.MintContainer
import com.example.ui.theme.OnMintContainer
import com.example.ui.theme.OnTerracottaContainer
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.PrimaryGreenLight
import com.example.ui.theme.SaffronGold
import com.example.ui.theme.Terracotta
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.UrgentRed
import com.example.ui.theme.TerracottaContainer
import com.example.ui.theme.UrgentBg
import com.example.ui.theme.UrgentRed

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
            .testTag("recipe_detail_screen")
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
                    modifier = Modifier.testTag("btn_back_recipe_detail")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = PrimaryGreen
                    )
                }

                Row {
                    IconButton(
                        onClick = { shareRecipe(context, recipe, currentServings) },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("btn_share_recipe")
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Compartilhar", tint = PrimaryGreen)
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
                            tint = if (isFavorite) Terracotta else PrimaryGreen
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
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryGreen,
                    lineHeight = 30.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = recipe.description,
                    fontSize = 14.sp,
                    color = TextSecondaryLight,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        // --- 3. Custo-Benefício & Waste Metrics Card (AAA Contrast) ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MintContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Paid, contentDescription = null, tint = PrimaryGreen)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Análise Custo-Benefício",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = PrimaryGreen
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = PrimaryGreen
                        ) {
                            Text(
                                text = "${recipe.wasteScore}% Zero Desperdício",
                                color = Color.White,
                                fontSize = 12.sp,
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
                            Text("Economia Gerada", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OnMintContainer)
                            Text(
                                "R$ ${String.format("%.2f", recipe.savingsEstimate)}",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Terracotta
                            )
                        }

                        Column {
                            Text("Custo por Pessoa", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OnMintContainer)
                            Text(
                                "R$ ${String.format("%.2f", recipe.costPerServing)}",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = PrimaryGreen
                            )
                        }

                        Column {
                            Text("Tempo Total", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OnMintContainer)
                            Text(
                                "${recipe.prepTimeMinutes} min",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = PrimaryGreen
                            )
                        }
                    }
                }
            }
        }

        // --- 4. Interactive Servings Adjuster ("Ajuste de Porções" with 48dp touch targets) ---
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
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
                            tint = PrimaryGreen
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Porções na Refeição",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = PrimaryGreen
                            )
                            Text(
                                text = "Ingredientes ajustados automaticamente",
                                fontSize = 12.sp,
                                color = TextSecondaryLight
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
                                .background(Color.White, CircleShape)
                                .testTag("btn_decrease_servings")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Diminuir porções",
                                tint = if (currentServings > 1) PrimaryGreen else Color.Gray
                            )
                        }

                        Text(
                            text = "$currentServings ${if (currentServings == 1) "pessoa" else "pessoas"}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = PrimaryGreen,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )

                        IconButton(
                            onClick = { if (currentServings < 12) onServingsChange(currentServings + 1) },
                            enabled = currentServings < 12,
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.White, CircleShape)
                                .testTag("btn_increase_servings")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Aumentar porções",
                                tint = if (currentServings < 12) PrimaryGreen else Color.Gray
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
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = PrimaryGreen
                )

                if (recipe.missingItems.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = UrgentBg
                    ) {
                        Text(
                            text = "${recipe.missingItems.size} faltantes",
                            color = UrgentRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
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
                    .padding(horizontal = 16.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (ing.isAvailable) MintContainer else UrgentBg,
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = if (ing.isAvailable) Icons.Default.Check else Icons.Default.WarningAmber,
                        contentDescription = null,
                        tint = if (ing.isAvailable) PrimaryGreen else UrgentRed,
                        modifier = Modifier.padding(3.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "${if (ing.amount % 1.0 == 0.0) ing.amount.toInt().toString() else ing.amount.toString()} ${ing.unit} de ${ing.name}",
                    fontSize = 14.sp,
                    color = if (ing.isAvailable) MaterialTheme.colorScheme.onSurface else UrgentRed,
                    fontWeight = if (ing.isAvailable) FontWeight.Medium else FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                if (!ing.isAvailable) {
                    Text(
                        text = "Falta comprar",
                        fontSize = 11.sp,
                        color = UrgentRed,
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
                        containerColor = Terracotta,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Adicionar ${recipe.missingItems.size} faltantes à Lista de Compras",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // --- 7. Instructions / Step by step ---
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Modo de Preparo Passo a Passo",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = PrimaryGreen,
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
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        if (isChecked) checkedInstructions.remove(index) else checkedInstructions.add(index)
                    }
                    .background(if (isChecked) MintContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface)
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { checked ->
                        if (checked) checkedInstructions.add(index) else checkedInstructions.remove(index)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = PrimaryGreen,
                        checkmarkColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = "Passo ${index + 1}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
                    )
                    Text(
                        text = step,
                        fontSize = 14.sp,
                        textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (isChecked) TextSecondaryLight else TextPrimaryLight,
                        lineHeight = 20.sp
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
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = SaffronGold,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Segredo SobraChef",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = PrimaryGreen
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = recipe.tips,
                            fontSize = 13.sp,
                            color = TextSecondaryLight,
                            lineHeight = 18.sp
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
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, tint = PrimaryGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Agendar no Cardápio Semanal", color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(96.dp))
        }
    }

    if (showScheduleDialog) {
        var selectedDay by remember { mutableStateOf("Hoje") }
        var selectedMeal by remember { mutableStateOf(MealType.ALMOCO) }

        AlertDialog(
            onDismissRequest = { showScheduleDialog = false },
            title = {
                Text("Agendar ${recipe.title}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PrimaryGreen)
            },
            text = {
                Column {
                    Text("Escolha o dia da refeição:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextSecondaryLight)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Hoje", "Amanhã", "Sábado", "Domingo").forEach { day ->
                            val isSelected = selectedDay == day
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) MintContainer else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedDay = day }
                            ) {
                                Text(
                                    text = day,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) OnMintContainer else TextPrimaryLight,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Escolha o tipo:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextSecondaryLight)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        MealType.values().forEach { meal ->
                            val isSelected = selectedMeal == meal
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) TerracottaContainer else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedMeal = meal }
                            ) {
                                Text(
                                    text = meal.displayName,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) OnTerracottaContainer else TextPrimaryLight,
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
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    modifier = Modifier.testTag("btn_confirm_schedule_dialog")
                ) {
                    Text("Salvar no Cardápio", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showScheduleDialog = false }) {
                    Text("Cancelar")
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
