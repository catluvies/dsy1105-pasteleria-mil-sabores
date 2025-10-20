package cl.duoc.dsy1105.pasteleriamilsabores.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.UserSessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    userSessionViewModel: UserSessionViewModel,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    onAdminPanelClick: () -> Unit  // ← NUEVO PARÁMETRO
) {
    val user by userSessionViewModel.currentUserState.collectAsStateWithLifecycle()
    val currentUser = user

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (currentUser != null) {
            var address by remember(currentUser.address) { mutableStateOf(currentUser.address) }
            var phone by remember(currentUser.phone) { mutableStateOf(currentUser.phone) }
            val userHasData = currentUser.address.isNotBlank() && currentUser.phone.isNotBlank()
            var isEditing by remember { mutableStateOf(!userHasData) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                OutlinedTextField(
                    value = currentUser.fullName,
                    onValueChange = {},
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
                OutlinedTextField(
                    value = currentUser.email,
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

                Spacer(modifier = Modifier.weight(1f))

                // ============ NUEVO: Botón Panel de Administración ============
                if (currentUser.isAdmin) {
                    Button(
                        onClick = onAdminPanelClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8B4513)
                        )
                    ) {
                        Text("Panel de Administración", style = MaterialTheme.typography.titleMedium)
                    }
                }
                // ==============================================================

                Button(
                    onClick = {
                        if (isEditing) {
                            userSessionViewModel.updateUserData(address, phone)
                        }
                        isEditing = !isEditing
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val buttonText = if (isEditing) "Guardar Cambios" else "Editar"
                    Text(buttonText, style = MaterialTheme.typography.titleMedium)
                }

                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar Sesión")
                }
            }
        }
    }
}