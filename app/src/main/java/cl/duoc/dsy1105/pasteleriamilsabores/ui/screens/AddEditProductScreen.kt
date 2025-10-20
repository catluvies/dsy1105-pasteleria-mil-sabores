package cl.duoc.dsy1105.pasteleriamilsabores.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.ProductViewModel
import coil.compose.AsyncImage
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

    var name by remember { mutableStateOf(existingProduct?.name ?: "") }
    var price by remember { mutableStateOf(existingProduct?.price?.toString() ?: "") }
    var description by remember { mutableStateOf(existingProduct?.description ?: "") }
    var selectedImageResId by remember { mutableStateOf(existingProduct?.imageResId) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val isEditing = existingProduct != null
    val title = if (isEditing) "Editar Producto" else "Agregar Producto"

    // Launcher para seleccionar imagen desde galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            selectedImageResId = null // Limpiamos el resource ID porque ahora usamos URI
        }
    }

    // Crear archivo temporal para la foto
    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.cacheDir
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    // Launcher para tomar foto con cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri?.let {
                selectedImageUri = it
                selectedImageResId = null
            }
        }
    }

    // Launcher para permisos de cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Crear archivo y lanzar cámara
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFF8E1)
                )
            )
        },
        containerColor = Color(0xFFFFF8E1)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sección de imagen
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
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
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Color(0xFF5D4037)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Preview de la imagen
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .border(2.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            // Si hay URI seleccionada (de galería o cámara)
                            selectedImageUri != null -> {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "Imagen seleccionada",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            // Si hay un imageResId (producto existente)
                            selectedImageResId != null -> {
                                Image(
                                    painter = painterResource(id = selectedImageResId!!),
                                    contentDescription = "Imagen del producto",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            // Placeholder cuando no hay imagen
                            else -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Upload,
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp),
                                        tint = Color(0xFFBDBDBD)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Sube o toma una foto del producto",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF9E9E9E)
                                    )
                                    Text(
                                        text = "PNG, JPG hasta 5MB",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFBDBDBD)
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
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF8B4513)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Upload,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Subir desde galería")
                        }

                        OutlinedButton(
                            onClick = {
                                // Verificar permiso de cámara
                                when (PackageManager.PERMISSION_GRANTED) {
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.CAMERA
                                    ) -> {
                                        // Permiso ya otorgado, abrir cámara
                                        val photoFile = createImageFile()
                                        val uri = FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.fileprovider",
                                            photoFile
                                        )
                                        photoUri = uri
                                        cameraLauncher.launch(uri)
                                    }
                                    else -> {
                                        // Solicitar permiso
                                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF8B4513)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Tomar foto")
                        }
                    }
                }
            }

            // Nombre del producto
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del producto") },
                placeholder = { Text("Ej: Pastel de Chocolate") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )

            // Precio
            OutlinedTextField(
                value = price,
                onValueChange = { if (it.all { char -> char.isDigit() }) price = it },
                label = { Text("Precio (CLP)") },
                placeholder = { Text("90000") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
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
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                maxLines = 4
            )

            // Mensaje de error
            if (showError) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botones
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
                        // Validación
                        when {
                            name.isBlank() -> {
                                showError = true
                                errorMessage = "El nombre es obligatorio"
                            }
                            price.isBlank() || price.toIntOrNull() == null -> {
                                showError = true
                                errorMessage = "El precio debe ser un número válido"
                            }
                            selectedImageResId == null && selectedImageUri == null && !isEditing -> {
                                showError = true
                                errorMessage = "Debes seleccionar una imagen"
                            }
                            else -> {
                                showError = false

                                // NOTA: Por ahora guardamos con imageResId = 0 si es URI
                                // En producción deberías subir la imagen a un servidor
                                val imageRes = selectedImageResId ?: existingProduct?.imageResId ?: 0

                                val productToSave = Product(
                                    id = existingProduct?.id ?: productViewModel.getNextProductId(),
                                    name = name,
                                    description = description,
                                    price = price.toInt(),
                                    imageResId = imageRes
                                )

                                if (isEditing) {
                                    productViewModel.updateProduct(productToSave)
                                } else {
                                    productViewModel.addProduct(productToSave)
                                }

                                onSaveSuccess()
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B4513)
                    )
                ) {
                    Text(if (isEditing) "Guardar Cambios" else "Guardar Producto")
                }
            }
        }
    }
}