package com.xdd.openweather.viewmodel

import android.content.Context
import androidx.databinding.ObservableBoolean
import com.xdd.openweather.model.enumModel.IJsonEnum

class EnumSelector<T : IJsonEnum>(val enumCompanion: IJsonEnum.ICompanion<T>) {
    val currentSelection = enumCompanion.enumList.map { it to ObservableBoolean() }.toMap()

    private val preferenceKey = enumCompanion.javaClass.name

    private fun getCurrentSelected() = currentSelection.filterValues(ObservableBoolean::get).keys

    fun getActualSelected(context: Context) = getPreference(context)
        .getStringSet(preferenceKey, null)?.mapNotNull { enumCompanion.nativeStringMap[it] }
        ?: emptyList()

    fun loadFromPreference(context: Context) {
        currentSelection.values.forEach {
            it.set(false)
        }
        getActualSelected(context).forEach {
            currentSelection[it]?.set(true)
        }
    }

    fun saveToPreference(context: Context) {
        getPreference(context).edit()
            .remove(preferenceKey)
            .putStringSet(preferenceKey, getCurrentSelected().map { it.toString() }.toSet())
            .apply()
    }

    private fun getPreference(context: Context) =
        context.getSharedPreferences(javaClass.simpleName, Context.MODE_PRIVATE)!!
}