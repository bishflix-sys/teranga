package com.example.data.payment

/** Adapter used by the domain layer; production implementations call the operator APIs. */
fun interface MobileMoneyGateway {
    suspend fun charge(method: String, phoneNumber: String, amountCfa: Int): Boolean
}

data class PaymentResult(
    val successful: Boolean,
    val methodUsed: String,
    val attemptedMethods: List<String>
)

class FallbackPaymentProcessor(
    private val gateway: MobileMoneyGateway,
    private val fallbackOrder: List<String> = listOf("Wave", "Orange Money", "Free Money")
) {
    suspend fun charge(
        preferredMethod: String,
        phoneNumber: String,
        amountCfa: Int
    ): PaymentResult {
        if (preferredMethod !in fallbackOrder || !isSenegaleseMobile(phoneNumber) || amountCfa <= 0) {
            return PaymentResult(false, preferredMethod, emptyList())
        }
        val methods = listOf(preferredMethod) + fallbackOrder.filter { it != preferredMethod }
        val attempted = mutableListOf<String>()
        for (method in methods) {
            attempted += method
            runCatching { gateway.charge(method, phoneNumber, amountCfa) }
                .getOrDefault(false)
                .takeIf { it }
                ?.let { return PaymentResult(true, method, attempted) }
        }
        return PaymentResult(false, preferredMethod, attempted)
    }

    private fun isSenegaleseMobile(phoneNumber: String): Boolean {
        val digits = phoneNumber.filter(Char::isDigit).removePrefix("221")
        return digits.matches(Regex("(?:70|74|75|76|77|78)\\d{7}"))
    }
}
