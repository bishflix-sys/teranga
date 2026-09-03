package com.example.data.ticket

/** Tarification officielle du TER pour les zones du Grand Dakar. */
object TerFareCalculator {
    const val ONE_ZONE_FARE_CFA = 500
    const val TWO_ZONES_FARE_CFA = 1_000
    const val THREE_ZONES_FARE_CFA = 1_500
    const val FIRST_CLASS_FARE_CFA = 2_500

    fun calculate(zones: Int, firstClass: Boolean = false): Int {
        if (firstClass) return FIRST_CLASS_FARE_CFA
        return when (zones.coerceIn(1, 3)) {
            1 -> ONE_ZONE_FARE_CFA
            2 -> TWO_ZONES_FARE_CFA
            else -> THREE_ZONES_FARE_CFA
        }
    }
}
