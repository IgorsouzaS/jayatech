package repository

import model.ExchangeRatesApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRatesApi {
    @GET("/latest")
    fun getCurrencyData(@Query("base") base: String): Call<ExchangeRatesApiResponse>
}