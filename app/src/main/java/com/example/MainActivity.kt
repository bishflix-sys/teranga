package com.example

import android.os.Bundle
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ui.MainScreen
import com.example.data.repository.UserAccountRepository
import com.example.ui.LoginScreen
import com.example.ui.TransitViewModel
import com.example.ui.language.LanguageSelectionViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  private val viewModel: TransitViewModel by viewModels()
  private val languageViewModel: LanguageSelectionViewModel by viewModels()
  private lateinit var accountRepository: UserAccountRepository
  private val locationPermissionLauncher = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) { permissions ->
    if (permissions.values.any { it }) viewModel.refreshUserLocation()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    accountRepository = UserAccountRepository(this)
    val onboardingPreferences = getSharedPreferences("teranga_onboarding", Context.MODE_PRIVATE)
    val onboardingCompleted = onboardingPreferences.getBoolean("completed", false)

    if (onboardingCompleted && accountRepository.isLoggedIn) {
      requestLocationAccess()
    }

    setContent {
      MyApplicationTheme {
        if (onboardingCompleted) {
          AuthenticatedContent()
        } else {
          com.example.ui.OnboardingScreen {
            onboardingPreferences.edit().putBoolean("completed", true).apply()
            showMainContent()
          }
        }
      }
    }
  }

  @androidx.compose.runtime.Composable
  private fun AppContent() {
    MainScreen(
      viewModel = viewModel,
      languageViewModel = languageViewModel
    )
  }

  private fun showMainContent() {
    setContent {
      MyApplicationTheme { AuthenticatedContent() }
    }
  }

  @androidx.compose.runtime.Composable
  private fun AuthenticatedContent() {
    if (accountRepository.isLoggedIn) {
      AppContent()
    } else {
      LoginScreen(
        accountRepository = accountRepository,
        onAuthenticated = { showMainContent() }
      )
    }
  }

  private fun requestLocationAccess() {
    locationPermissionLauncher.launch(
      arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
      )
    )
    viewModel.refreshUserLocation()
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "SunuYoon Dakar: Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  MyApplicationTheme { Greeting("Dakar") }
}

