package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ui.screens.MainMenuScreen
import com.example.ui.screens.MainGameScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.GameViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.GamePhase

enum class AppScreen {
    MAIN_MENU,
    GAME_DASHBOARD
}

class MainActivity : ComponentActivity() {
    private val gameViewModel: GameViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val gameState by gameViewModel.gameState.collectAsStateWithLifecycle()
            val isCorporate = gameState.gamePhase == GamePhase.CORPORATE

            MyApplicationTheme(darkTheme = isCorporate) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf(AppScreen.MAIN_MENU) }

                    when (currentScreen) {
                        AppScreen.MAIN_MENU -> {
                            MainMenuScreen(
                                viewModel = gameViewModel,
                                onStartGame = { currentScreen = AppScreen.GAME_DASHBOARD }
                            )
                        }
                        AppScreen.GAME_DASHBOARD -> {
                            MainGameScreen(
                                viewModel = gameViewModel,
                                onNavigateToMainMenu = { currentScreen = AppScreen.MAIN_MENU }
                            )
                        }
                    }
                }
            }
        }
    }
}


