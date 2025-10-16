package com.geopulse.app.api

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class PowerDailyResponse(
    val properties: Properties
) {
    data class Properties(
        val parameter: Parameter
    )

    data class Parameter(
        @SerializedName("T2M_MAX") val t2mMax: Map<String, Double>?,
        @SerializedName("T2M_MIN") val t2mMin: Map<String, Double>?,
        @SerializedName("PRECTOTCORR") val prectotcorr: Map<String, Double>?,
        @SerializedName("WS10M") val ws10m: Map<String, Double>?
    )
}

interface NasaPowerApi {
    @GET("api/temporal/daily/point")
    suspend fun getDailyData(
        @Query("parameters") parameters: String = "T2M_MAX,T2M_MIN,PRECTOTCORR,WS10M",
        @Query("community") community: String = "RE",
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("start") start: Int = 1981,
        @Query("end") end: Int = 2020,
        @Query("format") format: String = "JSON"
    ): PowerDailyResponse

    companion object {
        private const val BASE_URL = "https://power.larc.nasa.gov/"

        fun create(): NasaPowerApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NasaPowerApi::class.java)
        }
    }
}
