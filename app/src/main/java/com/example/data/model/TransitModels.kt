package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TransportCategory(val label: String, val badgeColorHex: Long) {
    DAKAR_DEM_DIKK("Dakar Dem Dikk", 0xFF00684A),
    AFTU_TATA("AFTU Tata", 0xFF00838F),
    BRT("BRT Dakar", 0xFF1565C0),
    TER("TER Train", 0xFF6A1B9A),
    CAR_RAPIDE("Car Rapide / Ndiaga", 0xFFF57F17),
    TAXI_CLANDO("Taxi Urbain / Clando", 0xFFE65100)
}

enum class CrowdingLevel(val label: String) {
    SEATS_AVAILABLE("Places assises"),
    STANDING_ONLY("Debout uniquement"),
    FULL("Complet")
}

data class TransportStop(
    val id: String,
    val name: String,
    val neighborhood: String,
    val latitude: Double,
    val longitude: Double
)

data class VehicleRealtime(
    val id: String,
    val lineCode: String,
    val vehicleNumber: String,
    val category: TransportCategory,
    val destination: String,
    val currentStop: String,
    val nextStop: String,
    val etaMinutes: Int,
    val crowding: CrowdingLevel,
    val speedKmH: Int,
    val latitude: Double,
    val longitude: Double,
    val heading: Float,
    val fareCfa: Int,
    val hasAirConditioning: Boolean = true
)

data class TransportLineInfo(
    val lineCode: String,
    val category: TransportCategory,
    val origin: String,
    val destination: String,
    val via: String,
    val frequencyMinutes: Int,
    val standardFareCfa: Int,
    val operatingHours: String,
    val stopsCount: Int,
    val isDisrupted: Boolean = false,
    val disruptionMessage: String? = null
)

@Entity(tableName = "tickets")
data class TicketEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ticketNumber: String,
    val lineCode: String,
    val transportCategory: String,
    val origin: String,
    val destination: String,
    val fareCfa: Int,
    val paymentMethod: String, // "Wave", "Orange Money", "Free Money"
    val timestamp: Long = System.currentTimeMillis(),
    val isValidated: Boolean = false,
    val qrCodeData: String
)

enum class IncidentCategory(
    val title: String,
    val emoji: String,
    val badgeColorHex: Long,
    val subtitle: String
) {
    ACCIDENT("Accident", "🚨", 0xFFD32F2F, "Collision, piéton, véhicule immobilisé"),
    TRAFFIC("Trafic", "🚦", 0xFFE65100, "Embouteillage, circulation bloquée, ralentissement"),
    SAFETY("Sécurité", "🛡️", 0xFFC2185B, "Danger public, agression, éclairage éteint"),
    ROAD_HAZARDS("Travaux", "🚧", 0xFFF57F17, "Chantier, voie rétrécie, nid de poule dangereux"),
    TRANSIT_ISSUE("Panne de bus", "🚌", 0xFF7B1FA2, "Bus DDD, Tata, BRT ou rame TER immobilisé"),
    WEATHER_FLOOD("Zone inondée", "🌊", 0xFF0288D1, "Eaux stagnantes, passage impraticable");

    companion object {
        fun fromCategoryName(name: String): IncidentCategory {
            return values().find { it.title.equals(name, ignoreCase = true) }
                ?: if (name.contains("Trafic", ignoreCase = true) || name.contains("Embouteillage", ignoreCase = true)) TRAFFIC
                else if (name.contains("Accident", ignoreCase = true)) ACCIDENT
                else if (name.contains("Sécurité", ignoreCase = true) || name.contains("Securite", ignoreCase = true)) SAFETY
                else if (name.contains("Travaux", ignoreCase = true)) ROAD_HAZARDS
                else if (name.contains("Panne", ignoreCase = true)) TRANSIT_ISSUE
                else if (name.contains("Inond", ignoreCase = true)) WEATHER_FLOOD
                else TRAFFIC
        }
    }
}

@Entity(tableName = "citizen_reports")
data class CitizenReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // "Embouteillage", "Accident", "Travaux", "Panne de bus", "Zone inondée", "Sécurité", etc.
    val locationName: String,
    val description: String,
    val severity: String, // "Normal", "Ralenti", "Critique"
    val timestamp: Long = System.currentTimeMillis(),
    val confirmationsCount: Int = 1,
    val authorName: String = "Citoyen Dakar"
)

@Entity(tableName = "pass_subscriptions")
data class PassSubscriptionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val passTitle: String,
    val holderName: String,
    val cardNumber: String,
    val category: String, // "Étudiant", "Travailleur", "Tout Public"
    val expiryDate: String,
    val tripsRemaining: Int,
    val isActive: Boolean = true,
    val priceCfa: Int = 10000
)

data class TrafficAlert(
    val id: String,
    val title: String,
    val location: String,
    val severity: String,
    val timeAgo: String,
    val alternativeRoute: String,
    val delayEstimateMinutes: Int
)
