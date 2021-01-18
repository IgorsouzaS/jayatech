package web.controller

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*
import model.Error
import model.ExchangeRatesApiResponse
import model.Transaction
import model.TransactionRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import repository.ExchangeRatesApi
import repository.TransactionRepository
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import web.exception.InvalidTransaction
import java.time.LocalDateTime

class TransactionController(private val transactionRepository: TransactionRepository)  {
    private companion object {
        val logger: Logger = LoggerFactory.getLogger(TransactionController::class.java)
    }

    private val invalidTransaction : String = "Invalid transaction"

    @OpenApi(
        summary = "Create transaction",
        operationId = "createTransaction",
        tags = ["Transaction"],
        requestBody = OpenApiRequestBody([OpenApiContent(TransactionRequest::class)]),
        responses = [
            OpenApiResponse("200", [OpenApiContent(Transaction::class)]),
            OpenApiResponse("400", [OpenApiContent(InvalidTransaction::class)])
        ],
        method = HttpMethod.POST
    )
    fun create(ctx: Context) {
        try {
            val transactionRequest = ctx.bodyAsClass(TransactionRequest::class.java)

            takeIf { transactionRequest.originCurrency.isBlank() || transactionRequest.destinationCurrency.isBlank() }?.apply {
                logger.error("Origin and destination currencies need to be informed")
                throw InvalidTransaction(type = invalidTransaction, message = "Origin and destination currencies need to be informed")
            }

            takeIf { transactionRequest.originAmount <= 0 }?.apply {
                logger.error("Invalid amount")
                throw InvalidTransaction(type = invalidTransaction, message = "Invalid amount")
            }

            transactionRequest.originCurrency = transactionRequest.originCurrency.toUpperCase()
            var data = getCurrenciesData(transactionRequest)!!

            takeIf { data !is ExchangeRatesApiResponse }?.apply {
                logger.error("Invalid currency")
                val errorMessage = data as Error
                throw InvalidTransaction(type = invalidTransaction, message = errorMessage.error)
            }

            data = data as ExchangeRatesApiResponse

            val rate = data.rates[transactionRequest.destinationCurrency]!!
            val destinationAmt = rate * transactionRequest.originAmount
            val transaction = Transaction(transactionRequest.userId,0,
                transactionRequest.originCurrency,
                transactionRequest.originAmount,
                transactionRequest.destinationCurrency,
                destinationAmt, rate, LocalDateTime.now())

            ctx.json(transactionRepository.create(transaction))
            logger.info("Successful transaction")

        } catch (ex: BadRequestResponse) {
            logger.error(ex.toString())
            throw InvalidTransaction(
                type = "Invalid transaction",
                message = ex.message.toString()
            )
        }
    }

    @OpenApi(
        summary = "Get a transaction by id",
        operationId = "getATransaction",
        tags = ["Transaction"],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Transaction::class)]),
            OpenApiResponse("400", [OpenApiContent(InvalidTransaction::class)])],
        method = HttpMethod.GET
    )
    fun getOne(ctx: Context){
        try {
            ctx.json(transactionRepository.getOne(ctx.pathParam("id").toLong()))
            logger.info("Transaction returned successfully")
        } catch (ex: BadRequestResponse) {
            logger.error(ex.toString())
            throw InvalidTransaction(
                type = "Transaction not found",
                message = ex.message.toString()
            )
        }
    }

    @OpenApi(
        summary = "Get all transactions",
        operationId = "getAllTransactions",
        tags = ["Transaction"],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Array<Transaction>::class)]),
            OpenApiResponse("400", [OpenApiContent(InvalidTransaction::class)])
        ],
        method = HttpMethod.GET
    )
    fun getAll(ctx: Context){
        try {
            ctx.json(transactionRepository.getAll())
            logger.info("Transactions list returned")
        } catch (ex: BadRequestResponse) {
            logger.error(ex.toString())
            throw BadRequestResponse(
                message = ex.message.toString()
            )
        }
    }

    @OpenApi(
        summary = "Delete a transaction",
        operationId = "deleteTransaction",
        tags = ["Transaction"],
        responses = [
            OpenApiResponse("200"),
            OpenApiResponse("400", [OpenApiContent(InvalidTransaction::class)])
        ],
        method = HttpMethod.DELETE
    )
    fun delete(ctx: Context) {
        try {
            transactionRepository.delete(ctx.pathParam("id").toLong())
            ctx.status(204)
            logger.info("Transaction successfully deleted")
        } catch (ex: BadRequestResponse) {
            logger.error(ex.toString())
            throw InvalidTransaction(
                type = "Transaction not found",
                message = ex.message.toString()
            )
        }
    }

    private fun getCurrenciesData(transaction: TransactionRequest): Any? {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.exchangeratesapi.io")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

        val service = retrofit.create(ExchangeRatesApi::class.java)
        val call = service.getCurrencyData(transaction.originCurrency)

        return try {
            val response: Response<ExchangeRatesApiResponse> = call.execute()
            if (response.isSuccessful) {
                logger.info("Returned data: ${response.body()}")
                response.body()
            } else {
                logger.error("Error")
                val error = retrofit.responseBodyConverter<Error>(
                    Error::class.java,
                    Error::class.java.annotations)
                    .convert(response.errorBody()!!)
                error
            }
        } catch (e: Exception) {
            logger.error("Error: " + e.message)
            null
        }
    }

}