package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.TransportCategory
import com.example.data.model.VehicleRealtime
import com.example.ui.TransitTab
import com.example.ui.TransitViewModel
import com.example.ui.components.CrowdingPill
import com.example.ui.components.TrafficAlertCard
import com.example.ui.components.TransitCategoryBadge
import com.example.ui.components.TransitLiveMapCanvas
import com.example.ui.components.VehicleDetailCard
import com.example.ui.theme.HighDensityAlertBg
import com.example.ui.theme.HighDensityAlertRed
import com.example.ui.theme.HighDensityAlertText
import com.example.ui.theme.HighDensityBg
import com.example.ui.theme.HighDensityCardBg
import com.example.ui.theme.HighDensityCardGlow
import com.example.ui.theme.HighDensityIndigo
import com.example.ui.theme.HighDensityIndigoLight
import com.example.ui.theme.HighDensityLiveGreen
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

@Composable
fun LiveMapScreen(
    viewModel: TransitViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val vehicles by viewModel.vehicles.collectAsStateWithLifecycle()
    val alerts by viewModel.alerts.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategoryFilter.collectAsStateWithLifecycle()
    val selectedVehicle by viewModel.selectedVehicle.collectAsStateWithLifecycle()
    val passes by viewModel.passes.collectAsStateWithLifecycle()
    val userLocation by viewModel.userLocation.collectAsStateWithLifecycle()

    val filteredVehicles = if (selectedCategory == null) {
        vehicles
    } else {
        vehicles.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(HighDensityBg)
            .testTag("screen_live_map"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Quick Search Bar (High Density pattern)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(HighDensitySlate100)
                    .border(1.dp, HighDensitySlate200, RoundedCornerShape(16.dp))
                    .clickable { viewModel.selectTab(TransitTab.ROUTES) }
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .testTag("high_density_search_bar"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Rechercher",
                    tint = HighDensitySlate400,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Où voulez-vous aller ?",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    color = HighDensitySlate500,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Recherche vocale",
                    tint = HighDensitySlate400,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // 2. Pass Card (High Density Digital Pass widget)
        item {
            val activePass = passes.firstOrNull()
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("high_density_hero_pass"),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = HighDensityCardBg)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(HighDensityCardBg)
                        .padding(18.dp)
                ) {
                    // Top-right radial glow
                    androidx.compose.foundation.Canvas(modifier = Modifier.matchParentSize()) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(HighDensityCardGlow, Color.Transparent),
                                center = Offset(size.width * 0.85f, size.height * 0.15f),
                                radius = size.width * 0.55f
                            )
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TÉRANGA PASS CITOYEN",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.85f),
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

                        // Bottom Balance & QR Action
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = if (activePass != null) "${activePass.tripsRemaining} Trajets" else "14.250",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (activePass != null) "disponibles" else "FCFA",
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
                                onClick = { viewModel.selectTab(TransitTab.TICKETS) },
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White,
                                contentColor = HighDensitySlate900,
                                modifier = Modifier.testTag("btn_hero_pass_qr")
                            ) {
                                Text(
                                    text = "Payer via QR",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Live Radar Map Canvas & Floating Incident Banner
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TransitLiveMapCanvas(
                    vehicles = filteredVehicles,
                    selectedVehicle = selectedVehicle,
                    onSelectVehicle = { viewModel.selectVehicle(it) },
                    userLocation = userLocation
                )

                Surface(
                    onClick = { viewModel.refreshUserLocation() },
                    shape = RoundedCornerShape(12.dp),
                    color = HighDensitySurface,
                    shadowElevation = 3.dp,
                    modifier = Modifier.testTag("btn_around_me")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.MyLocation, contentDescription = "Centrer autour de moi", tint = HighDensityIndigo, modifier = Modifier.size(17.dp))
                        Text("Autour de moi", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = HighDensitySlate900)
                    }
                }

                // High Density Floating Incident Card
                val topAlert = alerts.firstOrNull()
                if (topAlert != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(HighDensitySurface)
                            .border(1.dp, HighDensitySlate200, RoundedCornerShape(18.dp))
                            .clickable { viewModel.shareAlertViaWhatsApp(context, topAlert) }
                            .padding(12.dp)
                            .testTag("floating_map_incident"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(HighDensityAlertBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Alerte",
                                    tint = HighDensityAlertRed,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = topAlert.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = HighDensitySlate900
                                )
                                Text(
                                    text = "+${topAlert.delayEstimateMinutes} min vers ${topAlert.location}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 10.sp,
                                    color = HighDensitySlate500
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            IconButton(
                                onClick = { viewModel.announceAlert(topAlert) },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(HighDensityAlertBg)
                                    .testTag("btn_listen_incident")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Écouter l'alerte en audio",
                                    tint = HighDensityAlertRed,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(HighDensityAlertBg)
                                    .clickable { viewModel.openReportIncidentDialog() }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                                    .testTag("btn_map_report_incident")
                            ) {
                                Text(
                                    text = "SIGNALER",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = HighDensityAlertText,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Selected Vehicle Card overlay if active
        if (selectedVehicle != null) {
            item {
                VehicleDetailCard(
                    vehicle = selectedVehicle!!,
                    onBuyTicket = {
                        val v = selectedVehicle!!
                        viewModel.openPaymentModal(
                            lineCode = v.lineCode,
                            category = v.category,
                            origin = v.currentStop,
                            destination = v.destination,
                            fareCfa = v.fareCfa
                        )
                    },
                    onShareTrip = {
                        viewModel.shareTripViaWhatsApp(context, selectedVehicle!!)
                    },
                    onDismiss = { viewModel.selectVehicle(null) }
                )
            }
        }

        // 4. Category Filter Chips (High Density styling)
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { viewModel.setCategoryFilter(null) },
                        label = { Text("Tous (${vehicles.size})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = HighDensityIndigo,
                            selectedLabelColor = Color.White,
                            containerColor = HighDensitySurface,
                            labelColor = HighDensitySlate700
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selectedCategory == null,
                            borderColor = if (selectedCategory == null) HighDensityIndigo else HighDensitySlate200,
                            borderWidth = 1.dp
                        )
                    )
                }
                items(TransportCategory.values()) { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setCategoryFilter(cat) },
                        label = { Text(cat.label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = HighDensityIndigo,
                            selectedLabelColor = Color.White,
                            containerColor = HighDensitySurface,
                            labelColor = HighDensitySlate700
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) HighDensityIndigo else HighDensitySlate200,
                            borderWidth = 1.dp
                        )
                    )
                }
            }
        }

        // 5. Nearby Vehicles Section Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "À proximité",
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = HighDensitySlate900
                )
                Text(
                    text = "VOIR TOUT",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = HighDensityIndigo,
                    letterSpacing = 1.sp,
                    modifier = Modifier.clickable { viewModel.selectTab(TransitTab.ROUTES) }
                )
            }
        }

        // Nearby vehicles list items
        items(filteredVehicles, key = { it.id }) { veh ->
            HighDensityVehicleItem(
                vehicle = veh,
                onClick = { viewModel.selectVehicle(veh) },
                onQuickPay = {
                    viewModel.openPaymentModal(
                        lineCode = veh.lineCode,
                        category = veh.category,
                        origin = veh.currentStop,
                        destination = veh.destination,
                        fareCfa = veh.fareCfa
                    )
                }
            )
        }
    }
}

@Composable
private fun HighDensityVehicleItem(
    vehicle: VehicleRealtime,
    onClick: () -> Unit,
    onQuickPay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (badgeBg, badgeText, monogram) = when (vehicle.category) {
        TransportCategory.DAKAR_DEM_DIKK -> Triple(ModeDddBg, ModeDddBlue, vehicle.lineCode)
        TransportCategory.AFTU_TATA -> Triple(ModeTataBg, ModeTataAmber, vehicle.lineCode)
        TransportCategory.BRT -> Triple(ModeBrtBg, ModeBrtIndigo, "BRT")
        TransportCategory.TER -> Triple(ModeTerBg, ModeTerCyan, "TER")
        TransportCategory.CAR_RAPIDE -> Triple(ModeCarRapideBg, ModeCarRapideOrange, "CR")
        TransportCategory.TAXI_CLANDO -> Triple(ModeTaxiBg, ModeTaxiBrown, "TX")
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, HighDensitySlate200, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag("vehicle_item_${vehicle.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = HighDensitySurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Mode Badge Tile
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(badgeBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = monogram,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = badgeText
                    )
                }

                Column {
                    Text(
                        text = "Ligne ${vehicle.lineCode} • ${vehicle.destination}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = HighDensitySlate900
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Prochain : ${vehicle.nextStop}",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = HighDensitySlate500
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        CrowdingPill(level = vehicle.crowding)
                        Text(
                            text = "${vehicle.fareCfa} F",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = HighDensityIndigo
                        )
                    }
                }
            }

            // Right arrival status
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (vehicle.etaMinutes <= 2) {
                    Text(
                        text = "Arrive",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = HighDensityLiveGreen
                    )
                } else {
                    Text(
                        text = String.format("%02d min", vehicle.etaMinutes),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = HighDensitySlate900
                    )
                }

                Surface(
                    onClick = onQuickPay,
                    shape = RoundedCornerShape(10.dp),
                    color = HighDensityIndigoLight,
                    contentColor = HighDensityIndigo
                ) {
                    Text(
                        text = "Ticket",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

