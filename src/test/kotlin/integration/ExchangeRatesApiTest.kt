package integration

import config.ModulesConfig
import model.ExchangeRatesApiResponse
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import repository.ExchangeRatesApi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExchangeRatesApiTest : KoinTest {

    @Before
    fun before() {
        startKoin{
            modules(ModulesConfig.modules)
        }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun `get currency exchanges data`() {
        val response: Response<ExchangeRatesApiResponse> = getCurrenciesData()!!

        assertTrue(response.isSuccessful)
        assertEquals(200,response.code())
        assertTrue(response.body()!!.rates.isNotEmpty())
    }

    private fun getCurrenciesData(): Response<ExchangeRatesApiResponse>? {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.exchangeratesapi.io")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

        val service = retrofit.create(ExchangeRatesApi::class.java)
        val call = service.getCurrencyData("USD")

        return try {
            val response: Response<ExchangeRatesApiResponse> = call.execute()
            response
        } catch (e: Exception) {
            null
        }
    }

}