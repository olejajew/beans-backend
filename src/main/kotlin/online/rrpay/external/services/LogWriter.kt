package online.rrpay.external.services

import com.fasterxml.jackson.databind.ObjectMapper
import online.rrpay.external.data.models.LogModel
import org.springframework.stereotype.Service
import java.io.File

@Service
class LogWriter(private val objectMapper: ObjectMapper) {

    companion object {
        const val LOGS = "logs.txt"
        const val DUMPS = "dumps.txt"
        const val ANALYTICS = "analytics.txt"
    }

    private val baseFolder = File("./cache").apply {
        mkdir()
    }


    fun saveToFile(fileName: String, message: String) {
        File(baseFolder, fileName).apply {
            createNewFile()
            appendText(objectMapper.writeValueAsString(LogModel(message = message)) + "\n")
        }
    }

    fun getLastRows(fileName: String, count: Int): List<String> {
        return File(baseFolder, fileName).readLines().takeLast(count)
    }

    fun clearFile(fileName: String) {
        File(baseFolder, fileName).deleteOnExit()
        File(baseFolder, fileName).createNewFile()
    }

}