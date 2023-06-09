package com.mad43.stylista.util

import android.app.AlertDialog
import android.content.Context

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
}