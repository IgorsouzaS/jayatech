package unit

import config.ModulesConfig
import model.Transaction
import model.User
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import repository.TransactionRepository
import repository.UserRepository
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TransactionTest : KoinTest {

    private val transactionRepository: TransactionRepository by inject()
    private val userRepository: UserRepository by inject()
    private var user = User(1, "test", "test")
    private var transaction = Transaction(
        1, 1, "USD", 2.00, "BRL",
        2.00, 2f, LocalDateTime.now()
    )

    @Before
    fun before() {
        startKoin {
            modules(ModulesConfig.modules)
        }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun `a - should save user`() {
        val createdUser: User = userRepository.create(user)
        assertNotNull(createdUser)
    }

    @Test
    fun `b - should save transaction`() {
        val createdTransaction: Transaction = transactionRepository.create(transaction)
        assertNotNull(createdTransaction)
    }

    @Test
    fun `c - should return transactions list`() {
        val transactionList: List<Transaction> = transactionRepository.getAll()
        assertTrue(transactionList.isNotEmpty())
    }

    @Test
    fun `d - should return one transaction`() {
        val transaction: Transaction = transactionRepository.getOne(transaction.id!!)
        assertNotNull(transaction)
    }

    @Test
    fun `e - should not return any transaction`() {
        val result: Int = transactionRepository.delete(transaction.id!!)
        assertEquals(1, result)
    }
}
