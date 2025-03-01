package com.github.jactor.web

@JvmRecord
data class Technology(
    val message: String,
    val tech: String,
    val url: String
)

@JvmRecord
data class Request(val currentUrl: String, val chosenView: String)
