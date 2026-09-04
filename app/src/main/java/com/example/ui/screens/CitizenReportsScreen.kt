package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CarCrash
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Flood
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Traffic
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CitizenReportEntity
import com.example.data.model.IncidentCategory
import com.example.ui.TransitViewModel
import com.example.ui.components.ReportIncidentDialog
import com.example.ui.theme.HighDensityAlertBg
import com.example.ui.theme.HighDensityAlertBorder
import com.example.ui.theme.HighDensityAlertRed
import com.example.ui.theme.HighDensityAlertText
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
import com.example.ui.theme.terangaPattern
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CitizenReportsScreen(
    viewModel: TransitViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val reports by viewModel.reports.collectAsStateWithLifecycle()
    val isReportDialogOpen by viewModel.isReportIncidentDialogOpen.collectAsStateWithLifecycle()
    val preselectedCategory by viewModel.preselectedIncidentCategory.collectAsStateWithLifecycle()

    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }

    // Counts by category
    val accidentCount = reports.count { it.category.contains("Accident", ignoreCase = true) }
    val trafficCount = reports.count {
        it.category.contains("Trafic", ignoreCase = true) || it.category.contains("Embouteillage", ignoreCase = true)
    }
    val safetyCount = reports.count {
        it.category.contains("Sécurité", ignoreCase = true) || it.category.contains("Securite", ignoreCase = true)
    }

    val filterOptions = listOf(
        Pair(null, "Tous (${reports.size})"),
        Pair("Accident", "🚨 Accidents ($accidentCount)"),
        Pair("Trafic", "🚦 Trafic ($trafficCount)"),
        Pair("Sécurité", "🛡️ Sécurité ($safetyCount)"),
        Pair("Travaux", "🚧 Travaux"),
        Pair("Panne de bus", "🚌 Pannes"),
        Pair("Zone inondée", "🌊 Inondations")
    )

    val filteredReports = when (selectedCategoryFilter) {
        null -> reports
        "Trafic" -> reports.filter {
            it.category.contains("Trafic", ignoreCase = true) || it.category.contains("Embouteillage", ignoreCase = true)
        }
        "Accident" -> reports.filter { it.category.contains("Accident", ignoreCase = true) }
        "Sécurité" -> reports.filter {
            it.category.contains("Sécurité", ignoreCase = true) || it.category.contains("Securite", ignoreCase = true)
        }
        else -> reports.filter { it.category.equals(selectedCategoryFilter, ignoreCase = true) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .terangaPattern(alpha = 0.05f)
            .testTag("screen_citizen_reports")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Header & Community Title
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Signalements Citoyens",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = HighDensitySlate900
                        )
                        Text(
                            text = "En direct de Dakar",
                            style = MaterialTheme.typography.bodySmall,
                            color = HighDensitySlate500
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = HighDensityIndigoLight,
                        border = BorderStroke(1.dp, HighDensitySlate200)
                    ) {
                        Text(
                            text = "${reports.size} alertes",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = HighDensityIndigo,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        runCatching {
                            context.startActivity(android.content.Intent(android.content.Intent.ACTION_DIAL).apply {
                                data = android.net.Uri.parse("tel:112")
                            })
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("btn_emergency_sos"),
                    colors = ButtonDefaults.buttonColors(containerColor = HighDensityAlertRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Security, contentDescription = "Appeler les secours", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SOS • Appeler les secours", fontWeight = FontWeight.Bold)
                }
            }

            // 2. Dedicated Incident Reporting Action Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("banner_report_incident"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = HighDensitySurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    border = BorderStroke(1.dp, HighDensitySlate200)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = HighDensityAlertBg,
                                    modifier = Modifier.size(38.dp),
                                    border = BorderStroke(1.dp, HighDensityAlertBorder)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.ReportProblem,
                                            contentDescription = null,
                                            tint = HighDensityAlertRed,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = "Signaler un incident sur la route",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = HighDensitySlate900
                                    )
                                    Text(
                                        text = "Prévenez instantanément la communauté de Dakar",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = HighDensitySlate500
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick action buttons for the three main requested incident types
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // 1. Accident Quick Button
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFEF2F2))
                                    .border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(12.dp))
                                    .clickable { viewModel.openReportIncidentDialog("Accident") }
                                    .padding(vertical = 10.dp)
                                    .testTag("btn_quick_report_accident"),
                                color = Color.Transparent
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Text("🚨", fontSize = 16.sp)
                                    Text(
                                        "Accident",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFDC2626)
                                    )
                                }
                            }

                            // 2. Traffic Quick Button
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFFF7ED))
                                    .border(1.dp, Color(0xFFFED7AA), RoundedCornerShape(12.dp))
                                    .clickable { viewModel.openReportIncidentDialog("Trafic") }
                                    .padding(vertical = 10.dp)
                                    .testTag("btn_quick_report_traffic"),
                                color = Color.Transparent
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Text("🚦", fontSize = 16.sp)
                                    Text(
                                        "Trafic",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFEA580C)
                                    )
                                }
                            }

                            // 3. Safety Quick Button
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFDF2F8))
                                    .border(1.dp, Color(0xFFFBCFE8), RoundedCornerShape(12.dp))
                                    .clickable { viewModel.openReportIncidentDialog("Sécurité") }
                                    .padding(vertical = 10.dp)
                                    .testTag("btn_quick_report_safety"),
                                color = Color.Transparent
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Text("🛡️", fontSize = 16.sp)
                                    Text(
                                        "Sécurité",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFDB2777)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Category Filter Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 2.dp)
                ) {
                    items(filterOptions) { (key, label) ->
                        val isSelected = selectedCategoryFilter == key
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedCategoryFilter = if (selectedCategoryFilter == key) null else key
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = HighDensityIndigoLight,
                                selectedLabelColor = HighDensityIndigo
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) HighDensityIndigo else HighDensitySlate200
                            )
                        )
                    }
                }
            }

            // 4. Reports List or Empty State
            if (filteredReports.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = HighDensitySlate100)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("✨", fontSize = 28.sp)
                            Text(
                                text = "Aucun incident signalé dans cette catégorie",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = HighDensitySlate700
                            )
                            Text(
                                text = "Soyez le premier à informer les usagers en direct.",
                                style = MaterialTheme.typography.bodySmall,
                                color = HighDensitySlate500
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { viewModel.openReportIncidentDialog(selectedCategoryFilter) },
                                colors = ButtonDefaults.buttonColors(containerColor = HighDensityIndigo),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Signaler un incident")
                            }
                        }
                    }
                }
            } else {
                items(filteredReports, key = { it.id }) { report ->
                    CitizenReportCard(
                        report = report,
                        onConfirm = { viewModel.confirmReport(report.id) },
                        onDelete = { viewModel.deleteReport(report.id) },
                        onListen = {
                            viewModel.voiceAnnouncer.speak(
                                "Alerte ${report.category} à ${report.locationName}. ${report.description}"
                            )
                        },
                        onShare = {
                            val shareText = "⚠️ *Signalement Incident • Téranga Moov*\n" +
                                    "🚨 *${report.category}* : ${report.locationName}\n" +
                                    "📝 ${report.description}\n" +
                                    "⚡ Degré : ${report.severity} (${report.confirmationsCount} confirmations)\n\n" +
                                    "_Partagé via Téranga Moov Dakar_"
                            viewModel.repository.shareViaWhatsApp(context, shareText)
                        }
                    )
                }
            }
        }

        // Floating Action Button to quickly report an incident
        FloatingActionButton(
            onClick = { viewModel.openReportIncidentDialog() },
            containerColor = HighDensityIndigo,
            contentColor = Color.White,
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 76.dp, end = 20.dp)
                .testTag("fab_add_report")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Signaler un incident")
                Text("Signaler", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }

        // Incident Reporting Dialog
        if (isReportDialogOpen) {
            ReportIncidentDialog(
                initialCategory = preselectedCategory,
                onDismiss = { viewModel.closeReportIncidentDialog() },
                onSubmit = { cat, loc, desc, sev, author, announceVoice ->
                    viewModel.submitReport(
                        category = cat,
                        location = loc,
                        description = desc,
                        severity = sev,
                        author = author,
                        announceVoice = announceVoice
                    )
                }
            )
        }
    }
}

@Composable
private fun CitizenReportCard(
    report: CitizenReportEntity,
    onConfirm: () -> Unit,
    onDelete: () -> Unit,
    onListen: () -> Unit,
    onShare: () -> Unit
) {
    val formatter = remember { SimpleDateFormat("HH:mm", Locale.FRENCH) }
    val timeStr = remember(report.timestamp) { formatter.format(Date(report.timestamp)) }

    val (icon, color, emoji) = when {
        report.category.contains("Accident", ignoreCase = true) ->
            Triple(Icons.Default.CarCrash, Color(0xFFDC2626), "🚨")
        report.category.contains("Trafic", ignoreCase = true) || report.category.contains("Embouteillage", ignoreCase = true) ->
            Triple(Icons.Default.Traffic, Color(0xFFEA580C), "🚦")
        report.category.contains("Sécurité", ignoreCase = true) || report.category.contains("Securite", ignoreCase = true) ->
            Triple(Icons.Default.Security, Color(0xFFDB2777), "🛡️")
        report.category.contains("Travaux", ignoreCase = true) ->
            Triple(Icons.Default.Construction, Color(0xFFD97706), "🚧")
        report.category.contains("Panne", ignoreCase = true) ->
            Triple(Icons.Default.DirectionsBus, Color(0xFF7C3AED), "🚌")
        report.category.contains("Inond", ignoreCase = true) ->
            Triple(Icons.Default.Flood, Color(0xFF0284C7), "🌊")
        else ->
            Triple(Icons.Default.Warning, HighDensitySlate700, "⚠️")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("report_card_${report.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HighDensitySurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, HighDensitySlate200)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Category Badge + Severity
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = color.copy(alpha = 0.12f),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = icon,
                                contentDescription = report.category,
                                tint = color,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(emoji, fontSize = 13.sp)
                            Text(
                                text = report.category,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = color
                            )
                        }
                        Text(
                            text = "Signalé à $timeStr par ${report.authorName}",
                            style = MaterialTheme.typography.labelSmall,
                            color = HighDensitySlate500
                        )
                    }
                }

                // Severity Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (report.severity) {
                        "Critique" -> HighDensityAlertBg
                        "Important" -> Color(0xFFFEF3C7)
                        else -> Color(0xFFDCFCE7)
                    },
                    border = BorderStroke(
                        0.5.dp,
                        when (report.severity) {
                            "Critique" -> HighDensityAlertBorder
                            "Important" -> Color(0xFFFDE68A)
                            else -> Color(0xFFBBF7D0)
                        }
                    )
                ) {
                    Text(
                        text = report.severity,
                        color = when (report.severity) {
                            "Critique" -> HighDensityAlertText
                            "Important" -> Color(0xFFB45309)
                            else -> Color(0xFF15803D)
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Location
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = HighDensityIndigo,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = report.locationName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = HighDensitySlate900
                )
            }

            // Description
            if (report.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = report.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = HighDensitySlate700,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Actions Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "👍 ${report.confirmationsCount} confirmation${if (report.confirmationsCount > 1) "s" else ""}",
                        style = MaterialTheme.typography.labelSmall,
                        color = HighDensitySlate500
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Voice readout button
                    IconButton(
                        onClick = onListen,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(HighDensitySlate100)
                            .testTag("btn_listen_report_${report.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Écouter l'alerte en vocal",
                            tint = HighDensityIndigo,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Share on WhatsApp
                    IconButton(
                        onClick = onShare,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDCFCE7))
                            .testTag("btn_share_whatsapp_report_${report.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Partager sur WhatsApp",
                            tint = Color(0xFF16A34A),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Upvote / Confirm button
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = HighDensityIndigoLight,
                        modifier = Modifier
                            .clickable { onConfirm() }
                            .testTag("btn_confirm_report_${report.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ThumbUp,
                                contentDescription = "Confirmer",
                                tint = HighDensityIndigo,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Confirmer",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = HighDensityIndigo
                            )
                        }
                    }

                    // Delete / Retract button
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .testTag("btn_delete_report_${report.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Supprimer le signalement",
                            tint = HighDensitySlate400,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
