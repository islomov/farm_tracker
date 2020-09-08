package com.example.farmerapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.farmerapp.R
import kotlinx.android.synthetic.main.fragment_dialog_watering.*

class WateringDialogFragment(val level:String,val onSave:(level:String)->Unit) :DialogFragment(){


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_watering,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        save.setOnClickListener {
            val newLevel = edit.text.toString()
            if(newLevel.isNotEmpty()){
                onSave(newLevel)
                dismiss()
            }
        }
        cancel.setOnClickListener {
            dismiss()
        }
        edit.setText("$level",TextView.BufferType.EDITABLE)

    }
}