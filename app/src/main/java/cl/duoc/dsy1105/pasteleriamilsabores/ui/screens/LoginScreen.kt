package cl.duoc.dsy1105.pasteleriamilsabores.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.dsy1105.pasteleriamilsabores.model.User
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.LoginEvent
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onRegisterClick: () -> Unit,
    onBackToCatalog: () -> Unit,
    onLoginSuccess: (User) -> Unit
) {
    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        loginViewModel.loginEvent.collectLatest { event ->
            when (event) {
                is LoginEvent.Success -> {
                    Toast.makeText(context, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
                    onLoginSuccess(event.user)
                }
                is LoginEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Iniciar Sesión") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 20.dp)
        ) {
            Text(
                "🧁",
                fontSize = 48.sp
            )
            Text(
                "Pastelería Mil Sabores",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            Text("Bienvenido", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)

            OutlinedTextField(
                value = uiState.email,
                onValueChange = loginViewModel::onEmailChange,
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.password,
                onValueChange = loginViewModel::onPasswordChange,
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = loginViewModel::login,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }
            TextButton(onClick = onRegisterClick) {
                Text("¿No tienes cuenta? Regístrate")
            }

            Spacer(modifier = Modifier.weight(1f))

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