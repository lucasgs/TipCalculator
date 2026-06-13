package com.dendron.tipcalculator

import android.content.Context
import androidx.core.os.ConfigurationCompat
import java.util.Locale

fun Context.currentLocale(): Locale {
    return ConfigurationCompat.getLocales(resources.configuration)[0] ?: Locale.getDefault()
}
