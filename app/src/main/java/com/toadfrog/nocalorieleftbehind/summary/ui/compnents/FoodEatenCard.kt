package com.toadfrog.nocalorieleftbehind.summary.ui.compnents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toadfrog.nocalorieleftbehind.core.domain.model.Nutrition
import com.toadfrog.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.toadfrog.nocalorieleftbehind.core.ui.theme.montserratFamily
import com.toadfrog.nocalorieleftbehind.core.ui.theme.notoSansFamily
import com.toadfrog.nocalorieleftbehind.summary.ui.model.FoodUiState

@Composable
fun FoodEatenCard(
    modifier: Modifier,
    foodUiState: FoodUiState,
    onDeleteFood: (Long) -> Unit,
    onEditFood: (foodId: Long) -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Box {

            Box(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
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
                        text = { Text("Edit Food") },
                        onClick = { onEditFood(foodUiState.id) }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete Food") },
                        onClick = { onDeleteFood(foodUiState.id) }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(
                            text = foodUiState.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = montserratFamily,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = foodUiState.nutrition.calories.toString() + " cal",
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = notoSansFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.SpaceEvenly
                ) {
                    foodUiState.nutrition.getMacronutrients().forEach { nutrientDto ->
                        Column {
                            Text(
                                text = nutrientDto.amount.plus(" " + nutrientDto.nutrient.unit),
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = notoSansFamily,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = stringResource(nutrientDto.nutrient.nameResId),
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = notoSansFamily,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    NoCalorieLeftBehindTheme {
        FoodEatenCard(
            modifier = Modifier.fillMaxWidth(),
            foodUiState = FoodUiState(
                id = 0,
                name = "Watermelon",
                nutrition = Nutrition(
                    calories = 100,
                    protein = 4,
                    carbs = 16,
                    fats = 0,
                ),
                timeStampEpochSec = 1000L
            ),
            onDeleteFood = {},
            onEditFood = {}
        )
    }
}