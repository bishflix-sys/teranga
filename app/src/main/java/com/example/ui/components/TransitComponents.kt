package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsTransit
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import org.json.JSONArray
import com.example.data.model.CitizenReportEntity
import com.example.data.model.CrowdingLevel
import com.example.data.model.PassSubscriptionEntity
import com.example.data.model.TicketEntity
import com.example.data.model.TrafficAlert
import com.example.data.model.TransportCategory
import com.example.data.model.VehicleRealtime
import com.example.ui.UserLocation
import com.example.ui.PaymentUiState
import com.example.ui.theme.HighDensityAlertBg
import com.example.ui.theme.HighDensityAlertBorder
import com.example.ui.theme.HighDensityAlertRed
import com.example.ui.theme.HighDensityAlertText
import com.example.ui.theme.HighDensityCardBg
import com.example.ui.theme.HighDensityCardGlow
import com.example.ui.theme.HighDensityIndigo
import com.example.ui.theme.HighDensityIndigoBorder
import com.example.ui.theme.HighDensityIndigoDark
import com.example.ui.theme.HighDensityIndigoLight
import com.example.ui.theme.HighDensityLiveGreen
import com.example.ui.theme.HighDensityLiveGreenBg
import com.example.ui.theme.HighDensitySlate100
import com.example.ui.theme.HighDensitySlate200
import com.example.ui.theme.HighDensitySlate400
import com.example.ui.theme.HighDensitySlate500
import com.example.ui.theme.HighDensitySlate700
import com.example.ui.theme.HighDensitySlate900
import com.example.ui.theme.HighDensitySurface
import com.example.ui.theme.ModeBrtBg
import com.example.ui.theme.ModeBrtIndigo
import com.example.ui.theme.ModeCarRapideBg
import com.example.ui.theme.ModeCarRapideOrange
import com.example.ui.theme.ModeDddBg
import com.example.ui.theme.ModeDddBlue
import com.example.ui.theme.ModeTataAmber
import com.example.ui.theme.ModeTataBg
import com.example.ui.theme.ModeTaxiBg
import com.example.ui.theme.ModeTaxiBrown
import com.example.ui.theme.ModeTerBg
import com.example.ui.theme.ModeTerCyan
import com.example.ui.theme.SunuFreeMoney
import com.example.ui.theme.SunuGoldSecondary
import com.example.ui.theme.SunuGreenPrimary
import com.example.ui.theme.SunuOrangeMoney
import com.example.ui.theme.SunuWaveBlue

@Composable
fun TransitCategoryBadge(category: TransportCategory, modifier: Modifier = Modifier) {
    val (bgColor, textColor, icon) = when (category) {
        TransportCategory.DAKAR_DEM_DIKK -> Triple(ModeDddBg, ModeDddBlue, Icons.Default.DirectionsBus)
        TransportCategory.AFTU_TATA -> Triple(ModeTataBg, ModeTataAmber, Icons.Default.DirectionsBus)
        TransportCategory.BRT -> Triple(ModeBrtBg, ModeBrtIndigo, Icons.Default.ElectricCar)
        TransportCategory.TER -> Triple(ModeTerBg, ModeTerCyan, Icons.Default.DirectionsTransit)
        TransportCategory.CAR_RAPIDE -> Triple(ModeCarRapideBg, ModeCarRapideOrange, Icons.Default.DirectionsBus)
        TransportCategory.TAXI_CLANDO -> Triple(ModeTaxiBg, ModeTaxiBrown, Icons.Default.LocalTaxi)
    }

    Row(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(8.dp))
            .border(1.dp, textColor.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = category.label,
            tint = textColor,
            modifier = Modifier.size(13.dp)
        )
        Text(
            text = category.label,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun CrowdingPill(level: CrowdingLevel, modifier: Modifier = Modifier) {
    val (dotColor, bgColor, textColor, text) = when (level) {
        CrowdingLevel.SEATS_AVAILABLE -> Quad(HighDensityLiveGreen, HighDensityLiveGreenBg, Color(0xFF166534), "Places assises")
        CrowdingLevel.STANDING_ONLY -> Quad(ModeTataAmber, ModeTataBg, Color(0xFF92400E), "Debout")
        CrowdingLevel.FULL -> Quad(HighDensityAlertRed, HighDensityAlertBg, HighDensityAlertText, "Complet")
    }

    Row(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(12.dp))
            .border(1.dp, dotColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(dotColor, CircleShape)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

/** Interactive Leaflet map with a lightweight 3D visual treatment for Dakar transit. */
@Composable
fun TransitLiveMapCanvas(
    vehicles: List<VehicleRealtime>,
    selectedVehicle: VehicleRealtime?,
    onSelectVehicle: (VehicleRealtime) -> Unit,
    userLocation: UserLocation? = null,
    modifier: Modifier = Modifier
) {
    val vehicleJson = remember(vehicles) { vehicles.toLeafletJson() }
    val selectedId = selectedVehicle?.id.orEmpty()
    val userLocationJson = userLocation?.let { "{\"lat\":${it.latitude},\"lng\":${it.longitude}}" } ?: "null"

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(20.dp))
            .testTag("leaflet_live_map"),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.allowFileAccess = true
                settings.allowContentAccess = false
                settings.allowFileAccessFromFileURLs = false
                settings.allowUniversalAccessFromFileURLs = false
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {
                        view.evaluateJavascript(
                            "window.updateVehicles($vehicleJson, '$selectedId', $userLocationJson);",
                            null
                        )
                    }
                }
                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun selectVehicle(id: String) {
                        vehicles.firstOrNull { it.id == id }?.let(onSelectVehicle)
                    }
                }, "TerangaBridge")
                loadUrl("file:///android_asset/leaflet_map.html")
            }
        },
        update = { webView ->
            webView.evaluateJavascript("window.updateVehicles($vehicleJson, '$selectedId', $userLocationJson);", null)
        },
        onRelease = { webView ->
            webView.stopLoading()
            webView.removeJavascriptInterface("TerangaBridge")
            webView.destroy()
        }
    )
}

private fun List<VehicleRealtime>.toLeafletJson(): String = JSONArray().apply {
    forEach { vehicle ->
        put(org.json.JSONObject().apply {
            put("id", vehicle.id)
            put("line", vehicle.lineCode)
            put("number", vehicle.vehicleNumber)
            put("category", vehicle.category.label)
            put("lat", vehicle.latitude)
            put("lng", vehicle.longitude)
            put("heading", vehicle.heading)
        })
    }
}.toString()

/**
 * Detailed card showing active vehicle information with instant ticket purchase and WhatsApp share.
 */
@Composable
fun VehicleDetailCard(
    vehicle: VehicleRealtime,
    onBuyTicket: () -> Unit,
    onShareTrip: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("vehicle_detail_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = HighDensitySurface
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, HighDensitySlate200)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TransitCategoryBadge(category = vehicle.category)
                    Text(
                        text = vehicle.vehicleNumber,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HighDensitySlate900
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fermer",
                        tint = HighDensitySlate400
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Line & destination
            Text(
                text = "${vehicle.lineCode} • Vers ${vehicle.destination}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = HighDensityIndigo
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Current & next stop
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HighDensitySlate100, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "ARRÊT ACTUEL",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = HighDensitySlate400
                    )
                    Text(
                        text = vehicle.currentStop,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = HighDensitySlate700
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = HighDensityIndigo,
                    modifier = Modifier.padding(horizontal = 8.dp).size(18.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "PROCHAIN ARRÊT",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = HighDensitySlate400
                    )
                    Text(
                        text = vehicle.nextStop,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = HighDensitySlate700
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Key indicators: ETA, Crowding, Fare
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Arrivée estimée", style = MaterialTheme.typography.labelSmall, color = HighDensitySlate500)
                    Text(
                        text = "~${vehicle.etaMinutes} min",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = HighDensityLiveGreen
                    )
                }

                CrowdingPill(level = vehicle.crowding)

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Tarif", style = MaterialTheme.typography.labelSmall, color = HighDensitySlate500)
                    Text(
                        text = "${vehicle.fareCfa} FCFA",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = HighDensitySlate900
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onShareTrip,
                    modifier = Modifier.weight(1f).testTag("btn_share_whatsapp"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF25D366)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC))
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Partager WhatsApp",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onBuyTicket,
                    modifier = Modifier.weight(1.3f).testTag("btn_buy_ticket"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HighDensityIndigo)
                ) {
                    Icon(
                        imageVector = Icons.Default.Payment,
                        contentDescription = "Payer",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Payer (Wave/OM)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Traffic Alert Banner with instant alternative route recommendation.
 */
@Composable
fun TrafficAlertCard(
    alert: TrafficAlert,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("traffic_alert_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = HighDensityAlertBg
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, HighDensityAlertBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFEE2E2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Alerte",
                            tint = HighDensityAlertRed,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = alert.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = HighDensityAlertText
                    )
                }
                Text(
                    text = alert.timeAgo,
                    style = MaterialTheme.typography.labelSmall,
                    color = HighDensitySlate500
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "📍 ${alert.location}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = HighDensitySlate900
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "🔄 ${alert.alternativeRoute}",
                style = MaterialTheme.typography.bodySmall,
                color = HighDensitySlate500
            )

            if (alert.delayEstimateMinutes > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Retard estimé : +${alert.delayEstimateMinutes} min",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = HighDensityAlertRed
                    )
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFDCFCE7))
                            .clickable { onShare() }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "WhatsApp",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15803D)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Modern Digital Pass Card (High Density Luxury Carbon Card)
 */
@Composable
fun DigitalPassCard(
    pass: PassSubscriptionEntity,
    onRecharge: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("digital_pass_card"),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = HighDensityCardBg)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(HighDensityCardBg)
                .padding(18.dp)
        ) {
            // Subtle indigo blur glow in top-right corner
            Canvas(modifier = Modifier.matchParentSize()) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(HighDensityCardGlow, Color.Transparent),
                        center = Offset(size.width * 0.85f, size.height * 0.15f),
                        radius = size.width * 0.5f
                    )
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Top header: Label and Wave Connected Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PASS TRANSPORT DIGITAL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.7f),
                        letterSpacing = 1.2.sp
                    )

                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF60A5FA))
                        )
                        Text(
                            text = "Wave Connecté",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Middle: Category and card title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = pass.passTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Titulaire : ${pass.holderName} • ${pass.cardNumber}",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.QrCode2,
                        contentDescription = "Puce",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Bottom: Price / Trips and QR Action Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${pass.tripsRemaining} trajets",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "restants",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.padding(bottom = 3.dp)
                            )
                        }
                        Text(
                            text = "Dernier rechargement: Hier 18:42",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }

                    Surface(
                        onClick = onRecharge,
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        contentColor = HighDensitySlate900,
                        modifier = Modifier.testTag("btn_pass_qr_action")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Payer via QR",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Modal BottomSheet for mobile payment with Wave, Orange Money, and Free Money.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MobilePaymentSheet(
    state: PaymentUiState,
    onDismiss: () -> Unit,
    onSelectMethod: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onConfirmPayment: () -> Unit,
    onValidateTicket: (Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .testTag("mobile_payment_bottom_sheet"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.completedTicket != null) {
                // Ticket successfully issued view
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Succès",
                    tint = SunuGreenPrimary,
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Paiement Effectué avec Succès !",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SunuGreenPrimary
                )
                Text(
                    text = "Votre ticket dématérialisé est prêt à l'emploi.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Ticket preview
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.completedTicket.ticketNumber,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${state.completedTicket.lineCode} • ${state.completedTicket.transportCategory}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${state.completedTicket.origin} ➔ ${state.completedTicket.destination}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .background(Color.White, RoundedCornerShape(8.dp))
                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = "QR Code de validation",
                                tint = Color.Black,
                                modifier = Modifier.size(100.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Montant réglé : ${state.completedTicket.fareCfa} FCFA (${state.completedTicket.paymentMethod})",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().testTag("btn_close_ticket_modal"),
                    colors = ButtonDefaults.buttonColors(containerColor = SunuGreenPrimary)
                ) {
                    Text("Terminer")
                }
            } else {
                // Payment Form
                Text(
                    text = "Paiement Numérique Sécurisé",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${state.lineCode} (${state.category.label}) • ${state.origin} ➔ ${state.destination}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Amount display
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = HighDensityIndigoLight),
                    border = androidx.compose.foundation.BorderStroke(1.dp, HighDensityIndigoBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total à payer",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = HighDensityIndigoDark
                        )
                        Text(
                            text = "${state.fareCfa} FCFA",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = HighDensityIndigoDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Payment Method Selector
                Text(
                    text = "Choisissez votre opérateur",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = HighDensitySlate700,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PaymentMethodTile(
                        title = "Wave",
                        color = SunuWaveBlue,
                        isSelected = state.selectedMethod == "Wave",
                        onClick = { onSelectMethod("Wave") },
                        modifier = Modifier.weight(1f)
                    )
                    PaymentMethodTile(
                        title = "Orange Money",
                        color = SunuOrangeMoney,
                        isSelected = state.selectedMethod == "Orange Money",
                        onClick = { onSelectMethod("Orange Money") },
                        modifier = Modifier.weight(1f)
                    )
                    PaymentMethodTile(
                        title = "Free Money",
                        color = SunuFreeMoney,
                        isSelected = state.selectedMethod == "Free Money",
                        onClick = { onSelectMethod("Free Money") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Phone Input
                OutlinedTextField(
                    value = state.phoneNumber,
                    onValueChange = onPhoneChange,
                    label = { Text("Numéro mobile money (Sénégal)") },
                    placeholder = { Text("77 xxx xx xx") },
                    prefix = { Text("+221 ", fontWeight = FontWeight.Bold, color = HighDensityIndigo) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("input_phone_payment")
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onConfirmPayment,
                    enabled = !state.isProcessing && state.phoneNumber.isNotBlank(),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_confirm_payment"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (state.selectedMethod) {
                            "Wave" -> SunuWaveBlue
                            "Orange Money" -> SunuOrangeMoney
                            "Free Money" -> SunuFreeMoney
                            else -> HighDensityIndigo
                        }
                    )
                ) {
                    if (state.isProcessing) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Validation en cours...", fontWeight = FontWeight.SemiBold)
                    } else {
                        Text(
                            text = "Confirmer avec ${state.selectedMethod}",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PaymentMethodTile(
    title: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(54.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) color else HighDensitySlate200,
                shape = RoundedCornerShape(12.dp)
            ),
        color = if (isSelected) color.copy(alpha = 0.10f) else HighDensitySurface
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(4.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                color = if (isSelected) color else HighDensitySlate700,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Dialog for community / citizen incident reporting.
 */
@Composable
fun AddCitizenReportDialog(
    onDismiss: () -> Unit,
    onSubmit: (category: String, location: String, description: String, severity: String, author: String) -> Unit
) {
    var category by remember { mutableStateOf("Embouteillage") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var severity by remember { mutableStateOf("Important") }
    var author by remember { mutableStateOf("") }

    val categories = listOf("Embouteillage", "Accident", "Travaux", "Panne de bus", "Zone inondée")
    val severities = listOf("Normal", "Important", "Critique")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("dialog_add_report"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = HighDensitySurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, HighDensitySlate200)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Signalement Citoyen",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = HighDensitySlate900
                )
                Text(
                    text = "Aidez vos concitoyens à circuler plus sereinement à Dakar.",
                    style = MaterialTheme.typography.bodySmall,
                    color = HighDensitySlate500
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Nature de l'incident", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = HighDensitySlate700)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.take(3).forEach { cat ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { category = cat }
                                .background(if (category == cat) HighDensityIndigoLight else Color.Transparent)
                                .border(1.dp, if (category == cat) HighDensityIndigo else HighDensitySlate200, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = cat,
                                fontSize = 11.sp,
                                fontWeight = if (category == cat) FontWeight.Bold else FontWeight.Normal,
                                color = if (category == cat) HighDensityIndigoDark else HighDensitySlate700
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Lieu précis (ex: Rond-point Patte d'Oie)") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("input_report_location")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description & conseils d'itinéraire") },
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("input_report_description")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Votre prénom (facultatif)") },
                    placeholder = { Text("Ex: Moussa D.") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Annuler", color = HighDensitySlate700)
                    }
                    Button(
                        onClick = {
                            if (location.isNotBlank()) {
                                onSubmit(category, location, description, severity, author)
                            }
                        },
                        enabled = location.isNotBlank(),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).testTag("btn_submit_report"),
                        colors = ButtonDefaults.buttonColors(containerColor = HighDensityIndigo)
                    ) {
                        Text("Signaler", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
