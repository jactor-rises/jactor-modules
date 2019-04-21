package com.github.jactor.web

import java.util.*

data class Technology(
        val message: String,
        val tech: String,
        val url: String
)

class SpringBeanNames(
        private val beanNames: MutableList<String> = ArrayList(),
        private val tenNames: MutableList<String> = ArrayList()
) {
    fun add(name: String) {
        if (name.contains(".")) {
            tenNames.add(name.substring(name.lastIndexOf('.') + 1))
        } else {
            tenNames.add(name)
        }

        if (tenNames.size == 10) {
            beanNames.add(tenNames.joinToString(", "))
            tenNames.clear()
        }
    }

    private fun mergeBeanNamesWithTenNames(): List<String> {
        beanNames.add(tenNames.joinToString( ", "))
        tenNames.clear()
        return beanNames
    }

    fun listBeanNames(): List<String> {
        return mergeBeanNamesWithTenNames()
    }
}

data class Request(val currentUrl: String, val chosenView: String)