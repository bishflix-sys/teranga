package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NationalLanguage
import com.example.ui.theme.HighDensityIndigo
import com.example.ui.theme.HighDensityIndigoBorder
import com.example.ui.theme.HighDensityIndigoLight
import com.example.ui.theme.HighDensityLiveGreen
import com.example.ui.theme.HighDensitySlate100
import com.example.ui.theme.HighDensitySlate200
import com.example.ui.theme.HighDensitySlate400
import com.example.ui.theme.HighDensitySlate500
import com.example.ui.theme.HighDensitySlate700
import com.example.ui.theme.HighDensitySlate900
import com.example.ui.theme.HighDensitySurface

/**
 * Persistent UI component in the Top App Bar allowing users to toggle between
 * the 20 national languages of Senegal and French.
 *
 * Features:
 * 1. Persistent active language badge in the top bar.
 * 2. Dedicated 1-tap quick toggle button (SwapHoriz) to cycle through languages instantly.
 * 3. Dropdown menu with shortcuts to popular national languages.
 * 4. Action to open the full searchable bottom sheet for all 20 national languages.
 * 5. Audio playback preview button.
 */
@Composable
fun LanguageTopBarToggle(
    selectedLanguage: NationalLanguage,
    onSelectLanguage: (NationalLanguage) -> Unit,
    onToggleNextLanguage: () -> Unit,
    onOpenFullSheet: () -> Unit,
    onAnnounceGreeting: (NationalLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    // Curated quick-access national languages for the quick dropdown
    val quickAccessLanguages = remember {
        listOf(
            NationalLanguage.FRENCH,
            NationalLanguage.WOLOF,
            NationalLanguage.PULAAR,
            NationalLanguage.SERERE,
            NationalLanguage.MANDINKA,
            NationalLanguage.DIOLA,
            NationalLanguage.SONINKE
        )
    }

    Row(
        modifier = modifier
            .testTag("top_bar_language_toggle_container"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Main Interactive Language Pill with Dropdown
        Box {
            Row(
                modifier = Modifier
                    .height(38.dp)
                    .clip(RoundedCornerShape(19.dp))
                    .background(HighDensitySlate100)
                    .border(1.dp, HighDensityIndigoBorder.copy(alpha = 0.6f), RoundedCornerShape(19.dp))
                    .clickable { isMenuExpanded = true }
                    .padding(start = 8.dp, end = 6.dp)
                    .testTag("top_bar_language_toggle"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                // Senegal Flag 3-color micro dot indicator (Green / Gold / Red)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .padding(horizontal = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(4.dp, 10.dp).background(Color(0xFF00853F), RoundedCornerShape(1.dp)))
                    Box(modifier = Modifier.size(4.dp, 10.dp).background(Color(0xFFFDEF42), RoundedCornerShape(1.dp)))
                    Box(modifier = Modifier.size(4.dp, 10.dp).background(Color(0xFFE31B23), RoundedCornerShape(1.dp)))
                }

                // Animated Language code and name
                AnimatedContent(
                    targetState = selectedLanguage,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "language_label_anim"
                ) { lang ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = lang.code.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = HighDensityIndigo
                        )
                        Text(
                            text = lang.displayName.take(7),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = HighDensitySlate900,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = "Menu des 20 langues nationales",
                    tint = HighDensitySlate500,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Quick Dropdown Menu anchored to Top App Bar
            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false },
                modifier = Modifier
                    .widthIn(min = 260.dp, max = 310.dp)
                    .background(HighDensitySurface)
                    .border(1.dp, HighDensitySlate200, RoundedCornerShape(16.dp))
                    .testTag("menu_language_dropdown")
            ) {
                // Header
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = HighDensityIndigo,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "20 Langues Nationales",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = HighDensitySlate900
                        )
                    }
                    Text(
                        text = "Patrimoine & inclusion citoyenne",
                        fontSize = 10.sp,
                        color = HighDensitySlate500
                    )
                }

                HorizontalDivider(color = HighDensitySlate200, modifier = Modifier.padding(vertical = 4.dp))

                // Quick-access languages items
                quickAccessLanguages.forEach { lang ->
                    val isSelected = lang == selectedLanguage
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "[${lang.code.uppercase()}]",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = HighDensityIndigo
                                        )
                                        Text(
                                            text = lang.displayName,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) HighDensityIndigo else HighDensitySlate900
                                        )
                                    }
                                    Text(
                                        text = "« ${lang.greeting} »",
                                        fontSize = 10.sp,
                                        color = HighDensitySlate500,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Sélectionné",
                                        tint = HighDensityLiveGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        onClick = {
                            onSelectLanguage(lang)
                            isMenuExpanded = false
                        },
                        modifier = Modifier.testTag("menu_item_lang_${lang.code}")
                    )
                }

                HorizontalDivider(color = HighDensitySlate200, modifier = Modifier.padding(vertical = 4.dp))

                // Action: Explore all 20 national languages
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = HighDensityIndigo,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Toutes les 20 langues nationales…",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = HighDensityIndigo
                            )
                        }
                    },
                    onClick = {
                        isMenuExpanded = false
                        onOpenFullSheet()
                    },
                    modifier = Modifier.testTag("btn_open_all_20_languages")
                )
            }
        }

        // Dedicated 1-Tap Quick Toggle Button (Swap to next national language directly from top bar)
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(HighDensitySlate100)
                .border(1.dp, HighDensitySlate200, CircleShape)
                .clickable { onToggleNextLanguage() }
                .testTag("btn_quick_toggle_next_lang"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.SwapHoriz,
                contentDescription = "Basculer vers la langue nationale suivante",
                tint = HighDensityIndigo,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
