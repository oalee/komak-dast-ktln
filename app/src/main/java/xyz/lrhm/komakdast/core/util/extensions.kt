package xyz.lrhm.komakdast.core.util


fun String.numeralStringToPersianDigits(): String {
    val persianDigits = "۰۱۲۳۴۵۶۷۸۹"
    val result = CharArray(this.length)
    for (i in 0 until this.length) result[i] =
        if (Character.isDigit(this[i])) persianDigits[this[i] - '0'] else this[i]
    return String(result)
}