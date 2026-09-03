package com.example.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.AltRoute
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.LanguageTopBarToggle
import com.example.ui.components.MobilePaymentSheet
import com.example.ui.components.NationalLanguageBottomSheet
import com.example.ui.components.ReportIncidentDialog
import com.example.ui.language.LanguageSelectionViewModel
import com.example.ui.screens.CitizenReportsScreen
import com.example.ui.screens.LiveMapScreen
import com.example.ui.screens.RoutesScreen
import com.example.ui.screens.StatsAndProfileScreen
import com.example.ui.screens.TicketsAndPassScreen
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: TransitViewModel,
    languageViewModel: LanguageSelectionViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val paymentState by viewModel.paymentState.collectAsStateWithLifecycle()
    val snackbarMsg by viewModel.snackbarMessage.collectAsStateWithLifecycle()
    val tickets by viewModel.tickets.collectAsStateWithLifecycle()
    val reports by viewModel.reports.collectAsStateWithLifecycle()
    val selectedLanguage by languageViewModel.selectedLanguage.collectAsStateWithLifecycle()
    val isLanguageSheetOpen by viewModel.isLanguageSheetOpen.collectAsStateWithLifecycle()
    val isSpeaking by viewModel.isSpeaking.collectAsStateWithLifecycle()
    val isReportDialogOpen by viewModel.isReportIncidentDialogOpen.collectAsStateWithLifecycle()
    val preselectedCategory by viewModel.preselectedIncidentCategory.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMsg) {
        snackbarMsg?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("app_scaffold"),
        containerColor = HighDensityBg,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            // High Density Header with Téranga Moov branding & 20 National Languages Access
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HighDensityBg)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .testTag("high_density_header")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "TÉRANGA MOOV • SÉNÉGAL",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = HighDensityIndigo,
                            letterSpacing = 1.4.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Dakar & Régions",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = HighDensitySlate900
                            )
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(HighDensityLiveGreen)
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 1. Vocal Announcer Accessibility Button (for visually impaired & oral users)
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (isSpeaking) HighDensityIndigoLight else HighDensitySlate100)
                                .border(1.dp, if (isSpeaking) HighDensityIndigo else HighDensitySlate200, CircleShape)
                                .clickable {
                                    if (isSpeaking) {
                                        viewModel.stopVoiceAnnouncement()
                                    } else {
                                        viewModel.announceCurrentLanguageGreeting()
                                    }
                                }
                                .testTag("btn_voice_announcer"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                                contentDescription = "Synthèse vocale d'accessibilité",
                                tint = if (isSpeaking) HighDensityIndigo else HighDensitySlate700,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // 2. Persistent Language Toggle in Top App Bar (20 national languages of Senegal)
                        LanguageTopBarToggle(
                            selectedLanguage = selectedLanguage,
                            onSelectLanguage = { lang ->
                                languageViewModel.selectLanguage(lang, announce = true)
                                viewModel.setLanguage(lang)
                            },
                            onToggleNextLanguage = {
                                val next = languageViewModel.toggleNextLanguage()
                                viewModel.setLanguage(next)
                            },
                            onOpenFullSheet = {
                                viewModel.openLanguageSheet()
                            },
                            onAnnounceGreeting = { lang ->
                                languageViewModel.announceLanguage(lang)
                            }
                        )

                        // 3. User profile avatar badge (TM = Téranga Moov)
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(HighDensityIndigoLight)
                                .border(1.dp, HighDensityIndigoBorder, CircleShape)
                                .clickable { viewModel.selectTab(TransitTab.STATS) }
                                .testTag("btn_header_profile"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "TM",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = HighDensityIndigoDark
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            // High Density Bottom Navigation Bar localized in selected national language
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .border(width = 1.dp, color = HighDensitySlate100)
                    .testTag("main_bottom_nav"),
                containerColor = HighDensitySurface,
                tonalElevation = 0.dp
            ) {
                // 1. En Direct (Radar / Carte)
                NavigationBarItem(
                    selected = currentTab == TransitTab.LIVE_MAP,
                    onClick = { viewModel.selectTab(TransitTab.LIVE_MAP) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == TransitTab.LIVE_MAP) Icons.Filled.Map else Icons.Outlined.Map,
                            contentDescription = selectedLanguage.liveTabLabel
                        )
                    },
                    label = {
                        Text(
                            text = selectedLanguage.liveTabLabel,
                            fontSize = 10.sp,
                            fontWeight = if (currentTab == TransitTab.LIVE_MAP) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = HighDensityIndigo,
                        unselectedIconColor = HighDensitySlate400,
                        selectedTextColor = HighDensityIndigo,
                        unselectedTextColor = HighDensitySlate400,
                        indicatorColor = HighDensityIndigoLight
                    ),
                    modifier = Modifier.testTag("nav_item_live")
                )

                // 2. Lignes & Itinéraires
                NavigationBarItem(
                    selected = currentTab == TransitTab.ROUTES,
                    onClick = { viewModel.selectTab(TransitTab.ROUTES) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == TransitTab.ROUTES) Icons.Filled.AltRoute else Icons.Outlined.AltRoute,
                            contentDescription = selectedLanguage.routesTabLabel
                        )
                    },
                    label = {
                        Text(
                            text = selectedLanguage.routesTabLabel,
                            fontSize = 10.sp,
                            fontWeight = if (currentTab == TransitTab.ROUTES) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = HighDensityIndigo,
                        unselectedIconColor = HighDensitySlate400,
                        selectedTextColor = HighDensityIndigo,
                        unselectedTextColor = HighDensitySlate400,
                        indicatorColor = HighDensityIndigoLight
                    ),
                    modifier = Modifier.testTag("nav_item_routes")
                )

                // 3. Pass & Tickets
                NavigationBarItem(
                    selected = currentTab == TransitTab.TICKETS,
                    onClick = { viewModel.selectTab(TransitTab.TICKETS) },
                    icon = {
                        if (tickets.isNotEmpty()) {
                            BadgedBox(badge = {
                                Badge(containerColor = HighDensityIndigo) {
                                    Text("${tickets.size}", color = Color.White)
                                }
                            }) {
                                Icon(
                                    imageVector = if (currentTab == TransitTab.TICKETS) Icons.Filled.QrCode else Icons.Outlined.QrCode,
                                    contentDescription = selectedLanguage.ticketsTabLabel
                                )
                            }
                        } else {
                            Icon(
                                imageVector = if (currentTab == TransitTab.TICKETS) Icons.Filled.QrCode else Icons.Outlined.QrCode,
                                contentDescription = selectedLanguage.ticketsTabLabel
                            )
                        }
                    },
                    label = {
                        Text(
                            text = selectedLanguage.ticketsTabLabel,
                            fontSize = 10.sp,
                            fontWeight = if (currentTab == TransitTab.TICKETS) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = HighDensityIndigo,
                        unselectedIconColor = HighDensitySlate400,
                        selectedTextColor = HighDensityIndigo,
                        unselectedTextColor = HighDensitySlate400,
                        indicatorColor = HighDensityIndigoLight
                    ),
                    modifier = Modifier.testTag("nav_item_tickets")
                )

                // 4. Signalements Citoyens
                NavigationBarItem(
                    selected = currentTab == TransitTab.REPORTS,
                    onClick = { viewModel.selectTab(TransitTab.REPORTS) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == TransitTab.REPORTS) Icons.Filled.Campaign else Icons.Outlined.Campaign,
                            contentDescription = selectedLanguage.reportsTabLabel
                        )
                    },
                    label = {
                        Text(
                            text = selectedLanguage.reportsTabLabel,
                            fontSize = 10.sp,
                            fontWeight = if (currentTab == TransitTab.REPORTS) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = HighDensityIndigo,
                        unselectedIconColor = HighDensitySlate400,
                        selectedTextColor = HighDensityIndigo,
                        unselectedTextColor = HighDensitySlate400,
                        indicatorColor = HighDensityIndigoLight
                    ),
                    modifier = Modifier.testTag("nav_item_reports")
                )

                // 5. Mon Espace / Stats
                NavigationBarItem(
                    selected = currentTab == TransitTab.STATS,
                    onClick = { viewModel.selectTab(TransitTab.STATS) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == TransitTab.STATS) Icons.Filled.Person else Icons.Outlined.Person,
                            contentDescription = selectedLanguage.profileTabLabel
                        )
                    },
                    label = {
                        Text(
                            text = selectedLanguage.profileTabLabel,
                            fontSize = 10.sp,
                            fontWeight = if (currentTab == TransitTab.STATS) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = HighDensityIndigo,
                        unselectedIconColor = HighDensitySlate400,
                        selectedTextColor = HighDensityIndigo,
                        unselectedTextColor = HighDensitySlate400,
                        indicatorColor = HighDensityIndigoLight
                    ),
                    modifier = Modifier.testTag("nav_item_stats")
                )
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = currentTab,
            label = "tab_transition",
            modifier = Modifier.padding(innerPadding)
        ) { tab ->
            when (tab) {
                TransitTab.LIVE_MAP -> LiveMapScreen(viewModel = viewModel)
                TransitTab.ROUTES -> RoutesScreen(viewModel = viewModel)
                TransitTab.TICKETS -> TicketsAndPassScreen(viewModel = viewModel)
                TransitTab.REPORTS -> CitizenReportsScreen(viewModel = viewModel)
                TransitTab.STATS -> StatsAndProfileScreen(viewModel = viewModel)
            }
        }
    }

    // National Languages BottomSheet (All 20 national languages of Senegal)
    if (isLanguageSheetOpen) {
        NationalLanguageBottomSheet(
            selectedLanguage = selectedLanguage,
            onSelectLanguage = {
                languageViewModel.selectLanguage(it, announce = true)
                viewModel.setLanguage(it)
            },
            onPreviewAudio = { languageViewModel.announceLanguage(it) },
            onDismiss = { viewModel.closeLanguageSheet() }
        )
    }

    // Payment BottomSheet
    if (paymentState.isOpen) {
        MobilePaymentSheet(
            state = paymentState,
            onDismiss = { viewModel.closePaymentModal() },
            onSelectMethod = { viewModel.updatePaymentMethod(it) },
            onPhoneChange = { viewModel.updatePhoneNumber(it) },
            onConfirmPayment = { viewModel.executePayment() },
            onValidateTicket = { viewModel.validateTicket(it) }
        )
    }

    // Report Incident Dialog (Room Persistence for Accidents, Traffic, Safety Issues)
    if (isReportDialogOpen) {
        ReportIncidentDialog(
            initialCategory = preselectedCategory,
            onDismiss = { viewModel.closeReportIncidentDialog() },
            onSubmit = { category, location, description, severity, author, announceVoice ->
                viewModel.submitReport(
                    category = category,
                    location = location,
                    description = description,
                    severity = severity,
                    author = author,
                    announceVoice = announceVoice
                )
            }
        )
    }
}

