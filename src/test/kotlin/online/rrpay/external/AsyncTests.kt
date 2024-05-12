package online.rrpay.external

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.test.runTest
import online.rrpay.external.data.enums.InvoiceCurrency
import online.rrpay.external.data.rest.NewInvoiceRequest
import online.rrpay.external.data.rest.NewInvoiceResponse
import online.rrpay.external.services.KafkaProducerService
import online.rrpay.external.services.async.AsyncService
import online.rrpay.external.services.async.KafkaRouting
import online.rrpay.external.services.async.data.AsyncRequest
import online.rrpay.external.services.async.data.AsyncResponse
import online.rrpay.external.utils.readOrNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.UUID

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AsyncTests(
    @Autowired private val kafkaProducerService: KafkaProducerService,
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val asyncService: AsyncService
) {

    private val defaultInvoice = NewInvoiceRequest(
        BigDecimal(100.0),
        InvoiceCurrency.RUB,
        UUID.randomUUID().toString() //Это id со стороны мерчанта
    )

    @Test
    fun baseAsyncTest() = runTest { //Только в корутине!
        val asyncRequest = AsyncRequest(
            uuid = UUID.randomUUID(), //По этому id происходит отлавливание колбека
            objectMapper.writeValueAsString(defaultInvoice)
        )

        //Передаем запрос
        //Поток блокируется!
        //Получаем ответ (5 секунд ожидание максимальное)
        val newInvoiceRequest = asyncService.asyncRequest(asyncRequest.uuid) {
            kafkaProducerService.sendMessage(KafkaRouting.Topics.INVOICE_REQUEST, asyncRequest) //послали запрос в kafka
        }

        assertNotNull(newInvoiceRequest)
    }

    /**
     * Событие на этот слушатель посылается изнутри AsyncService.asyncRequest
     */
    @KafkaListener(topics = [KafkaRouting.Topics.INVOICE_REQUEST])
    fun emulateInvoiceRequest(message: String) {
        //Поймали модель AsyncResponse
        val asyncRequest = objectMapper.readOrNull(message, AsyncRequest::class.java) ?: throw Error()

        //Проверили передачу даных
        val data = objectMapper.readOrNull(asyncRequest.body ?: "", NewInvoiceRequest::class.java) ?: return
        assertEquals(data, defaultInvoice)

        val body = objectMapper.writeValueAsString(
            NewInvoiceResponse()
        )
        //Модель ответа для колбека
        val asyncResponse = AsyncResponse(
            uuid = asyncRequest.uuid, //Id асинхронного запроса
            success = true,
            body = body
        )

        //Тут мы явно передаем в Kafka.
        kafkaProducerService.sendMessage(KafkaRouting.Topics.INVOICE_RESPONSE, asyncResponse)
    }

}
