package com.fahruaz.farmernusantara.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.fahruaz.farmernusantara.databinding.ActivityVerificationBinding
import com.fahruaz.farmernusantara.ui.customviews.GenericKeyEvent
import com.fahruaz.farmernusantara.ui.customviews.GenericTextWatcher

class VerificationActivity : AppCompatActivity() {

    private var binding: ActivityVerificationBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //GenericTextWatcher here works only for moving to next EditText when a number is entered
        //first parameter is the current EditText and second parameter is next EditText
        binding?.otpET1?.addTextChangedListener(GenericTextWatcher(binding?.otpET1!!, binding?.otpET2!!))
        binding?.otpET2?.addTextChangedListener(GenericTextWatcher(binding?.otpET2!!, binding?.otpET3!!))
        binding?.otpET3?.addTextChangedListener(GenericTextWatcher(binding?.otpET3!!, binding?.otpET4!!))
        binding?.otpET4?.addTextChangedListener(GenericTextWatcher(binding?.otpET4!!, binding?.otpET5!!))
        binding?.otpET5?.addTextChangedListener(GenericTextWatcher(binding?.otpET5!!, null))

        //GenericKeyEvent here works for deleting the element and to switch back to previous EditText
        //first parameter is the current EditText and second parameter is previous EditText
        binding?.otpET1?.setOnKeyListener(GenericKeyEvent(binding?.otpET1!!, null))
        binding?.otpET2?.setOnKeyListener(GenericKeyEvent(binding?.otpET2!!, binding?.otpET1!!))
        binding?.otpET3?.setOnKeyListener(GenericKeyEvent(binding?.otpET3!!, binding?.otpET2!!))
        binding?.otpET4?.setOnKeyListener(GenericKeyEvent(binding?.otpET4!!, binding?.otpET3!!))
        binding?.otpET5?.setOnKeyListener(GenericKeyEvent(binding?.otpET5!!, binding?.otpET4!!))

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}