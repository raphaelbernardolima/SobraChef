package com.example.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShoppingItem

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
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (pendingCount == 0) "Tudo comprado para suas receitas!" else "$pendingCount itens pendentes para suas receitas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
                                tint = MaterialTheme.colorScheme.primary
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
                                tint = MaterialTheme.colorScheme.secondary
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
                    placeholder = {
                        Text(
                            "Adicionar item essencial faltante...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_shopping_name"),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
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
                        if (newItemName.isNotBlank()) {
                            onAddItem(newItemName.trim(), newItemQty)
                            newItemName = ""
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
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
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(72.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Sua lista está limpa!",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Adicione itens que faltam para suas receitas ou planeje compras conscientes para não gerar sobras esquecidas.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp)
            ) {
                items(items, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = item.isChecked,
                                onCheckedChange = { onToggleItem(item) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.primary,
                                    checkmarkColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = if (item.isChecked) FontWeight.Normal else FontWeight.SemiBold,
                                    textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                                    color = if (item.isChecked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                                )

                                if (item.quantity.isNotBlank() && item.quantity != "1 un") {
                                    Text(
                                        text = item.quantity,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            IconButton(
                                onClick = { itemToDelete = item },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Excluir ${item.name}",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Confirmation dialog for clearing completed items
    if (showClearCompletedDialog) {
        AlertDialog(
            onDismissRequest = { showClearCompletedDialog = false },
            title = {
                Text(
                    text = "Limpar itens comprados?",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = "Deseja remover da lista todos os $completedCount itens já marcados como comprados?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onClearCompleted()
                        showClearCompletedDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Limpar Comprados", style = MaterialTheme.typography.labelLarge)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showClearCompletedDialog = false },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        )
    }

    // Confirmation dialog for single item deletion
    if (itemToDelete != null) {
        val item = itemToDelete!!
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = {
                Text(
                    text = "Remover da lista?",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = "Deseja remover \"${item.name}\" da sua lista de compras?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteItem(item)
                        itemToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Sim, remover", style = MaterialTheme.typography.labelLarge)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { itemToDelete = null },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        )
    }
}

private fun shareShoppingList(context: Context, items: List<ShoppingItem>) {
    val pending = items.filter { !it.isChecked }
    val bought = items.filter { it.isChecked }

    val builder = StringBuilder()
    builder.append("🛒 *Lista de Compras - SobraChef*\n\n")

    if (pending.isNotEmpty()) {
        builder.append("📌 *A Comprar:*\n")
        pending.forEach { builder.append("☐ ${it.name} (${it.quantity})\n") }
        builder.append("\n")
    }

    if (bought.isNotEmpty()) {
        builder.append("✅ *Já Comprado:*\n")
        bought.forEach { builder.append("☑ ${it.name}\n") }
        builder.append("\n")
    }

    builder.append("Gerado pelo app SobraChef — Cozinha sem desperdício!")

    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, builder.toString())
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Compartilhar Lista de Compras")
    context.startActivity(shareIntent)
}
