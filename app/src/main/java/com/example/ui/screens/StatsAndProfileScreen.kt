package com.example.ui.screens

import android.content.Context
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Co2
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.TransitViewModel
import com.example.R
import com.example.data.repository.UserAccountRepository
import com.example.ui.theme.HighDensityBg
import com.example.ui.theme.HighDensityIndigo
import com.example.ui.theme.HighDensityIndigoBorder
import com.example.ui.theme.HighDensityIndigoDark
import com.example.ui.theme.HighDensityIndigoLight
import com.example.ui.theme.terangaOutlinedTextFieldColors
import com.example.ui.theme.HighDensityLiveGreen
import com.example.ui.theme.HighDensitySlate100
import com.example.ui.theme.HighDensitySlate200
import com.example.ui.theme.HighDensitySlate400
import com.example.ui.theme.HighDensitySlate500
import com.example.ui.theme.HighDensitySlate700
import com.example.ui.theme.HighDensitySlate900
import com.example.ui.theme.HighDensitySurface
import com.example.ui.theme.terangaPattern

@Composable
fun StatsAndProfileScreen(
    viewModel: TransitViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val accountRepository = remember { UserAccountRepository(context) }
    val tickets by viewModel.tickets.collectAsStateWithLifecycle()
    val passes by viewModel.passes.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val isSpeaking by viewModel.isSpeaking.collectAsStateWithLifecycle()
    val dataSaverEnabled by viewModel.dataSaverEnabled.collectAsStateWithLifecycle()

    var whatsappAlertsEnabled by remember { mutableStateOf(true) }
    var smartReroutingEnabled by remember { mutableStateOf(true) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showPinDialog by remember { mutableStateOf(false) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var accountMessage by remember { mutableStateOf<String?>(null) }
    var profilePhoto by remember { mutableStateOf(accountRepository.profilePhoto) }
    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            runCatching {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            accountRepository.updateProfilePhoto(it)
            profilePhoto = it
        }
    }

    val totalTripsThisMonth = (tickets.size * 3) + 24
    val estimatedTimeSavedMinutes = totalTripsThisMonth * 35 // 35 min saved per trip with BRT/TER & live traffic
    val financialSavingsCfa = totalTripsThisMonth * 750 // Difference compared to solo taxi
    val carbonSavedKg = (totalTripsThisMonth * 1.8f)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(HighDensityBg)
            .terangaPattern(alpha = 0.05f)
            .testTag("screen_stats_and_profile"),
        contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 88.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Téranga Moov • Mon Espace",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = HighDensitySlate900
            )
            Text(
                text = "Mobilité citoyenne, impact écologique et inclusion en 20 langues nationales",
                style = MaterialTheme.typography.bodySmall,
                color = HighDensitySlate500
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = HighDensitySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, HighDensityIndigoBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(HighDensityIndigoLight),
                            contentAlignment = Alignment.Center
                        ) {
                            profilePhoto?.let { uri ->
                                val bitmap = remember(uri) {
                                    context.contentResolver.openInputStream(uri)?.use { stream ->
                                        BitmapFactory.decodeStream(stream)?.asImageBitmap()
                                    }
                                }
                                if (bitmap != null) {
                                    Image(
                                        bitmap = bitmap,
                                        contentDescription = "Photo de profil",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.AddAPhoto,
                                        contentDescription = "Ajouter une photo de profil",
                                        tint = HighDensityIndigo,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            } ?: Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = "Ajouter une photo de profil",
                                tint = HighDensityIndigo,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = accountRepository.displayName.ifBlank { "Mon profil" },
                                fontWeight = FontWeight.Bold,
                                color = HighDensitySlate900
                            )
                            Text(
                                text = accountRepository.identifier,
                                style = MaterialTheme.typography.bodySmall,
                                color = HighDensitySlate500
                            )
                        }
                        OutlinedButton(onClick = { photoPicker.launch(arrayOf("image/*")) }) {
                            Text("Photo")
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { showPasswordDialog = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Mot de passe")
                        }
                        OutlinedButton(
                            onClick = { showPinDialog = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Pin, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (accountRepository.hasPin) "Modifier PIN" else "Créer PIN")
                        }
                    }
                    accountMessage?.let { message ->
                        Text(message, color = HighDensityIndigo, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
        }

        // Section: Inclusion & 20 Langues Nationales du Sénégal
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("card_national_languages_setting"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = HighDensitySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, HighDensityIndigoBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = HighDensityIndigoLight,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Language,
                                        contentDescription = null,
                                        tint = HighDensityIndigo,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "Langues Nationales (${selectedLanguage.displayName})",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = HighDensitySlate900
                                )
                                Text(
                                    text = "${selectedLanguage.regionOrGroup} • « ${selectedLanguage.greeting} »",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = HighDensityIndigo
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Téranga Moov est accessible dans les 20 langues officielles du Sénégal pour ne laisser aucun citoyen de côté.",
                        style = MaterialTheme.typography.bodySmall,
                        color = HighDensitySlate700
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.openLanguageSheet() },
                            modifier = Modifier.weight(1f).testTag("btn_change_language"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = HighDensityIndigo)
                        ) {
                            Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Changer la langue", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                if (isSpeaking) {
                                    viewModel.stopVoiceAnnouncement()
                                } else {
                                    viewModel.announceCurrentLanguageGreeting()
                                }
                            },
                            modifier = Modifier.weight(1f).testTag("btn_voice_test"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = HighDensityIndigo)
                        ) {
                            Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isSpeaking) "Arrêter" else "Écouter la voix", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("card_accessibility_settings"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = HighDensitySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, HighDensitySlate200)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Économie de données", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = HighDensitySlate900)
                            Text("Réduit le chargement des tuiles cartographiques en EDGE/3G", style = MaterialTheme.typography.bodySmall, color = HighDensitySlate500)
                        }
                        Switch(
                            checked = dataSaverEnabled,
                            onCheckedChange = viewModel::setDataSaverEnabled,
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = HighDensityIndigo)
                        )
                    }
                    Text(
                        text = if (viewModel.nfcPassRechargeAvailable) "Recharge NFC disponible sur cet appareil" else "Recharge NFC indisponible sur cet appareil",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (viewModel.nfcPassRechargeAvailable) HighDensityLiveGreen else HighDensitySlate500
                    )
                }
            }
        }

        // Stats Highlights Grid
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = HighDensitySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, HighDensitySlate200)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Bilan Mobilité • Ce Mois",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = HighDensitySlate900
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatTile(
                            title = "Temps économisé",
                            value = "${estimatedTimeSavedMinutes / 60}h ${estimatedTimeSavedMinutes % 60}m",
                            subtitle = "grâce aux couloirs BRT & alertes",
                            icon = Icons.Default.Timer,
                            tint = HighDensityIndigo,
                            modifier = Modifier.weight(1f)
                        )
                        StatTile(
                            title = "Économies réalisées",
                            value = "$financialSavingsCfa F",
                            subtitle = "vs taxi urbain classique",
                            icon = Icons.Default.Savings,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatTile(
                            title = "Trajets Effectués",
                            value = "$totalTripsThisMonth",
                            subtitle = "DDD, Tata, BRT & TER",
                            icon = Icons.Default.TrendingUp,
                            tint = Color(0xFF0284C7),
                            modifier = Modifier.weight(1f)
                        )
                        StatTile(
                            title = "CO₂ évité",
                            value = "${String.format("%.1f", carbonSavedKg)} kg",
                            subtitle = "impact écologique vert",
                            icon = Icons.Default.Co2,
                            tint = HighDensityLiveGreen,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // WhatsApp Integration Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF25D366),
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Send, contentDescription = "WhatsApp", tint = Color.White, modifier = Modifier.size(18.dp))
                                }
                            }
                            Column {
                                Text(
                                    text = "Alertes Trafic sur WhatsApp",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = HighDensitySlate900
                                )
                                Text(
                                    text = "Recevez le point trafic matinal à 7h30",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF166534)
                                )
                            }
                        }

                        Switch(
                            checked = whatsappAlertsEnabled,
                            onCheckedChange = { whatsappAlertsEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF25D366),
                                checkedTrackColor = Color(0xFFBBF7D0)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            val alertMsg = "🚌 *Point Trafic Dakar • Téranga Moov*\n" +
                                    "Circulation fluide sur le corridor BRT Petersen.\n" +
                                    "Ralentissement sur la VDN vers Pont Sénégal92 (+15 min).\n" +
                                    "TER Dakar-Diamniadio à l'heure (toutes les 10 min).\n\n" +
                                    "_Partagé via Téranga Moov, la super-app citoyenne du Sénégal_"
                            viewModel.repository.shareViaWhatsApp(context, alertMsg)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("btn_test_whatsapp_alert"),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF166534))
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Tester l'envoi d'un bulletin WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Modèle Économique & Formule Freemium
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
                        Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = HighDensityIndigo)
                        Text(
                            text = "Modèle Économique & Offres",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = HighDensitySlate900
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "• Gratuit pour tous : Radar temps réel des bus, calcul d'itinéraire basique et signalements citoyens.\n" +
                                "• Formule Premium Pass : Notifications proactives avant vos départs quotidiens, alertes WhatsApp VIP et synchronisation multimodale.\n" +
                                "• Commissions sur paiements : Micro-frais transparents inclus avec les opérateurs Wave, Orange Money et Free Money.\n" +
                                "• Offre Entreprises : Gestion de flottes de transport pour employés sur les axes Plateau, VDN et Diamniadio.",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 20.sp,
                        color = HighDensitySlate700
                    )
                }
            }
        }

        // Partenaires Stratégiques & Écosystème
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
                        Icon(Icons.Default.Handshake, contentDescription = null, tint = HighDensityIndigo)
                        Text(
                            text = "Partenaires Stratégiques",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = HighDensitySlate900
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val partners = listOf(
                        "Dakar Dem Dikk (DDD)",
                        "AFTU (Association de Financement des Transports Urbains)",
                        "CETUD & Ministère des Transports",
                        "Wave Sénégal & Orange Money",
                        "SETER (Exploitant du TER Dakar-Diamniadio)",
                        "BRT Dakar (Bus Rapid Transit)",
                        "Universités UCAD, UVS & Écoles supérieures"
                    )

                    partners.forEach { partner ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = HighDensityLiveGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = partner,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = HighDensitySlate900
                            )
                        }
                    }
                }
            }
        }
    }

    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Changer le mot de passe") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(currentPassword, { currentPassword = it }, label = { Text("Mot de passe actuel") }, singleLine = true, colors = terangaOutlinedTextFieldColors())
                    OutlinedTextField(newPassword, { newPassword = it }, label = { Text("Nouveau mot de passe") }, singleLine = true, colors = terangaOutlinedTextFieldColors())
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val updated = accountRepository.updatePassword(currentPassword, newPassword)
                    accountMessage = if (updated) "Mot de passe mis à jour." else "Vérifiez le mot de passe actuel et utilisez 6 caractères minimum."
                    currentPassword = ""
                    newPassword = ""
                    showPasswordDialog = false
                }) { Text("Enregistrer") }
            },
            dismissButton = { TextButton(onClick = { showPasswordDialog = false }) { Text("Annuler") } }
        )
    }

    if (showPinDialog) {
        AlertDialog(
            onDismissRequest = { showPinDialog = false },
            title = { Text("Créer un code PIN") },
            text = {
                OutlinedTextField(
                    value = pin,
                    onValueChange = { value -> pin = value.filter(Char::isDigit).take(4) },
                    label = { Text("Code à 4 chiffres") },
                    singleLine = true,
                    colors = terangaOutlinedTextFieldColors()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    accountMessage = if (accountRepository.setPin(pin)) "Code PIN enregistré." else "Le code doit contenir exactement 4 chiffres."
                    pin = ""
                    showPinDialog = false
                }) { Text("Enregistrer") }
            },
            dismissButton = { TextButton(onClick = { showPinDialog = false }) { Text("Annuler") } }
        )
    }
}

@Composable
private fun StatTile(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = tint.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, tint.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(imageVector = icon, contentDescription = title, tint = tint, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = title, fontSize = 11.sp, color = HighDensitySlate500, fontWeight = FontWeight.Medium)
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = tint)
            Text(text = subtitle, fontSize = 10.sp, color = HighDensitySlate500)
        }
    }
}
