package com.example.ui

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.payment.FallbackPaymentProcessor
import com.example.data.payment.MobileMoneyGateway
import com.example.data.local.SunuDatabase
import com.example.data.model.CitizenReportEntity
import com.example.data.model.NationalLanguage
import com.example.data.model.PassSubscriptionEntity
import com.example.data.model.TicketEntity
import com.example.data.model.TrafficAlert
import com.example.data.model.TransportCategory
import com.example.data.model.TransportLineInfo
import com.example.data.model.VehicleRealtime
import com.example.data.repository.LanguagePreferencesRepository
import com.example.data.repository.TransitSettingsRepository
import com.example.data.repository.TransitRepository
import com.example.data.nfc.NfcPassRechargeService
import com.example.data.ticket.OfflineQrTokenService
import com.example.ui.util.VoiceAnnouncer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class TransitTab(val title: String) {
    LIVE_MAP("En Direct"),
    ROUTES("Lignes"),
    TICKETS("Pass & Tickets"),
    REPORTS("Signalements"),
    STATS("Mon Espace")
}

data class PaymentUiState(
    val isOpen: Boolean = false,
    val lineCode: String = "",
    val category: TransportCategory = TransportCategory.DAKAR_DEM_DIKK,
    val origin: String = "",
    val destination: String = "",
    val fareCfa: Int = 200,
    val selectedMethod: String = "Wave", // "Wave", "Orange Money", "Free Money"
    val phoneNumber: String = "",
    val isProcessing: Boolean = false,
    val completedTicket: TicketEntity? = null
)

data class UserLocation(val latitude: Double, val longitude: Double)

class TransitViewModel(application: Application) : AndroidViewModel(application) {
    private val database = SunuDatabase.getDatabase(application)
    val repository = TransitRepository(
        ticketDao = database.ticketDao(),
        reportDao = database.citizenReportDao(),
        passDao = database.passDao(),
        offlineQrTokenService = OfflineQrTokenService(application)
    )

    // Voice accessibility announcer
    val voiceAnnouncer = VoiceAnnouncer(application)
    val isSpeaking: StateFlow<Boolean> = voiceAnnouncer.isSpeaking

    // Language preferences data layer
    val languagePreferencesRepository = LanguagePreferencesRepository.getInstance(application)
    private val settingsRepository = TransitSettingsRepository(application)
    val dataSaverEnabled: StateFlow<Boolean> = settingsRepository.dataSaverEnabled
    val nfcPassRechargeAvailable = NfcPassRechargeService(application).isAvailable
    private val _userLocation = MutableStateFlow<UserLocation?>(null)
    val userLocation: StateFlow<UserLocation?> = _userLocation.asStateFlow()

    // Language selection: observed from data layer
    val selectedLanguage: StateFlow<NationalLanguage> = languagePreferencesRepository.selectedLanguage

    private val _isLanguageSheetOpen = MutableStateFlow(false)
    val isLanguageSheetOpen: StateFlow<Boolean> = _isLanguageSheetOpen.asStateFlow()

    // Current screen navigation
    private val _currentTab = MutableStateFlow(TransitTab.LIVE_MAP)
    val currentTab: StateFlow<TransitTab> = _currentTab.asStateFlow()

    // Filters
    private val _selectedCategoryFilter = MutableStateFlow<TransportCategory?>(null)
    val selectedCategoryFilter: StateFlow<TransportCategory?> = _selectedCategoryFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Realtime vehicles
    val vehicles: StateFlow<List<VehicleRealtime>> = repository.vehicles

    // Traffic Alerts
    val alerts: StateFlow<List<TrafficAlert>> = repository.alerts

    // Database Flows
    val tickets: StateFlow<List<TicketEntity>> = repository.allTickets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reports: StateFlow<List<CitizenReportEntity>> = repository.allReports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val passes: StateFlow<List<PassSubscriptionEntity>> = repository.allPasses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected vehicle for details
    private val _selectedVehicle = MutableStateFlow<VehicleRealtime?>(null)
    val selectedVehicle: StateFlow<VehicleRealtime?> = _selectedVehicle.asStateFlow()

    // Payment state
    private val _paymentState = MutableStateFlow(PaymentUiState())
    val paymentState: StateFlow<PaymentUiState> = _paymentState.asStateFlow()
    private val paymentProcessor = FallbackPaymentProcessor(
        MobileMoneyGateway { _, _, _ -> true }
    )

    // Snackbar message
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    // Report Incident Dialog State
    private val _isReportIncidentDialogOpen = MutableStateFlow(false)
    val isReportIncidentDialogOpen: StateFlow<Boolean> = _isReportIncidentDialogOpen.asStateFlow()

    private val _preselectedIncidentCategory = MutableStateFlow<String?>(null)
    val preselectedIncidentCategory: StateFlow<String?> = _preselectedIncidentCategory.asStateFlow()

    init {
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
        repository.startRealtimeSimulation(viewModelScope)
    }

    fun selectTab(tab: TransitTab) {
        _currentTab.value = tab
    }

    fun setCategoryFilter(category: TransportCategory?) {
        _selectedCategoryFilter.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setDataSaverEnabled(enabled: Boolean) {
        settingsRepository.setDataSaverEnabled(enabled)
    }

    fun refreshUserLocation() {
        val hasPermission = ContextCompat.checkSelfPermission(
            getApplication(), android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            getApplication(), android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasPermission) return
        val manager = getApplication<Application>().getSystemService(LocationManager::class.java)
        val location = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
            .asSequence()
            .mapNotNull { provider -> runCatching { manager.getLastKnownLocation(provider) }.getOrNull() }
            .maxByOrNull { it.time }
        location?.let { _userLocation.value = UserLocation(it.latitude, it.longitude) }
    }

    fun selectVehicle(vehicle: VehicleRealtime?) {
        _selectedVehicle.value = vehicle
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    // Payment Actions
    fun openPaymentModal(
        lineCode: String,
        category: TransportCategory,
        origin: String,
        destination: String,
        fareCfa: Int
    ) {
        _paymentState.value = PaymentUiState(
            isOpen = true,
            lineCode = lineCode,
            category = category,
            origin = origin,
            destination = destination,
            fareCfa = fareCfa,
            selectedMethod = "Wave",
            phoneNumber = "77 123 45 67"
        )
    }

    fun closePaymentModal() {
        _paymentState.value = PaymentUiState(isOpen = false)
    }

    fun updatePaymentMethod(method: String) {
        _paymentState.value = _paymentState.value.copy(selectedMethod = method)
    }

    fun updatePhoneNumber(phone: String) {
        _paymentState.value = _paymentState.value.copy(phoneNumber = phone)
    }

    fun executePayment() {
        val state = _paymentState.value
        viewModelScope.launch {
            _paymentState.value = state.copy(isProcessing = true)
            kotlinx.coroutines.delay(1200)
            val payment = paymentProcessor.charge(
                preferredMethod = state.selectedMethod,
                phoneNumber = state.phoneNumber,
                amountCfa = state.fareCfa
            )
            if (!payment.successful) {
                _paymentState.value = state.copy(isProcessing = false)
                _snackbarMessage.value = "Paiement indisponible. Réessayez dans quelques instants."
                return@launch
            }
            val newTicket = repository.buyTicket(
                lineCode = state.lineCode,
                category = state.category,
                origin = state.origin,
                destination = state.destination,
                fareCfa = state.fareCfa,
                paymentMethod = payment.methodUsed
            )
            _paymentState.value = state.copy(
                isProcessing = false,
                selectedMethod = payment.methodUsed,
                completedTicket = newTicket
            )
            val fallbackNotice = if (payment.methodUsed != state.selectedMethod) {
                " ${state.selectedMethod} indisponible, ${payment.methodUsed} utilisé."
            } else ""
            _snackbarMessage.value = "Ticket ${newTicket.ticketNumber} généré avec succès !$fallbackNotice"
        }
    }

    fun validateTicket(ticketId: Int) {
        viewModelScope.launch {
            val validated = repository.validateTicket(ticketId)
            _snackbarMessage.value = if (validated) {
                "Ticket validé auprès du contrôleur / borne."
            } else {
                "Ticket expiré, déjà utilisé ou introuvable."
            }
        }
    }

    // Citizen Reports & Incident Management
    fun openReportIncidentDialog(category: String? = null) {
        _preselectedIncidentCategory.value = category
        _isReportIncidentDialogOpen.value = true
    }

    fun closeReportIncidentDialog() {
        _isReportIncidentDialogOpen.value = false
        _preselectedIncidentCategory.value = null
    }

    fun submitReport(
        category: String,
        location: String,
        description: String,
        severity: String,
        author: String,
        announceVoice: Boolean = false
    ) {
        viewModelScope.launch {
            val savedReport = repository.addCitizenReport(
                category = category,
                locationName = location,
                description = description,
                severity = severity,
                authorName = author
            )
            _isReportIncidentDialogOpen.value = false
            _preselectedIncidentCategory.value = null
            _snackbarMessage.value = "Incident enregistré : ${savedReport.category} à ${savedReport.locationName}"
            if (announceVoice) {
                val voiceText = "Alerte signalée : ${savedReport.category} à ${savedReport.locationName}. Merci pour la communauté."
                voiceAnnouncer.speak(voiceText)
            }
        }
    }

    fun confirmReport(reportId: Int) {
        viewModelScope.launch {
            repository.confirmReport(reportId)
            _snackbarMessage.value = "Merci ! Votre confirmation aide tous les usagers."
        }
    }

    fun deleteReport(reportId: Int) {
        viewModelScope.launch {
            repository.deleteCitizenReport(reportId)
            _snackbarMessage.value = "Signalement retiré."
        }
    }

    // Pass Subscription
    fun subscribeNewPass(
        title: String,
        holderName: String,
        category: String,
        priceCfa: Int
    ) {
        viewModelScope.launch {
            repository.subscribePass(title, holderName, category, priceCfa)
            _snackbarMessage.value = "Nouvel abonnement activé avec succès !"
        }
    }

    // Language & Voice
    fun setLanguage(language: NationalLanguage) {
        languagePreferencesRepository.setLanguage(language)
        _isLanguageSheetOpen.value = false
        _snackbarMessage.value = "Langue changée : ${language.displayName}"
    }

    fun toggleNextLanguage(): NationalLanguage {
        val next = languagePreferencesRepository.toggleNextLanguage()
        _snackbarMessage.value = "Langue : ${next.displayName}"
        return next
    }

    fun openLanguageSheet() {
        _isLanguageSheetOpen.value = true
    }

    fun closeLanguageSheet() {
        _isLanguageSheetOpen.value = false
    }

    fun announceCurrentLanguageGreeting() {
        val lang = selectedLanguage.value
        val text = "${lang.greeting}. ${lang.trafficAlertVocalSummary}"
        voiceAnnouncer.speak(text)
    }

    fun announceAlert(alert: TrafficAlert) {
        val lang = selectedLanguage.value
        val text = when (lang) {
            NationalLanguage.FRENCH -> "Alerte circulation à ${alert.location}. ${alert.title}. Retard estimé : ${alert.delayEstimateMinutes} minutes. Itinéraire recommandé : ${alert.alternativeRoute}"
            NationalLanguage.WOLOF -> "Yégle ci ${alert.location}. ${alert.title}. Retard bi : ${alert.delayEstimateMinutes} simili. Yoon bu gën : ${alert.alternativeRoute}"
            else -> "${lang.displayName} : ${alert.location}. ${alert.title}. +${alert.delayEstimateMinutes} min. ${lang.trafficAlertVocalSummary}"
        }
        voiceAnnouncer.speak(text)
    }

    fun stopVoiceAnnouncement() {
        voiceAnnouncer.stop()
    }

    override fun onCleared() {
        super.onCleared()
        voiceAnnouncer.shutdown()
    }

    // WhatsApp Sharing
    fun shareAlertViaWhatsApp(context: Context, alert: TrafficAlert) {
        val message = "🚨 *Alerte Trafic Téranga Moov Sénégal*\n\n" +
                "📍 *Lieu :* ${alert.location}\n" +
                "⚠️ *Situation :* ${alert.title}\n" +
                "⏱️ *Retard estimé :* +${alert.delayEstimateMinutes} min\n" +
                "🔄 *Itinéraire alternatif :* ${alert.alternativeRoute}\n\n" +
                "_Partagé depuis Téranga Moov - La super-app citoyenne de mobilité au Sénégal_"
        repository.shareViaWhatsApp(context, message)
    }

    fun shareTripViaWhatsApp(context: Context, vehicle: VehicleRealtime) {
        val message = "🚌 *Mon Trajet en direct • Téranga Moov*\n\n" +
                "Je suis à bord de : *${vehicle.lineCode}* (${vehicle.category.label})\n" +
                "📍 *Arrêt actuel :* ${vehicle.currentStop}\n" +
                "🎯 *Direction :* ${vehicle.destination}\n" +
                "⏱️ *Arrivée prévue dans :* ~${vehicle.etaMinutes} min\n" +
                "👥 *Affluence :* ${vehicle.crowding.label}\n\n" +
                "_Suivi GPS en temps réel avec Téranga Moov_"
        repository.shareViaWhatsApp(context, message)
    }
}
