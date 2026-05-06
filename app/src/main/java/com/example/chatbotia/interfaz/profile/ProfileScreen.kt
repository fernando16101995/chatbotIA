package com.example.chatbotia.interfaz.profile

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    onBack: () -> Unit = {},
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: ProfileViewModel = viewModel(factory = ViewModelFactory(context))
    val summary = viewModel.summary
    val scrollState = rememberScrollState()

    val initial = "S"

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Perfil",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.logout(onLogout) }) {
                        Icon(Icons.Default.ExitToApp, "Cerrar sesión", tint = TextSecondary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBackground)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Avatar circular con inicial
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(AccentPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initial,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Text(
                text = "Mi cuenta",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Resumen de bienestar",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.align(Alignment.Start)
            )

            if (viewModel.isLoading) {
                repeat(3) { ShimmerCard() }
            } else if (summary != null) {
                ProfileStatCard(
                    title = "Evaluaciones PHQ-9",
                    value = summary.totalPhq9Assessments.toString(),
                    icon = Icons.Default.Info
                )
                ProfileStatCard(
                    title = "Detecciones de ánimo",
                    value = summary.depressionDetectionCount.toString(),
                    icon = Icons.Default.Info,
                    highlight = summary.depressionDetectionCount > 5
                )
                ProfileStatCard(
                    title = "Nivel de riesgo",
                    value = summary.overallRiskLevel.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }.ifEmpty { "—" },
                    icon = Icons.Default.Info,
                    riskLevel = summary.overallRiskLevel
                )
            } else {
                Text(
                    text = viewModel.errorMessage ?: "No hay datos disponibles aún",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { viewModel.logout(onLogout) },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorColor),
                border = BorderStroke(1.dp, ErrorColor.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(Icons.Default.ExitToApp, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar sesión", style = MaterialTheme.typography.labelLarge)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ProfileStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    highlight: Boolean = false,
    riskLevel: String? = null
) {
    val valueColor = when {
        riskLevel != null -> when (riskLevel.lowercase()) {
            "critical", "severe" -> ErrorColor
            "moderate" -> androidx.compose.ui.graphics.Color(0xFFFFD180)
            "mild" -> androidx.compose.ui.graphics.Color(0xFFFFFF8D)
            else -> SuccessColor
        }
        highlight -> ErrorColor
        else -> TextPrimary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppSurface),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = AccentPrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Text(value, style = MaterialTheme.typography.titleMedium, color = valueColor, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun ShimmerCard() {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val alpha by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "shimmerAlpha"
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(AppSurface.copy(alpha = alpha + 0.5f))
    )
}
