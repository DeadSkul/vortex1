package com.geopulse.app.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import java.util.Date

class AppState : ViewModel() {
    var coordinate by mutableStateOf<LatLng?>(null)
    var placename by mutableStateOf<String?>(null)
    var selectedDate by mutableStateOf(Date())
    var selectedPreset by mutableStateOf<ComfortPreset?>(null)

    var customMaxHot by mutableStateOf<Double?>(null)
    var customMinCold by mutableStateOf<Double?>(null)
    var customMaxRain by mutableStateOf<Double?>(null)
    var customMaxWind by mutableStateOf<Double?>(null)

    var results by mutableStateOf<List<OddsResult>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    var weatherSummary by mutableStateOf<WeatherSummary?>(null)

    val presets = defaultPresets
}
