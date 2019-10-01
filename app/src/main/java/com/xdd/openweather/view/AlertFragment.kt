package com.xdd.openweather.view

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AlertFragment(alertBuilder: AlertDialog.Builder) : DialogFragment() {
    companion object {
        val TAG = AlertFragment::class.java.simpleName
    }

    private var alertDialogBuilder: AlertDialog.Builder? = alertBuilder

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return alertDialogBuilder!!.also { alertDialogBuilder = null  }.create()
    }
}