package com.toadfrog.nocalorieleftbehind.summary.ui.model

import androidx.annotation.Keep
import androidx.compose.runtime.Immutable
import com.toadfrog.nocalorieleftbehind.core.domain.model.Nutrition
import kotlinx.serialization.Serializable

@Serializable
@Immutable
@Keep
data class FoodUiState(
    val id: Long,
    val name: String,
    val nutrition: Nutrition,
    val timeStampEpochSec: Long
)