package online.rrpay.external.data

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.lang.Exception
import java.sql.Connection
import javax.annotation.PostConstruct

//@Repository
//class DatabaseRepository {
//
//    @Value("\${spring.datasource.url}")
//    lateinit var url: String
//
//    @Value("\${spring.datasource.driver:org.postgresql.Driver}")
//    lateinit var driver: String
//
//    @Value("\${spring.datasource.username}")
//    lateinit var username: String
//
//    @Value("\${spring.datasource.password}")
//    private lateinit var password: String
//
//    lateinit var database: Database
//
//    private val logger = LoggerFactory.getLogger(this::class.java)
//
//    private val tables = arrayOf<IdTable<*>>(
//    )
//
//    @PostConstruct
//    fun init() {
//        database = Database.connect(
//            url = url,
//            driver = driver,
//            user = username,
//            password = password
//        )
//        TransactionManager.manager.defaultIsolationLevel =
//            Connection.TRANSACTION_SERIALIZABLE
//        (LoggerFactory.getLogger("Exposed") as? ch.qos.logback.classic.Logger)?.level =
//            ch.qos.logback.classic.Level.TRACE
//        transaction(database) {
//            SchemaUtils.createMissingTablesAndColumns(
//                *tables
//            )
//        }
//    }
//
//    fun <T : Any> transaction(
//        repeatIfError: Boolean = false,
//        function: (Transaction) -> TransactionResult<T>
//    ): TransactionResult<T> {
//        return transaction(database) {
//            try {
//                val result = function.invoke(this)
//                if (result is TransactionResult.Error) {
//                    logger.error(result.message)
//                }
//                result
//            } catch (e: Exception) {
//                if (repeatIfError) {
//                    val result = transaction(false, function)
//                    result
//                } else {
//                    logger.error("TRANSACTION ERROR = ${e.localizedMessage}")
//                    logger.error(e.stackTraceToString())
//                    e.printStackTrace()
//                    rollback()
//                    TransactionResult.Error(e.localizedMessage ?: "")
//                }
//            }
//        }
//    }
//
//}
