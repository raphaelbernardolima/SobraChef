package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Recipe
import com.example.ui.theme.MintContainer
import com.example.ui.theme.OnMintContainer
import com.example.ui.theme.OnTerracottaContainer
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.Terracotta
import com.example.ui.theme.TerracottaContainer
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.UrgentRed

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    recipes: List<Recipe>,
    selectedMaxTime: Int?,
    selectedDifficulty: String,
    onlyComplete: Boolean,
    onTimeFilterSelected: (Int?) -> Unit,
    onDifficultySelected: (String) -> Unit,
    onOnlyCompleteToggle: (Boolean) -> Unit,
    onRecipeSelected: (Recipe) -> Unit,
    onToggleFavorite: (Recipe) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredList = recipes.filter {
        val matchesSearch = if (searchQuery.isBlank()) true
        else it.title.contains(searchQuery, ignoreCase = true) ||
                it.description.contains(searchQuery, ignoreCase = true) ||
                it.ingredients.any { ing -> ing.name.contains(searchQuery, ignoreCase = true) }
        matchesSearch
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("recipe_list_screen")
    ) {
        // Search bar with clear button
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar receita por nome ou ingrediente...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = PrimaryGreen
                )
            },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(
                        onClick = { searchQuery = "" },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Limpar busca",
                            tint = PrimaryGreen
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("recipe_search_input"),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        // Prep Time Filters (High contrast chips)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedMaxTime == null,
                    onClick = { onTimeFilterSelected(null) },
                    label = { Text("Qualquer Tempo", fontWeight = if (selectedMaxTime == null) FontWeight.Bold else FontWeight.Medium) },
                    modifier = Modifier.sizeIn(minHeight = 48.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MintContainer,
                        selectedLabelColor = OnMintContainer,
                        containerColor = Color.White,
                        labelColor = TextPrimaryLight
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedMaxTime == 15,
                    onClick = { onTimeFilterSelected(if (selectedMaxTime == 15) null else 15) },
                    label = { Text("Até 15 min ⚡", fontWeight = if (selectedMaxTime == 15) FontWeight.Bold else FontWeight.Medium) },
                    modifier = Modifier.sizeIn(minHeight = 48.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MintContainer,
                        selectedLabelColor = OnMintContainer,
                        containerColor = Color.White,
                        labelColor = TextPrimaryLight
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedMaxTime == 30,
                    onClick = { onTimeFilterSelected(if (selectedMaxTime == 30) null else 30) },
                    label = { Text("Até 30 min", fontWeight = if (selectedMaxTime == 30) FontWeight.Bold else FontWeight.Medium) },
                    modifier = Modifier.sizeIn(minHeight = 48.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MintContainer,
                        selectedLabelColor = OnMintContainer,
                        containerColor = Color.White,
                        labelColor = TextPrimaryLight
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedMaxTime == 45,
                    onClick = { onTimeFilterSelected(if (selectedMaxTime == 45) null else 45) },
                    label = { Text("Até 45 min", fontWeight = if (selectedMaxTime == 45) FontWeight.Bold else FontWeight.Medium) },
                    modifier = Modifier.sizeIn(minHeight = 48.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MintContainer,
                        selectedLabelColor = OnMintContainer,
                        containerColor = Color.White,
                        labelColor = TextPrimaryLight
                    )
                )
            }
        }

        // Difficulty & Only Complete Filters
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedDifficulty == "Todos",
                    onClick = { onDifficultySelected("Todos") },
                    label = { Text("Todas as Dificuldades", fontWeight = if (selectedDifficulty == "Todos") FontWeight.Bold else FontWeight.Medium) },
                    modifier = Modifier.sizeIn(minHeight = 48.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MintContainer,
                        selectedLabelColor = OnMintContainer,
                        containerColor = Color.White,
                        labelColor = TextPrimaryLight
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedDifficulty == "Fácil",
                    onClick = { onDifficultySelected(if (selectedDifficulty == "Fácil") "Todos" else "Fácil") },
                    label = { Text("Fácil", fontWeight = if (selectedDifficulty == "Fácil") FontWeight.Bold else FontWeight.Medium) },
                    modifier = Modifier.sizeIn(minHeight = 48.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MintContainer,
                        selectedLabelColor = OnMintContainer,
                        containerColor = Color.White,
                        labelColor = TextPrimaryLight
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedDifficulty == "Médio",
                    onClick = { onDifficultySelected(if (selectedDifficulty == "Médio") "Todos" else "Médio") },
                    label = { Text("Médio", fontWeight = if (selectedDifficulty == "Médio") FontWeight.Bold else FontWeight.Medium) },
                    modifier = Modifier.sizeIn(minHeight = 48.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MintContainer,
                        selectedLabelColor = OnMintContainer,
                        containerColor = Color.White,
                        labelColor = TextPrimaryLight
                    )
                )
            }
            item {
                FilterChip(
                    selected = onlyComplete,
                    onClick = { onOnlyCompleteToggle(!onlyComplete) },
                    label = { Text("100% com o que tenho", fontWeight = if (onlyComplete) FontWeight.Bold else FontWeight.Medium) },
                    modifier = Modifier.sizeIn(minHeight = 48.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TerracottaContainer,
                        selectedLabelColor = OnTerracottaContainer,
                        containerColor = Color.White,
                        labelColor = TextPrimaryLight
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${filteredList.size} receitas encontradas",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondaryLight
            )

            if (searchQuery.isNotBlank() || selectedMaxTime != null || selectedDifficulty != "Todos" || onlyComplete) {
                Text(
                    text = "Limpar filtros",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Terracotta,
                    modifier = Modifier
                        .clickable {
                            searchQuery = ""
                            onTimeFilterSelected(null)
                            onDifficultySelected("Todos")
                            onOnlyCompleteToggle(false)
                        }
                        .padding(4.dp)
                )
            }
        }

        // Empty state or recipe list (Rule 9: oriented next step)
        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MintContainer,
                        modifier = Modifier.size(72.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.FilterAltOff,
                                contentDescription = null,
                                tint = PrimaryGreen,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Nenhuma receita encontrada",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (onlyComplete)
                            "Você filtrou por '100% com o que tenho'. Desmarque este filtro para ver receitas que usam a maioria das suas sobras com poucos itens faltantes."
                        else if (searchQuery.isNotBlank())
                            "Não encontramos receitas contendo \"$searchQuery\". Experimente pesquisar por ingredientes básicos como 'arroz', 'ovo' ou 'tomate'."
                        else
                            "Tente ajustar o tempo de preparo ou o nível de dificuldade selecionado.",
                        fontSize = 14.sp,
                        color = TextSecondaryLight,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            searchQuery = ""
                            onTimeFilterSelected(null)
                            onDifficultySelected("Todos")
                            onOnlyCompleteToggle(false)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.sizeIn(minHeight = 48.dp)
                    ) {
                        Text("Ver Todas as Receitas", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp)
            ) {
                items(filteredList) { recipe ->
                    RecipeListItem(
                        recipe = recipe,
                        onClick = { onRecipeSelected(recipe) },
                        onToggleFavorite = { onToggleFavorite(recipe) }
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeListItem(
    recipe: Recipe,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
            .testTag("recipe_item_${recipe.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recipe.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = PrimaryGreen,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = if (recipe.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Favoritar ${recipe.title}",
                        tint = if (recipe.isFavorite) Terracotta else TextSecondaryLight
                    )
                }
            }

            Text(
                text = recipe.description,
                fontSize = 13.sp,
                color = TextSecondaryLight,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Cost benefit & attributes (High Contrast AAA)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MintContainer
                ) {
                    Text(
                        text = "${recipe.wasteScore}% aproveitado",
                        color = OnMintContainer,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = TerracottaContainer
                ) {
                    Text(
                        text = "Economia R$ ${String.format("%.2f", recipe.savingsEstimate)}",
                        color = OnTerracottaContainer,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = TextSecondaryLight
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${recipe.prepTimeMinutes}m",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondaryLight
                    )
                }
            }

            if (recipe.missingItems.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Falta comprar: ${recipe.missingItems.joinToString(", ")}",
                    fontSize = 12.sp,
                    color = UrgentRed,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

