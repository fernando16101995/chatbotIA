package com.example.chatbotia.interfaz.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.chatbotia.data.model.DashboardMetrics
import com.example.chatbotia.interfaz.theme.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    token: String,
    onBackClick: () -> Unit
) {
    val metricsState = viewModel.dashboardMetrics.observeAsState()
    val isLoading = viewModel.isLoading.observeAsState(initial = false)
    val errorMessage = viewModel.errorMessage.observeAsState(initial = "")

    LaunchedEffect(token) {
        if (metricsState.value == null) {
            viewModel.loadDashboardMetrics(token)
        }
    }

    Scaffold(
        containerColor = BgDark,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Panel de Administración",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgDark)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val currentMetrics = metricsState.value
            when {
                isLoading.value -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = AccentViolet
                    )
                }
                errorMessage.value.isNotEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Ocurrió un error",
                            style = MaterialTheme.typography.titleMedium,
                            color = ErrorRed
                        )
                        Text(text = errorMessage.value, color = TextSecondary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.loadDashboardMetrics(token) },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentViolet)
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
                currentMetrics != null -> {
                    DashboardContent(metrics = currentMetrics)
                }
                else -> {
                    Text(
                        text = "No hay datos disponibles",
                        modifier = Modifier.align(Alignment.Center),
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardContent(metrics: DashboardMetrics) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MetricCard(
            title = "Gestión de Usuarios",
            items = listOf(
                "Total de registrados: ${metrics.users.total}",
                "Usuarios activos: ${metrics.users.active}",
                "Casos de alto riesgo: ${metrics.users.requiring_attention}",
                "Nuevos (semana): ${metrics.users.new_this_week}"
            )
        )
        MetricCard(
            title = "Actividad de Chat",
            items = listOf(
                "Mensajes totales: ${metrics.messages.total}",
                "Esta semana: ${metrics.messages.this_week}",
                "Promedio diario por usuario: ${String.format(Locale.getDefault(), "%.2f", metrics.messages.avg_per_user)}"
            )
        )
        MetricCard(
            title = "Diagnósticos PHQ-9",
            items = listOf(
                "Evaluaciones realizadas: ${metrics.phq9_assessments.total}",
                "Esta semana: ${metrics.phq9_assessments.this_week}",
                "Puntuación media: ${String.format(Locale.getDefault(), "%.2f", metrics.phq9_assessments.avg_score)}",
                "Puntuación máxima: ${metrics.phq9_assessments.max_score}"
            )
        )
        MetricCard(
            title = "Análisis de Depresión",
            items = listOf(
                "Detecciones: ${metrics.depression_detections.total}",
                "Casos positivos: ${metrics.depression_detections.positive}",
                "Tasa: ${metrics.depression_detections.positive_rate}"
            )
        )
        MetricCard(
            title = "Entrevistas Conversacionales",
            items = listOf(
                "Total iniciadas: ${metrics.conversational_assessments.total}",
                "Finalizadas: ${metrics.conversational_assessments.completed}",
                "En progreso: ${metrics.conversational_assessments.in_progress}"
            )
        )
    }
}

@Composable
fun MetricCard(title: String, items: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AccentViolet
            )
            HorizontalDivider(color = BorderSubtle)
            items.forEach { item ->
                Text(text = item, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }
        }
    }
}
