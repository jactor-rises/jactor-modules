package com.github.jactor.web.interceptor

import com.github.jactor.web.ENGLISH
import com.github.jactor.web.Language
import com.github.jactor.web.NORSK
import com.github.jactor.web.Request
import com.github.jactor.web.THAI
import com.github.jactor.web.interceptor.RequestInterceptor.Companion.CHOSEN_LANGUAGE
import com.github.jactor.web.interceptor.RequestInterceptor.Companion.CURRENT_REQUEST
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.web.servlet.ModelAndView
import java.util.Locale

@SpringBootTest
@DisplayName("A RequestInterceptor")
class RequestInterceptorTest {

    @Autowired
    private lateinit var requestInterceptorToTest: RequestInterceptor

    @MockBean
    private lateinit var httpServletRequestMock: HttpServletRequest

    @MockBean
    private lateinit var httpServletResponseMock: HttpServletResponse

    private val handler: Any = Object()

    @BeforeEach
    fun `mock request`() {
        whenever(httpServletRequestMock.requestURI).thenReturn("/page")
        whenever(httpServletRequestMock.queryString).thenReturn("some=param")
    }

    @Test
    fun `should add current url without language to the model`() {
        val modelAndView = ModelAndView()

        whenever(httpServletRequestMock.requestURI).thenReturn("/somewhere")
        whenever(httpServletRequestMock.queryString).thenReturn("out=there&lang=something&another=param")

        requestInterceptorToTest.postHandle(httpServletRequestMock, httpServletResponseMock, handler, modelAndView)

        val model = modelAndView.model
        val currentRequest = model[CURRENT_REQUEST]

        assertAll(
            { assertThat(currentRequest).isInstanceOf(Request::class.java) },
            { assertThat(currentRequest as Request).extracting(Request::currentUrl).isEqualTo("/somewhere?out=there&another=param") }
        )
    }

    @Test
    fun `should add Norwegian language to model`() {
        LocaleContextHolder.setLocale(Locale("no"))
        val modelAndView = ModelAndView()

        requestInterceptorToTest.postHandle(httpServletRequestMock, httpServletResponseMock, handler, modelAndView)

        val language = modelAndView.model.getOrDefault(CHOSEN_LANGUAGE, Language(Locale("svada"), "there")) as Language

        assertAll(
            { assertThat(language.name).`as`("name").isEqualTo(NORSK) },
            { assertThat(language.locale).isEqualTo(Locale("no")) }
        )
    }

    @Test
    @DisplayName("")
    fun `should add English language to model`() {
        LocaleContextHolder.setLocale(Locale("en"))
        val modelAndView = ModelAndView()

        requestInterceptorToTest.postHandle(httpServletRequestMock, httpServletResponseMock, handler, modelAndView)

        val language = modelAndView.model.getOrDefault(CHOSEN_LANGUAGE, Language(Locale("svada"), "there")) as Language

        assertAll(
            { assertThat(language.name).`as`("name").isEqualTo(ENGLISH) },
            { assertThat(language.locale).isEqualTo(Locale("en")) }
        )
    }

    @Test
    fun `should add Thai language to model`() {
        LocaleContextHolder.setLocale(Locale("th"))
        val modelAndView = ModelAndView()

        requestInterceptorToTest.postHandle(httpServletRequestMock, httpServletResponseMock, handler, modelAndView)

        val language = modelAndView.model.getOrDefault(CHOSEN_LANGUAGE, Language(Locale("svada"), "there")) as Language

        assertAll(
            { assertThat(language.name).`as`("name").isEqualTo(THAI) },
            { assertThat(language.locale).isEqualTo(Locale("th")) }
        )
    }

    @Test
    fun `should add Thai language to model from language parameters`() {
        LocaleContextHolder.setLocale(Locale("no"))
        val modelAndView = ModelAndView()
        whenever(httpServletRequestMock.queryString).thenReturn("select=something&lang=th")

        requestInterceptorToTest.postHandle(httpServletRequestMock, httpServletResponseMock, handler, modelAndView)

        val language = modelAndView.model.getOrDefault(CHOSEN_LANGUAGE, Language(Locale("svada"), "there")) as Language

        assertAll(
            { assertThat(language.name).`as`("name").isEqualTo(THAI) },
            { assertThat(language.locale).isEqualTo(Locale("th")) }
        )
    }

    @Test
    fun `should add English language to model from language parameters`() {
        LocaleContextHolder.setLocale(Locale("no"))
        val modelAndView = ModelAndView()
        whenever(httpServletRequestMock.queryString).thenReturn("select=something&lang=en")

        requestInterceptorToTest.postHandle(httpServletRequestMock, httpServletResponseMock, handler, modelAndView)

        val language = modelAndView.model.getOrDefault(CHOSEN_LANGUAGE, Language(Locale("svada"), "there")) as Language

        assertAll(
            { assertThat(language.name).`as`("name").isEqualTo(ENGLISH) },
            { assertThat(language.locale).isEqualTo(Locale("en")) }
        )
    }

    @Test
    fun `should add Norwegian language to model from language parameters`() {
        LocaleContextHolder.setLocale(Locale("en"))
        val modelAndView = ModelAndView()
        whenever(httpServletRequestMock.getQueryString()).thenReturn("select=something&lang=no")

        requestInterceptorToTest.postHandle(httpServletRequestMock, httpServletResponseMock, handler, modelAndView)

        val language = modelAndView.model.getOrDefault(CHOSEN_LANGUAGE, Language(Locale("svada"), "there")) as Language

        assertAll(
            { assertThat(language.name).`as`("name").isEqualTo(NORSK) },
            { assertThat(language.locale).isEqualTo(Locale("no")) }
        )
    }

    @Test
    fun `should add English language to model when unknown language parameter`() {
        LocaleContextHolder.setLocale(Locale("th"))
        val modelAndView = ModelAndView()
        whenever(httpServletRequestMock.getQueryString()).thenReturn("select=something&lang=fi")

        requestInterceptorToTest.postHandle(httpServletRequestMock, httpServletResponseMock, handler, modelAndView)

        val language = modelAndView.model.getOrDefault(CHOSEN_LANGUAGE, Language(Locale("svada"), "there")) as Language

        assertAll(
            { assertThat(language.name).`as`("name").isEqualTo(ENGLISH) },
            { assertThat(language.locale).isEqualTo(Locale("en")) }
        )
    }

    @Test
    fun `should add chosen view to the model`() {
        val modelAndView = ModelAndView()
        whenever(httpServletRequestMock.requestURI).thenReturn("/user")

        requestInterceptorToTest.postHandle(httpServletRequestMock, httpServletResponseMock, handler, modelAndView)

        val model = modelAndView.model
        val currentRequest = model[CURRENT_REQUEST]

        assertAll(
            { assertThat(currentRequest).isInstanceOf(Request::class.java) },
            { assertThat(currentRequest as Request).extracting(Request::chosenView).isEqualTo("user") }
        )
    }
}
