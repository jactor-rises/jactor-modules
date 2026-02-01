package com.github.jactor.rises.shared

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

        if (tenNames.size == SpringBeanNames.NO_OF_BEANS_ON_LINE) {
            beanNames.add(tenNames.joinToString(separator = ", "))
            tenNames.clear()
        }
    }
}
