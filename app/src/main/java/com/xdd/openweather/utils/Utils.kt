@file:Suppress("unused")

package com.xdd.openweather.utils

import android.os.Bundle

private val unserializableValues: MutableMap<String, Any> = mutableMapOf()

fun Bundle.putUnserializable(key: String, value: Any) {
    val cacheKey = value.toString()

    putString(key, cacheKey)
    unserializableValues[cacheKey] = value
}

fun Bundle.getUnserializable(key: String) = unserializableValues[getString(key)]!!

fun Bundle.removeUnserializable(key: String) = unserializableValues.remove(getString(key))!!