package com.geopulse.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geopulse.app.data.AppState
import com.geopulse.app.data.ComfortPreset
import com.geopulse.app.ui.theme.YellowAccent
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComfortProfileScreen(
    appState: AppState,
    onNavigateToResults: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var showAdvanced by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Comfort Profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color.Black
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Pick a simple preset",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Text(
                        text = "These map to temperature / rain / wind thresholds behind the scenes.",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.height(600.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(appState.presets) { preset ->
                        PresetCard(
                            preset = preset,
                            isSelected = appState.selectedPreset == preset,
                            onClick = {
                                appState.selectedPreset = preset
                                appState.customMaxHot = null
                                appState.customMinCold = null
                                appState.customMaxRain = null
                                appState.customMaxWind = null
                            }
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.1f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showAdvanced = !showAdvanced },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = null,
                                    tint = YellowAccent
                                )
                                Text(
                                    text = "Advanced (optional)",
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                            Icon(
                                imageVector = if (showAdvanced) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.7f)
                            )
                        }

                        if (showAdvanced) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Tweak thresholds to your liking.",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                SliderControl(
                                    title = "Too hot >",
                                    unit = "°C",
                                    value = appState.customMaxHot
                                        ?: appState.selectedPreset?.maxHotC ?: 30.0,
                                    range = 20f..45f,
                                    onValueChange = { appState.customMaxHot = it.toDouble() }
                                )

                                SliderControl(
                                    title = "Too cold <",
                                    unit = "°C",
                                    value = appState.customMinCold
                                        ?: appState.selectedPreset?.minColdC ?: 10.0,
                                    range = -5f..20f,
                                    onValueChange = { appState.customMinCold = it.toDouble() }
                                )

                                SliderControl(
                                    title = "Rain ≥",
                                    unit = "mm",
                                    value = appState.customMaxRain
                                        ?: appState.selectedPreset?.maxRainMM ?: 10.0,
                                    range = 1f..50f,
                                    onValueChange = { appState.customMaxRain = it.toDouble() }
                                )

                                SliderControl(
                                    title = "Wind ≥",
                                    unit = "km/h",
                                    value = appState.customMaxWind
                                        ?: appState.selectedPreset?.maxWindKPH ?: 30.0,
                                    range = 5f..80f,
                                    onValueChange = { appState.customMaxWind = it.toDouble() }
                                )
                            }
                        }
                    }
                }
            }

            item {
                val hasCustoms = listOf(
                    appState.customMaxHot,
                    appState.customMinCold,
                    appState.customMaxRain,
                    appState.customMaxWind
                ).any { it != null }

                Button(
                    onClick = onNavigateToResults,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = appState.selectedPreset != null || hasCustoms,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.15f),
                        contentColor = YellowAccent,
                        disabledContainerColor = Color.White.copy(alpha = 0.05f),
                        disabledContentColor = YellowAccent.copy(alpha = 0.3f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "View Weather Odds",
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PresetCard(
    preset: ComfortPreset,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) YellowAccent.copy(alpha = 0.16f) else Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = preset.emoji, fontSize = 20.sp)
                    Text(
                        text = preset.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = YellowAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Text(
                text = preset.description,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f)
            )

            HorizontalDivider(color = Color.White.copy(alpha = 0.3f))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    preset.maxHotC?.let {
                        ThresholdTag("Hot > ${it.toInt()}°C")
                    }
                    preset.minColdC?.let {
                        ThresholdTag("Cold < ${it.toInt()}°C")
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    preset.maxRainMM?.let {
                        ThresholdTag("Rain ≥ ${it.toInt()}mm")
                    }
                    preset.maxWindKPH?.let {
                        ThresholdTag("Wind ≥ ${it.toInt()}km/h")
                    }
                }
            }
        }
    }
}

@Composable
fun ThresholdTag(text: String) {
    Text(
        text = text,
        fontSize = 10.sp,
        color = Color.White.copy(alpha = 0.8f),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .padding(horizontal = 6.dp, vertical = 4.dp)
    )
}

@Composable
fun SliderControl(
    title: String,
    unit: String,
    value: Double,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "$title ${value.roundToInt()}$unit",
                fontSize = 14.sp,
                color = Color.White
            )
            Slider(
                value = value.toFloat(),
                onValueChange = onValueChange,
                valueRange = range,
                steps = ((range.endInclusive - range.start).toInt() - 1).coerceAtLeast(0),
                colors = SliderDefaults.colors(
                    thumbColor = YellowAccent,
                    activeTrackColor = YellowAccent,
                    inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                )
            )
        }
    }
}
