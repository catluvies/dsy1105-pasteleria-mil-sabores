package cl.duoc.dsy1105.pasteleriamilsabores.utils

import java.text.NumberFormat
import java.util.Locale

object Formatters {

    val clPriceFormatter: NumberFormat = NumberFormat.getCurrencyInstance(
        Locale.Builder().setLanguage("es").setRegion("CL").build()
    )
}