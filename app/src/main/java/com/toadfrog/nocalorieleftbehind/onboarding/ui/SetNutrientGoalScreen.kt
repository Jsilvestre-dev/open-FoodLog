package com.toadfrog.nocalorieleftbehind.onboarding.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toadfrog.nocalorieleftbehind.R
import com.toadfrog.nocalorieleftbehind.core.di.CoreModule
import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient
import com.toadfrog.nocalorieleftbehind.core.domain.nutrientList
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutritionUiState
import com.toadfrog.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.toadfrog.nocalorieleftbehind.core.ui.theme.montserratFamily
import com.toadfrog.nocalorieleftbehind.core.ui.theme.notoSansFamily
import com.toadfrog.nocalorieleftbehind.core.util.State
import com.toadfrog.nocalorieleftbehind.onboarding.di.OnboardingModule
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutrientDto
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SetNutrientGoalScreen(
    onboardingUiState: () -> OnboardingUiState,
    trackedNutrients: () -> List<Nutrient>,
    onNutrientGoalInput: (NutrientDto) -> Unit,
    onSave: () -> Unit,
) {
    AnimatedContent(
        targetState = remember { derivedStateOf { onboardingUiState().state } }.value
    ) { stateTarget ->
        when (stateTarget) {
            is State.Error -> {

            }

            is State.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }

            is State.Success -> {
                SuccessfulUI(
                    trackedNutrients = trackedNutrients,
                    nutritionUi = { onboardingUiState().nutritionUiState },
                    onNutrientGoalInput = onNutrientGoalInput,
                    onSave = onSave,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SuccessfulUI(
    trackedNutrients: () -> List<Nutrient>,
    nutritionUi: () -> NutritionUiState,
    onNutrientGoalInput: (NutrientDto) -> Unit,
    onSave: () -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.set_daily_intake_goal),
                        fontFamily = montserratFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FilledTonalIconButton(
                modifier =
                    Modifier
                        .padding(horizontal = 8.dp)
                        .size(
                            IconButtonDefaults.largeContainerSize(
                                IconButtonDefaults.IconButtonWidthOption.Wide
                            )
                        ),
                onClick = onSave,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(IconButtonDefaults.largeIconSize)
                )
            }

        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = trackedNutrients(),
                key = { it.name },
                contentType = { nutritionUi().getNutrientUi(it) }
            ) { nutrient ->

                val nutrientUiState = nutritionUi().getNutrientUi(nutrient) ?: return@items
                val textFieldState = rememberTextFieldState()
                val hasStarted = remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        modifier = Modifier.weight(.4f),
                        text = stringResource(nutrient.nameResId),
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = notoSansFamily
                    )

                    OutlinedTextField(
                        modifier = Modifier.weight(.6f),
                        isError = nutrientUiState.state is State.Error,
                        labelPosition = TextFieldLabelPosition.Attached(),
                        label = {
                            if (nutrient != Nutrient.CALORIES) {
                                Text(
                                    text = stringResource(R.string.optional),
                                    fontFamily = notoSansFamily
                                )
                            }
                        },

                        state = textFieldState,
                        keyboardOptions = KeyboardOptions(
                            showKeyboardOnFocus = true,
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        suffix = {
                            Text(
                                text = nutrient.unit,
                                fontWeight = FontWeight.Medium,
                                fontFamily = notoSansFamily,
                            )
                        },
                        supportingText = {
                            AnimatedVisibility(visible = nutrientUiState.state is State.Error) {
                                nutrientUiState.errorMessage?.let { errorMessage ->
                                    Text(
                                        text = stringResource(errorMessage),
                                        fontFamily = notoSansFamily,
                                    )
                                }
                            }
                        },
                        inputTransformation = InputTransformation.maxLength(4)
                    )
                }

                textFieldState.text.toString().let { text ->
                    LaunchedEffect(text) {
                        if (hasStarted.value) {
                            onNutrientGoalInput(
                                NutrientDto(
                                    amount = text,
                                    nutrient = nutrient
                                )
                            )
                        }
                        hasStarted.value = true
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSuccessfulUi() {
    val context = LocalContext.current
    KoinApplication(
        application = {
            androidContext(context)
            modules(OnboardingModule, CoreModule)
        }
    ) {
        NoCalorieLeftBehindTheme {
            SuccessfulUI(
                trackedNutrients = { nutrientList(true) },
                nutritionUi = { NutritionUiState() },
                onNutrientGoalInput = {},
                onSave = {}
            )
        }
    }
}