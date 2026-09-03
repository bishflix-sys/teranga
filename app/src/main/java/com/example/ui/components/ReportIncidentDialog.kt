package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CarCrash
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Flood
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Traffic
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.IncidentCategory
import com.example.ui.theme.HighDensityAlertBg
import com.example.ui.theme.HighDensityAlertBorder
import com.example.ui.theme.HighDensityAlertRed
import com.example.ui.theme.HighDensityAlertText
import com.example.ui.theme.HighDensityIndigo
import com.example.ui.theme.HighDensityIndigoLight
import com.example.ui.theme.HighDensitySlate100
import com.example.ui.theme.HighDensitySlate200
import com.example.ui.theme.HighDensitySlate400
import com.example.ui.theme.HighDensitySlate500
import com.example.ui.theme.HighDensitySlate700
import com.example.ui.theme.HighDensitySlate900
import com.example.ui.theme.HighDensitySurface

/**
 * Modern Material 3 Dialog for submitting real-time incident reports
 * (Accidents, Traffic delays, Safety issues, etc.) into the Room database.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReportIncidentDialog(
    initialCategory: String? = null,
    onDismiss: () -> Unit,
    onSubmit: (category: String, location: String, description: String, severity: String, author: String, announceVoice: Boolean) -> Unit
) {
    var selectedCategory by remember {
        mutableStateOf(
            if (initialCategory != null) {
                IncidentCategory.fromCategoryName(initialCategory)
            } else {
                IncidentCategory.ACCIDENT
            }
        )
    }

    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var severity by remember { mutableStateOf("Important") } // "Modéré", "Important", "Critique"
    var author by remember { mutableStateOf("") }
    var announceVoice by remember { mutableStateOf(true) }

    // Popular Dakar hotspot locations for fast one-tap input
    val dakarHotspots = listOf(
        "Rond-point Patte d'Oie",
        "Échangeur Malick Sy",
        "Pont de l'Émergence",
        "Corniche Ouest",
        "Autoroute Sortie 9",
        "Avenue Cheikh Anta Diop",
        "Terminus Petersen",
        "Rond-point Liberté 6",
        "Pikine Tally Boumack"
    )

    // Quick advice helpers depending on category
    val quickDescriptionSnippets = when (selectedCategory) {
        IncidentCategory.ACCIDENT -> listOf(
            "Voie de droite neutralisée",
            "Policiers et secours sur place",
            "Collision matérielle, fort ralentissement",
            "Déviation conseillée par la VDN"
        )
        IncidentCategory.TRAFFIC -> listOf(
            "Circulation totalement à l'arrêt",
            "Bouchon dense depuis 20 min",
            "Feu tricolore en panne",
            "Prendre la corniche pour contourner"
        )
        IncidentCategory.SAFETY -> listOf(
            "Éclairage nocturne hors service",
            "Zone sombre, prudence aux piétons",
            "Présence d'obstacles dangereux sur la voie",
            "Passerelle piétonne déconseillée seule"
        )
        IncidentCategory.ROAD_HAZARDS -> listOf(
            "Nid de poule profond sur voie rapide",
            "Chantier en cours, voie rétrécie",
            "Camion immobilisé sur la chaussée"
        )
        IncidentCategory.TRANSIT_ISSUE -> listOf(
            "Bus DDD en panne bloquant le carrefour",
            "Arrêt de bus temporairement inaccessible",
            "Retard de 25 min sur la ligne"
        )
        IncidentCategory.WEATHER_FLOOD -> listOf(
            "Chaussée inondée sur 100m",
            "Passage délicat pour deux-roues et berlines",
            "Ralentissement important suite aux eaux"
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .heightIn(max = 680.dp)
                .testTag("dialog_report_incident"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = HighDensitySurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, HighDensitySlate200)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header
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
                            modifier = Modifier.size(42.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, HighDensityAlertBorder)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.ReportProblem,
                                    contentDescription = null,
                                    tint = HighDensityAlertRed,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "Signaler un Incident",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = HighDensitySlate900
                            )
                            Text(
                                text = "En direct de Dakar",
                                style = MaterialTheme.typography.labelSmall,
                                color = HighDensitySlate500
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp).testTag("btn_close_incident_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fermer",
                            tint = HighDensitySlate500
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 1. Incident Category Selection (Accidents, Traffic, Safety, Hazards, etc.)
                Text(
                    text = "TYPE D'INCIDENT",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = HighDensitySlate500
                )
                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IncidentCategory.values().forEach { category ->
                        val isSelected = selectedCategory == category
                        val categoryColor = Color(category.badgeColorHex)

                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedCategory = category }
                                .background(
                                    if (isSelected) categoryColor.copy(alpha = 0.14f) else HighDensitySlate100
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) categoryColor else HighDensitySlate200,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .testTag("chip_category_${category.name.lowercase()}"),
                            color = Color.Transparent
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(category.emoji, fontSize = 14.sp)
                                Text(
                                    text = category.title,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) categoryColor else HighDensitySlate700
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = categoryColor,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Severity Degree (Modéré, Important, Critique)
                Text(
                    text = "DEGRÉ D'URGENCE / SÉVÉRITÉ",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = HighDensitySlate500
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val severityOptions = listOf(
                        Triple("Modéré", Color(0xFF16A34A), Color(0xFFDCFCE7)),
                        Triple("Important", Color(0xFFD97706), Color(0xFFFEF3C7)),
                        Triple("Critique", HighDensityAlertRed, HighDensityAlertBg)
                    )

                    severityOptions.forEach { (label, tintColor, bgColor) ->
                        val isSelected = severity == label
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { severity = label }
                                .background(if (isSelected) bgColor else HighDensitySlate100)
                                .border(
                                    1.dp,
                                    if (isSelected) tintColor else HighDensitySlate200,
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(vertical = 8.dp)
                                .testTag("btn_severity_${label.lowercase()}"),
                            color = Color.Transparent
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(tintColor, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) tintColor else HighDensitySlate700
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Precise Location with Hotspot Chips
                Text(
                    text = "LIEU PRÉCIS DE L'INCIDENT *",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = HighDensitySlate500
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    placeholder = { Text("Ex: Rond-point Patte d'Oie, vers Autoroute", color = HighDensitySlate400, fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = HighDensityIndigo)
                    },
                    trailingIcon = {
                        if (location.isNotEmpty()) {
                            IconButton(onClick = { location = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Effacer", tint = HighDensitySlate400)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HighDensityIndigo,
                        unfocusedBorderColor = HighDensitySlate200,
                        focusedContainerColor = HighDensitySurface,
                        unfocusedContainerColor = HighDensitySurface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_incident_location")
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Quick location chips
                Text(
                    text = "Lieux fréquents à Dakar :",
                    style = MaterialTheme.typography.labelSmall,
                    color = HighDensitySlate500
                )
                Spacer(modifier = Modifier.height(4.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    dakarHotspots.take(6).forEach { hotspot ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { location = hotspot }
                                .background(if (location == hotspot) HighDensityIndigoLight else HighDensitySlate100)
                                .border(
                                    0.5.dp,
                                    if (location == hotspot) HighDensityIndigo else HighDensitySlate200,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color.Transparent
                        ) {
                            Text(
                                text = hotspot,
                                fontSize = 11.sp,
                                color = if (location == hotspot) HighDensityIndigo else HighDensitySlate700
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 4. Description & Practical Advice
                Text(
                    text = "DESCRIPTION & CONSEILS AUX USAGERS",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = HighDensitySlate500
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = {
                        Text(
                            "Précisez l'impact sur le trafic, les voies bloquées ou les conseils de contournement...",
                            color = HighDensitySlate400,
                            fontSize = 13.sp
                        )
                    },
                    minLines = 2,
                    maxLines = 4,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HighDensityIndigo,
                        unfocusedBorderColor = HighDensitySlate200,
                        focusedContainerColor = HighDensitySurface,
                        unfocusedContainerColor = HighDensitySurface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_incident_description")
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Quick phrases suggestion
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickDescriptionSnippets.take(3).forEach { phrase ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    description = if (description.isBlank()) phrase else "$description. $phrase"
                                }
                                .background(HighDensitySlate100)
                                .border(0.5.dp, HighDensitySlate200, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color.Transparent
                        ) {
                            Text("+ $phrase", fontSize = 11.sp, color = HighDensitySlate700)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 5. Author Name
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = author,
                        onValueChange = { author = it },
                        placeholder = { Text("Votre prénom (ex: Amadou S.)", color = HighDensitySlate400, fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = HighDensitySlate400)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = HighDensityIndigo,
                            unfocusedBorderColor = HighDensitySlate200,
                            focusedContainerColor = HighDensitySurface,
                            unfocusedContainerColor = HighDensitySurface
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_incident_author")
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 6. Voice announcement toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(HighDensitySlate100)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = null,
                            tint = HighDensityIndigo,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "Annonce vocale immédiate",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = HighDensitySlate900
                            )
                            Text(
                                text = "Lit l'alerte à haute voix après enregistrement",
                                fontSize = 11.sp,
                                color = HighDensitySlate500
                            )
                        }
                    }

                    Switch(
                        checked = announceVoice,
                        onCheckedChange = { announceVoice = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = HighDensityIndigo
                        ),
                        modifier = Modifier.testTag("switch_announce_voice")
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f).testTag("btn_cancel_incident")
                    ) {
                        Text("Annuler", color = HighDensitySlate700, fontWeight = FontWeight.Medium)
                    }

                    val isFormValid = location.isNotBlank()
                    Button(
                        onClick = {
                            if (isFormValid) {
                                onSubmit(
                                    selectedCategory.title,
                                    location.trim(),
                                    description.trim(),
                                    severity,
                                    author.trim(),
                                    announceVoice
                                )
                            }
                        },
                        enabled = isFormValid,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HighDensityIndigo,
                            disabledContainerColor = HighDensitySlate200
                        ),
                        modifier = Modifier.weight(1.5f).testTag("btn_submit_incident")
                    ) {
                        Text(
                            text = "Enregistrer l'incident",
                            fontWeight = FontWeight.Bold,
                            color = if (isFormValid) Color.White else HighDensitySlate400
                        )
                    }
                }
            }
        }
    }
}
