package online.rrpay.external.services.analytics

data class BaseTransactionRevenue(
    val firstName: String,
    val secondName: String,
    val firstPoints: Int,
    val firstGotCards: Int,
    val secondPoints: Int,
    val secondGotCards: Int
)