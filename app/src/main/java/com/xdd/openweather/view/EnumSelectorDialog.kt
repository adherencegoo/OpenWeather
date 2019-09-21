package com.xdd.openweather.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
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

        fun <T : IJsonEnum> newInstance(kClass: KClass<T>): EnumSelectorDialog {
            return EnumSelectorDialog().apply {
                arguments = Bundle().apply {
                    putUnserializable(KEY_ENUM_CLASS, kClass)
                }
            }
        }
    }

    class Adapter<T : IJsonEnum>(private val enumSelector: EnumSelector<T>) :
        AbstractRecyclerViewAdapter<EnumSelectorRowBinding, T>(enumSelector.enums.toMutableList()) {

        override fun onBindData(binding: EnumSelectorRowBinding, data: T) {
            binding.enumSelector = enumSelector
            binding.jsonEnum = data
        }

        override fun onCreateBinding(inflater: LayoutInflater, parent: ViewGroup) =
            EnumSelectorRowBinding.inflate(inflater, parent, false)!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val localActivity = activity ?: return super.onCreateDialog(savedInstanceState)

        return AlertDialog.Builder(localActivity)
            .setView(createCustomView(localActivity))
            .setPositiveButton(android.R.string.ok, null)
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

    private fun createCustomView(localActivity: FragmentActivity): View? {
        val enumClass = arguments?.getUnserializable(KEY_ENUM_CLASS)
            ?: error("Missing argument: KEY_ENUM_CLASS, arguments:$arguments")

        val viewModel = ViewModelProviders.of(localActivity).get(WeatherViewModel::class.java)
        val enumSelector =
            viewModel.enumSelectorMap[enumClass] ?: error("Unexpected enumClass: $enumClass")

        val viewDataBinding =
            EnumSelectorDialogBinding.inflate(LayoutInflater.from(localActivity), null, false)

        viewDataBinding.paramsRecycler.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = Adapter(enumSelector)
        }

        return viewDataBinding.root
    }
}