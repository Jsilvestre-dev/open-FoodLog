package com.toadfrog.nocalorieleftbehind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.toadfrog.nocalorieleftbehind.core.data.local.datastore.AppConfigDatastoreDataSource
import com.toadfrog.nocalorieleftbehind.core.di.CoreModule
import com.toadfrog.nocalorieleftbehind.core.ui.LogFood
import com.toadfrog.nocalorieleftbehind.core.ui.Onboarding
import com.toadfrog.nocalorieleftbehind.core.ui.Preference
import com.toadfrog.nocalorieleftbehind.core.ui.Screen
import com.toadfrog.nocalorieleftbehind.core.ui.Summary
import com.toadfrog.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.toadfrog.nocalorieleftbehind.logfood.di.LogFoodModule
import com.toadfrog.nocalorieleftbehind.logfood.ui.LogFoodScreen
import com.toadfrog.nocalorieleftbehind.onboarding.ui.NutrientSelectionScreen
import com.toadfrog.nocalorieleftbehind.onboarding.ui.SetNutrientGoalScreen
import com.toadfrog.nocalorieleftbehind.onboarding.ui.OnboardingViewModel
import com.toadfrog.nocalorieleftbehind.onboarding.di.OnboardingModule
import com.toadfrog.nocalorieleftbehind.preference.di.PreferenceModule
import com.toadfrog.nocalorieleftbehind.preference.ui.PreferenceScreen
import com.toadfrog.nocalorieleftbehind.summary.di.SummaryModule
import com.toadfrog.nocalorieleftbehind.summary.ui.SummaryScreen
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.sharedKoinViewModel

class MainActivity : ComponentActivity() {

    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        analytics = Firebase.analytics
        val appConfig = AppConfigDatastoreDataSource(this)

        setContent {
            KoinApplication(
                application = {
                    androidContext(this@MainActivity)
                    modules(
                        CoreModule,
                        PreferenceModule,
                        SummaryModule,
                        OnboardingModule,
                        LogFoodModule,
                    )
                }
            ) {
                NoCalorieLeftBehindTheme {
                    val navController = rememberNavController()
                    val startDestination = remember { mutableStateOf<Screen>(Summary) }

                    LaunchedEffect(key1 = Unit) {
                        appConfig.isOnboardingCompleted().also { isOnboardingCompleted ->
                            if (!isOnboardingCompleted) startDestination.value = Onboarding
                        }
                    }

                    NavHost(
                        navController = navController,
                        startDestination = startDestination.value
                    ) {
                        navigation<Onboarding>(startDestination = Onboarding.NutrientSelection) {
                            composable<Onboarding.NutrientSelection> { navBackStackEntry ->
                                val viewModel = navBackStackEntry.sharedKoinViewModel<OnboardingViewModel>(
                                    navController = navController,
                                    navGraphRoute = Onboarding
                                )
                                val onboardingUiState = viewModel.onboardingUiState.collectAsStateWithLifecycle()

                                NutrientSelectionScreen(
                                    nutritionUiState = { onboardingUiState.value.nutritionUiState },
                                    onTrackNutrient = viewModel::onTrackNutrient,
                                    onContinue = { navController.navigate(route = Onboarding.SetNutrientGoal) }
                                )
                            }
                            composable<Onboarding.SetNutrientGoal> { navBackStackEntry ->
                                val viewModel = navBackStackEntry.sharedKoinViewModel<OnboardingViewModel>(
                                    navController = navController,
                                    navGraphRoute = Onboarding
                                )
                                val onboardingUiState = viewModel.onboardingUiState.collectAsStateWithLifecycle()

                                SetNutrientGoalScreen(
                                    onboardingUiState = { onboardingUiState.value },
                                    trackedNutrients = {
                                        onboardingUiState.value.nutritionUiState.trackedNutrients(
                                            includeCalories = true
                                        )
                                    },
                                    onNutrientGoalInput = viewModel::onNutrientGoalInput,
                                    onSave = {
                                        viewModel.savePreference(
                                            onCompletion = {
                                                navController.navigate(route = Summary)
                                                viewModel.viewModelScope.launch {
                                                    appConfig.onboardingCompleted()
                                                }
                                            }
                                        )
                                    }
                                )
                            }
                        }
                        composable<Summary> {
                            SummaryScreen(
                                onEditFood = { navController.navigate(LogFood(foodId = it)) },
                                onLogFood = { navController.navigate(LogFood()) },
                                onNavigateToPreference = { navController.navigate(Preference) }
                            )
                        }
                        composable<LogFood> { navBackstackEntry ->
                            val logFood = navBackstackEntry.toRoute<LogFood>()

                            LogFoodScreen(
                                foodId = logFood.foodId,
                                onFinishedScreen = { navController.popBackStack(route = Summary, inclusive = false) }
                            )
                        }
                        composable<Preference> {
                            PreferenceScreen()
                        }
                    }
                }
            }
        }
    }
}
