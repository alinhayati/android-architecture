package com.digigene.android.moviefinder.ui

import android.content.Context
import android.databinding.BindingAdapter
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.digigene.android.moviefinder.R

object MainViewBindingAdapter {
    @BindingAdapter("clearContentsOnFocus")
    @JvmStatic
    fun EditText.clearContentsOnFocus(boolean: Boolean) {
        if (!boolean) return
        val existingTag = getTag(R.id.previous_value) as CharSequence? ?: ""
        setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                (view as TextView).text = ""
            } else {
                if (text.toString() == "") (view as TextView).text = existingTag
            }
        }
    }

    @BindingAdapter("hideKeyboardWhenNotInUse")
    @JvmStatic
    fun EditText.hideKeyboardWhenNotInUse(boolean: Boolean) {
        if (!boolean) return
        setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearFocus()
                val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
            }
            false
        }
    }
}