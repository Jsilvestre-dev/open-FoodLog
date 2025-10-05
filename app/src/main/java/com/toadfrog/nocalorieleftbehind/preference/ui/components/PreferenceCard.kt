package com.toadfrog.nocalorieleftbehind.preference.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutrientUiState
import com.toadfrog.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.toadfrog.nocalorieleftbehind.core.ui.theme.montserratFamily
import com.toadfrog.nocalorieleftbehind.core.ui.theme.notoSansFamily

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PreferenceCard(
    modifier: Modifier = Modifier,
    selectedNutrient: () -> Nutrient?,
    nutrientUiState: NutrientUiState,
    onRemove: (Nutrient) -> Unit,
    onEdit: () -> Unit
) {
    AnimatedVisibility(
        visible = remember { derivedStateOf { nutrientUiState.nutrient != selectedNutrient() } }.value,
        modifier = modifier,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
        label = "${nutrientUiState.nutrient.name}_card"
    ) {
        Box(
            modifier = modifier.sharedBounds(
                sharedContentState = rememberSharedContentState("${nutrientUiState.nutrient.name}_bounds"),
                animatedVisibilityScope = this@AnimatedVisibility,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
            )
        ) {
            Card {
                Column(
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Box(
                            modifier = Modifier.sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "${nutrientUiState.nutrient.name}_title"),
                                animatedVisibilityScope = this@AnimatedVisibility,
                            )
                        ) {
                            Text(
                                text = stringResource(nutrientUiState.nutrient.nameResId),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = montserratFamily
                            )
                        }

                        Box {
                            val expanded = remember { mutableStateOf(false) }

                            IconButton(
                                onClick = { expanded.value = true },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More Options"
                                )
                            }

                            DropdownMenu(
                                expanded = expanded.value,
                                onDismissRequest = { expanded.value = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Edit") },
                                    onClick = remember {
                                        {
                                            onEdit()
                                            expanded.value = false
                                        }
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Remove") },
                                    onClick = remember {
                                        {
                                            onRemove(nutrientUiState.nutrient)
                                            expanded.value = false
                                        }
                                    },
                                    enabled = nutrientUiState.nutrient != Nutrient.CALORIES
                                )
                            }
                        }
                    }
                    Text(
                        text = nutrientUiState.data.plus(" ${nutrientUiState.nutrient.unit}"),
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = notoSansFamily,
                        fontWeight = FontWeight.Medium
                    )
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
                PreferenceCard(
                    selectedNutrient = { null },
                    nutrientUiState = NutrientUiState(nutrient = Nutrient.CALORIES, data = "0"),
                    onRemove = {},
                    onEdit = {}
                )
            }
        }
    }
}