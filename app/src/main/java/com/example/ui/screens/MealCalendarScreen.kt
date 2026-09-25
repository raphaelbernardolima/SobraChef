package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MealPlanItem
import com.example.data.model.MealType
import com.example.ui.theme.MintContainer
import com.example.ui.theme.OnMintContainer
import com.example.ui.theme.OnTerracottaContainer
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.Terracotta
import com.example.ui.theme.TerracottaContainer
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.UrgentRed

@Composable
fun MealCalendarScreen(
    mealPlans: List<MealPlanItem>,
    onAddMealPlan: (title: String, day: String, type: MealType, notes: String) -> Unit,
    onDeleteMealPlan: (Long) -> Unit
) {
    val context = LocalContext.current
    val daysOfWeek = listOf("Todos", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo", "Hoje")
    var selectedDayFilter by remember { mutableStateOf("Todos") }
    var showAddDialog by remember { mutableStateOf(false) }
    var planToDelete by remember { mutableStateOf<MealPlanItem?>(null) }

    val filteredPlans = if (selectedDayFilter == "Todos") {
        mealPlans
    } else {
        mealPlans.filter { it.dayOfWeek.equals(selectedDayFilter, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("meal_calendar_screen")
    ) {
        // Header
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Cardápio Semanal",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryGreen
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Planeje suas refeições e evite compras por impulso.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondaryLight
                    )
                }

                Button(
                    onClick = { showAddDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .sizeIn(minHeight = 48.dp)
                        .testTag("btn_add_meal_plan")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Adicionar", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Days Filter with minimum 48dp height
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(daysOfWeek) { day ->
                val isSelected = selectedDayFilter == day
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedDayFilter = day },
                    label = {
                        Text(
                            text = day,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
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

        Spacer(modifier = Modifier.height(10.dp))

        if (filteredPlans.isEmpty()) {
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
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = PrimaryGreen,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Nenhuma refeição em $selectedDayFilter",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = PrimaryGreen,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Abra qualquer receita para agendá-la na semana ou toque no botão 'Adicionar' acima para planejar pratos caseiros.",
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
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(filteredPlans, key = { it.id }) { plan ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MintContainer
                                    ) {
                                        Text(
                                            text = plan.dayOfWeek,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = OnMintContainer,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = TerracottaContainer
                                    ) {
                                        Text(
                                            text = plan.mealType.displayName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = OnTerracottaContainer,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = plan.recipeTitle,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryGreen
                                )

                                if (plan.notes.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = plan.notes,
                                        fontSize = 13.sp,
                                        color = TextSecondaryLight
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Sync to device Calendar intent
                                IconButton(
                                    onClick = { syncWithDeviceCalendar(context, plan) },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .testTag("btn_sync_calendar_${plan.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Sync,
                                        contentDescription = "Sincronizar com Agenda do Celular",
                                        tint = PrimaryGreen,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                IconButton(
                                    onClick = { planToDelete = plan },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .testTag("btn_delete_calendar_${plan.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Excluir agendamento",
                                        tint = TextSecondaryLight,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Confirmation dialog before deleting a meal plan (Rule 5: Irreversible action)
    planToDelete?.let { plan ->
        AlertDialog(
            onDismissRequest = { planToDelete = null },
            title = {
                Text("Remover do Cardápio?", fontWeight = FontWeight.Bold, color = PrimaryGreen)
            },
            text = {
                Text(
                    "Deseja remover \"${plan.recipeTitle}\" (${plan.dayOfWeek} - ${plan.mealType.displayName}) do seu cardápio?",
                    color = TextPrimaryLight,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteMealPlan(plan.id)
                        planToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UrgentRed)
                ) {
                    Text("Remover", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { planToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showAddDialog) {
        var mealTitle by remember { mutableStateOf("") }
        var dayChoice by remember { mutableStateOf("Hoje") }
        var mealChoice by remember { mutableStateOf(MealType.ALMOCO) }
        var notesInput by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text("Novo Agendamento", fontWeight = FontWeight.Bold, color = PrimaryGreen)
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = mealTitle,
                        onValueChange = { mealTitle = it },
                        label = { Text("Nome da Refeição") },
                        placeholder = { Text("Ex: Arroz de Forno com Sobras", color = TextSecondaryLight) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = notesInput,
                        onValueChange = { notesInput = it },
                        label = { Text("Observações (opcional)") },
                        placeholder = { Text("Ex: Tirar o frango para descongelar", color = TextSecondaryLight) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (mealTitle.isNotBlank()) {
                            onAddMealPlan(mealTitle.trim(), dayChoice, mealChoice, notesInput.trim())
                            showAddDialog = false
                        }
                    },
                    enabled = mealTitle.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("Salvar", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

private fun syncWithDeviceCalendar(context: Context, plan: MealPlanItem) {
    try {
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.Events.TITLE, "${plan.mealType.displayName}: ${plan.recipeTitle}")
            .putExtra(CalendarContract.Events.DESCRIPTION, "Refeição planejada pelo SobraChef.\n${plan.notes}")
            .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
