@file:Suppress("unused")

package com.xdd.openweather.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

private val unserializableValues: MutableMap<String, Any> = mutableMapOf()

fun Bundle.putUnserializable(key: String, value: Any) {
    val cacheKey = value.toString()

    putString(key, cacheKey)
    unserializableValues[cacheKey] = value
}

fun Bundle.getUnserializable(key: String) = unserializableValues[getString(key)]!!

fun Bundle.removeUnserializable(key: String) = unserializableValues.remove(getString(key))!!

fun DialogFragment.open(manager: FragmentManager, tag: String) {
    val transaction = manager.beginTransaction()

    // remove existing fragment if any
    manager.findFragmentByTag(tag)?.let(transaction::remove)

    // will also commit the transaction
    show(transaction, tag)
}

fun Fragment.updateActionBar() {
    (requireActivity() as? AppCompatActivity)?.let {
        val enabled = it.supportFragmentManager.backStackEntryCount > 0
        it.supportActionBar?.setDisplayHomeAsUpEnabled(enabled)
    }
}