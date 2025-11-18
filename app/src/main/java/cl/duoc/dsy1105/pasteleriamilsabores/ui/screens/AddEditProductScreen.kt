package cl.duoc.dsy1105.pasteleriamilsabores.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import cl.duoc.dsy1105.pasteleriamilsabores.R
import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.ProductViewModel
import cl.duoc.dsy1105.pasteleriamilsabores.utils.FirebaseStorageHelper
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    productViewModel: ProductViewModel,
    existingProduct: Product? = null,
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf(existingProduct?.name ?: "") }
    var price by remember { mutableStateOf(existingProduct?.price?.toString() ?: "") }
    var description by remember { mutableStateOf(existingProduct?.description ?: "") }
    var selectedImageUrl by remember { mutableStateOf(existingProduct?.imageUrl) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val isEditing = existingProduct != null
    val title = if (isEditing) "Editar Producto" else "Agregar Producto"

    // Galería (URI para preview)
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            selectedImageUrl = null
        }
    }

    // Archivo temporal para cámara
    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.cacheDir
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    // Cámara (URI para preview)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri?.let {
                selectedImageUri = it
                selectedImageUrl = null
            }
        }
    }

    // Permiso de cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val photoFile = createImageFile()
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                photoFile
            )
            photoUri = uri
            cameraLauncher.launch(uri)
        } else {
            showError = true
            errorMessage = "Se requiere permiso de cámara para tomar fotos"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Imagen
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Imagen del producto",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            selectedImageUri != null -> {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "Imagen seleccionada",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            selectedImageUrl != null -> {
                                AsyncImage(
                                    model = selectedImageUrl,
                                    contentDescription = "Imagen del producto",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    error = coil.compose.rememberAsyncImagePainter(R.drawable.torta_chocolate)
                                )
                            }
                            else -> {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.Upload,
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Sube o toma una foto del producto",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "PNG, JPG hasta 5MB",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { galleryLauncher.launch("image/*") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Subir desde galería")
                        }

                        OutlinedButton(
                            onClick = {
                                when (PackageManager.PERMISSION_GRANTED) {
                                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                                        val photoFile = createImageFile()
                                        val uri = FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.fileprovider",
                                            photoFile
                                        )
                                        photoUri = uri
                                        cameraLauncher.launch(uri)
                                    }
                                    else -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Tomar foto")
                        }
                    }
                }
            }

            // Nombre
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del producto") },
                placeholder = { Text("Ej: Pastel de Chocolate") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                singleLine = true
            )

            // Precio
            OutlinedTextField(
                value = price,
                onValueChange = { if (it.all { ch -> ch.isDigit() }) price = it },
                label = { Text("Precio (CLP)") },
                placeholder = { Text("90000") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                singleLine = true
            )

            // Descripción
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                placeholder = { Text("Describe el producto...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                maxLines = 4
            )

            if (showError) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        when {
                            name.isBlank() -> {
                                showError = true
                                errorMessage = "El nombre es obligatorio"
                            }
                            price.isBlank() || price.toIntOrNull() == null -> {
                                showError = true
                                errorMessage = "El precio debe ser un número válido"
                            }
                            selectedImageUrl == null && selectedImageUri == null && !isEditing -> {
                                showError = true
                                errorMessage = "Debes seleccionar una imagen"
                            }
                            else -> {
                                showError = false
                                isUploading = true

                                coroutineScope.launch {
                                    try {
                                        // Si hay una imagen nueva seleccionada, subirla a Firebase
                                        val imageUrl = if (selectedImageUri != null) {
                                            val uploadResult = FirebaseStorageHelper.uploadImage(selectedImageUri!!, context)
                                            if (uploadResult.isSuccess) {
                                                uploadResult.getOrNull()!!
                                            } else {
                                                throw uploadResult.exceptionOrNull() ?: Exception("Error al subir imagen")
                                            }
                                        } else {
                                            // Usar la URL existente o placeholder
                                            selectedImageUrl
                                                ?: existingProduct?.imageUrl
                                                ?: "https://via.placeholder.com/300/8B4513/FFFFFF?text=Sin+Imagen"
                                        }

                                        val productToSave = Product(
                                            id = existingProduct?.id ?: 0, // 0 = nuevo producto, backend genera el ID
                                            name = name,
                                            description = description,
                                            price = price.toInt(),
                                            imageUrl = imageUrl
                                        )

                                        if (isEditing) {
                                            productViewModel.updateProduct(productToSave)
                                        } else {
                                            productViewModel.addProduct(productToSave)
                                        }

                                        isUploading = false
                                        onSaveSuccess()
                                    } catch (e: Exception) {
                                        isUploading = false
                                        showError = true
                                        errorMessage = "Error al guardar: ${e.message}"
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isUploading
                ) {
                    if (isUploading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(if (isEditing) "Guardar Cambios" else "Guardar Producto")
                    }
                }
            }
        }
    }
}
