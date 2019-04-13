package com.github.jactor.persistence

data class SpringBeanNames(
        private val beanNames : MutableList<String> = ArrayList(),
        private val tenNames : MutableList<String> = ArrayList()
) {

    fun add(name: String) {
        if (name.contains(".")) {
            val index = name.lastIndexOf('.')
            tenNames.add(name.substring(index + 1))
        } else {
            tenNames.add(name)
        }

        if (tenNames.size == 10) {
            beanNames.add(tenNames.joinToString(", "))
            tenNames.clear()
        }
    }

    private fun mergeBeanNamesWithFiveNames(): List<String> {
        beanNames.add(tenNames.joinToString(", "))
        tenNames.clear()

        return beanNames
    }

    fun listBeanNames(): List<String> {
        return mergeBeanNamesWithFiveNames()
    }

}