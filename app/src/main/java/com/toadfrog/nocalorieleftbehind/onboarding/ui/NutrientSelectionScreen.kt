package com.toadfrog.nocalorieleftbehind.onboarding.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toadfrog.nocalorieleftbehind.R
import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient
import com.toadfrog.nocalorieleftbehind.core.domain.nutrientList
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutritionUiState
import com.toadfrog.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.toadfrog.nocalorieleftbehind.core.ui.theme.montserratFamily
import com.toadfrog.nocalorieleftbehind.onboarding.ui.components.NutrientSelectionUi

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NutrientSelectionScreen(
    nutritionUiState: () -> NutritionUiState,
    onContinue: () -> Unit,
    onTrackNutrient: (Nutrient) -> Unit,
) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        fontFamily = montserratFamily,
                        fontWeight = FontWeight.Bold,
                        text = stringResource(R.string.nutrient_selection)
                    )
                }
            )
        },
        floatingActionButton = {
            FilledTonalIconButton(
                modifier =
                    Modifier
                        .size(
                            IconButtonDefaults.largeContainerSize(
                                IconButtonDefaults.IconButtonWidthOption.Wide
                            )
                        ),
                onClick = onContinue,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(IconButtonDefaults.extraLargeIconSize)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        NutrientSelectionUi(
            modifier = Modifier.padding(horizontal = 16.dp),
            paddingValues = paddingValues,
            trackedNutrients = { nutritionUiState().trackedNutrients() },
            onTrackNutrient = onTrackNutrient,
            selectableNutrients = nutrientList()
        )
    }
}

@Composable
@Preview
private fun Preview() {
    NoCalorieLeftBehindTheme {
        NutrientSelectionScreen(
            nutritionUiState = { NutritionUiState() },
            onContinue = {},
            onTrackNutrient = {}
        )
    }
}