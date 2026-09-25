package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IngredientItem
import com.example.data.model.Recipe
import com.example.data.model.StorageLocation

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

    // Dialog confirmation for ingredient removal
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
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = "Deseja remover \"${item.name}\" $locName?\n\nAs receitas sugeridas deixarão de aproveitar esse item para evitar desperdício alimentar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteIngredient(item.id)
                        itemToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.sizeIn(minHeight = 48.dp)
                ) {
                    Text("Sim, remover", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { itemToDelete = null },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.sizeIn(minHeight = 48.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Text("Cancelar", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
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
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF063A22),
                                    Color(0xFF0F5A38),
                                    Color(0xFF137A4B)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = Color.White.copy(alpha = 0.18f),
                                shape = RoundedCornerShape(20.dp),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Eco,
                                        contentDescription = null,
                                        tint = Color(0xFF6EE7B7),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Desperdício Zero",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }

                            if (urgentCount > 0) {
                                Surface(
                                    color = MaterialTheme.colorScheme.errorContainer,
                                    shape = RoundedCornerShape(20.dp),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.WarningAmber,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "$urgentCount urgentes!",
                                            color = MaterialTheme.colorScheme.onErrorContainer,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "O que sobrou em casa vira prato gourmet em minutos.",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineLarge
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Economize até R$ 280/mês aproveitando 100% dos alimentos comprados.",
                            color = Color(0xFFD1FAE5),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Frosted Metrics Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White.copy(alpha = 0.14f),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.22f))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Paid,
                                            contentDescription = null,
                                            tint = Color(0xFFFDE68A),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Economia Média",
                                            color = Color.White.copy(alpha = 0.9f),
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "R$ 24,50 / prato",
                                        color = Color.White,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }

                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White.copy(alpha = 0.14f),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.22f))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.TrendingDown,
                                            contentDescription = null,
                                            tint = Color(0xFF6EE7B7),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Desperdício",
                                            color = Color.White.copy(alpha = 0.9f),
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "-92% no lixo",
                                        color = Color.White,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 2. Action Toolbar: Photo Scan & AI Generation ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onOpenPhotoScanner,
                    modifier = Modifier
                        .weight(1f)
                        .sizeIn(minHeight = 52.dp)
                        .testTag("btn_open_camera_scanner"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Fotografar Sobras", style = MaterialTheme.typography.labelLarge)
                }

                Button(
                    onClick = onGenerateAiRecipe,
                    modifier = Modifier
                        .weight(1f)
                        .sizeIn(minHeight = 52.dp)
                        .testTag("btn_ai_recipe_generator"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                    enabled = !isGeneratingAi
                ) {
                    if (isGeneratingAi) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Criando...", style = MaterialTheme.typography.labelLarge)
                    } else {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Gerar c/ IA", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }

        // --- 3. Inventory Manager Section (Modern Segmented Pill Switcher) ---
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "O que tem em casa agora?",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Adicione o que sobrou para ver receitas sob medida.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Modern Segmented Control
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Geladeira Tab
                        val isFridgeSelected = selectedLocation == StorageLocation.GELADEIRA
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedLocation = StorageLocation.GELADEIRA }
                                .sizeIn(minHeight = 44.dp)
                                .testTag("tab_fridge"),
                            shape = RoundedCornerShape(12.dp),
                            color = if (isFridgeSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
                            shadowElevation = if (isFridgeSelected) 2.dp else 0.dp
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Kitchen,
                                    contentDescription = null,
                                    tint = if (isFridgeSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Geladeira (${fridgeItems.size})",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (isFridgeSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Dispensa Tab
                        val isPantrySelected = selectedLocation == StorageLocation.DISPENSA
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedLocation = StorageLocation.DISPENSA }
                                .sizeIn(minHeight = 44.dp)
                                .testTag("tab_pantry"),
                            shape = RoundedCornerShape(12.dp),
                            color = if (isPantrySelected) MaterialTheme.colorScheme.surface else Color.Transparent,
                            shadowElevation = if (isPantrySelected) 2.dp else 0.dp
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Kitchen,
                                    contentDescription = null,
                                    tint = if (isPantrySelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Dispensa (${pantryItems.size})",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (isPantrySelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Fast Add Input
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
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        )
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
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier
                            .sizeIn(minWidth = 52.dp, minHeight = 52.dp)
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
                            color = if (isUrgentFlag) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surfaceVariant,
                            border = if (isUrgentFlag) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.size(22.dp)
                        ) {
                            if (isUrgentFlag) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onError,
                                    modifier = Modifier.padding(3.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Marcar como 'Sobra Urgente' (consumir o quanto antes)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isUrgentFlag) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (isUrgentFlag) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }

                // Quick One-Click Leftover Suggestions
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Atalhos rápidos com 1 toque:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
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
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = suggestion,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Interactive Chips of Ingredients
                if (currentList.isEmpty()) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Kitchen,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Nenhum ingrediente na ${if (selectedLocation == StorageLocation.GELADEIRA) "geladeira" else "dispensa"}.",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Toque em um dos atalhos acima ou fotografe sua geladeira!",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                            val chipBg = if (item.isUrgent) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surface
                            val chipBorder = if (item.isUrgent) MaterialTheme.colorScheme.error.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant
                            val chipTextColor = if (item.isUrgent) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurface

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = chipBg,
                                border = BorderStroke(1.dp, chipBorder),
                                modifier = Modifier.clip(RoundedCornerShape(14.dp))
                            ) {
                                Row(
                                    modifier = Modifier.padding(start = 12.dp, end = 2.dp, top = 2.dp, bottom = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (item.isUrgent) {
                                        Icon(
                                            imageVector = Icons.Default.WarningAmber,
                                            contentDescription = "Urgente",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Eco,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                    }

                                    Text(
                                        text = "${item.name} (${item.quantity} ${item.unit})",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = chipTextColor
                                    )

                                    // 48dp minimum accessible touch target for delete
                                    IconButton(
                                        onClick = { itemToDelete = item },
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remover ${item.name}",
                                            tint = if (item.isUrgent) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
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
            Spacer(modifier = Modifier.height(26.dp))
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
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${suggestedRecipes.size} pratos aproveitando seus ingredientes",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = onViewAllRecipes,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .sizeIn(minHeight = 48.dp)
                        .testTag("btn_see_all_recipes")
                ) {
                    Text("Ver Todas", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
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
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "${recipe.wasteScore}% aproveitamento",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = recipe.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Footer row with attributes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = "Economia R$ ${String.format("%.2f", recipe.savingsEstimate)}",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.labelSmall,
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
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${recipe.prepTimeMinutes} min",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
