package com.peep.nocalorieleftbehind.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.peep.nocalorieleftbehind.core.di.CoreModule
import com.peep.nocalorieleftbehind.core.domain.Nutrient
import com.peep.nocalorieleftbehind.core.domain.nutrientList
import com.peep.nocalorieleftbehind.core.ui.NutritionUi
import com.peep.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.peep.nocalorieleftbehind.core.ui.theme.montserratFamily
import com.peep.nocalorieleftbehind.core.ui.theme.notoSansFamily
import com.peep.nocalorieleftbehind.core.util.Ui
import com.peep.nocalorieleftbehind.core.util.UiElement
import com.peep.nocalorieleftbehind.onboarding.di.OnboardingModule
import com.peep.nocalorieleftbehind.preference.ui.NutrientData
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NutrientTargetsScreen(
    ui: () -> Ui,
    selectedNutrient: () -> List<Nutrient>,
    nutritionUi: () -> NutritionUi,
    onInput: (NutrientData) -> Unit,
    onSave: () -> Unit,
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Set daily Nutrient goals",
                        fontFamily = montserratFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
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
        }
    ) { paddingValues ->

        AnimatedContent(
            targetState = ui()
        ) { uiTarget ->
            when (uiTarget) {
                is Ui.Error -> {

                }

                is Ui.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator()
                    }
                }

                is Ui.Success -> {
                    SuccessfulUI(
                        paddingValues = paddingValues,
                        selectedNutrient = selectedNutrient,
                        nutritionUi = nutritionUi,
                        onInput = onInput,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SuccessfulUI(
    paddingValues: PaddingValues,
    selectedNutrient: () -> List<Nutrient>,
    nutritionUi: () -> NutritionUi,
    onInput: (NutrientData) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = selectedNutrient(),
            key = { it.name },
            contentType = { nutritionUi().getNutrientUi(it) }
        ) { nutrient ->

            val nutrientUiState = nutritionUi().getNutrientUi(nutrient)
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
                    isError = nutrientUiState is UiElement.Error,
                    labelPosition = TextFieldLabelPosition.Attached(),
                    label = {
                        if (nutrient != Nutrient.CALORIES) {
                            Text(
                                text = "Optional",
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
                        if (nutrientUiState is UiElement.Error) {
                            nutrientUiState.messageRes?.let {
                                Text(
                                    text = stringResource(nutrientUiState.messageRes),
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
                        onInput(
                            NutrientData(
                                value = text,
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
        Scaffold { paddingValues ->
            NoCalorieLeftBehindTheme {
                SuccessfulUI(
                    paddingValues = paddingValues,
                    selectedNutrient = { nutrientList(true) },
                    nutritionUi = { NutritionUi() },
                    onInput = {},
                )
            }
        }
    }
}