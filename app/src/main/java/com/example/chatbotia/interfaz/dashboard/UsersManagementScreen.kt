package com.example.chatbotia.interfaz.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.chatbotia.data.model.UpdateUserRequest
import com.example.chatbotia.data.model.UserItem
import com.example.chatbotia.interfaz.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersManagementScreen(
    viewModel: DashboardViewModel,
    token: String,
    onBackClick: () -> Unit
) {
    val usersListState = viewModel.usersList.observeAsState()
    val isLoading = viewModel.isLoading.observeAsState(false)
    val operationResult = viewModel.operationResult.observeAsState("")

    var userToDelete by remember { mutableStateOf<UserItem?>(null) }
    var userToEdit by remember { mutableStateOf<UserItem?>(null) }

    LaunchedEffect(token) {
        viewModel.loadUsersList(token)
    }

    // Snackbar cuando hay resultado de operación
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(operationResult.value) {
        if (operationResult.value.isNotEmpty()) {
            snackbarHostState.showSnackbar(operationResult.value)
            viewModel.loadUsersList(token)
        }
    }

    Scaffold(
        containerColor = BgDark,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Usuarios", color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgDark)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                isLoading.value -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AccentViolet
                )
                usersListState.value != null -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(usersListState.value!!.users) { user ->
                            UserItemCard(
                                user = user,
                                onEditClick = { userToEdit = user },
                                onDeleteClick = { userToDelete = user }
                            )
                        }
                    }
                }
                else -> Text(
                    "No hay usuarios",
                    modifier = Modifier.align(Alignment.Center),
                    color = TextSecondary
                )
            }
        }
    }

    // Diálogo de confirmación para eliminar
    userToDelete?.let { user ->
        AlertDialog(
            onDismissRequest = { userToDelete = null },
            containerColor = SurfaceDark,
            title = { Text("Eliminar usuario", color = TextPrimary) },
            text = { Text("¿Seguro que deseas eliminar a ${user.email}?", color = TextSecondary) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteUser(token, user.id) { userToDelete = null }
                    userToDelete = null
                }) { Text("Eliminar", color = ErrorRed) }
            },
            dismissButton = {
                TextButton(onClick = { userToDelete = null }) {
                    Text("Cancelar", color = TextSecondary)
                }
            }
        )
    }

    // Diálogo de edición
    userToEdit?.let { user ->
        EditUserDialog(
            user = user,
            onDismiss = { userToEdit = null },
            onConfirm = { request ->
                viewModel.updateUser(token, user.id, request) { userToEdit = null }
                userToEdit = null
            }
        )
    }
}

@Composable
fun UserItemCard(
    user: UserItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(user.email, color = TextPrimary, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Activo: ${user.is_active}  |  Admin: ${user.is_admin}  |  Riesgo: ${user.risk_level}",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = AccentViolet)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = ErrorRed)
            }
        }
    }
}

@Composable
fun EditUserDialog(
    user: UserItem,
    onDismiss: () -> Unit,
    onConfirm: (UpdateUserRequest) -> Unit
) {
    var email by remember { mutableStateOf(user.email) }
    var isActive by remember { mutableStateOf(user.is_active) }
    var isAdmin by remember { mutableStateOf(user.is_admin) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        title = { Text("Editar usuario", color = TextPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = TextSecondary) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AccentViolet,
                        unfocusedBorderColor = BorderSubtle
                    )
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isActive,
                        onCheckedChange = { isActive = it },
                        colors = CheckboxDefaults.colors(checkedColor = AccentViolet)
                    )
                    Text("Activo", color = TextSecondary)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isAdmin,
                        onCheckedChange = { isAdmin = it },
                        colors = CheckboxDefaults.colors(checkedColor = AccentViolet)
                    )
                    Text("Administrador", color = TextSecondary)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(UpdateUserRequest(
                    email = email.takeIf { it != user.email },
                    is_active = isActive.takeIf { it != user.is_active },
                    is_admin = isAdmin.takeIf { it != user.is_admin }
                ))
            }) { Text("Guardar", color = AccentViolet) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextSecondary)
            }
        }
    )
}