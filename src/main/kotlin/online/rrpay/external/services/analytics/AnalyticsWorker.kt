package online.rrpay.external.services.analytics

import com.fasterxml.jackson.databind.ObjectMapper
import online.rrpay.external.services.LogWriter
import org.springframework.stereotype.Service
import java.io.File
import javax.annotation.PostConstruct
import kotlin.math.log

@Service
class AnalyticsWorker(
    private val objectMapper: ObjectMapper,
    private val logWriter: LogWriter
) {

    @PostConstruct
    fun proceedAnalytics() {
        val file = File("./10.05.txt")
        logWriter.clearFile(LogWriter.ANALYTICS)
        if (!file.exists()) {
            return
        }
        val lines = file.readLines()
        val analytics = lines.map {
            objectMapper.readValue(it, BaseAnalyticsModel::class.java)
        }

        val grouped = baseAnalytics(analytics)
        val names = grouped.groupBy { it.firstName }.map { it.key }
        val rows = mutableListOf<BaseTransactionRevenue>()
        names.forEach { firstName ->
            val hisRows = grouped.filter { it.firstName == firstName }
            hisRows.forEach { revenueHeSeed ->
                val revenueHePeer =
                    grouped.filter { it.firstName == revenueHeSeed.secondName && it.secondName == firstName }.first()

                rows.add(BaseTransactionRevenue(
                    revenueHeSeed.firstName,
                    revenueHeSeed.secondName,
                    revenueHeSeed.firstPoints + revenueHePeer.secondPoints,
                    revenueHeSeed.firstGotCards + revenueHePeer.secondGotCards,
                    revenueHeSeed.secondPoints + revenueHePeer.firstPoints,
                    revenueHeSeed.secondGotCards + revenueHePeer.firstGotCards
                ))

            }
        }
        rows.forEach { saveToLog(objectMapper.writeValueAsString(it))  }

    }

    private fun baseAnalytics(analytics: List<BaseAnalyticsModel>): MutableList<BaseTransactionRevenue> {
        val rows = mutableListOf<BaseTransactionRevenue>()
        analytics.groupBy {
            it.first
        }.forEach { (name, allTransfers) ->
            allTransfers.groupBy {
                it.giveTo
            }.forEach { (giveTo, transfers) ->
                println("$name отдал $giveTo:")
                val revenue = transfers.map {
                    successData(it)
                }
                val sumGotCards = revenue.sumBy { it.firstGotCards }
                val firstPoints = revenue.sumBy { it.firstPoints }
                val sumSecondCards = revenue.sumBy { it.secondGotCards }
                val secondPoints = revenue.sumBy { it.secondPoints }

                rows.add(
                    BaseTransactionRevenue(
                        name,
                        giveTo,
                        firstPoints,
                        sumGotCards,
                        secondPoints,
                        sumSecondCards
                    )
                )
            }
        }
        return rows
    }

    private fun saveToLog(string: String) {
        logWriter.saveToFile(LogWriter.ANALYTICS, string + "\n")
    }

    private fun successData(baseAnalyticsModel: BaseAnalyticsModel): BaseTransactionRevenue {
        val firstCardsOnCount = baseAnalyticsModel.give.countInDeck * baseAnalyticsModel.count
        val secondCardsOnCount = baseAnalyticsModel.giveFor.countInDeck * baseAnalyticsModel.forCount
        return BaseTransactionRevenue(
            baseAnalyticsModel.first,
            baseAnalyticsModel.giveTo,
            firstCardsOnCount,
            baseAnalyticsModel.forCount,
            secondCardsOnCount,
            baseAnalyticsModel.count
        )
    }

}