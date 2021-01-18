package integration

import model.Transaction
import model.TransactionRequest
import model.User
import model.UserRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IntegrationTest {
    @POST("/users")
    fun postUser(@Body user: UserRequest): Call<User>

    @POST("/transactions")
    fun postTransaction(@Body transaction: TransactionRequest): Call<Transaction>

    @GET("/transactions")
    fun getTransactions(): Call<List<Transaction>>
}
