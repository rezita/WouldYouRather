package com.example.wouldyourather

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.wouldyourather.databinding.DialogNewEditQuestionBinding

class DialogNewQuestion : DialogSetter()  {
    private lateinit var binding: DialogNewEditQuestionBinding
    //val maxOptionLength = 80


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            binding = DialogNewEditQuestionBinding.inflate(layoutInflater)

            setLayoutThings()

            builder.setView(binding.root)
                .setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener{dialog, id ->
                        createNewQuestion()
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener{dialog, id ->
                        dialog.cancel()
                    })
            builder.setMessage(R.string.add_new_message)

            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setLayoutThings(){
        setCounterText(binding.textOption1, binding.textCounter1)
        setCounterText(binding.textOption2, binding.textCounter2)

        setTextChangeListener(binding.textOption1, binding.textCounter1)
        setTextChangeListener(binding.textOption2, binding.textCounter2)
    }
/*
    private fun setTextChangeListener(source: EditText, target: TextView) {
        source.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.i("info", "before")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setCounterText(source, target)
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.i("info", "after")
            }
        })
    }

    private fun getMaxLength(source: EditText): Int? {
        return source?.filters?.filterIsInstance<InputFilter.LengthFilter>()?.firstOrNull()?.max
    }

    private fun setCounterText(source: EditText, target: TextView){
        val maxLength = getMaxLength(source) ?: maxOptionLength
        val text = "$maxLength / ${maxLength - source.length()}"
        target.text = "$maxLength / ${maxLength - source.length()}"
    }
*/
    private fun createNewQuestion() {
        val option1 = binding.textOption1.text.toString()
        val option2 = binding.textOption2.text.toString()
        val newQuestion = Question(option1, option2)
        (activity as MainActivity?)?.addNewQuestion(newQuestion)
        dismiss()
    }
}