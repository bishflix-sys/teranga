package com.example.data.ticket

import com.example.data.payment.FallbackPaymentProcessor
import com.example.data.payment.MobileMoneyGateway
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TicketDomainTest {
    @Test
    fun terFareUsesOfficialZoneAndClassPrices() {
        assertEquals(500, TerFareCalculator.calculate(1))
        assertEquals(1_000, TerFareCalculator.calculate(2))
        assertEquals(1_500, TerFareCalculator.calculate(3))
        assertEquals(2_500, TerFareCalculator.calculate(1, firstClass = true))
    }

    @Test
    fun paymentFallsBackToNextOperatorWhenPreferredFails() = runTest {
        val attempts = mutableListOf<String>()
        val processor = FallbackPaymentProcessor(
            MobileMoneyGateway { method, _, _ ->
                attempts += method
                method == "Orange Money"
            }
        )

        val result = processor.charge("Wave", "77 000 00 00", 500)

        assertTrue(result.successful)
        assertEquals("Orange Money", result.methodUsed)
        assertEquals(listOf("Wave", "Orange Money"), attempts)
    }
}
