package online.rrpay.external.data.models

import java.time.Instant

data class LogModel(
    val dateTime: String = Instant.now().toString(),
    val message: String = "",
)