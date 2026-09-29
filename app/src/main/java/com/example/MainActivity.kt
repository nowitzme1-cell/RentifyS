package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.db.RentifyDatabase
import com.example.data.repository.RentifyRepository
import com.example.ui.RentifyViewModel
import com.example.ui.RentifyViewModelFactory
import com.example.ui.Screen
import com.example.ui.components.FloatingGlassBottomBar
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ItemDetailScreen
import com.example.ui.screens.PostItemScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RequestsScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = RentifyDatabase.getDatabase(applicationContext, lifecycleScope)
        val repository = RentifyRepository(database.rentifyDao())

        setContent {
            val viewModel: RentifyViewModel = viewModel(
                factory = RentifyViewModelFactory(repository)
            )

            MyApplicationTheme {
                RentifyApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun RentifyApp(viewModel: RentifyViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val allRequests by viewModel.allRequests.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val pendingRequestsCount = allRequests.count { it.status.equals("pending", ignoreCase = true) }

    // Toast feedback handler
    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    // Hardware Back Button Handler
    BackHandler(enabled = currentScreen !is Screen.Home) {
        val handled = viewModel.navigateBack()
        if (!handled) {
            viewModel.navigateTo(Screen.Home)
        }
    }

    val isRootTab = currentScreen is Screen.Home ||
            currentScreen is Screen.Search ||
            currentScreen is Screen.Post ||
            currentScreen is Screen.Requests ||
            currentScreen is Screen.Profile

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "screen_transition",
            modifier = Modifier.fillMaxSize()
        ) { screen ->
            when (screen) {
                is Screen.Home -> HomeScreen(viewModel = viewModel)
                is Screen.Search -> SearchScreen(viewModel = viewModel)
                is Screen.Post -> PostItemScreen(viewModel = viewModel)
                is Screen.Requests -> RequestsScreen(viewModel = viewModel)
                is Screen.Profile -> ProfileScreen(viewModel = viewModel)
                is Screen.ItemDetail -> ItemDetailScreen(viewModel = viewModel)
                is Screen.Chat -> ChatScreen(viewModel = viewModel)
                is Screen.Admin -> AdminScreen(viewModel = viewModel)
                is Screen.Verification -> ProfileScreen(viewModel = viewModel)
            }
        }

        // Floating Bottom Navigation Bar (Shown on main root tabs)
        if (isRootTab) {
            FloatingGlassBottomBar(
                currentScreen = currentScreen,
                pendingRequestsCount = pendingRequestsCount,
                onTabSelected = { targetScreen ->
                    viewModel.navigateTo(targetScreen)
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
