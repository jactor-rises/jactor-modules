package com.github.jactor.shared

private typealias TenNames = String

@JvmRecord
data class SpringBeanNames(
    private val beanNames: MutableList<String> = ArrayList(),
    private val tenNames: MutableList<TenNames> = ArrayList(),
) {
    fun add(name: String) {
        name.contains(".").whenTrue {
            name.lastIndexOf('.').let { tenNames.add(name.substring(it + 1)) }
        } ?: tenNames.add(name)

        if (tenNames.size == 10) {
            beanNames.add(tenNames.joinToString(", "))
            tenNames.clear()
        }
    }

    fun listBeanNames(): List<String> {
        tenNames.isNotEmpty().whenTrue {
            beanNames.add(tenNames.joinToString(", "))
            tenNames.clear()
        }

        return beanNames
    }
}
