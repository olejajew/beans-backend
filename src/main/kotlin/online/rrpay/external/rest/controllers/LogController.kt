package online.rrpay.external.rest.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import online.rrpay.external.services.LogWriter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/logs")
class LogController(
    private val logWriter: LogWriter
) {

    @PostMapping("/log")
    fun log(
        @RequestBody string: String
    ) {
        logWriter.saveToFile(LogWriter.LOGS, "$string\n")
    }

    @PostMapping("/dump")
    fun dump(
        @RequestBody string: String
    ) {
        logWriter.saveToFile(LogWriter.DUMPS, "$string\n")
    }

    @GetMapping("/last_rows")
    fun getLastRows(
        @RequestParam("count") count: Int
    ): String {
        return logWriter.getLastRows(LogWriter.LOGS, count).reversed().joinToString("<br>")
    }
}