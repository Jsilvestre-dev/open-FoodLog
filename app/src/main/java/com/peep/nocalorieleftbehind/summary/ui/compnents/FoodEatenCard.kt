package com.peep.nocalorieleftbehind.summary.ui.compnents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peep.nocalorieleftbehind.core.domain.model.Nutrition
import com.peep.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.peep.nocalorieleftbehind.core.ui.theme.montserratFamily
import com.peep.nocalorieleftbehind.core.ui.theme.notoSansFamily
import com.peep.nocalorieleftbehind.summary.ui.model.FoodUi

@Composable
fun FoodEatenCard(
    modifier: Modifier,
    foodUi: FoodUi,
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
                        text = foodUi.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = montserratFamily,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = foodUi.nutrition.calories.toString() + " cal",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = notoSansFamily,
                        fontWeight = FontWeight.SemiBold
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
                            text = { Text("Edit Food") },
                            onClick = { onEditFood(foodUi.id) }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete Food") },
                            onClick = { onDeleteFood(foodUi.id) }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column {
                    Text(
                        text = foodUi.nutrition.protein.toString() + " g",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = notoSansFamily,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "protein",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = notoSansFamily,
                        fontWeight = FontWeight.Normal
                    )
                }

                Column {
                    Text(
                        text = foodUi.nutrition.carbs.toString() + " g",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = notoSansFamily,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "carbs",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = notoSansFamily,
                        fontWeight = FontWeight.Normal
                    )
                }

                Column {
                    Text(
                        text = foodUi.nutrition.fats.toString() + " g",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = notoSansFamily,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "fats",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = notoSansFamily,
                        fontWeight = FontWeight.Normal
                    )
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
            foodUi = FoodUi(
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