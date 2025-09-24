package com.peep.nocalorieleftbehind

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
import com.peep.nocalorieleftbehind.core.data.local.datastore.AppConfigDatastoreDataSource
import com.peep.nocalorieleftbehind.core.di.CoreModule
import com.peep.nocalorieleftbehind.core.ui.LogFood
import com.peep.nocalorieleftbehind.core.ui.Onboarding
import com.peep.nocalorieleftbehind.core.ui.Preference
import com.peep.nocalorieleftbehind.core.ui.Screen
import com.peep.nocalorieleftbehind.core.ui.Summary
import com.peep.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.peep.nocalorieleftbehind.logfood.di.LogFoodModule
import com.peep.nocalorieleftbehind.logfood.ui.LogFoodScreen
import com.peep.nocalorieleftbehind.onboarding.NutrientSelectionScreen
import com.peep.nocalorieleftbehind.onboarding.NutrientTargetsScreen
import com.peep.nocalorieleftbehind.onboarding.OnboardingViewModel
import com.peep.nocalorieleftbehind.onboarding.di.OnboardingModule
import com.peep.nocalorieleftbehind.preference.di.PreferenceModule
import com.peep.nocalorieleftbehind.preference.ui.PreferenceScreen
import com.peep.nocalorieleftbehind.summary.di.SummaryModule
import com.peep.nocalorieleftbehind.summary.ui.SummaryScreen
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.sharedKoinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

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
                            println("onboarding completed: $isOnboardingCompleted")
                            if (!isOnboardingCompleted) startDestination.value = Onboarding
                        }
                    }

                    NavHost(
                        navController = navController,
                        startDestination = startDestination.value
                    ) {
                        navigation<Onboarding>(startDestination = Onboarding.NutrientSelection) {
                            composable<Onboarding.NutrientSelection> {
                                val viewModel = it.sharedKoinViewModel<OnboardingViewModel>(
                                    navController = navController,
                                    navGraphRoute = Onboarding
                                )
                                val nutritionUiState = viewModel.nutritionUiFlow.collectAsStateWithLifecycle()

                                NutrientSelectionScreen(
                                    selectedNutrient = { nutritionUiState.value.trackedNutrients() },
                                    onSelectedNutrient = viewModel::onSelected,
                                    onContinue = { navController.navigate(route = Onboarding.NutrientTargets) }
                                )
                            }
                            composable<Onboarding.NutrientTargets> {
                                val viewModel = it.sharedKoinViewModel<OnboardingViewModel>(
                                    navController = navController,
                                    navGraphRoute = Onboarding
                                )
                                val nutritionUiState = viewModel.nutritionUiFlow.collectAsStateWithLifecycle()
                                val screenUiState = viewModel.screenUiFlow.collectAsStateWithLifecycle()

                                NutrientTargetsScreen(
                                    screenUiState = { screenUiState.value },
                                    selectedNutrient = { nutritionUiState.value.trackedNutrients(includeCalories = true) },
                                    nutritionUi = { nutritionUiState.value },
                                    onInput = viewModel::onInput,
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
