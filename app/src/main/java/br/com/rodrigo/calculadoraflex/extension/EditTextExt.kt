package br.com.rodrigo.calculadoraflex.extension

import android.widget.EditText

fun EditText.toDouble() = this.text.toString().toDouble()