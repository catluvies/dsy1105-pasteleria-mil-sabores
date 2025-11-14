package cl.duoc.dsy1105.pasteleriamilsabores.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.dsy1105.pasteleriamilsabores.model.User
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.RegisterViewModel
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.RegistrationEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit,
    onBackToCatalog: () -> Unit,
    onRegisterSuccess: (User) -> Unit,
    registerViewModel: RegisterViewModel = viewModel()
) {
    val uiState by registerViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        registerViewModel.registrationEvent.collectLatest { event ->
            when (event) {
                is RegistrationEvent.Success -> {
                    onRegisterSuccess(event.user)
                }
                is RegistrationEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .padding(all = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            Text(
                "🧁",
                fontSize = 36.sp
            )
            Text(
                text = "Únete a Pastelería Mil Sabores",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
            )

            OutlinedTextField(
                value = uiState.fullName,
                onValueChange = registerViewModel::onFullNameChange,
                label = { Text("Nombre Completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errors.fullName != null,
                supportingText = {
                    uiState.errors.fullName?.let { Text(it) }
                }
            )

            OutlinedTextField(
                value = uiState.email,
                onValueChange = registerViewModel::onEmailChange,
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errors.email != null,
                supportingText = {
                    uiState.errors.email?.let { Text(it) }
                }
            )

            OutlinedTextField(
                value = uiState.password,
                onValueChange = registerViewModel::onPasswordChange,
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errors.password != null,
                supportingText = {
                    uiState.errors.password?.let { Text(it) }
                }
            )

            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = registerViewModel::onConfirmPasswordChange,
                label = { Text("Confirmar Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errors.confirmPassword != null,
                supportingText = {
                    uiState.errors.confirmPassword?.let { Text(it) }
                }
            )

            Button(
                onClick = registerViewModel::register,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }

            TextButton(onClick = onLoginClick) {
                Text("¿Ya tienes cuenta? Inicia Sesión")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onBackToCatalog,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Volver al Catálogo",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}