package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.repository.UserAccountRepository
import com.example.ui.theme.HighDensityBg
import com.example.ui.theme.HighDensityIndigo
import com.example.ui.theme.HighDensityIndigoLight
import com.example.ui.theme.HighDensitySlate500
import com.example.ui.theme.HighDensitySlate900
import com.example.ui.theme.terangaOutlinedTextFieldColors
import com.example.ui.theme.terangaPattern

@Composable
fun LoginScreen(
    accountRepository: UserAccountRepository,
    onAuthenticated: () -> Unit
) {
    var isCreatingAccount by remember { mutableStateOf(!accountRepository.hasAccount) }
    var name by remember { mutableStateOf("") }
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HighDensityBg)
            .terangaPattern(alpha = 0.08f)
            .padding(horizontal = 22.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(14.dp))
        Image(
            painter = painterResource(R.drawable.img_app_icon),
            contentDescription = "Logo Téranga Moov",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(118.dp).clip(RoundedCornerShape(28.dp))
        )
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = if (isCreatingAccount) "Bienvenue à Téranga Moov" else "Bon retour à Téranga Moov",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = HighDensityIndigo,
            textAlign = TextAlign.Center
        )
        Text(
            text = if (isCreatingAccount) "Créez votre compte pour continuer" else "Connectez-vous pour continuer",
            color = HighDensitySlate500,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (isCreatingAccount) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; errorMessage = null },
                modifier = Modifier.fillMaxWidth(),
                colors = terangaOutlinedTextFieldColors(),
                label = { Text("Nom complet") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        OutlinedTextField(
            value = identifier,
            onValueChange = { identifier = it; errorMessage = null },
            modifier = Modifier.fillMaxWidth(),
            colors = terangaOutlinedTextFieldColors(),
            label = { Text("Téléphone ou email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMessage = null },
            modifier = Modifier.fillMaxWidth(),
            colors = terangaOutlinedTextFieldColors(),
            label = { Text("Mot de passe") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (passwordVisible) "Masquer le mot de passe" else "Afficher le mot de passe"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true
        )
        if (isCreatingAccount) {
            Text(
                text = "6 caractères minimum",
                color = HighDensitySlate500,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp)
            )
        }
        errorMessage?.let {
            Text(text = it, color = Color(0xFFD9381E), modifier = Modifier.padding(top = 10.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                val authenticated = if (isCreatingAccount) {
                    accountRepository.createAccount(name, identifier, password)
                } else {
                    accountRepository.login(identifier, password)
                }
                if (authenticated) onAuthenticated() else errorMessage = if (isCreatingAccount) {
                    "Renseignez vos informations et utilisez un mot de passe de 6 caractères minimum."
                } else {
                    "Identifiant ou mot de passe incorrect."
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = HighDensityIndigo)
        ) {
            Text(if (isCreatingAccount) "Créer mon compte" else "Se connecter", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text(if (isCreatingAccount) "Vous avez déjà un compte ?" else "Pas encore de compte ?", color = HighDensitySlate500)
            Text(
                text = if (isCreatingAccount) " Se connecter" else " Créer un compte",
                color = HighDensityIndigo,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable { isCreatingAccount = !isCreatingAccount; errorMessage = null }
            )
        }
    }
}