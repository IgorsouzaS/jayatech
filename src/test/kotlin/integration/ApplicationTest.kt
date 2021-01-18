package integration

import config.AppConfig
import io.javalin.Javalin
import model.Transaction
import model.TransactionRequest
import model.User
import model.UserRequest
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.koin.core.context.stopKoin
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.FileInputStream
import java.util.Properties
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ApplicationTest {

    private val baseUrl: String = "http://localhost:" + getPort()
    private var user = UserRequest("test", "test")
    private var transaction: TransactionRequest = TransactionRequest(
        1,
        "USD", 2.00, "CAD"
    )
    private lateinit var app: Javalin

    @Before
    fun before() {
        app = AppConfig().startApplication()
    }

    @After
    fun after() {
        stopKoin()
        app.stop()
    }

    @Test
    fun `a - createUserTest`() {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

        val service = retrofit.create(IntegrationTest::class.java)
        val call = service.postUser(user)
        val response: Response<User> = call.execute()

        val newUser = response.body()

        assertEquals(user.email, newUser!!.email)
        assertEquals(200, response.code())
    }

    @Test
    fun `b - createTransactionTest`() {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

        val service = retrofit.create(IntegrationTest::class.java)
        val call = service.postTransaction(transaction)

        val response: Response<Transaction> = call.execute()
        assertEquals(200, response.code())
    }

    @Test
    fun `c - getTransactionsTest`() {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

        val service = retrofit.create(IntegrationTest::class.java)
        val call = service.getTransactions()

        val response: Response<List<Transaction>> = call.execute()

        assertEquals(200, response.code())
        assertTrue(response.body()!!.isNotEmpty())
    }

    private fun getPort(): Int {
        val prop = Properties()
        val fis = FileInputStream("src/main/resources/application.properties")
        prop.load(fis)

        return prop.getProperty("server.port").toInt()
    }
}
