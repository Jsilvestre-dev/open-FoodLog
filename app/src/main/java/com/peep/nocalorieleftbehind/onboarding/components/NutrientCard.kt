package com.peep.nocalorieleftbehind.onboarding.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.rectangle
import androidx.graphics.shapes.star
import com.peep.nocalorieleftbehind.core.domain.Nutrient
import com.peep.nocalorieleftbehind.core.ui.MorphPolygonShape
import com.peep.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.peep.nocalorieleftbehind.core.ui.theme.notoSansFamily

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NutrientCard(
    isSelected: Boolean,
    nutrient: Nutrient,
    onClick: () -> Unit
) {
    val checked = remember { mutableStateOf(isSelected) }
    checked.value = isSelected
    val containerColor =
        if (checked.value) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerHighest
    val contentColor =
        if (checked.value) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
    val morph = remember {
        Morph(
            start = RoundedPolygon.rectangle(rounding = CornerRounding(radius = .6f)),
            end = RoundedPolygon.star(
                numVerticesPerRadius = 8,
                innerRadius = .8f,
                rounding = CornerRounding(radius = .15f),
            )
        )
    }
    val animatedProgress = animateFloatAsState(
        targetValue = if (checked.value) 1f else 0f,
        label = "progress",
    )

    Box(
        modifier = Modifier
            .graphicsLayer {
                shape = MorphPolygonShape(morph, animatedProgress.value)
                clip = true
            }
            .drawBehind {
                drawRect(containerColor)
            }
            .clickable(
                onClick = {
                    onClick()
                    checked.value = isSelected
                }
            )
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(IconButtonDefaults.extraLargeIconSize),
                imageVector = ImageVector.vectorResource(nutrient.iconResId),
                contentDescription = null,
                tint = contentColor
            )

            Text(
                color = contentColor,
                fontFamily = notoSansFamily,
                style = MaterialTheme.typography.titleLarge,
                text = stringResource(nutrient.nameResId),
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    NoCalorieLeftBehindTheme {
        NutrientCard(
            isSelected = false,
            nutrient = Nutrient.PROTEIN,
            onClick = {}
        )
    }
}