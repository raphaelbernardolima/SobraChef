package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MintContainer
import com.example.ui.theme.OnMintContainer
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.Terracotta
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PhotoScanDialog(
    isScanning: Boolean,
    detectedItems: List<String>,
    onDismiss: () -> Unit,
    onPhotoCaptured: (Bitmap) -> Unit,
    onAddItemsToFridge: (List<String>) -> Unit
) {
    val context = LocalContext.current
    val selectedItems = remember(detectedItems) {
        mutableStateListOf<String>().apply { addAll(detectedItems) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let { onPhotoCaptured(it) }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val bitmap = if (Build.VERSION.SDK_INT < 28) {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                }
                onPhotoCaptured(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Foto da Geladeira ou Dispensa",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = PrimaryGreen
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("close_scan_dialog")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Fechar")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Aponte a câmera para os alimentos que sobraram. O SobraChef identifica os itens para sugerir pratos zero desperdício.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondaryLight,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons to take photo or pick (min 48dp height)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { cameraLauncher.launch(null) },
                        modifier = Modifier
                            .weight(1f)
                            .sizeIn(minHeight = 48.dp)
                            .testTag("btn_take_camera_photo"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Tirar Foto", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            galleryLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .sizeIn(minHeight = 48.dp)
                            .testTag("btn_pick_gallery_photo")
                    ) {
                        Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Galeria", fontWeight = FontWeight.Bold, color = PrimaryGreen)
                    }
                }

                if (isScanning) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = PrimaryGreen)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Analisando ingredientes na geladeira...",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryGreen
                        )
                    }
                } else if (detectedItems.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Ingredientes Detectados (${selectedItems.size} selecionados):",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = PrimaryGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        detectedItems.forEach { item ->
                            val isSelected = selectedItems.contains(item)
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) MintContainer else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable {
                                        if (isSelected) selectedItems.remove(item) else selectedItems.add(item)
                                    }
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) PrimaryGreen else Color.Transparent,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = PrimaryGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    Text(
                                        text = item,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) OnMintContainer else TextPrimaryLight
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                    // Quick suggested leftovers tags
                    Text(
                        text = "Sobras mais comuns para adicionar rápido:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondaryLight
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val commonLeftovers = listOf("Arroz cozido", "Ovos", "Tomate", "Cenoura", "Queijo", "Cebola", "Batata")
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        commonLeftovers.forEach { item ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MintContainer,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable {
                                        onAddItemsToFridge(listOf(item))
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = PrimaryGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = item,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = OnMintContainer
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (selectedItems.isNotEmpty()) {
                Button(
                    onClick = {
                        onAddItemsToFridge(selectedItems.toList())
                        onDismiss()
                    },
                    modifier = Modifier
                        .sizeIn(minHeight = 48.dp)
                        .testTag("btn_confirm_add_scanned"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Terracotta,
                        contentColor = Color.White
                    )
                ) {
                    Text("Adicionar ${selectedItems.size} à Geladeira", fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.sizeIn(minHeight = 48.dp)
            ) {
                Text("Cancelar")
            }
        }
    )
}
