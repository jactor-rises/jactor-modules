package com.github.jactor.web.interceptor

import com.github.jactor.web.Language
import com.github.jactor.web.Request
import com.github.jactor.web.RequestManager
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.util.Locale

@Component
@PropertySource("classpath:application.properties")
class RequestInterceptor(@param:Value("\${server.servlet.context-path}") private val contextPath: String) : HandlerInterceptor {

    companion object {
        const val CHOSEN_LANGUAGE = "chosenLanguage"
        const val CURRENT_REQUEST = "currentRequest"

        @JvmStatic
        private val LANGUAGE_DEFAULT_IS_ENGLISH = Language(Locale("en"), "English")

        @JvmStatic
        private val SUPPORTED_LANGUAGES = listOf(
            LANGUAGE_DEFAULT_IS_ENGLISH,
            Language(Locale("no"), "Norsk"),
            Language(Locale("th"), "ไทย")
        )
    }

    override fun postHandle(
        servletRequest: HttpServletRequest,
        servletResponse: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        if (modelAndView != null) {
            val requestManager = RequestManager(contextPath, servletRequest)
            val request = Request(requestManager.fetchCurrentUrl(), requestManager.fetchChosenView())
            val chosenLanguage = fetchChosenLangugae(requestManager)

            modelAndView.modelMap.addAttribute(CHOSEN_LANGUAGE, chosenLanguage)
            modelAndView.modelMap.addAttribute(CURRENT_REQUEST, request)
        }
    }

    private fun fetchChosenLangugae(requestManager: RequestManager): Language {
        return if (requestManager.noLanguageParameters()) {
            requestManager.fetchFrom(SUPPORTED_LANGUAGES, LocaleContextHolder.getLocale(), LANGUAGE_DEFAULT_IS_ENGLISH)
        } else requestManager.fetchFromParameters(SUPPORTED_LANGUAGES, LANGUAGE_DEFAULT_IS_ENGLISH)
    }
}