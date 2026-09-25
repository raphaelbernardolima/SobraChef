package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IngredientItem
import com.example.data.model.Recipe
import com.example.data.model.StorageLocation
import com.example.ui.theme.LeafGreen
import com.example.ui.theme.MintContainer
import com.example.ui.theme.OnMintContainer
import com.example.ui.theme.OnTerracottaContainer
import com.example.ui.theme.OnUrgentBg
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.PrimaryGreenLight
import com.example.ui.theme.SaffronGold
import com.example.ui.theme.Terracotta
import com.example.ui.theme.TerracottaContainer
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.UrgentBg
import com.example.ui.theme.UrgentRed

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    fridgeItems: List<IngredientItem>,
    pantryItems: List<IngredientItem>,
    suggestedRecipes: List<Recipe>,
    onAddIngredient: (name: String, location: StorageLocation, quantity: String, unit: String, isUrgent: Boolean) -> Unit,
    onDeleteIngredient: (Long) -> Unit,
    onToggleUrgency: (IngredientItem) -> Unit,
    onOpenPhotoScanner: () -> Unit,
    onRecipeSelected: (Recipe) -> Unit,
    onViewAllRecipes: () -> Unit,
    onGenerateAiRecipe: () -> Unit,
    isGeneratingAi: Boolean
) {
    var selectedLocation by remember { mutableStateOf(StorageLocation.GELADEIRA) }
    var newItemName by remember { mutableStateOf("") }
    var newItemQty by remember { mutableStateOf("1") }
    var newItemUnit by remember { mutableStateOf("un") }
    var isUrgentFlag by remember { mutableStateOf(false) }

    // Dialog confirmation for ingredient removal (Rule 5: explicit confirmation with real consequence)
    var itemToDelete by remember { mutableStateOf<IngredientItem?>(null) }

    val currentList = if (selectedLocation == StorageLocation.GELADEIRA) fridgeItems else pantryItems
    val urgentCount = fridgeItems.count { it.isUrgent }

    if (itemToDelete != null) {
        val item = itemToDelete!!
        val locName = if (item.location == StorageLocation.GELADEIRA) "da geladeira" else "da dispensa"
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = {
                Text(
                    text = "Remover ingrediente?",
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Deseja remover \"${item.name}\" $locName?\n\nAs receitas sugeridas deixarão de aproveitar esse item para evitar desperdício alimentar.",
                    fontSize = 14.sp,
                    color = TextSecondaryLight,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteIngredient(item.id)
                        itemToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UrgentRed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.sizeIn(minHeight = 48.dp)
                ) {
                    Text("Sim, remover", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { itemToDelete = null },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.sizeIn(minHeight = 48.dp)
                ) {
                    Text("Cancelar", fontWeight = FontWeight.Medium, color = PrimaryGreen)
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen_lazy_column"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // --- 1. Signature Hero Card: Impact & Economy ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryGreen)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MintContainer,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Eco,
                                    contentDescription = null,
                                    tint = PrimaryGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Desperdício Zero",
                                    color = OnMintContainer,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (urgentCount > 0) {
                            Surface(
                                color = UrgentBg,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.WarningAmber,
                                        contentDescription = null,
                                        tint = UrgentRed,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "$urgentCount sobras urgentes!",
                                        color = OnUrgentBg,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "O que sobrou em casa vira prato gourmet em minutos.",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Economize até R$ 280/mês aproveitando 100% dos alimentos comprados.",
                        color = MintContainer,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Metrics Row (High Contrast AAA)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            color = PrimaryGreenLight
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Paid,
                                        contentDescription = null,
                                        tint = SaffronGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Economia Média", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "R$ 24,50 / prato",
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            color = PrimaryGreenLight
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.TrendingDown,
                                        contentDescription = null,
                                        tint = MintContainer,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Desperdício", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "-92% no lixo",
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 2. Action Toolbar: Photo Scan & AI Generation (Fitts's Law: Large prominent touch targets) ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onOpenPhotoScanner,
                    modifier = Modifier
                        .weight(1f)
                        .sizeIn(minHeight = 50.dp)
                        .testTag("btn_open_camera_scanner"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Fotografar Sobras", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Button(
                    onClick = onGenerateAiRecipe,
                    modifier = Modifier
                        .weight(1f)
                        .sizeIn(minHeight = 50.dp)
                        .testTag("btn_ai_recipe_generator"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Terracotta,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isGeneratingAi
                ) {
                    if (isGeneratingAi) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Criando...", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    } else {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Gerar c/ IA", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }

        // --- 3. Inventory Manager Section (Geladeira vs Dispensa) ---
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "O que tem em casa agora?",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
                Text(
                    text = "Adicione o que sobrou para ver receitas sob medida.",
                    fontSize = 13.sp,
                    color = TextSecondaryLight
                )

                Spacer(modifier = Modifier.height(12.dp))

                TabRow(
                    selectedTabIndex = selectedLocation.ordinal,
                    containerColor = MintContainer.copy(alpha = 0.5f),
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedLocation.ordinal]),
                            color = PrimaryGreen
                        )
                    }
                ) {
                    Tab(
                        selected = selectedLocation == StorageLocation.GELADEIRA,
                        onClick = { selectedLocation = StorageLocation.GELADEIRA },
                        text = {
                            Text(
                                "Geladeira (${fridgeItems.size})",
                                fontWeight = if (selectedLocation == StorageLocation.GELADEIRA) FontWeight.Bold else FontWeight.Medium,
                                color = PrimaryGreen,
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier
                            .sizeIn(minHeight = 48.dp)
                            .testTag("tab_fridge")
                    )
                    Tab(
                        selected = selectedLocation == StorageLocation.DISPENSA,
                        onClick = { selectedLocation = StorageLocation.DISPENSA },
                        text = {
                            Text(
                                "Dispensa (${pantryItems.size})",
                                fontWeight = if (selectedLocation == StorageLocation.DISPENSA) FontWeight.Bold else FontWeight.Medium,
                                color = PrimaryGreen,
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier
                            .sizeIn(minHeight = 48.dp)
                            .testTag("tab_pantry")
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Fast Add Input (With real-time validation: disabled if blank)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newItemName,
                        onValueChange = { newItemName = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_ingredient_name"),
                        placeholder = {
                            Text(
                                text = if (selectedLocation == StorageLocation.GELADEIRA)
                                    "Ex: Arroz amanhecido, ovos..."
                                else
                                    "Ex: Farinha de trigo, alho...",
                                color = TextSecondaryLight
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val trimmed = newItemName.trim()
                            if (trimmed.isNotBlank()) {
                                onAddIngredient(trimmed, selectedLocation, newItemQty, newItemUnit, isUrgentFlag)
                                newItemName = ""
                                isUrgentFlag = false
                            }
                        },
                        enabled = newItemName.isNotBlank(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryGreen,
                            contentColor = Color.White,
                            disabledContainerColor = PrimaryGreen.copy(alpha = 0.3f),
                            disabledContentColor = Color.White.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier
                            .sizeIn(minWidth = 50.dp, minHeight = 50.dp)
                            .testTag("btn_add_ingredient")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar Ingrediente")
                    }
                }

                // Urgent leftover toggle
                if (selectedLocation == StorageLocation.GELADEIRA) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { isUrgentFlag = !isUrgentFlag }
                            .padding(vertical = 6.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isUrgentFlag) UrgentRed else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(22.dp)
                        ) {
                            if (isUrgentFlag) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.padding(3.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Marcar como 'Sobra Urgente' (consumir o quanto antes)",
                            fontSize = 13.sp,
                            color = if (isUrgentFlag) UrgentRed else TextSecondaryLight,
                            fontWeight = if (isUrgentFlag) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }

                // Quick One-Click Leftover Suggestions (Hick's & Fitts's Laws: 0-typing shortcut)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Atalhos rápidos com 1 toque:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondaryLight
                )
                Spacer(modifier = Modifier.height(6.dp))
                val quickSuggestions = if (selectedLocation == StorageLocation.GELADEIRA) {
                    listOf("Arroz cozido", "Ovos", "Tomate", "Cenoura", "Queijo", "Frango")
                } else {
                    listOf("Alho", "Cebola", "Farinha", "Macarrão", "Azeite", "Milho")
                }
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickSuggestions.forEach { suggestion ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable {
                                    onAddIngredient(suggestion, selectedLocation, "1", "un", false)
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = PrimaryGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = suggestion,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextPrimaryLight
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Interactive Chips of Ingredients
                if (currentList.isEmpty()) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Kitchen,
                                contentDescription = null,
                                tint = PrimaryGreen,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Nenhum ingrediente adicionado na ${if (selectedLocation == StorageLocation.GELADEIRA) "geladeira" else "dispensa"}.",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimaryLight
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Toque em um dos atalhos rápidos acima ou fotografe sua geladeira!",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryLight
                            )
                        }
                    }
                } else {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        currentList.forEach { item ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (item.isUrgent) UrgentBg else MintContainer,
                                modifier = Modifier.clip(RoundedCornerShape(16.dp))
                            ) {
                                Row(
                                    modifier = Modifier.padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (item.isUrgent) {
                                        Icon(
                                            imageVector = Icons.Default.WarningAmber,
                                            contentDescription = "Urgente",
                                            tint = UrgentRed,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    Text(
                                        text = "${item.name} (${item.quantity} ${item.unit})",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (item.isUrgent) OnUrgentBg else OnMintContainer
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    // 48dp minimum accessible touch target for delete
                                    IconButton(
                                        onClick = { itemToDelete = item },
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remover ${item.name}",
                                            tint = if (item.isUrgent) UrgentRed else PrimaryGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 4. Matching Zero-Waste Recipes Suggested ---
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Receitas com suas sobras",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
                    )
                    Text(
                        text = "${suggestedRecipes.size} pratos aproveitando seus ingredientes",
                        fontSize = 13.sp,
                        color = TextSecondaryLight
                    )
                }

                Button(
                    onClick = onViewAllRecipes,
                    colors = ButtonDefaults.textButtonColors(contentColor = PrimaryGreen),
                    modifier = Modifier
                        .sizeIn(minHeight = 48.dp)
                        .testTag("btn_see_all_recipes")
                ) {
                    Text("Ver Todas", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(suggestedRecipes.take(4)) { recipe ->
            RecipeCard(
                recipe = recipe,
                onClick = { onRecipeSelected(recipe) }
            )
        }
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
            .testTag("recipe_card_${recipe.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with title and waste score badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = recipe.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = PrimaryGreen,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MintContainer
                ) {
                    Text(
                        text = "${recipe.wasteScore}% aproveitamento",
                        color = OnMintContainer,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = recipe.description,
                fontSize = 13.sp,
                color = TextSecondaryLight,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Cost benefit & attributes badges (High Contrast AA)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = TerracottaContainer
                ) {
                    Text(
                        text = "Economiza R$ ${String.format("%.2f", recipe.savingsEstimate)}",
                        color = OnTerracottaContainer,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = TextSecondaryLight
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${recipe.prepTimeMinutes} min",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondaryLight
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = recipe.difficulty,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondaryLight,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "R$ ${String.format("%.2f", recipe.costPerServing)}/porção",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryGreen
                )
            }
        }
    }
}

