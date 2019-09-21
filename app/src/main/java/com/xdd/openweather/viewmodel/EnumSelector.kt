package com.xdd.openweather.viewmodel

import android.content.Context
import com.xdd.openweather.model.enumModel.IJsonEnum

class EnumSelector<T : IJsonEnum>(val enumCompanion: IJsonEnum.ICompanion<T>) {
    private val tmpSelection = HashMap<T, Boolean>()

    private val preferenceKey = enumCompanion.javaClass.name

    fun onSelectionChanged(jsonEnum: T, checked: Boolean) {
        tmpSelection[jsonEnum] = checked
    }

    fun isTmpSelected(enum: T) = tmpSelection[enum] ?: false

    private fun getTmpSelected() = tmpSelection.filterValues { it }.keys

    fun getActualSelected(context: Context) = getPreference(context)
        .getStringSet(preferenceKey, null)?.mapNotNull { enumCompanion.nativeStringMap[it] }
        ?: emptyList()

    fun loadFromPreference(context: Context) {
        tmpSelection.clear()
        getActualSelected(context).forEach {
            tmpSelection[it] = true
        }
    }

    fun saveToPreference(context: Context) {
        getPreference(context).edit()
            .remove(preferenceKey)
            .putStringSet(preferenceKey, getTmpSelected().map { it.toString() }.toSet())
            .apply()
    }

    private fun getPreference(context: Context) =
        context.getSharedPreferences(javaClass.simpleName, Context.MODE_PRIVATE)!!
}