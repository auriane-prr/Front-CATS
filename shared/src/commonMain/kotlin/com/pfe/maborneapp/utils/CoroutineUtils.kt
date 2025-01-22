package com.pfe.maborneapp.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

fun provideViewModelScope(): CoroutineScope {
    return CoroutineScope(SupervisorJob() + Dispatchers.Main)
}
