package com.toadfrog.nocalorieleftbehind.core.ui

import kotlinx.serialization.Serializable

// routes
@Serializable
object Summary : Screen

@Serializable
data class LogFood(val foodId: Long? = null) : Screen

@Serializable
object Preference : Screen

@Serializable
object Onboarding : Screen {
    @Serializable
    object NutrientSelection

    @Serializable
    object SetNutrientGoal
}

interface Screen