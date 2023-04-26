package com.example.wouldyourather

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DialogConfirm():DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.delete_message)
            builder.setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.cancel() })
            builder.setPositiveButton(R.string.delete,
                DialogInterface.OnClickListener { dialogInterface, i -> delete() })
            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }

    private fun delete(){
        (activity as MainActivity?)?.deleteQuestion()
        dismiss()
    }
}