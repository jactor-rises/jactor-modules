package com.github.jactor.persistence.entity

object UniqueUsername {
    fun generate(name: String): String {
        return "$name@${java.lang.Long.toHexString(System.currentTimeMillis())}"
    }
}