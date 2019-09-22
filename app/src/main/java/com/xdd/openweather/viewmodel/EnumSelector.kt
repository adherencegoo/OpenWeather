package com.xdd.openweather.viewmodel

import android.content.Context
import android.widget.ImageButton
import androidx.databinding.BindingAdapter
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.xddlib.presentation.Lg
import com.xdd.openweather.R
import androidx.databinding.ObservableBoolean
import com.xdd.openweather.model.enumModel.IJsonEnum
import com.xdd.openweather.utils.DiffLiveData

class EnumSelector<T : IJsonEnum>(val enumCompanion: IJsonEnum.ICompanion<T>) {
    companion object {
        @JvmStatic
        @BindingAdapter("setSelectDeselectButton")
        fun ImageButton.setSelectDeselectButton(toSelect: Boolean?) {
            //wtf not work???? never called when CheckBox changed
            Lg.i(this, toSelect)
            setImageResource(if (toSelect == true) R.drawable.to_select_icon else R.drawable.to_deselect_icon)
        }
    }

    private val _toSelectAll = MutableLiveData<Boolean>()

    val toSelectAll: LiveData<Boolean> = DiffLiveData<Boolean>(_toSelectAll).apply {
        observeForever {
            Lg.d(this, it)//xdd
        }
    }

    private val onSelectionChangedListener = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            notifyCurrentSelectionChanged()
        }
    }

    val currentSelection = enumCompanion.enumList.map {
        it to ObservableBoolean().apply {
            addOnPropertyChangedCallback(onSelectionChangedListener)
        }
    }.toMap()

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

    private fun notifyCurrentSelectionChanged() {
        _toSelectAll.postValue(currentSelection.values.any { !it.get() })
    }

    fun onSelectDeselectAllClicked() {
        val checked = toSelectAll.value ?: false
        currentSelection.values.forEach {
            it.set(checked)
        }
    }
}