package com.github.jactor.shared

import com.github.jactor.shared.SpringBeanNames.Companion.NO_OF_BEANS_ON_LINE

@JvmRecord
data class SpringBeanNames(
    private val beanNames: BeanNames = BeanNames(),
) {
    companion object {
        const val NO_OF_BEANS_ON_LINE = 4
    }

    val names: List<String> get() = beanNames.names

    fun add(name: String) {
        beanNames.add(name.substringAfterLast(delimiter = '.'))
    }
}

@JvmRecord
data class BeanNames(
    private val beanNames: MutableList<String> = mutableListOf(),
    private val tenNames: MutableList<String> = mutableListOf(),
) {
    val names: List<String>
        get() = joinLists()

    private fun joinLists(): List<String> = tenNames.isNotEmpty().whenTrue {
        beanNames + tenNames.joinToString(separator = ", ")
    } ?: beanNames

    fun add(name: String) {
        tenNames.add(name)

        if (tenNames.size == NO_OF_BEANS_ON_LINE) {
            beanNames.add(tenNames.joinToString(separator = ", "))
            tenNames.clear()
        }
    }
}
