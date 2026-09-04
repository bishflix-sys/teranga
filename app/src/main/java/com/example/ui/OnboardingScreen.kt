package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.HighDensityBg
import com.example.ui.theme.HighDensityIndigo
import com.example.ui.theme.HighDensityIndigoLight
import com.example.ui.theme.HighDensitySlate200
import com.example.ui.theme.HighDensitySlate500
import com.example.ui.theme.HighDensitySlate900
import com.example.ui.theme.SunuGoldSecondary
import com.example.ui.theme.terangaPattern

private data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

private val onboardingPages = listOf(
    OnboardingPage(
        title = "TÉRANGA MOOV",
        description = "Dakar & Régions\nLa mobilité sénégalaise, simple et accessible.",
        icon = Icons.Default.DirectionsBus
    ),
    OnboardingPage(
        title = "Trouvez le meilleur trajet",
        description = "Comparez les lignes, le BRT, le TER et les transports de proximité en temps réel.",
        icon = Icons.Default.AltRoute
    ),
    OnboardingPage(
        title = "Voyagez en toute confiance",
        description = "Achetez vos tickets, recevez les alertes et restez informé pendant chaque déplacement.",
        icon = Icons.Default.CheckCircle
    )
)

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    var pageIndex by remember { mutableIntStateOf(0) }
    val page = onboardingPages[pageIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HighDensityBg)
            .terangaPattern(alpha = 0.09f)
            .padding(horizontal = 22.dp)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(R.drawable.img_app_icon),
                    contentDescription = "Logo Téranga Moov",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(if (pageIndex == 0) 178.dp else 132.dp)
                        .clip(RoundedCornerShape(38.dp))
                )

                if (pageIndex == 1) {
                    Image(
                        painter = painterResource(R.drawable.img_dakar_transit),
                        contentDescription = "Transports de Dakar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(top = 26.dp)
                            .fillMaxWidth()
                            .height(170.dp)
                            .clip(RoundedCornerShape(24.dp))
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))
                Icon(
                    imageVector = page.icon,
                    contentDescription = null,
                    tint = HighDensityIndigo,
                    modifier = Modifier.size(42.dp)
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = page.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (pageIndex == 0) HighDensityIndigo else HighDensitySlate900,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = page.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = HighDensitySlate500,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 18.dp)
        ) {
            onboardingPages.indices.forEach { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == pageIndex) 26.dp else 8.dp, 8.dp)
                        .clip(CircleShape)
                        .background(if (index == pageIndex) HighDensityIndigo else HighDensitySlate200)
                )
            }
        }

        Button(
            onClick = {
                if (pageIndex == onboardingPages.lastIndex) onFinished() else pageIndex++
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = HighDensityIndigo,
                contentColor = Color.White
            )
        ) {
            Text(
                text = if (pageIndex == onboardingPages.lastIndex) "Commencer" else "Suivant",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
    }
}