package com.peep.nocalorieleftbehind.core.util

sealed interface Ui {
    object Success: Ui
    object Loading : Ui
    object Error : Ui
}

