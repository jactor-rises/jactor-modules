package com.github.jactor.web

import java.util.*

const val ENGLISH = "English"
const val NORSK = "Norsk"
const val THAI = "ไทย"

data class Language(
        val locale: Locale,
        val name: String = ""
) {
    fun matches(locale: Locale): Boolean {
        return this.locale == locale
    }

    fun matches(language: String): Boolean {
        return if (language.startsWith("lang=")) locale.language == language.replace("lang=", "")
        else locale.language == language
    }

    fun isEnglish(): Boolean {
        return name == ENGLISH
    }

    fun isNorwegian(): Boolean {
        return name == NORSK
    }

    fun isThai(): Boolean {
        return name == THAI
    }

}
