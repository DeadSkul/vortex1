package com.geopulse.app.data

import com.google.android.gms.maps.model.LatLng
import java.util.Date

data class ComfortPreset(
    val id: String,
    val name: String,
    val emoji: String,
    val description: String,
    val maxHotC: Double?,
    val minColdC: Double?,
    val maxRainMM: Double?,
    val maxWindKPH: Double?
)

data class OddsResult(
    val id: String,
    val label: String,
    val valuePercent: Double,
    val note: String,
    val systemIcon: String
)

data class WeatherSummary(
    val avgHighC: Double,
    val avgLowC: Double,
    val avgPrecipMM: Double,
    val avgWindKPH: Double
)

val defaultPresets = listOf(
    ComfortPreset(
        id = "warm_sunny",
        name = "Warm & Sunny",
        emoji = "☀️",
        description = "Prefer heat, avoid rain/wind.",
        maxHotC = 35.0,
        minColdC = 15.0,
        maxRainMM = 5.0,
        maxWindKPH = 25.0
    ),
    ComfortPreset(
        id = "mild_pleasant",
        name = "Mild & Pleasant",
        emoji = "🙂",
        description = "Comfortable temps, little rain.",
        maxHotC = 30.0,
        minColdC = 10.0,
        maxRainMM = 8.0,
        maxWindKPH = 30.0
    ),
    ComfortPreset(
        id = "cool_breezy",
        name = "Cool & Breezy",
        emoji = "🍃",
        description = "Cooler temps, OK with wind.",
        maxHotC = 25.0,
        minColdC = 5.0,
        maxRainMM = 10.0,
        maxWindKPH = 35.0
    )
)
