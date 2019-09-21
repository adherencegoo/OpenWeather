package com.xdd.openweather.viewmodel

import com.xdd.openweather.model.enumModel.IJsonEnum

class EnumSelector<T : IJsonEnum>(val enums: List<T>) {
    private val selection = enums.map { it to false }.toMap().toMutableMap()

    fun onSelectionChanged(jsonEnum: T, checked: Boolean) {
        selection[jsonEnum] = checked
    }

    fun isSelected(enum: T) = selection[enum] ?: error("Unexpected key:$enum, in:$selection")

    fun getSelectedEnums() = selection.filterValues { it }.keys

    override fun toString(): String {
        return selection.toString()
    }
}