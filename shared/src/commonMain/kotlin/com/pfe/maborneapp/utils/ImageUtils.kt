package com.pfe.maborneapp.utils

import androidx.compose.ui.graphics.ImageBitmap

expect suspend fun loadImageBitmap(url: String): ImageBitmap
