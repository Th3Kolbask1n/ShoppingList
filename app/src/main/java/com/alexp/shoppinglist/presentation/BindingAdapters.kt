package com.alexp.shoppinglist.presentation

import androidx.databinding.BindingAdapter
import com.alexp.shoppinglist.R
import com.google.android.material.textfield.TextInputLayout
import java.lang.Error

@BindingAdapter("errorInputCount")
fun bindErrorInputCount(textInputLayout: TextInputLayout, isError: Boolean)
{
    val message = if(isError)
    {
        textInputLayout.context.getString(R.string.error_input_count)
    }
    else
    {
        null
    }
    textInputLayout.error = message
}


@BindingAdapter("errorInputName")
fun bindErrorInputName(textInputLayout: TextInputLayout, isError: Boolean)
{
    val message = if(isError)
    {
        textInputLayout.context.getString(R.string.error_input_name)
    }
    else
    {
        null
    }
    textInputLayout.error = message
}