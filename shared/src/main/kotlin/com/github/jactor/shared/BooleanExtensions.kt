package com.github.jactor.shared

fun <T> Boolean.whenTrue(callback: () -> T?): T? = takeIf { this }?.let { callback() }
fun <T> Boolean.whenFalse(callback: () -> T?): T? = takeIf { !this }?.let { callback() }
