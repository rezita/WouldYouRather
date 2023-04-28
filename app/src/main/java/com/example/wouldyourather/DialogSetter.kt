package com.example.wouldyourather

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment

abstract class DialogSetter(): DialogFragment() {
    private val maxOptionLength = 100


    fun setTextChangeListener(source: EditText, target: TextView) {
        source.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Log.i("info", "before")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setCounterText(source, target)
            }

            override fun afterTextChanged(p0: Editable?) {
                //Log.i("info", "after")
            }
        })
    }

    private fun getMaxLength(source: EditText): Int? {
        return source?.filters?.filterIsInstance<InputFilter.LengthFilter>()?.firstOrNull()?.max
    }

    fun setCounterText(source: EditText, target: TextView){
        var maxLength = getMaxLength(source)
        if (maxLength == null) {
            maxLength = maxOptionLength
            source.filters = arrayOf(InputFilter.LengthFilter(maxOptionLength))
        }
        val text = "$maxLength / ${maxLength - source.length()}"
        target.text = "$maxLength / ${maxLength - source.length()}"
    }

}