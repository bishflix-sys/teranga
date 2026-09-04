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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.PassSubscriptionEntity
import com.example.data.model.TicketEntity
import com.example.data.model.TransportCategory
import com.example.ui.TransitViewModel
import com.example.ui.components.DigitalPassCard
import com.example.data.ticket.TerFareCalculator
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
import com.example.ui.theme.terangaOutlinedTextFieldColors
import com.example.ui.theme.terangaPattern
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TicketsAndPassScreen(
    viewModel: TransitViewModel,
    modifier: Modifier = Modifier
) {
    val tickets by viewModel.tickets.collectAsStateWithLifecycle()
    val passes by viewModel.passes.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Mes Tickets, 1 = Cartes & Abonnements
    var showSubscribeDialog by remember { mutableStateOf(false) }
    var selectedOperator by remember { mutableStateOf(TransportCategory.TER) }
    var selectedTerZones by remember { mutableIntStateOf(3) }
    var firstClassTer by remember { mutableStateOf(false) }
    val selectedFare = if (selectedOperator == TransportCategory.TER) {
        TerFareCalculator.calculate(selectedTerZones, firstClassTer)
    } else {
        when (selectedOperator) {
            TransportCategory.BRT -> 400
            TransportCategory.DAKAR_DEM_DIKK -> 200
            TransportCategory.AFTU_TATA -> 250
            TransportCategory.TAXI_CLANDO -> 1_000
            else -> 500
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(HighDensityBg)
            .terangaPattern(alpha = 0.05f)
            .testTag("screen_tickets_and_pass"),
        contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Titres de Transport & Pass",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = HighDensitySlate900
            )
            Text(
                text = "Paiement dématérialisé avec Wave, Orange Money et Free Money",
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
                            .background(if (selectedTab == 0) Color.White else Color.Transparent)
                            .clickable { selectedTab = 0 }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tickets Unitaires (${tickets.size})",
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTab == 0) HighDensitySlate900 else HighDensitySlate500
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selectedTab == 1) Color.White else Color.Transparent)
                            .clickable { selectedTab = 1 }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Pass & Cartes (${passes.size})",
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTab == 1) HighDensitySlate900 else HighDensitySlate500
                        )
                    }

                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Opérateur", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = HighDensitySlate900)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listOf(TransportCategory.TER, TransportCategory.BRT, TransportCategory.DAKAR_DEM_DIKK, TransportCategory.AFTU_TATA, TransportCategory.TAXI_CLANDO)) { operator ->
                        FilterChip(
                            selected = selectedOperator == operator,
                            onClick = { selectedOperator = operator },
                            label = { Text(operator.shortLabel()) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = HighDensityIndigoLight, selectedLabelColor = HighDensityIndigo)
                        )
                    }
                }
                if (selectedOperator == TransportCategory.TER) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(listOf(1, 2, 3, 4)) { option ->
                            if (option <= 3) {
                                FilterChip(
                                    selected = selectedTerZones == option && !firstClassTer,
                                    onClick = { selectedTerZones = option; firstClassTer = false },
                                    label = { Text("$option zone${if (option > 1) "s" else ""} • ${TerFareCalculator.calculate(option)} F") }
                                )
                            } else {
                                FilterChip(
                                    selected = firstClassTer,
                                    onClick = { firstClassTer = true },
                                    label = { Text("1ère classe • ${TerFareCalculator.FIRST_CLASS_FARE_CFA} F") }
                                )
                            }
                        }
                    }
                }
                Button(
                    onClick = {
                        viewModel.openPaymentModal(
                            lineCode = selectedOperator.shortLabel(),
                            category = selectedOperator,
                            origin = if (selectedOperator == TransportCategory.TER) "Dakar" else "Gare Petersen",
                            destination = if (selectedOperator == TransportCategory.TER) "Diamniadio" else "Centre-ville",
                            fareCfa = selectedFare
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(46.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HighDensityIndigo)
                ) {
                    Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Acheter • $selectedFare FCFA", fontWeight = FontWeight.Bold)
                }
            }
        }

        if (selectedTab == 0) {
            // Tickets Section
            if (tickets.isEmpty()) {
                item {
                    EmptyTicketsCard(
                        onBuyFirst = {
                            viewModel.openPaymentModal(
                                lineCode = "BRT B1",
                                category = TransportCategory.BRT,
                                origin = "Guédiawaye",
                                destination = "Gare Petersen",
                                fareCfa = 400
                            )
                        }
                    )
                }
            } else {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Vos titres de voyage",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = HighDensitySlate900
                        )
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = HighDensityIndigoLight,
                            modifier = Modifier.clickable {
                                viewModel.openPaymentModal(
                                    lineCode = "Ligne 1 DDD",
                                    category = TransportCategory.DAKAR_DEM_DIKK,
                                    origin = "Petersen",
                                    destination = "Almadies",
                                    fareCfa = 200
                                )
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = HighDensityIndigo, modifier = Modifier.size(16.dp))
                                Text("Acheter un ticket", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = HighDensityIndigo)
                            }
                        }
                    }
                }

                items(tickets, key = { it.id }) { ticket ->
                    TicketItemCard(
                        ticket = ticket,
                        onValidate = { viewModel.validateTicket(ticket.id) }
                    )
                }
            }
        } else {
            // Passes Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cartes Numériques Actives",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = HighDensitySlate900
                    )
                    Button(
                        onClick = { showSubscribeDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = HighDensityIndigo),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(36.dp).testTag("btn_add_pass")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Prendre un Pass", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            items(passes, key = { it.id }) { pass ->
                DigitalPassCard(
                    pass = pass,
                    onRecharge = {
                        viewModel.openPaymentModal(
                            lineCode = pass.passTitle,
                            category = TransportCategory.BRT,
                            origin = "Recharge Pass",
                            destination = "30 jours",
                            fareCfa = pass.priceCfa
                        )
                    }
                )
            }

            // Benefits of Pass Mobility
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = HighDensityIndigoLight),
                    border = androidx.compose.foundation.BorderStroke(1.dp, HighDensityIndigoBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = HighDensityIndigo)
                            Text(
                                text = "Avantages des Cartes Numériques",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = HighDensityIndigoDark
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• Pass Étudiant : -40% sur le réseau Dakar Dem Dikk & BRT Dakar avec la carte UCAD/UVS.\n" +
                                    "• Pass Travailleur : Accès illimité intermodal BRT + TER pour les actifs de Dakar et Diamniadio.\n" +
                                    "• Rechargement instantané via Wave et Orange Money sans file d'attente au guichet.",
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 20.sp,
                            color = HighDensitySlate700
                        )
                    }
                }
            }
        }
    }

    if (showSubscribeDialog) {
        SubscribePassDialog(
            onDismiss = { showSubscribeDialog = false },
            onConfirm = { title, name, category, price ->
                viewModel.subscribeNewPass(title, name, category, price)
                showSubscribeDialog = false
            }
        )
    }
}

private fun TransportCategory.shortLabel(): String = when (this) {
    TransportCategory.TER -> "TER"
    TransportCategory.BRT -> "BRT"
    TransportCategory.DAKAR_DEM_DIKK -> "DDD"
    TransportCategory.AFTU_TATA -> "AFTU"
    TransportCategory.TAXI_CLANDO -> "Taxi"
    TransportCategory.CAR_RAPIDE -> "Car rapide"
}

@Composable
private fun TicketItemCard(
    ticket: TicketEntity,
    onValidate: () -> Unit
) {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRENCH)
    val dateStr = formatter.format(Date(ticket.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ticket_card_${ticket.id}"),
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
                Text(
                    text = ticket.ticketNumber,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = HighDensityIndigo
                )

                if (ticket.isValidated) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Validé", tint = HighDensityLiveGreen, modifier = Modifier.size(16.dp))
                        Text("Composté", color = HighDensityLiveGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFFF3E0)
                    ) {
                        Text(
                            text = "Valide 2h",
                            color = Color(0xFFE65100),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${ticket.lineCode} (${ticket.transportCategory})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = HighDensitySlate900
            )

            Text(
                text = "${ticket.origin} ➔ ${ticket.destination}",
                style = MaterialTheme.typography.bodyMedium,
                color = HighDensitySlate700
            )

            Text(
                text = "Acheté le $dateStr via ${ticket.paymentMethod}",
                style = MaterialTheme.typography.bodySmall,
                color = HighDensitySlate500
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // QR code representation
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .border(1.dp, HighDensitySlate200, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = "Code de validation",
                        tint = HighDensitySlate900,
                        modifier = Modifier.size(56.dp)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "${ticket.fareCfa} FCFA",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = HighDensityIndigoDark
                    )

                    if (!ticket.isValidated) {
                        Button(
                            onClick = onValidate,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = HighDensityIndigo),
                            modifier = Modifier.testTag("btn_validate_ticket_${ticket.id}")
                        ) {
                            Text("Composter", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyTicketsCard(onBuyFirst: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = HighDensitySurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, HighDensitySlate200)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(HighDensityIndigoLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsBus,
                    contentDescription = null,
                    tint = HighDensityIndigo,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Aucun ticket actif",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = HighDensitySlate900
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Achetez votre premier ticket dématérialisé pour monter à bord sans monnaie.",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = HighDensitySlate500
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onBuyFirst,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HighDensityIndigo)
            ) {
                Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Acheter un Ticket (Wave / OM)", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SubscribePassDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, name: String, category: String, price: Int) -> Unit
) {
    var selectedPassType by remember { mutableStateOf("Étudiant") }
    var holderName by remember { mutableStateOf("") }

    val passOptions = listOf(
        Triple("Pass Étudiant UCAD & Écoles", "Étudiant", 7500),
        Triple("Pass Travailleur Plateau/Diamniadio", "Travailleur", 15000),
        Triple("Pass Hebdomadaire Découverte", "Tout Public", 5000)
    )

    val currentOption = passOptions.first { it.second == selectedPassType }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = HighDensitySurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, HighDensitySlate200)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Souscrire à un Pass Mobilité",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = HighDensitySlate900
                )
                Text(
                    text = "Abonnement mensuel dématérialisé sur votre smartphone.",
                    style = MaterialTheme.typography.bodySmall,
                    color = HighDensitySlate500
                )

                Spacer(modifier = Modifier.height(14.dp))

                passOptions.forEach { (title, category, price) ->
                    val isSelected = selectedPassType == category
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedPassType = category }
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) HighDensityIndigo else HighDensitySlate200,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        color = if (isSelected) HighDensityIndigoLight else Color.Transparent
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = HighDensitySlate900
                                )
                                Text(
                                    text = "Validité : 30 jours",
                                    fontSize = 11.sp,
                                    color = HighDensitySlate500
                                )
                            }
                            Text(
                                text = "$price FCFA",
                                fontWeight = FontWeight.ExtraBold,
                                color = HighDensityIndigoDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = holderName,
                    onValueChange = { holderName = it },
                    label = { Text("Nom et Prénom du titulaire") },
                    placeholder = { Text("Ex: Awa Diop") },
                    singleLine = true,
                    colors = terangaOutlinedTextFieldColors(),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("input_pass_holder_name")
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
                            if (holderName.isNotBlank()) {
                                onConfirm(currentOption.first, holderName, currentOption.second, currentOption.third)
                            }
                        },
                        enabled = holderName.isNotBlank(),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).testTag("btn_confirm_pass_subscription"),
                        colors = ButtonDefaults.buttonColors(containerColor = HighDensityIndigo)
                    ) {
                        Text("Valider", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

