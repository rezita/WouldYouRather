package com.example.wouldyourather

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.wouldyourather.databinding.DialogNewEditQuestionBinding

class DialogEditQuestion: DialogSetter() {
    private lateinit var binding: DialogNewEditQuestionBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            binding = DialogNewEditQuestionBinding.inflate(layoutInflater)

            setLayoutThings()

            builder.setView(binding.root)
                .setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener{ dialog, id ->
                        editQuestion()
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener{ dialog, id ->
                        dialog.cancel()
                    })
            builder.setMessage(R.string.edit_question_message)

            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setLayoutThings(){
        binding.textOption1.setText(
            (activity as MainActivity?)?.getCurrentQuestion()?.option1 ?: "")

        binding.textOption2.setText(
            (activity as MainActivity?)?.getCurrentQuestion()?.option2 ?: "")

        setCounterText(binding.textOption1, binding.textCounter1)
        setCounterText(binding.textOption2, binding.textCounter2)

        setTextChangeListener(binding.textOption1, binding.textCounter1)
        setTextChangeListener(binding.textOption2, binding.textCounter2)
    }

    private fun editQuestion() {
        val option1 = binding.textOption1.text.toString()
        val option2 = binding.textOption2.text.toString()
        (activity as MainActivity?)?.editQuestion(option1, option2)
        dismiss()
    }
}