package com.mad43.stylista.util

import android.app.AlertDialog
import android.content.Context
import androidx.navigation.Navigation
import com.mad43.stylista.R

class MyDialog {
     fun showAlertDialog(message: String,context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }

    fun showAlertDialog(message: String, context: Context, callback: (Boolean) -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                callback(true)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                callback(false)
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }



}