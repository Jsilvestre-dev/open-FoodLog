package com.peep.nocalorieleftbehind.logfood.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peep.nocalorieleftbehind.R
import com.peep.nocalorieleftbehind.core.di.CoreModule
import com.peep.nocalorieleftbehind.core.ui.model.NameUiState
import com.peep.nocalorieleftbehind.core.ui.model.NutritionUiState
import com.peep.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.peep.nocalorieleftbehind.core.ui.theme.montserratFamily
import com.peep.nocalorieleftbehind.core.ui.theme.notoSansFamily
import com.peep.nocalorieleftbehind.core.util.State
import com.peep.nocalorieleftbehind.logfood.di.LogFoodModule
import com.peep.nocalorieleftbehind.onboarding.di.OnboardingModule
import com.peep.nocalorieleftbehind.core.ui.model.NutrientInput
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LogFoodScreen(
    foodId: Long?,
    onFinishedScreen: () -> Unit
) {
    val viewModel = koinViewModel<LogFoodViewModel>()
    viewModel.initialize(foodId = foodId)
    val logFoodUiState = viewModel.logFoodUiFLow.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = onFinishedScreen,
                        colors = IconButtonDefaults.iconButtonVibrantColors()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.go_back)
                        )
                    }
                },
                title = {
                    Text(
                        fontFamily = montserratFamily,
                        text = "Logging Food",
                        style = MaterialTheme.typography.headlineSmall,
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
                    onClick = { viewModel.logFood(onFinishedScreen) },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Done,
                        contentDescription = null,
                        modifier = Modifier.size(IconButtonDefaults.largeIconSize)
                    )
                }
            }
        }
    ) { paddingValues ->
        AnimatedContent(
            targetState = logFoodUiState.value.state
        ) { targetState ->
            when (targetState) {
                is State.Error -> {}
                is State.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator()
                    }
                }

                is State.Success -> {
                    Ui(
                        paddingValues = paddingValues,
                        nameUiState = { logFoodUiState.value.nameUiState },
                        nutritionUi = { logFoodUiState.value.nutritionUi },
                        onFoodName = viewModel::onFoodName,
                        onMacro = viewModel::onNutrientInput
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun Ui(
    paddingValues: PaddingValues,
    nameUiState: () -> NameUiState,
    nutritionUi: () -> NutritionUiState,
    onFoodName: (String) -> Unit,
    onMacro: (NutrientInput) -> Unit
) {
    val focusManager = LocalFocusManager.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(
            key = "food_name"
        ) {
            val textFieldState = rememberTextFieldState()
            val focusedState = remember { mutableStateOf(false) }
            val hasStarted = remember { mutableStateOf(false) }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        focusedState.value = it.isFocused
                    },
                state = textFieldState,
                label = {
                    Text(
                        fontFamily = notoSansFamily,
                        text = stringResource(R.string.food_name)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    showKeyboardOnFocus = true
                ),
                onKeyboardAction = {
                    focusManager.moveFocus(FocusDirection.Down)
                },
                isError = nameUiState().state is State.Error,
                supportingText = {
                    AnimatedVisibility(nameUiState().state is State.Error) {
                        nameUiState().errorMessage?.let { stringRes ->
                            Text(
                                fontFamily = notoSansFamily,
                                text = stringResource(stringRes),
                            )
                        }
                    }
                },
                trailingIcon = {
                    AnimatedVisibility(focusedState.value) {
                        IconButton(
                            modifier = Modifier.size(IconButtonDefaults.extraSmallContainerSize()),
                            onClick = textFieldState::clearText
                        ) {
                            Icon(
                                modifier = Modifier.size(IconButtonDefaults.extraSmallIconSize),
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = stringResource(R.string.clear_text),
                            )
                        }
                    }
                }
            )

            textFieldState.text.toString().let { foodName ->
                LaunchedEffect(foodName) {
                    if (hasStarted.value) {
                        onFoodName(foodName)
                    }
                    hasStarted.value = true
                }
            }
        }

        items(
            items = nutritionUi().trackedNutrients(true),
            key = { nutrient -> nutrient.name },
            contentType = { nutrient -> nutritionUi().getNutrientUi(nutrient) }
        ) { nutrient ->

            val nutrientUiState = nutritionUi().getNutrientUi(nutrient) ?: return@items
            val textFieldState = rememberTextFieldState(initialText = nutrientUiState.data)
            val hasStarted = remember { mutableStateOf(false) }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(nutrient.iconResId),
                    contentDescription = null
                )

                OutlinedTextField(
                    modifier = Modifier,
                    state = textFieldState,
                    keyboardOptions = KeyboardOptions(
                        showKeyboardOnFocus = true,
                        keyboardType = KeyboardType.Number,
                        imeAction = if (nutrient == nutritionUi().trackedNutrients(true)
                                .last()
                        ) ImeAction.Done else ImeAction.Next
                    ),
                    onKeyboardAction = {
                        if (nutrient == nutritionUi().trackedNutrients(true)
                                .last()
                        ) focusManager.clearFocus() else focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    },
                    label = {
                        Text(
                            modifier = Modifier.weight(.5f),
                            fontFamily = notoSansFamily,
                            text = stringResource(nutrient.nameResId),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    isError = nutrientUiState.state is State.Error,
                    trailingIcon = {
                        AnimatedVisibility(nutrientUiState.state is State.Error) {
                            Icon(
                                modifier = Modifier.size(IconButtonDefaults.extraSmallIconSize),
                                imageVector = ImageVector.vectorResource(R.drawable.rounded_error_24),
                                contentDescription = null
                            )
                        }
                    },
                    suffix = {
                        Text(
                            text = nutrient.unit,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            fontFamily = notoSansFamily,
                        )
                    },
                    supportingText = {
                        AnimatedVisibility(nutrientUiState.state is State.Error) {
                            nutrientUiState.errorMessage?.let { stringRes ->
                                Text(
                                    fontFamily = notoSansFamily,
                                    text = stringResource(stringRes),
                                )
                            }
                        }
                    },
                    inputTransformation = InputTransformation.maxLength(4)
                )
            }


            textFieldState.text.toString().let { text ->
                LaunchedEffect(key1 = text) {
                    if (hasStarted.value) {
                        onMacro(
                            NutrientInput(
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

@Composable
@Preview
private fun Preview() {
    val context = LocalContext.current
    KoinApplicationPreview(
        application = {
            androidContext(context)
            modules(LogFoodModule, OnboardingModule, CoreModule)
        }
    ) {
        NoCalorieLeftBehindTheme {
            Scaffold { paddingValues ->
                Ui(
                    paddingValues = paddingValues,
                    nameUiState = { NameUiState() },
                    nutritionUi = {
                        NutritionUiState()
                    },
                    onFoodName = {},
                    onMacro = {}
                )
            }
        }
    }
}