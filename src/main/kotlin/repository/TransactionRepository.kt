package repository

import config.DbConfig
import model.Transaction
import org.h2.jdbcx.JdbcDataSource
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.*
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

    fun toDomain(row: ResultRow): model.Transaction {
        return model.Transaction(
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

class TransactionRepository(db: DbConfig) : Repository<model.Transaction>{

    private var dataSource: JdbcDataSource = db.getDataSource()

    init {
        transaction(Database.connect(dataSource)) {
            SchemaUtils.create(Transactions)
        }
    }

    private val logger = LoggerFactory.getLogger(TransactionRepository::class.java)

    override fun create(entity: Transaction): Transaction {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Transaction> {
        TODO("Not yet implemented")
    }

    override fun getOne(id: Long): Transaction {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long): Int {
        TODO("Not yet implemented")
    }
}