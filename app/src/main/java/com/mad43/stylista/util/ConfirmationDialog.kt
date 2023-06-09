package com.mad43.stylista.util

import android.app.AlertDialog
import android.content.Context
import androidx.fragment.app.Fragment

fun Fragment.showConfirmationDialog(message: String,operation : ()-> Unit) {
    val builder = AlertDialog.Builder(requireContext())
    builder.setMessage(message)
        .setPositiveButton(android.R.string.ok) { _, _ ->
            operation()
        }.setNegativeButton(android.R.string.cancel){ dialog, _ ->
            dialog.dismiss()
        }
    val dialog = builder.create()
    dialog.show()
}

fun Fragment.showDialog(message: String,positiveButtonText :String,negativeButtonText :String,positiveOperation : ()-> Unit) {
    val builder = AlertDialog.Builder(requireContext())
    builder.setMessage(message)
        .setPositiveButton(positiveButtonText) { _, _ ->
            positiveOperation()
        }.setNegativeButton(negativeButtonText){ dialog, _ ->
            dialog.dismiss()
        }
    val dialog = builder.create()
    dialog.show()
}