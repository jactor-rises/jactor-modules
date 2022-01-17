package com.github.jactor.web.i18n

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

@Component
class MyMessages @Autowired constructor(private val messageSource: MessageSource) {
    fun fetchMessage(code: String?): String {
        return messageSource.getMessage(code!!, arrayOf(), LocaleContextHolder.getLocale())
    }
}