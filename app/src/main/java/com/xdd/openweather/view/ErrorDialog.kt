package com.xdd.openweather.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ErrorDialog : DialogFragment() {
    companion object {
        val TAG = ErrorDialog::class.java.simpleName
        private const val KEY_THROWABLE = "KEY_THROWABLE"

        fun newInstance(throwable: Throwable) = ErrorDialog().apply {
            arguments = Bundle().apply {
                putSerializable(KEY_THROWABLE, throwable)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val t = arguments?.getSerializable(KEY_THROWABLE) as Throwable
        return AlertDialog.Builder(requireContext())
            .setTitle(t.javaClass.simpleName)
            .setMessage(t.localizedMessage)
            .setPositiveButton(android.R.string.ok) {_, _ -> }
            .create()
    }
}