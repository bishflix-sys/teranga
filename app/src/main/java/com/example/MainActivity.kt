package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

private val GreenPrimary = Color(0xFF006837)
private val GreenDark = Color(0xFF004D25)
private val YellowAccent = Color(0xFFF9A825)
private val RedAccent = Color(0xFFE53935)
private val BackgroundLight = Color(0xFFF4F6F8)
private const val BusImage = "https://images.unsplash.com/photo-1570125909232-eb263c188f7e?w=800&q=80"
private const val MapImage = "https://images.unsplash.com/photo-1524661135-423995f22d0b?w=800&q=80"

private object Route {
    const val Splash = "splash"
    const val Login = "login"
    const val Home = "home"
    const val Search = "search"
    const val Map = "map"
    const val Tickets = "tickets"
    const val Alerts = "alerts"
    const val Profile = "profile"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = androidx.compose.material3.lightColorScheme(
                    primary = GreenPrimary,
                    secondary = YellowAccent,
                    background = BackgroundLight
                )
            ) { TerangaMoovApp() }
        }
    }
}

@Composable
private fun TerangaMoovApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Route.Splash) {
        composable(Route.Splash) { SplashScreen { navController.replace(Route.Login) } }
        composable(Route.Login) { LoginScreen { navController.replace(Route.Home) } }
        composable(Route.Home) { AppScaffold(navController) { HomeScreen(navController) } }
        composable(Route.Search) { AppScaffold(navController) { SearchScreen(navController) } }
        composable(Route.Map) { MapScreen { navController.popBackStack() } }
        composable(Route.Tickets) { AppScaffold(navController) { TicketsScreen() } }
        composable(Route.Alerts) { AppScaffold(navController) { AlertsScreen() } }
        composable(Route.Profile) { AppScaffold(navController) { ProfileScreen() } }
    }
}

private fun NavHostController.replace(route: String) {
    navigate(route) { popUpTo(currentBackStackEntry?.destination?.route ?: Route.Splash) { inclusive = true } }
}

@Composable
private fun SplashScreen(onDone: () -> Unit) {
    LaunchedEffect(Unit) { delay(1200); onDone() }
    Box(Modifier.fillMaxSize().background(GreenDark), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(Modifier.size(88.dp), CircleShape, Color.White.copy(alpha = .14f)) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.DirectionsBus, null, tint = YellowAccent, modifier = Modifier.size(52.dp)) }
            }
            Spacer(Modifier.height(16.dp))
            Text("TÉRANGA", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text("MOOV", color = YellowAccent, fontSize = 36.sp, fontWeight = FontWeight.Black)
            Text("Dakar & Régions", color = Color.White.copy(alpha = .8f))
        }
    }
}

@Composable
private fun LoginScreen(onLogin: () -> Unit) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().background(BackgroundLight).verticalScroll(rememberScrollState())) {
        Box(Modifier.fillMaxWidth().height(230.dp).background(GreenDark), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.DirectionsBus, null, tint = YellowAccent, modifier = Modifier.size(58.dp))
                Text("TÉRANGA MOOV", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
        Card(Modifier.fillMaxWidth().padding(16.dp), colors = CardDefaults.cardColors(Color.White)) {
            Column(Modifier.padding(20.dp)) {
                Text("Connexion / Dukku", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Accédez à vos billets et itinéraires", color = Color.Gray, fontSize = 12.sp)
                Spacer(Modifier.height(20.dp))
                OutlinedTextField(phone, { phone = it }, label = { Text("Numéro de téléphone") }, leadingIcon = { Text("+221") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(password, { password = it }, label = { Text("Mot de passe") }, visualTransformation = PasswordVisualTransformation(), singleLine = true, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(20.dp))
                Button(onClick = onLogin, Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(GreenPrimary)) { Text("Se connecter") }
            }
        }
    }
}

@Composable
private fun AppScaffold(navController: NavHostController, content: @Composable () -> Unit) {
    Scaffold(bottomBar = { BottomBar(navController) }) { padding -> Box(Modifier.padding(padding)) { content() } }
}

@Composable
private fun BottomBar(navController: NavHostController) {
    val route by navController.currentBackStackEntryAsState()
    val current = route?.destination?.route
    NavigationBar(containerColor = Color.White) {
        listOf(Route.Home to (Icons.Default.Home to "Accueil"), Route.Search to (Icons.Default.DirectionsBus to "Trajets"), Route.Tickets to (Icons.Default.ConfirmationNumber to "Tickets"), Route.Alerts to (Icons.Default.Notifications to "Alertes"), Route.Profile to (Icons.Default.Person to "Profil")).forEach { (destination, item) ->
            NavigationBarItem(selected = current == destination, onClick = { if (current != destination) navController.navigate(destination) }, icon = { Icon(item.first, item.second) }, label = { Text(item.second) })
        }
    }
}

@Composable
private fun HomeScreen(navController: NavHostController) {
    Column(Modifier.fillMaxSize().background(BackgroundLight).verticalScroll(rememberScrollState()).padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Column { Text("TÉRANGA MOOV", color = GreenPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp); Text("Dakar & Régions", color = Color.Gray, fontSize = 12.sp) }
            IconButton({ navController.navigate(Route.Alerts) }) { Icon(Icons.Default.Notifications, "Alertes") }
        }
        Spacer(Modifier.height(16.dp))
        Card(Modifier.fillMaxWidth().clickable { navController.navigate(Route.Search) }, colors = CardDefaults.cardColors(Color.White), shape = RoundedCornerShape(24.dp)) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Search, null, tint = Color.Gray); Spacer(Modifier.width(12.dp)); Text("Où allez-vous ? / Fan nga jëm ?", color = Color.Gray) }
        }
        Spacer(Modifier.height(16.dp))
        Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(GreenPrimary), shape = RoundedCornerShape(16.dp)) {
            Column(Modifier.padding(18.dp)) { Text("TÉRANGA PASS CITOYEN", color = Color.White, fontWeight = FontWeight.Bold); Spacer(Modifier.height(12.dp)); Text("46", color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Bold); Text("Trajets disponibles", color = Color.White.copy(alpha = .8f)); Spacer(Modifier.height(12.dp)); Button({ navController.navigate(Route.Tickets) }, colors = ButtonDefaults.buttonColors(Color.White)) { Icon(Icons.Default.QrCode, null, tint = GreenPrimary); Spacer(Modifier.width(6.dp)); Text("Payer QR", color = GreenPrimary) } }
        }
        Spacer(Modifier.height(16.dp))
        Card(Modifier.fillMaxWidth().clickable { navController.navigate(Route.Map) }, colors = CardDefaults.cardColors(Color.White)) {
            Column(Modifier.padding(16.dp)) { Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) { Text("RADAR GPS EN DIRECT", fontWeight = FontWeight.Bold); Text("8 bus proches", color = GreenPrimary) }; Spacer(Modifier.height(8.dp)); AsyncImage(MapImage, "Carte", Modifier.fillMaxWidth().height(130.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop) }
        }
        Spacer(Modifier.height(16.dp))
        Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(RedAccent.copy(alpha = .1f))) { Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Notifications, null, tint = RedAccent); Spacer(Modifier.width(12.dp)); Column { Text("Signalement citoyen", color = RedAccent, fontWeight = FontWeight.Bold); Text("Signaler un problème sur votre trajet", color = Color.DarkGray, fontSize = 12.sp) } } }
    }
}

@Composable
private fun SearchScreen(navController: NavHostController) {
    var departure by remember { mutableStateOf("Guédiawaye Arrêt Double Less") }
    var destination by remember { mutableStateOf("Plateau - Gare Petersen") }
    Column(Modifier.fillMaxSize().background(BackgroundLight).verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text("Lignes & itinéraires", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(departure, { departure = it }, label = { Text("Point de départ") }, leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = GreenPrimary) }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(destination, { destination = it }, label = { Text("Destination") }, leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = RedAccent) }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(18.dp))
        Text("Options suggérées", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))
        listOf("BRT Dakar B1 Omnibus" to "400 FCFA • 32 min", "TER Train Express Régional" to "500 FCFA • 20 min", "Car Rapide / Ndiaga Ndiaye" to "150 FCFA • 45 min").forEach { (title, detail) ->
            Card(Modifier.fillMaxWidth().padding(vertical = 5.dp).clickable { navController.navigate(Route.Tickets) }, colors = CardDefaults.cardColors(Color.White)) { Column(Modifier.padding(14.dp)) { Text(title, fontWeight = FontWeight.Bold); Text(detail, color = GreenPrimary, fontSize = 13.sp) } }
        }
    }
}

@Composable
private fun MapScreen(onBack: () -> Unit) {
    Box(Modifier.fillMaxSize()) { AsyncImage(MapImage, "Carte GPS", Modifier.fillMaxSize(), contentScale = ContentScale.Crop); IconButton(onBack, Modifier.padding(16.dp).background(Color.White, CircleShape)) { Icon(Icons.Default.ArrowBack, "Retour") }; Card(Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(16.dp), colors = CardDefaults.cardColors(Color.White)) { Column(Modifier.padding(16.dp)) { Text("Prochain passage BRT", color = Color.Gray); Text("Dans 4 min", fontSize = 24.sp, color = GreenPrimary, fontWeight = FontWeight.Bold); Text("Station Place de la Nation") } } }
}

@Composable
private fun TicketsScreen() {
    Column(Modifier.fillMaxSize().background(BackgroundLight).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Votre ticket actif", fontSize = 22.sp, fontWeight = FontWeight.Bold); Text("Présentez le QR Code au valideur", color = Color.Gray, fontSize = 13.sp); Spacer(Modifier.height(24.dp)); Surface(Modifier.size(220.dp), RoundedCornerShape(16.dp), Color.White, shadowElevation = 4.dp) { Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.QrCode, "QR Code", Modifier.size(170.dp), tint = Color.Black) } }; Spacer(Modifier.height(16.dp)); Text("Valide pour : Ligne BRT B1", fontWeight = FontWeight.Bold); Text("Expire dans : 45 minutes", color = RedAccent, fontSize = 12.sp)
    }
}

@Composable
private fun AlertsScreen() {
    val alerts = listOf("Embouteillage saturé - VDN Mermoz" to "Ralentissement important vers le centre-ville.", "Affluence BRT Place de la Nation" to "Forte affluence aux guichets.")
    LazyColumn(Modifier.fillMaxSize().background(BackgroundLight).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { item { Text("Alertes & Trafic", fontSize = 22.sp, fontWeight = FontWeight.Bold) }; items(alerts) { (title, description) -> Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(Color.White)) { Column(Modifier.padding(16.dp)) { Text("BRT B1", color = RedAccent, fontWeight = FontWeight.Bold); Text(title, fontWeight = FontWeight.Bold); Text(description, color = Color.Gray, fontSize = 13.sp) } } } }
}

@Composable
private fun ProfileScreen() {
    Column(Modifier.fillMaxSize().background(BackgroundLight).verticalScroll(rememberScrollState()).padding(16.dp)) {
        Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(GreenPrimary)) { Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) { Surface(Modifier.size(64.dp), CircleShape, YellowAccent) { Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, null, tint = GreenDark) } }; Spacer(Modifier.width(16.dp)); Column { Text("Abdoulaye Sène", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp); Text("+221 77 123 45 67", color = Color.White.copy(alpha = .8f)) } } }
        Spacer(Modifier.height(16.dp)); Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(Color.White)) { Column(Modifier.padding(16.dp)) { Text("Langue de l'application / Làkk", fontWeight = FontWeight.Bold); Spacer(Modifier.height(10.dp)); Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Translate, null, tint = GreenPrimary); Spacer(Modifier.width(10.dp)); Text("Français") } } }
        Spacer(Modifier.height(16.dp)); Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(Color.White)) { Column(Modifier.padding(16.dp)) { Text("Historique des trajets", Modifier.padding(vertical = 10.dp)); Text("Trajets favoris", Modifier.padding(vertical = 10.dp)); Text("Aide & Support", Modifier.padding(vertical = 10.dp)) } }
    }
}
