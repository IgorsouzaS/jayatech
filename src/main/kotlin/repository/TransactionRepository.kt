package repository

import config.DbConfig
import io.javalin.http.BadRequestResponse
import model.Transaction
import org.h2.jdbcx.JdbcDataSource
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.CurrentDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.datetime
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

object Transactions : LongIdTable("TRANSACTIONS") {
    val originCurrency: Column<String> = varchar("originCurrency", 200)
    val destinationCurrency: Column<String> = varchar("destinationCurrency", 100)
    val userId: Column<Long> = long("userId")
    val originAmount: Column<Double> = double("originAmount")
    val destinyAmount: Column<Double> = double("destinationAmount")
    val rate: Column<Float> = float("rate")
    private val createdAt: Column<LocalDateTime> = datetime("createdAt").defaultExpression(CurrentDateTime())

    fun toDomain(row: ResultRow): Transaction {
        return Transaction(
            id = row[id].value,
            userId = row[userId],
            originCurrency = row[originCurrency],
            destinationCurrency = row[destinationCurrency],
            originAmount = row[originAmount],
            destinationAmount = row[destinyAmount],
            rate = row[rate],
            createdAt = row[createdAt]
        )
    }
}

class TransactionRepository(db: DbConfig) : Repository<Transaction> {

    private var dataSource: JdbcDataSource = db.getDataSource()

    init {
        transaction(Database.connect(dataSource)) {
            SchemaUtils.create(Transactions)
        }
    }

    private val logger = LoggerFactory.getLogger(TransactionRepository::class.java)

    override fun create(entity: Transaction): Transaction {

        val exists: Boolean = transaction(Database.connect(dataSource)) {
            Users.select { Users.id eq entity.userId }.count() > 0
        }

        takeIf { !exists }?.apply {
            logger.error("User not found")
            throw BadRequestResponse(message = "User not found")
        }

        val transactionId: Long = transaction {
            Transactions.insertAndGetId { row ->
                row[originCurrency] = entity.originCurrency
                row[destinationCurrency] = entity.destinationCurrency
                row[originAmount] = entity.originAmount
                row[destinyAmount] = entity.destinationAmount
                row[rate] = entity.rate
                row[userId] = entity.userId
            }.value
        }
        logger.info("Save transaction with success")
        return getOne(transactionId)
    }

    override fun getAll(): List<Transaction> {
        logger.info("Transactions list returned")
        return transaction {
            Transactions.selectAll().map {
                Transactions.toDomain(it)
            }.toList()
        }
    }

    override fun getOne(id: Long): Transaction {
        logger.info("Transaction data returned")
        return transaction(Database.connect(dataSource)) {
            Transactions.select { Transactions.id eq id }
                .map { Transactions.toDomain(it) }
                .firstOrNull()
        }!!
    }

    override fun delete(id: Long): Int {
        logger.info("Transaction successfully deleted")
        return transaction(Database.connect(dataSource)) {
            Transactions.deleteWhere { Transactions.id eq id }
        }
    }
}
