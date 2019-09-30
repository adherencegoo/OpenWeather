package com.xdd.openweather.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.xdd.openweather.databinding.AuthorizationDialogBinding
import com.xdd.openweather.viewmodel.AuthorizationViewModel

class AuthorizationDialog : DialogFragment() {
    companion object {
        val TAG = AuthorizationDialog::class.java.simpleName

        fun newInstance() = AuthorizationDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val localContext = context ?: return super.onCreateDialog(savedInstanceState)

        val authViewModel = ViewModelProviders.of(this).get(AuthorizationViewModel::class.java)

        val binding = AuthorizationDialogBinding.inflate(
            LayoutInflater.from(localContext),
            null,
            false
        )!!.apply {
            lifecycleOwner = this@AuthorizationDialog
            viewModel = authViewModel
        }

        return AlertDialog.Builder(localContext)
            .setTitle("氣象開放資料平台會員授權碼")
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                authViewModel.setAuthorization()
            }
            .create()
    }
}