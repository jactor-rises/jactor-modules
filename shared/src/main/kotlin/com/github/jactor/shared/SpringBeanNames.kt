package com.github.jactor.shared

@JvmRecord
data class SpringBeanNames(
    private val beanNames: BeanNames = BeanNames(),
) {
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

        if (tenNames.size == 10) {
            beanNames.add(tenNames.joinToString(separator = ", "))
            tenNames.clear()
        }
    }
}
