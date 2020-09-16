package com.chogoon.cglib

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.chogoon.cglib.databinding.DialogBaseBinding

class BaseDialog(context: Context) : Dialog(context, R.style.CgDialog), View.OnClickListener {

    val message = MutableLiveData<String>()
    val ok = MutableLiveData<String>()
    val no = MutableLiveData<String>()
    val yes = MutableLiveData<String>()

    val one = ObservableBoolean(false)
    private var binding: DialogBaseBinding? = null
    private var onClickListener: View.OnClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_base, null, false)
        binding?.let {
            setContentView(it.root)
            it.view = this@BaseDialog
        }
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        setCanceledOnTouchOutside(false)
    }

    override fun onClick(v: View) {
        dismiss()
        onClickListener?.onClick(v)
    }

    private fun setMessage(text: String, isOne: Boolean) {
        show()
        one.set(isOne)
        message.value = text
    }

    fun setText(message: String, confirm: String) {
        setMessage(message, true)
        if (confirm.isNotEmpty()) {
            ok.value = confirm
        } else {
            ok.value = context.getString(R.string.confirm)
        }
    }

    fun setText(message: String) {
        setText(message, "")
    }

    fun setText(message: Int, confirm: Int) {
        setText(if (message > 0) context.getString(message) else "",
                if (confirm > 0) context.getString(confirm) else ""
        )
    }

    fun setText(message: Int) {
        setText(if (message > 0) context.getString(message) else "", "")
    }

    fun setText(message: String, left_text: String, right_text: String) {
        setMessage(message, false)
        if (left_text.isNotEmpty()) {
            no.value = left_text
        } else {
            no.value = context.getString(R.string.no)
        }

        if (right_text.isNotEmpty()) {
            yes.value = right_text
        } else {
            yes.value = context.getString(R.string.yes)
        }
    }

    fun setText(message: Int, left_text: Int, right_text: Int) {
        setText(if (message > 0) context.getString(message) else "",
                if (left_text > 0) context.getString(left_text) else "",
                if (right_text > 0) context.getString(right_text) else ""
        )
    }

    public fun setOnClickListener(onClickListener: View.OnClickListener?) {
        this.onClickListener = onClickListener
    }
}