package com.example.chatbotia.interfaz.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbotia.interfaz.ViewModelFactory
import com.example.chatbotia.interfaz.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: ProfileViewModel = viewModel(factory = ViewModelFactory(context))
    val summary = viewModel.summary

    Scaffold(
        containerColor = BgDark,
        topBar = {
            TopAppBar(
                title = { Text("Perfil", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = TextPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.logout(onLogout) }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, "Cerrar sesión", tint = ErrorRed)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgDark)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Avatar minimalista con inicial
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(SurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "U", // Aquí podría ir la inicial del nombre real
                    style = MaterialTheme.typography.headlineLarge,
                    color = AccentViolet
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Resumen de Bienestar",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )
                Text(
                    text = "Estadísticas de tu camino emocional",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            if (viewModel.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AccentViolet, strokeWidth = 2.dp)
                }
            } else if (summary != null) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCardMinimal(
                        title = "Evaluaciones PHQ-9",
                        value = summary.totalPhq9Assessments.toString(),
                        icon = Icons.Default.Info
                    )
                    StatCardMinimal(
                        title = "Detecciones de ánimo",
                        value = summary.depressionDetectionCount.toString(),
                        icon = Icons.Default.Info
                    )
                    
                    val riskLevel = summary.overallRiskLevel
                    StatCardMinimal(
                        title = "Nivel de riesgo",
                        value = riskLevel.uppercase(),
                        icon = Icons.Default.Info,
                        valueColor = when(riskLevel.lowercase()) {
                            "high", "alto" -> ErrorRed
                            "moderate", "moderado" -> Color(0xFFFFA726)
                            else -> Color(0xFF66BB6A)
                        }
                    )
                }
            } else {
                Text(
                    text = viewModel.errorMessage ?: "No hay datos disponibles",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun StatCardMinimal(
    title: String,
    value: String,
    icon: ImageVector,
    valueColor: Color = TextPrimary
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceDark,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                Text(value, style = MaterialTheme.typography.titleMedium, color = valueColor, fontWeight = FontWeight.Bold)
            }
        }
    }
}
