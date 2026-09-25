package com.example.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShoppingItem
import com.example.ui.theme.MintContainer
import com.example.ui.theme.OnMintContainer
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.Terracotta
import com.example.ui.theme.TerracottaContainer
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.UrgentRed

@Composable
fun ShoppingListScreen(
    items: List<ShoppingItem>,
    onAddItem: (name: String, quantity: String) -> Unit,
    onToggleItem: (ShoppingItem) -> Unit,
    onDeleteItem: (ShoppingItem) -> Unit,
    onClearCompleted: () -> Unit
) {
    val context = LocalContext.current
    var newItemName by remember { mutableStateOf("") }
    var newItemQty by remember { mutableStateOf("1 un") }
    var itemToDelete by remember { mutableStateOf<ShoppingItem?>(null) }
    var showClearCompletedDialog by remember { mutableStateOf(false) }

    val pendingCount = items.count { !it.isChecked }
    val completedCount = items.count { it.isChecked }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("shopping_list_screen")
    ) {
        // Top Header
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Lista de Compras Inteligente",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryGreen
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (pendingCount == 0) "Tudo comprado para suas receitas!" else "$pendingCount itens pendentes para suas receitas",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondaryLight
                    )
                }

                Row {
                    if (items.isNotEmpty()) {
                        IconButton(
                            onClick = { shareShoppingList(context, items) },
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("btn_share_shopping_list")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Compartilhar Lista",
                                tint = PrimaryGreen
                            )
                        }
                    }

                    if (completedCount > 0) {
                        IconButton(
                            onClick = { showClearCompletedDialog = true },
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("btn_clear_completed_shopping")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CleaningServices,
                                contentDescription = "Limpar Comprados",
                                tint = Terracotta
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
                    placeholder = { Text("Adicionar item essencial faltante...", color = TextSecondaryLight) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_shopping_name"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (newItemName.isNotBlank()) {
                            onAddItem(newItemName.trim(), newItemQty)
                            newItemName = ""
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .size(52.dp)
                        .testTag("btn_add_shopping_item"),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar à Lista",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        if (items.isEmpty()) {
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
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = PrimaryGreen,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Sua lista está limpa!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = PrimaryGreen,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ao visualizar qualquer receita com ingredientes faltantes, toque em 'Adicionar faltantes à Lista de Compras' para trazê-los aqui com 1 clique.",
                        fontSize = 14.sp,
                        color = TextSecondaryLight,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
            ) {
                items(items, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (item.isChecked) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (item.isChecked) 0.dp else 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = item.isChecked,
                                onCheckedChange = { onToggleItem(item) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = PrimaryGreen,
                                    checkmarkColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.name,
                                    fontSize = 15.sp,
                                    fontWeight = if (item.isChecked) FontWeight.Normal else FontWeight.SemiBold,
                                    textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                                    color = if (item.isChecked) TextSecondaryLight else PrimaryGreen
                                )

                                if (item.recipeOrigin.isNotBlank()) {
                                    Text(
                                        text = "Para receita: ${item.recipeOrigin}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Terracotta
                                    )
                                }
                            }

                            IconButton(
                                onClick = { itemToDelete = item },
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("btn_delete_shopping_${item.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Remover ${item.name}",
                                    tint = TextSecondaryLight,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Confirmation dialog for deleting an item (Rule 5: Irreversible action confirmation)
    itemToDelete?.let { item ->
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = {
                Text("Remover Item?", fontWeight = FontWeight.Bold, color = PrimaryGreen)
            },
            text = {
                Text(
                    "Deseja remover \"${item.name}\" da sua lista de compras?",
                    color = TextPrimaryLight,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteItem(item)
                        itemToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UrgentRed)
                ) {
                    Text("Remover", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { itemToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Confirmation dialog for clearing completed items (Rule 5)
    if (showClearCompletedDialog) {
        AlertDialog(
            onDismissRequest = { showClearCompletedDialog = false },
            title = {
                Text("Limpar Itens Comprados?", fontWeight = FontWeight.Bold, color = PrimaryGreen)
            },
            text = {
                Text(
                    "Deseja remover todos os $completedCount itens já marcados como comprados?",
                    color = TextPrimaryLight,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onClearCompleted()
                        showClearCompletedDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta)
                ) {
                    Text("Limpar Comprados", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showClearCompletedDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

private fun shareShoppingList(context: Context, items: List<ShoppingItem>) {
    val pending = items.filter { !it.isChecked }
    val text = if (pending.isEmpty()) {
        "🛒 *Lista de Compras SobraChef:*\nTodos os itens foram comprados!"
    } else {
        val list = pending.joinToString("\n") { "• ${it.name} (${it.quantity})" }
        "🛒 *Lista de Compras SobraChef (Itens Faltantes):*\n\n$list\n\nEconomize e evite desperdício alimentar!"
    }

    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(intent, "Compartilhar Lista de Compras"))
}
