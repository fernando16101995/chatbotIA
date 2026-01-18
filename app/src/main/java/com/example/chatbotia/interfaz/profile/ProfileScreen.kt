package com.example.chatbotia.interfaz.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatbotia.interfaz.ViewModelFactory
import com.example.chatbotia.interfaz.background.AnimatedRandomPaintBackground
import com.example.chatbotia.interfaz.theme.VioletPrimary
import com.example.chatbotia.interfaz.theme.YellowAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: ProfileViewModel = viewModel(factory = ViewModelFactory(context))
    val summary = viewModel.summary

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedRandomPaintBackground()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Mi Perfil", color = Color.White, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, "Volver", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.logout(onLogout) }) {
                            Icon(Icons.Default.ExitToApp, "Cerrar sesión", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = VioletPrimary.copy(alpha = 0.9f)
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(100.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.padding(20.dp),
                        tint = Color.White
                    )
                }

                Text(
                    text = "Resumen de Bienestar",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                if (viewModel.isLoading) {
                    CircularProgressIndicator(color = YellowAccent)
                } else if (summary != null) {
                    // Tarjetas de estadísticas con los campos reales del servidor
                    StatCard(
                        title = "Evaluaciones PHQ-9",
                        value = summary.totalAssessments.toString(),
                        icon = Icons.Default.Info
                    )
                    StatCard(
                        title = "Detecciones de ánimo",
                        value = summary.depressionDetections.toString(),
                        icon = Icons.Default.Info,
                        color = if (summary.depressionDetections > 5) Color(0xFFFFCDD2) else Color.White
                    )
                    
                    val riskLevel = summary.currentRisk ?: "minimal"
                    StatCard(
                        title = "Nivel de riesgo",
                        value = riskLevel.uppercase(),
                        icon = Icons.Default.Info,
                        color = when(riskLevel.lowercase()) {
                            "high", "alto" -> Color(0xFFFF8A80)
                            "moderate", "moderado" -> Color(0xFFFFD180)
                            "minimal", "bajo" -> Color(0xFFB9F6CA)
                            else -> Color.White
                        }
                    )
                } else {
                    Text(
                        text = viewModel.errorMessage ?: "No hay datos disponibles aún",
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color = Color.White
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.9f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = VioletPrimary)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontSize = 14.sp, color = Color.Gray)
                Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = VioletPrimary)
            }
        }
    }
}
