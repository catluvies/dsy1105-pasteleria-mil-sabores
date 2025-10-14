package cl.duoc.dsy1105.pasteleriamilsabores.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.dsy1105.pasteleriamilsabores.model.User
import cl.duoc.dsy1105.pasteleriamilsabores.ui.theme.PasteleriaMilSaboresTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    // ----- PUNTO DE CONEXIÓN FUTURO #3: ENTRADA DE DATOS REALES -----
    // En la aplicación real, este objeto 'user' no vendrá de 'sampleUser',
    // sino que será pasado a través del sistema de navegación después
    // de que el usuario inicie sesión o se registre.
    user: User
) {

    var address by remember { mutableStateOf(user.address) }
    var phone by remember { mutableStateOf(user.phone) }

    val userHasData = user.address.isNotBlank() && user.phone.isNotBlank()
    var isEditing by remember { mutableStateOf(!userHasData) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi Perfil",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        // ----- PUNTO DE CONEXIÓN FUTURO #2: ACCIÓN DE LA FLECHA ATRÁS -----
                        // Aquí se conectará la lógica de navegación para volver a la
                        // pantalla anterior (probablemente el catálogo).
                        // Ejemplo: navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            OutlinedTextField(
                value = user.fullName,
                onValueChange = {},
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            OutlinedTextField(
                value = user.email,
                onValueChange = {},
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Tu Dirección de Envío") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth(),
                readOnly = !isEditing
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Tu Número de Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                readOnly = !isEditing
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isEditing) {

                        // Justo aquí, antes de cambiar el estado de la UI, se debe
                        // llamar a la lógica de negocio para guardar los datos

                    }
                    // Se invierte el estado de edición para cambiar la UI
                    isEditing = !isEditing
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                val buttonText = if (isEditing) "Guardar Cambios" else "Editar"
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


@Preview(showBackground = true, name = "Usuario Nuevo Sin Datos")
@Composable
fun UserProfileScreenPreview_NewUser() {
    PasteleriaMilSaboresTheme {
        val newUser = User(1, "Usuario Nuevo", "nuevo@email.com", "", "")
        UserProfileScreen(user = newUser)
    }
}

@Preview(showBackground = true, name = "Usuario Existente Con Datos")
@Composable
fun UserProfileScreenPreview_ExistingUser() {
    PasteleriaMilSaboresTheme {
        val existingUser = User(1, "Ana López", "ana@email.com", "Avenida Siempre Viva 742", "+56912345678")
        UserProfileScreen(user = existingUser)
    }
}