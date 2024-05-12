package online.rrpay.external.data

import org.jetbrains.exposed.sql.Transaction

sealed class TransactionResult<T : Any?> {

    data class Success<T : Any?>(val body: T?) : TransactionResult<T>()

    data class Error<T : Any>(val message: String) : TransactionResult<T>()

    fun getData(): T? {
        return if (this is Success) {
            this.body
        } else {
            null
        }
    }
    
}

/**
 * success -> If request passed without problems
 */
fun <T> Transaction.success(body: T? = null): TransactionResult<T> {
    return TransactionResult.Success(body)
}

/**
 * error -> If request failed
 */
fun <T : Any> Transaction.error(message: String): TransactionResult.Error<T> {
    return TransactionResult.Error(message)
}