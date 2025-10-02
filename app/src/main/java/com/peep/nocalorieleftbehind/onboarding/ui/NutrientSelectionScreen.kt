package com.peep.nocalorieleftbehind.onboarding.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peep.nocalorieleftbehind.core.domain.Nutrient
import com.peep.nocalorieleftbehind.core.domain.nutrientList
import com.peep.nocalorieleftbehind.core.ui.model.NutritionUiState
import com.peep.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.peep.nocalorieleftbehind.onboarding.ui.components.NutrientSelectionUi

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NutrientSelectionScreen(
    nutritionUiState: () -> NutritionUiState,
    onContinue: () -> Unit,
    onTrackNutrient: (Nutrient) -> Unit,
) {
    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        contentWindowInsets = WindowInsets(left = 16.dp, right = 16.dp),
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