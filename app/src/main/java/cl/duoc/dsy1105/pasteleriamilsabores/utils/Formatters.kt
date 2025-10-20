package cl.duoc.dsy1105.pasteleriamilsabores.utils

import java.text.NumberFormat
import java.util.Locale

fun formatPriceChilean(price: Number): String {
    val locale = Locale.Builder().setLanguage("es").setRegion("CL").build()
    val format = NumberFormat.getCurrencyInstance(locale)
    format.maximumFractionDigits = 0
    return format.format(price)
}