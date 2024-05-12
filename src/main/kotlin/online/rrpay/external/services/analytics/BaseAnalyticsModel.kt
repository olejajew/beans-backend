package online.rrpay.external.services.analytics

import online.rrpay.external.data.models.BeanCard

data class BaseAnalyticsModel(
    val first: String = "",
    val give: BeanCard = BeanCard.ERROR,
    val count: Int = 0,
    val giveTo: String = "",
    val giveFor: BeanCard = BeanCard.ERROR,
    val forCount: Int = 0
)