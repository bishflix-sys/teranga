package com.example.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.data.local.CitizenReportDao
import com.example.data.local.PassDao
import com.example.data.local.TicketDao
import com.example.data.model.CitizenReportEntity
import com.example.data.model.CrowdingLevel
import com.example.data.model.PassSubscriptionEntity
import com.example.data.model.TicketEntity
import com.example.data.model.TrafficAlert
import com.example.data.model.TransportCategory
import com.example.data.model.TransportLineInfo
import com.example.data.model.VehicleRealtime
import com.example.data.ticket.OfflineQrTokenService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class TransitRepository(
    private val ticketDao: TicketDao,
    private val reportDao: CitizenReportDao,
    private val passDao: PassDao,
    private val offlineQrTokenService: OfflineQrTokenService? = null
) {
    val allTickets: Flow<List<TicketEntity>> = ticketDao.getAllTickets()
    val allReports: Flow<List<CitizenReportEntity>> = reportDao.getAllReports()
    val allPasses: Flow<List<PassSubscriptionEntity>> = passDao.getAllPasses()
    val ticketsCount: Flow<Int> = ticketDao.getTicketsCount()

    // Real-time vehicles feed
    private val _vehicles = MutableStateFlow<List<VehicleRealtime>>(emptyList())
    val vehicles: StateFlow<List<VehicleRealtime>> = _vehicles.asStateFlow()

    // Traffic Alerts
    private val _alerts = MutableStateFlow<List<TrafficAlert>>(emptyList())
    val alerts: StateFlow<List<TrafficAlert>> = _alerts.asStateFlow()

    // Official Lines Catalogue
    val linesCatalog: List<TransportLineInfo> = listOf(
        TransportLineInfo(
            lineCode = "Ligne 1",
            category = TransportCategory.DAKAR_DEM_DIKK,
            origin = "Gare Petersen (Plateau)",
            destination = "Almadies / Ngor",
            via = "Fann, Point E, Mermoz, Ouakam",
            frequencyMinutes = 10,
            standardFareCfa = 200,
            operatingHours = "06:00 - 22:30",
            stopsCount = 28
        ),
        TransportLineInfo(
            lineCode = "Ligne 4",
            category = TransportCategory.DAKAR_DEM_DIKK,
            origin = "Gare Petersen",
            destination = "Golf Sud / Guédiawaye",
            via = "Colobane, Patte d'Oie, Grand Yoff",
            frequencyMinutes = 12,
            standardFareCfa = 200,
            operatingHours = "06:00 - 22:00",
            stopsCount = 32
        ),
        TransportLineInfo(
            lineCode = "Ligne 8",
            category = TransportCategory.DAKAR_DEM_DIKK,
            origin = "Palais de Justice",
            destination = "Aéroport LSS / Yoff",
            via = "Corniche Ouest, Fann Résidence, Cité Keur Gorgui",
            frequencyMinutes = 15,
            standardFareCfa = 250,
            operatingHours = "06:00 - 21:30",
            stopsCount = 24
        ),
        TransportLineInfo(
            lineCode = "BRT B1 Omnibus",
            category = TransportCategory.BRT,
            origin = "Préfecture Guédiawaye",
            destination = "Gare Petersen",
            via = "Grand Médine, Liberté 6, Sacré-Cœur, Allées Papa Guèye Fall",
            frequencyMinutes = 4,
            standardFareCfa = 400,
            operatingHours = "05:30 - 23:00",
            stopsCount = 23,
            isDisrupted = false
        ),
        TransportLineInfo(
            lineCode = "BRT B3 Express",
            category = TransportCategory.BRT,
            origin = "Gare Petersen",
            destination = "Parcelles Assainies",
            via = "Couloir 100% réservé - Direct HLM",
            frequencyMinutes = 6,
            standardFareCfa = 500,
            operatingHours = "06:00 - 22:30",
            stopsCount = 14
        ),
        TransportLineInfo(
            lineCode = "TER Dakar-Diamniadio",
            category = TransportCategory.TER,
            origin = "Gare Centrale de Dakar",
            destination = "Gare de Diamniadio",
            via = "Colobane, Hann, Pikine, Thiaroye, Keur Massar, Bargny",
            frequencyMinutes = 10,
            standardFareCfa = 1500,
            operatingHours = "05:30 - 22:30",
            stopsCount = 14
        ),
        TransportLineInfo(
            lineCode = "AFTU Tata 219",
            category = TransportCategory.AFTU_TATA,
            origin = "Guédiawaye Arrêt Double Less",
            destination = "Sandaga / Petersen",
            via = "Case Bi, Castors, Médina",
            frequencyMinutes = 7,
            standardFareCfa = 150,
            operatingHours = "05:45 - 23:00",
            stopsCount = 30
        ),
        TransportLineInfo(
            lineCode = "AFTU Tata 44",
            category = TransportCategory.AFTU_TATA,
            origin = "Pikine Tally Boumack",
            destination = "Colobane",
            via = "Bountou Pikine, Baux Maraîchers, Autoroute",
            frequencyMinutes = 8,
            standardFareCfa = 150,
            operatingHours = "06:00 - 22:00",
            stopsCount = 25
        ),
        TransportLineInfo(
            lineCode = "Car Rapide CR-01",
            category = TransportCategory.CAR_RAPIDE,
            origin = "Gare Petersen",
            destination = "Rufisque Gare",
            via = "Poutte, Thiaroye, Mbao",
            frequencyMinutes = 5,
            standardFareCfa = 200,
            operatingHours = "05:00 - 00:00",
            stopsCount = 18
        ),
        TransportLineInfo(
            lineCode = "Car Rapide CR-08",
            category = TransportCategory.CAR_RAPIDE,
            origin = "Rond-Point Liberté 6",
            destination = "Pikine Icotaf",
            via = "Front de Terre, Castors, Rond-point Camberène",
            frequencyMinutes = 5,
            standardFareCfa = 150,
            operatingHours = "05:30 - 23:30",
            stopsCount = 16
        ),
        TransportLineInfo(
            lineCode = "Taxi Clando TC-Ngor",
            category = TransportCategory.TAXI_CLANDO,
            origin = "Rond-point Almadies",
            destination = "Plage de Ngor / Embarcadère",
            via = "Route de l'Aéroport",
            frequencyMinutes = 3,
            standardFareCfa = 250,
            operatingHours = "24h/24",
            stopsCount = 6
        )
    )

    init {
        // Initialize base vehicle positions
        _vehicles.value = generateInitialVehicles()
        _alerts.value = generateTrafficAlerts()
    }

    suspend fun checkAndSeedInitialData() {
        val existingReports = reportDao.getAllReports().first()
        if (existingReports.isEmpty()) {
            val initialReports = listOf(
                CitizenReportEntity(
                    category = "Embouteillage",
                    locationName = "VDN hauteur Mermoz vers Fann",
                    description = "Ralentissement important causé par des travaux sur la voie de droite. Prévoir 20 min de plus.",
                    severity = "Critique",
                    timestamp = System.currentTimeMillis() - (12 * 60 * 1000),
                    confirmationsCount = 24,
                    authorName = "Moussa Diop (Chauffeur DDD)"
                ),
                CitizenReportEntity(
                    category = "Accident",
                    locationName = "Rond-point Patte d'Oie vers Autoroute",
                    description = "Accident matériel entre deux véhicules légers. Voie partiellement bloquée, policiers en cours de régulation.",
                    severity = "Important",
                    timestamp = System.currentTimeMillis() - (28 * 60 * 1000),
                    confirmationsCount = 15,
                    authorName = "Fatou Ndiaye"
                ),
                CitizenReportEntity(
                    category = "Panne de bus",
                    locationName = "Colobane sous le pont",
                    description = "Un bus Tata en panne mécanique bloque l'entrée du rond-point. Les cars rapides bifurquent par la Médina.",
                    severity = "Ralenti",
                    timestamp = System.currentTimeMillis() - (45 * 60 * 1000),
                    confirmationsCount = 9,
                    authorName = "Ibrahima Sarr"
                ),
                CitizenReportEntity(
                    category = "Sécurité",
                    locationName = "Passerelle piétonne Baux Maraîchers",
                    description = "Éclairage nocturne hors service et passage sombre. Traverser en groupe ou privilégier l'avenue principale.",
                    severity = "Important",
                    timestamp = System.currentTimeMillis() - (35 * 60 * 1000),
                    confirmationsCount = 18,
                    authorName = "Ousmane Kane"
                ),
                CitizenReportEntity(
                    category = "Zone inondée",
                    locationName = "Route de Grand Yoff près du marché",
                    description = "Flaque d'eau importante suite aux travaux de canalisation. Ralentissement modéré.",
                    severity = "Ralenti",
                    timestamp = System.currentTimeMillis() - (80 * 60 * 1000),
                    confirmationsCount = 6,
                    authorName = "Awa Sow"
                )
            )
            initialReports.forEach { reportDao.insertReport(it) }
        }

        val existingPasses = passDao.getAllPasses().first()
        if (existingPasses.isEmpty()) {
            val defaultPass = PassSubscriptionEntity(
                passTitle = "Pass Étudiant UCAD & Ecoles",
                holderName = "Amadou Diallo",
                cardNumber = "SN-PASS-2026-9481",
                category = "Étudiant",
                expiryDate = "31 Décembre 2026",
                tripsRemaining = 46,
                isActive = true,
                priceCfa = 7500
            )
            passDao.insertPass(defaultPass)
        }
    }

    private fun generateInitialVehicles(): List<VehicleRealtime> {
        return listOf(
            VehicleRealtime(
                id = "veh-brt-101",
                lineCode = "BRT B1",
                vehicleNumber = "BRT #104",
                category = TransportCategory.BRT,
                destination = "Gare Petersen (Plateau)",
                currentStop = "Sacré-Cœur Station",
                nextStop = "Gare Allées Papa Guèye Fall",
                etaMinutes = 3,
                crowding = CrowdingLevel.SEATS_AVAILABLE,
                speedKmH = 42,
                latitude = 14.7145,
                longitude = -17.4580,
                heading = 160f,
                fareCfa = 400
            ),
            VehicleRealtime(
                id = "veh-brt-102",
                lineCode = "BRT B3 Express",
                vehicleNumber = "BRT #118",
                category = TransportCategory.BRT,
                destination = "Préfecture Guédiawaye",
                currentStop = "Grand Médine",
                nextStop = "Parcelles Assainies U22",
                etaMinutes = 5,
                crowding = CrowdingLevel.STANDING_ONLY,
                speedKmH = 46,
                latitude = 14.7450,
                longitude = -17.4390,
                heading = 340f,
                fareCfa = 500
            ),
            VehicleRealtime(
                id = "veh-ddd-01",
                lineCode = "Ligne 1",
                vehicleNumber = "DDD #412",
                category = TransportCategory.DAKAR_DEM_DIKK,
                destination = "Almadies / Ngor",
                currentStop = "Point E - Piscine Olympique",
                nextStop = "Mermoz Ancienne Piste",
                etaMinutes = 7,
                crowding = CrowdingLevel.STANDING_ONLY,
                speedKmH = 28,
                latitude = 14.6980,
                longitude = -17.4640,
                heading = 310f,
                fareCfa = 200
            ),
            VehicleRealtime(
                id = "veh-ddd-04",
                lineCode = "Ligne 4",
                vehicleNumber = "DDD #389",
                category = TransportCategory.DAKAR_DEM_DIKK,
                destination = "Golf Sud / Guédiawaye",
                currentStop = "Colobane Gare Routière",
                nextStop = "Patte d'Oie Échangeur",
                etaMinutes = 4,
                crowding = CrowdingLevel.FULL,
                speedKmH = 22,
                latitude = 14.7080,
                longitude = -17.4420,
                heading = 45f,
                fareCfa = 200
            ),
            VehicleRealtime(
                id = "veh-ter-01",
                lineCode = "TER Express",
                vehicleNumber = "Rame Coradia #06",
                category = TransportCategory.TER,
                destination = "Diamniadio Gare",
                currentStop = "Gare Beaux Maraîchers (Pikine)",
                nextStop = "Gare de Thiaroye",
                etaMinutes = 2,
                crowding = CrowdingLevel.SEATS_AVAILABLE,
                speedKmH = 120,
                latitude = 14.7520,
                longitude = -17.3890,
                heading = 95f,
                fareCfa = 1500
            ),
            VehicleRealtime(
                id = "veh-tata-219",
                lineCode = "Ligne 219",
                vehicleNumber = "Tata #8812",
                category = TransportCategory.AFTU_TATA,
                destination = "Sandaga / Petersen",
                currentStop = "Castors Rond-Point",
                nextStop = "Médina Rue 6",
                etaMinutes = 6,
                crowding = CrowdingLevel.STANDING_ONLY,
                speedKmH = 31,
                latitude = 14.7020,
                longitude = -17.4510,
                heading = 190f,
                fareCfa = 150
            ),
            VehicleRealtime(
                id = "veh-cr-01",
                lineCode = "Car Rapide Petersen",
                vehicleNumber = "CR-DK-4410",
                category = TransportCategory.CAR_RAPIDE,
                destination = "Rufisque",
                currentStop = "Baux Maraîchers",
                nextStop = "Poste Thiaroye",
                etaMinutes = 3,
                crowding = CrowdingLevel.FULL,
                speedKmH = 35,
                latitude = 14.7430,
                longitude = -17.3980,
                heading = 110f,
                fareCfa = 200,
                hasAirConditioning = false
            ),
            VehicleRealtime(
                id = "veh-taxi-05",
                lineCode = "Taxi Urbain",
                vehicleNumber = "DK-9022-A",
                category = TransportCategory.TAXI_CLANDO,
                destination = "Almadies / Ngor",
                currentStop = "Cité Keur Gorgui",
                nextStop = "Rond-point OMVS",
                etaMinutes = 2,
                crowding = CrowdingLevel.SEATS_AVAILABLE,
                speedKmH = 38,
                latitude = 14.7110,
                longitude = -17.4690,
                heading = 330f,
                fareCfa = 1500
            )
        )
    }

    private fun generateTrafficAlerts(): List<TrafficAlert> {
        return listOf(
            TrafficAlert(
                id = "alert-1",
                title = "Embouteillage saturé - VDN Mermoz",
                location = "VDN vers Pont Sénégal92",
                severity = "Critique",
                timeAgo = "Il y a 8 min",
                alternativeRoute = "Emprunter la Voie de Dégagement Nord 3 (VDN 3) ou la Corniche Ouest.",
                delayEstimateMinutes = 22
            ),
            TrafficAlert(
                id = "alert-2",
                title = "Circulation Fluide sur le BRT",
                location = "Couloir Dédié Guédiawaye - Petersen",
                severity = "Fluide",
                timeAgo = "En direct",
                alternativeRoute = "Gagnez 35 minutes en optant pour le BRT plutôt que l'autoroute.",
                delayEstimateMinutes = 0
            ),
            TrafficAlert(
                id = "alert-3",
                title = "Travaux routiers - Échangeur Hann",
                location = "Sortie Autoroute vers Maristes",
                severity = "Modéré",
                timeAgo = "Il y a 30 min",
                alternativeRoute = "Prendre la sortie Patte d'Oie ou contourner par Dalifort.",
                delayEstimateMinutes = 12
            )
        )
    }

    // Dynamic tick simulation for real-time live map animation
    fun startRealtimeSimulation(scope: CoroutineScope) {
        scope.launch(Dispatchers.Default) {
            while (true) {
                delay(4000)
                val currentList = _vehicles.value
                val updated = currentList.map { veh ->
                    // Slight variation to simulate movement along Dakar axes
                    val latDelta = (Random.nextDouble() - 0.5) * 0.0008
                    val lonDelta = (Random.nextDouble() - 0.5) * 0.0008
                    val newEta = if (veh.etaMinutes > 1) {
                        if (Random.nextInt(5) == 0) veh.etaMinutes - 1 else veh.etaMinutes
                    } else {
                        Random.nextInt(4, 12)
                    }
                    veh.copy(
                        latitude = (veh.latitude + latDelta).coerceIn(14.65, 14.80),
                        longitude = (veh.longitude + lonDelta).coerceIn(-17.52, -17.30),
                        etaMinutes = newEta,
                        speedKmH = (veh.speedKmH + Random.nextInt(-4, 5)).coerceIn(15, 90)
                    )
                }
                _vehicles.value = updated
            }
        }
    }

    // Purchase ticket with mobile money
    suspend fun buyTicket(
        lineCode: String,
        category: TransportCategory,
        origin: String,
        destination: String,
        fareCfa: Int,
        paymentMethod: String
    ): TicketEntity {
        val ticketNumber = "SN-${category.name.take(3)}-${Random.nextInt(1000, 9999)}"
        // The encrypted token carries its own 15-minute expiry for offline validation.
        val issuedAt = System.currentTimeMillis()
        val qrData = offlineQrTokenService?.generate(ticketNumber, fareCfa, issuedAt)
            ?: "SUNUYOON:${ticketNumber}:${issuedAt}:${fareCfa}"
        val ticket = TicketEntity(
            ticketNumber = ticketNumber,
            lineCode = lineCode,
            transportCategory = category.label,
            origin = origin,
            destination = destination,
            fareCfa = fareCfa,
            paymentMethod = paymentMethod,
            timestamp = issuedAt,
            isValidated = false,
            qrCodeData = qrData
        )
        val id = ticketDao.insertTicket(ticket)
        return ticket.copy(id = id.toInt())
    }

    suspend fun validateTicket(ticketId: Int): Boolean {
        val ticket = ticketDao.getTicket(ticketId) ?: return false
        val isValid = offlineQrTokenService?.consume(ticket.qrCodeData) ?: true
        if (!isValid || ticket.isValidated) return false
        ticketDao.markValidated(ticketId)
        return true
    }

    // Post citizen report
    suspend fun addCitizenReport(
        category: String,
        locationName: String,
        description: String,
        severity: String,
        authorName: String
    ): CitizenReportEntity {
        val report = CitizenReportEntity(
            category = category,
            locationName = locationName,
            description = description,
            severity = severity,
            timestamp = System.currentTimeMillis(),
            confirmationsCount = 1,
            authorName = authorName.ifBlank { "Citoyen Dakar" }
        )
        val id = reportDao.insertReport(report)
        return report.copy(id = id.toInt())
    }

    suspend fun confirmReport(reportId: Int) {
        reportDao.confirmReport(reportId)
    }

    suspend fun deleteCitizenReport(reportId: Int) {
        reportDao.deleteReportById(reportId)
    }

    fun getReportsByCategory(category: String): Flow<List<CitizenReportEntity>> {
        return reportDao.getReportsByCategory(category)
    }

    // Subscribe to or buy Pass
    suspend fun subscribePass(
        passTitle: String,
        holderName: String,
        category: String,
        priceCfa: Int
    ): PassSubscriptionEntity {
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH)
        val expiry = formatter.format(Date(System.currentTimeMillis() + 30L * 24 * 3600 * 1000))
        val cardNumber = "SN-PASS-${Random.nextInt(1000, 9999)}-${Random.nextInt(100, 999)}"
        val pass = PassSubscriptionEntity(
            passTitle = passTitle,
            holderName = holderName,
            cardNumber = cardNumber,
            category = category,
            expiryDate = expiry,
            tripsRemaining = if (category == "Étudiant") 50 else 60,
            isActive = true,
            priceCfa = priceCfa
        )
        val id = passDao.insertPass(pass)
        return pass.copy(id = id.toInt())
    }

    // WhatsApp Alert Sharing Intent
    fun shareViaWhatsApp(context: Context, message: String) {
        try {
            val sendIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://api.whatsapp.com/send?text=" + Uri.encode(message))
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(sendIntent)
        } catch (e: Exception) {
            // Fallback to standard share sheet
            val fallbackIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(Intent.createChooser(fallbackIntent, "Partager via"))
        }
    }
}
