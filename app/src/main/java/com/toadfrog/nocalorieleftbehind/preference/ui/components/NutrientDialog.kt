package com.toadfrog.nocalorieleftbehind.preference.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.ArcMode
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toadfrog.nocalorieleftbehind.R
import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutrientUiState
import com.toadfrog.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.toadfrog.nocalorieleftbehind.core.ui.theme.montserratFamily
import com.toadfrog.nocalorieleftbehind.core.ui.theme.notoSansFamily
import com.toadfrog.nocalorieleftbehind.core.util.State
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutrientInput

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.NutrientDialog(
    nutrientUiState: () -> NutrientUiState?,
    onInput: (NutrientInput) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
) {
    val hasStarted = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val contentBoundsTransform = BoundsTransform { initialBounds, targetBounds ->
        keyframes {
            durationMillis = 500
            initialBounds at 0 using ArcMode.ArcLinear using FastOutSlowInEasing
            targetBounds at 500
        }
    }

    AnimatedVisibility(
        visible = remember { derivedStateOf { nutrientUiState() != null } }.value,
        enter = EnterTransition.None,
        exit = ExitTransition.None,
        label = "nutrient_dialog"
    ) {
        val nutrientUiState = nutrientUiState()
        if (nutrientUiState != null) {

            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = .5f))
                )

                Box(
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState("${nutrientUiState.nutrient.name}_bounds"),
                            animatedVisibilityScope = this@AnimatedVisibility,
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                            boundsTransform = contentBoundsTransform

                        )
                ) {

                    Card(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        val textFieldState = rememberTextFieldState(initialText = nutrientUiState.data)
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            Box(
                                modifier = Modifier
                                    .sharedBounds(
                                        sharedContentState = rememberSharedContentState(key = "${nutrientUiState.nutrient.name}_title"),
                                        animatedVisibilityScope = this@AnimatedVisibility,
                                        resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                                        boundsTransform = contentBoundsTransform

                                    )
                            ) {
                                Text(
                                    text = stringResource(nutrientUiState.nutrient.nameResId),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = montserratFamily
                                )
                            }

                            OutlinedTextField(
                                state = textFieldState,
                                keyboardOptions = KeyboardOptions(
                                    showKeyboardOnFocus = true,
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                onKeyboardAction = {
                                    focusManager.clearFocus()
                                },
                                isError = nutrientUiState.state is State.Error,
                                suffix = {
                                    Text(
                                        text = nutrientUiState.nutrient.unit,
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
                                inputTransformation = InputTransformation.maxLength(4),
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                TextButton(
                                    modifier = Modifier.width(100.dp),
                                    onClick = onDismiss,
                                ) {
                                    Text(
                                        text = stringResource(R.string.cancel),
                                        fontFamily = notoSansFamily,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                TextButton(
                                    modifier = Modifier.width(100.dp),
                                    onClick = onSave,
                                    enabled = nutrientUiState.state is State.Success
                                ) {
                                    Text(
                                        text = stringResource(R.string.save),
                                        fontFamily = notoSansFamily,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        textFieldState.text.toString().let { input ->
                            LaunchedEffect(input) {
                                println("textField $input")
                                if (hasStarted.value) {
                                    onInput(
                                        NutrientInput(
                                            nutrient = nutrientUiState.nutrient,
                                            amount = input
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
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun Preview() {
    NoCalorieLeftBehindTheme {
        SharedTransitionLayout {
            AnimatedVisibility(true) {
                NutrientDialog(
                    nutrientUiState = { NutrientUiState(nutrient = Nutrient.CALORIES) },
                    onInput = {},
                    onDismiss = {},
                    onSave = {}
                )
            }
        }
    }
}