package br.com.rodrigo.calculadoraflex.extension

fun Double.format(digits: Int) =
    java.lang.String.format("%.${digits}f", this)