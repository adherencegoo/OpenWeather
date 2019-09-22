package com.xdd.openweather.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xdd.openweather.databinding.EnumSelectorDialogBinding
import com.xdd.openweather.databinding.EnumSelectorRowBinding
import com.xdd.openweather.model.enumModel.IJsonEnum
import com.xdd.openweather.utils.getUnserializable
import com.xdd.openweather.utils.putUnserializable
import com.xdd.openweather.viewmodel.EnumSelector
import com.xdd.openweather.viewmodel.WeatherViewModel
import kotlin.reflect.KClass

class EnumSelectorDialog : DialogFragment() {
    companion object {
        val TAG = EnumSelectorDialog::class.java.simpleName
        private const val KEY_ENUM_CLASS = "keyEnumClass"
        private const val KEY_KEEP_SELECTOR_STATE = "KEY_KEEP_SELECTOR_STATE"

        fun <T : IJsonEnum> newInstance(kClass: KClass<T>): EnumSelectorDialog {
            return EnumSelectorDialog().apply {
                arguments = Bundle().apply {
                    putUnserializable(KEY_ENUM_CLASS, kClass)
                }
            }
        }
    }

    class Adapter<T : IJsonEnum>(private val enumSelector: EnumSelector<T>) :
        AbstractRecyclerViewAdapter<EnumSelectorRowBinding, T>(enumSelector.enumCompanion.enumList.toMutableList()) {

        companion object {
            val viewPool = RecyclerView.RecycledViewPool()
        }

        override fun onBindData(binding: EnumSelectorRowBinding, data: T) {
            binding.isEnumSelected = enumSelector.currentSelection[data]
            binding.jsonEnum = data
        }

        override fun onCreateBinding(inflater: LayoutInflater, parent: ViewGroup) =
            EnumSelectorRowBinding.inflate(inflater, parent, false)!!
    }

    private fun getEnumSelector(): EnumSelector<*> {
        val enumClass = arguments?.getUnserializable(KEY_ENUM_CLASS)
            ?: error("Missing argument: KEY_ENUM_CLASS, arguments:$arguments")

        val viewModel = ViewModelProviders.of(requireActivity()).get(WeatherViewModel::class.java)
        return viewModel.enumSelectorMap[enumClass] ?: error("Unexpected enumClass: $enumClass")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_KEEP_SELECTOR_STATE, true)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val enumSelector = getEnumSelector()

        // Enter if null or false
        if (savedInstanceState?.getBoolean(KEY_KEEP_SELECTOR_STATE) != true) {
            enumSelector.loadFromPreference(requireContext())
        }

        return AlertDialog.Builder(requireActivity())
            .setView(createCustomView(enumSelector))
            .setPositiveButton(android.R.string.ok) { _, _ ->
                enumSelector.saveToPreference(requireActivity())
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

    private fun createCustomView(enumSelector: EnumSelector<*>): View {
        val viewDataBinding =
            EnumSelectorDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)

        viewDataBinding.paramsRecycler.apply {
            layoutManager = GridLayoutManager(context, 2)
            setRecycledViewPool(Adapter.viewPool)
            adapter = Adapter(enumSelector)
        }

        viewDataBinding.enumSelector = enumSelector

        return viewDataBinding.root
    }
}