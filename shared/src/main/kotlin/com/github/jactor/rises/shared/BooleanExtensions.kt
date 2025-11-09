package com.github.jactor.rises.shared

fun <T> Boolean.whenTrue(callback: () -> T?): T? = takeIf { this }?.let { callback() }
fun <T> Boolean.whenFalse(callback: () -> T?): T? = takeIf { !this }?.let { callback() }
