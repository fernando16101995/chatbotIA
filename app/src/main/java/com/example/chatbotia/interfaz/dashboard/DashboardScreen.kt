package com.example.chatbotia.interfaz.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chatbotia.data.model.DashboardMetrics
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    token: String,
    onBackClick: () -> Unit
) {
    // Observamos los estados del ViewModel
    val metricsState = viewModel.dashboardMetrics.observeAsState()
    val isLoading = viewModel.isLoading.observeAsState(initial = false)
    val errorMessage = viewModel.errorMessage.observeAsState(initial = "")

    // Cargar datos al entrar o si el token cambia
    LaunchedEffect(token) {
        if (metricsState.value == null) {
            viewModel.loadDashboardMetrics(token)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Panel de Administración", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
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
                        modifier = Modifier.align(Alignment.Center)
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
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(text = errorMessage.value)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadDashboardMetrics(token) }) {
                            Text("Reintentar")
                        }
                    }
                }
                currentMetrics != null -> {
                    DashboardContent(metrics = currentMetrics)
                }
                else -> {
                    // Si no está cargando, no hay error, pero metrics es null
                    Text(
                        text = "No hay datos disponibles",
                        modifier = Modifier.align(Alignment.Center)
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Card Usuarios
        MetricCard(
            title = "👥 Gestión de Usuarios",
            items = listOf(
                "Total de registrados: ${metrics.users.total}",
                "Usuarios activos: ${metrics.users.active}",
                "Casos de alto riesgo: ${metrics.users.requiring_attention}",
                "Nuevos ingresos (semana): ${metrics.users.new_this_week}"
            )
        )

        // Card Mensajes
        MetricCard(
            title = "💬 Actividad de Chat",
            items = listOf(
                "Mensajes totales: ${metrics.messages.total}",
                "Interacciones esta semana: ${metrics.messages.this_week}",
                "Promedio diario por usuario: ${String.format("%.2f", metrics.messages.avg_per_user)}"
            )
        )

        // Card PHQ-9
        MetricCard(
            title = "📋 Diagnósticos PHQ-9",
            items = listOf(
                "Evaluaciones realizadas: ${metrics.phq9_assessments.total}",
                "Completadas esta semana: ${metrics.phq9_assessments.this_week}",
                "Puntuación media: ${String.format("%.2f", metrics.phq9_assessments.avg_score)}",
                "Puntuación máxima detectada: ${metrics.phq9_assessments.max_score}"
            )
        )

        // Card Depresión
        MetricCard(
            title = "😞 Análisis de Depresión",
            items = listOf(
                "Detecciones automáticas: ${metrics.depression_detections.total}",
                "Casos positivos: ${metrics.depression_detections.positive}",
                "Tasa de detección: ${metrics.depression_detections.positive_rate}"
            )
        )

        // Card Evaluaciones Conversacionales
        MetricCard(
            title = "🗣️ Entrevistas Conversacionales",
            items = listOf(
                "Total iniciadas: ${metrics.conversational_assessments.total}",
                "Finalizadas con éxito: ${metrics.conversational_assessments.completed}",
                "Abandonos / En progreso: ${metrics.conversational_assessments.in_progress}"
            )
        )
    }
}

@Composable
fun MetricCard(title: String, items: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            items.forEach { item ->
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


