package com.geopulse.app.api

import com.geopulse.app.data.OddsResult
import com.geopulse.app.data.WeatherSummary
import com.google.android.gms.maps.model.LatLng
import java.util.Calendar
import java.util.Date
import java.util.UUID

class NasaPowerService {
    private val api = NasaPowerApi.create()
    private val cache = mutableMapOf<String, PowerDailyResponse>()

    suspend fun fetchComputeWithSummary(
        coord: LatLng,
        date: Date,
        maxHotC: Double?,
        minColdC: Double?,
        maxRainMM: Double?,
        maxWindKPH: Double?
    ): Pair<List<OddsResult>, WeatherSummary?> {
        val key = "${String.format("%.4f", coord.latitude)},${String.format("%.4f", coord.longitude)}"

        val data = cache[key] ?: run {
            val response = api.getDailyData(
                longitude = coord.longitude,
                latitude = coord.latitude
            )
            cache[key] = response
            response
        }

        val params = data.properties.parameter
        val tmax = params.t2mMax ?: emptyMap()
        val tmin = params.t2mMin ?: emptyMap()
        val precip = params.prectotcorr ?: emptyMap()
        val ws = params.ws10m ?: emptyMap()

        val calendar = Calendar.getInstance()
        calendar.time = date
        val targetDOY = calendar.get(Calendar.DAY_OF_YEAR)

        data class Row(val tmax: Double, val tmin: Double, val p: Double, val windKph: Double)
        val rows = mutableListOf<Row>()

        for ((dateKey, tx) in tmax) {
            val doy = dateKeyToDayOfYear(dateKey)
            if (doy == targetDOY) {
                val tn = tmin[dateKey] ?: tx
                val p = precip[dateKey] ?: 0.0
                val windKph = (ws[dateKey] ?: 0.0) * 3.6
                rows.add(Row(tx, tn, p, windKph))
            }
        }

        val n = rows.size.coerceAtLeast(1)
        fun pct(count: Int): Double = (count.toDouble() / n.toDouble()) * 100.0

        val odds = mutableListOf<OddsResult>()

        maxHotC?.let { hot ->
            val count = rows.count { it.tmax > hot }
            odds.add(
                OddsResult(
                    id = UUID.randomUUID().toString(),
                    label = "Too hot > ${hot.toInt()} °C",
                    valuePercent = pct(count),
                    note = "Historical odds of hotter-than-threshold.",
                    systemIcon = "thermometer_sun"
                )
            )
        }

        minColdC?.let { cold ->
            val count = rows.count { it.tmin < cold }
            odds.add(
                OddsResult(
                    id = UUID.randomUUID().toString(),
                    label = "Too cold < ${cold.toInt()} °C",
                    valuePercent = pct(count),
                    note = "Historical odds of colder-than-threshold.",
                    systemIcon = "thermometer_snowflake"
                )
            )
        }

        maxRainMM?.let { rain ->
            val count = rows.count { it.p >= rain }
            odds.add(
                OddsResult(
                    id = UUID.randomUUID().toString(),
                    label = "Rain ≥ ${rain.toInt()} mm",
                    valuePercent = pct(count),
                    note = "Daily precipitation exceedance odds.",
                    systemIcon = "cloud_rain"
                )
            )
        }

        maxWindKPH?.let { wind ->
            val count = rows.count { it.windKph >= wind }
            odds.add(
                OddsResult(
                    id = UUID.randomUUID().toString(),
                    label = "Wind ≥ ${wind.toInt()} km/h",
                    valuePercent = pct(count),
                    note = "Daily wind exceedance odds.",
                    systemIcon = "wind"
                )
            )
        }

        val summary = if (rows.isNotEmpty()) {
            WeatherSummary(
                avgHighC = rows.map { it.tmax }.average(),
                avgLowC = rows.map { it.tmin }.average(),
                avgPrecipMM = rows.map { it.p }.average(),
                avgWindKPH = rows.map { it.windKph }.average()
            )
        } else null

        return Pair(odds, summary)
    }

    private fun dateKeyToDayOfYear(dateKey: String): Int {
        val year = dateKey.substring(0, 4).toIntOrNull() ?: 2000
        val month = dateKey.substring(4, 6).toIntOrNull() ?: 1
        val day = dateKey.substring(6, 8).toIntOrNull() ?: 1

        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day)
        return calendar.get(Calendar.DAY_OF_YEAR)
    }
}
