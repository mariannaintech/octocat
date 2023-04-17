package com.octocat.utils

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

inline fun <reified T : Parcelable> Bundle.requireParcelable(key: String): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java) ?: error("'$key' parcelable not found")
    } else {
        getParcelable(key) ?: error("'$key' parcelable not found")
    }
}