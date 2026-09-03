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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.TransportCategory
import com.example.data.model.TransportLineInfo
import com.example.ui.TransitViewModel
import com.example.ui.components.TransitCategoryBadge
import com.example.ui.theme.HighDensityAlertRed
import com.example.ui.theme.HighDensityBg
import com.example.ui.theme.HighDensityIndigo
import com.example.ui.theme.HighDensityIndigoBorder
import com.example.ui.theme.HighDensityIndigoDark
import com.example.ui.theme.HighDensityIndigoLight
import com.example.ui.theme.HighDensityLiveGreen
import com.example.ui.theme.HighDensitySlate100
import com.example.ui.theme.HighDensitySlate200
import com.example.ui.theme.HighDensitySlate400
import com.example.ui.theme.HighDensitySlate500
import com.example.ui.theme.HighDensitySlate700
import com.example.ui.theme.HighDensitySlate900
import com.example.ui.theme.HighDensitySurface

data class ItineraryProposal(
    val title: String,
    val durationMinutes: Int,
    val fareCfa: Int,
    val recommended: Boolean,
    val legs: List<String>,
    val delayRisk: String
)

@Composable
fun RoutesScreen(
    viewModel: TransitViewModel,
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableIntStateOf(0) } // 0 = Calculateur d'itinéraire, 1 = Annuaire des lignes
    var originInput by remember { mutableStateOf("Guédiawaye Arrêt Double Less") }
    var destinationInput by remember { mutableStateOf("Plateau - Gare Petersen") }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf<TransportCategory?>(null) }

    val allLines = viewModel.repository.linesCatalog

    val filteredLines = allLines.filter { line ->
        (selectedCategoryFilter == null || line.category == selectedCategoryFilter) &&
                (searchQuery.isBlank() ||
                        line.lineCode.contains(searchQuery, ignoreCase = true) ||
                        line.origin.contains(searchQuery, ignoreCase = true) ||
                        line.destination.contains(searchQuery, ignoreCase = true) ||
                        line.via.contains(searchQuery, ignoreCase = true))
    }

    val sampleProposals = listOf(
        ItineraryProposal(
            title = "Option Rapide : BRT Dakar B1 Omnibus",
            durationMinutes = 32,
            fareCfa = 400,
            recommended = true,
            legs = listOf("Couloir Dédié Express", "Sans embouteillages", "Fréquence 4 min"),
            delayRisk = "Faible (Priorité aux carrefours)"
        ),
        ItineraryProposal(
            title = "Option Économique : AFTU Tata 219",
            durationMinutes = 55,
            fareCfa = 150,
            recommended = false,
            legs = listOf("Guédiawaye", "Castors", "Gare Petersen"),
            delayRisk = "Moyen (Ralentissements vers Castors)"
        ),
        ItineraryProposal(
            title = "Option Combinée : Taxi Clando + TER",
            durationMinutes = 40,
            fareCfa = 1750,
            recommended = false,
            legs = listOf("Taxi vers Gare Beaux Maraîchers (Pikine)", "TER vers Dakar Centrale (14 min)"),
            delayRisk = "Très faible"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(HighDensityBg)
            .testTag("screen_routes"),
        contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Lignes & Itinéraires de Transport",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = HighDensitySlate900
            )
            Text(
                text = "Planifiez vos déplacements et évitez les embouteillages urbains",
                style = MaterialTheme.typography.bodySmall,
                color = HighDensitySlate500
            )
        }

        // High Density Switcher Tabs
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = HighDensitySlate100,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, HighDensitySlate200, RoundedCornerShape(14.dp))
                    .padding(4.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selectedSection == 0) Color.White else Color.Transparent)
                            .clickable { selectedSection = 0 }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Itinéraire",
                            fontSize = 13.sp,
                            fontWeight = if (selectedSection == 0) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedSection == 0) HighDensitySlate900 else HighDensitySlate500
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selectedSection == 1) Color.White else Color.Transparent)
                            .clickable { selectedSection = 1 }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Lignes Officielles",
                            fontSize = 13.sp,
                            fontWeight = if (selectedSection == 1) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedSection == 1) HighDensitySlate900 else HighDensitySlate500
                        )
                    }
                }
            }
        }

        if (selectedSection == 0) {
            // Itinerary Planner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = HighDensitySurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, HighDensitySlate200)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = null,
                                tint = HighDensityIndigo,
                                modifier = Modifier.size(18.dp)
                            )
                            OutlinedTextField(
                                value = originInput,
                                onValueChange = { originInput = it },
                                label = { Text("Point de départ") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth().testTag("input_itinerary_origin")
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(
                                onClick = {
                                    val temp = originInput
                                    originInput = destinationInput
                                    destinationInput = temp
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwapVert,
                                    contentDescription = "Inverser",
                                    tint = HighDensityIndigo
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = HighDensityAlertRed,
                                modifier = Modifier.size(18.dp)
                            )
                            OutlinedTextField(
                                value = destinationInput,
                                onValueChange = { destinationInput = it },
                                label = { Text("Destination souhaitée") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth().testTag("input_itinerary_destination")
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { /* Refresh proposals */ },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("btn_find_itinerary"),
                            colors = ButtonDefaults.buttonColors(containerColor = HighDensityIndigo)
                        ) {
                            Icon(imageVector = Icons.Default.AltRoute, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Rechercher le meilleur trajet", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Options de trajet suggérées",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = HighDensitySlate900
                )
            }

            items(sampleProposals) { proposal ->
                ItineraryProposalCard(
                    proposal = proposal,
                    onSelect = {
                        viewModel.openPaymentModal(
                            lineCode = proposal.title.substringBefore(" :"),
                            category = if (proposal.title.contains("BRT")) TransportCategory.BRT
                            else if (proposal.title.contains("Tata")) TransportCategory.AFTU_TATA
                            else TransportCategory.TER,
                            origin = originInput,
                            destination = destinationInput,
                            fareCfa = proposal.fareCfa
                        )
                    }
                )
            }
        } else {
            // Lines Catalog Section
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Rechercher une ligne, un arrêt ou quartier...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = HighDensitySlate400) },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Effacer")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().testTag("input_search_lines")
                )
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedCategoryFilter == null,
                            onClick = { selectedCategoryFilter = null },
                            label = { Text("Toutes (${allLines.size})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = HighDensityIndigo,
                                selectedLabelColor = Color.White,
                                containerColor = HighDensitySurface,
                                labelColor = HighDensitySlate700
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedCategoryFilter == null,
                                borderColor = if (selectedCategoryFilter == null) HighDensityIndigo else HighDensitySlate200,
                                borderWidth = 1.dp
                            )
                        )
                    }
                    items(TransportCategory.values()) { cat ->
                        val isSelected = selectedCategoryFilter == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategoryFilter = cat },
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

            items(filteredLines, key = { it.lineCode }) { line ->
                LineCatalogCard(
                    line = line,
                    onBuyTicket = {
                        viewModel.openPaymentModal(
                            lineCode = line.lineCode,
                            category = line.category,
                            origin = line.origin,
                            destination = line.destination,
                            fareCfa = line.standardFareCfa
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ItineraryProposalCard(
    proposal: ItineraryProposal,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("proposal_card_${proposal.fareCfa}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (proposal.recommended) HighDensityIndigoLight else HighDensitySurface
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (proposal.recommended) HighDensityIndigoBorder else HighDensitySlate200
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (proposal.recommended) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = HighDensityIndigo
                    ) {
                        Text(
                            text = "CONSEILLÉ • LE PLUS RAPIDE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(4.dp))
                }

                Text(
                    text = "${proposal.fareCfa} FCFA",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = HighDensityIndigoDark
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = proposal.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = HighDensitySlate900
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Durée estimée : ~${proposal.durationMinutes} min",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = HighDensitySlate700
            )

            Text(
                text = "Trafic : ${proposal.delayRisk}",
                style = MaterialTheme.typography.bodySmall,
                color = HighDensitySlate500
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = proposal.legs.joinToString(" • "),
                    style = MaterialTheme.typography.labelSmall,
                    color = HighDensitySlate400,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onSelect,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HighDensityIndigo),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Choisir", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun LineCatalogCard(
    line: TransportLineInfo,
    onBuyTicket: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = HighDensitySurface),
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
                    TransitCategoryBadge(category = line.category)
                    Text(
                        text = line.lineCode,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = HighDensitySlate900
                    )
                }

                Text(
                    text = "${line.standardFareCfa} FCFA",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = HighDensityIndigo
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${line.origin} ➔ ${line.destination}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = HighDensitySlate900
            )

            Text(
                text = "Via : ${line.via}",
                style = MaterialTheme.typography.bodySmall,
                color = HighDensitySlate500
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⏱️ Toutes les ${line.frequencyMinutes} min",
                        style = MaterialTheme.typography.labelSmall,
                        color = HighDensitySlate700
                    )
                    Text(
                        text = "🕒 ${line.operatingHours}",
                        style = MaterialTheme.typography.labelSmall,
                        color = HighDensitySlate500
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = HighDensityIndigoLight,
                    modifier = Modifier.clickable { onBuyTicket() }
                ) {
                    Text(
                        text = "Ticket Wave / OM",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = HighDensityIndigo,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}

