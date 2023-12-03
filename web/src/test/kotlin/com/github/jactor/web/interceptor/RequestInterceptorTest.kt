package com.github.jactor.web.interceptor

import java.util.Locale
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.web.servlet.ModelAndView
import com.github.jactor.web.ENGLISH
import com.github.jactor.web.Language
import com.github.jactor.web.NORSK
import com.github.jactor.web.Request
import com.github.jactor.web.THAI
import com.github.jactor.web.interceptor.RequestInterceptor.Companion.CHOSEN_LANGUAGE
import com.github.jactor.web.interceptor.RequestInterceptor.Companion.CURRENT_REQUEST
import com.ninjasquad.springmockk.MockkBean
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import io.mockk.every
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@SpringBootTest
class RequestInterceptorTest {

    @Autowired
    private lateinit var requestInterceptorToTest: RequestInterceptor

    @MockkBean
    private lateinit var httpServletRequestMock: HttpServletRequest

    @MockkBean
    private lateinit var httpServletResponseMock: HttpServletResponse

    private val handler: Any = Object()

    @BeforeEach
    fun `mock request`() {
        every { httpServletRequestMock.requestURI } returns "/page"
        every { httpServletRequestMock.queryString } returns "some=param"
    }

    @Test
    fun `should add current url without language to the model`() {
        val modelAndView = ModelAndView()

        every { httpServletRequestMock.requestURI } returns "/somewhere"
        every { httpServletRequestMock.queryString } returns "out=there&lang=something&another=param"

        requestInterceptorToTest.postHandle(httpServletRequestMock, httpServletResponseMock, handler, modelAndView)

        val model = modelAndView.model
        val currentRequest = model[CURRENT_REQUEST] ?: throw IllegalStateException("No current request!")

        assertAll {
            assertThat(currentRequest).isInstanceOf(Request::class.java)
            assertThat((currentRequest as Request).currentUrl).isEqualTo("/somewhere?out=there&another=param")
        }
    }

    @Test
    fun `should add Norwegian language to model`() {
        LocaleContextHolder.setLocale(Locale.of("no"))
        val modelAndView = ModelAndView()

        requestInterceptorToTest.postHandle(httpServletRequestMock, httpServletResponseMock, handler, modelAndView)

        val language = modelAndView.model.getOrDefault(
            CHOSEN_LANGUAGE, Language(Locale.of("svada"), "there")
        ) as Language

        assertAll {
            assertThat(language.name).isEqualTo(NORSK)
            assertThat(language.locale).isEqualTo(Locale.of("no"))
        }
    }

    @Test
    fun `should add English language to model`() {
        LocaleContextHolder.setLocale(Locale.of("en"))
        val modelAndView = ModelAndView()

        requestInterceptorToTest.postHandle(httpServletRequestMock, httpServletResponseMock, handler, modelAndView)

        val language = modelAndView.model.getOrDefault(
            CHOSEN_LANGUAGE, Language(Locale.of("svada"), "there")
        ) as Language

        assertAll {
            assertThat(language.name).isEqualTo(ENGLISH)
            assertThat(language.locale).isEqualTo(Locale.of("en"))
        }
    }

    @Test
    fun `should add Thai language to model`() {
        LocaleContextHolder.setLocale(Locale.of("th"))
        val modelAndView = ModelAndView()

        requestInterceptorToTest.postHandle(httpServletRequestMock, httpServletResponseMock, handler, modelAndView)

        val language = modelAndView.model.getOrDefault(
            CHOSEN_LANGUAGE, Language(Locale.of("svada"), "there")
        ) as Language

        assertAll {
            assertThat(language.name).isEqualTo(THAI)
            assertThat(language.locale).isEqualTo(Locale.of("th"))
        }
    }

    @Test
    fun `should add Thai language to model from language parameters`() {
        LocaleContextHolder.setLocale(Locale.of("no"))
        val modelAndView = ModelAndView()
        every { httpServletRequestMock.queryString } returns "select=something&lang=th"

        requestInterceptorToTest.postHandle(httpServletRequestMock, httpServletResponseMock, handler, modelAndView)

        val language = modelAndView.model.getOrDefault(
            CHOSEN_LANGUAGE, Language(Locale.of("svada"), "there")
        ) as Language

        assertAll {
            assertThat(language.name).isEqualTo(THAI)
            assertThat(language.locale).isEqualTo(Locale.of("th"))
        }
    }

    @Test
    fun `should add English language to model from language parameters`() {
        LocaleContextHolder.setLocale(Locale.of("no"))
        val modelAndView = ModelAndView()
        every { httpServletRequestMock.queryString } returns "select=something&lang=en"

        requestInterceptorToTest.postHandle(httpServletRequestMock, httpServletResponseMock, handler, modelAndView)

        val language = modelAndView.model.getOrDefault(
            CHOSEN_LANGUAGE, Language(Locale.of("svada"), "there")
        ) as Language

        assertAll {
            assertThat(language.name).isEqualTo(ENGLISH)
            assertThat(language.locale).isEqualTo(Locale.of("en"))
        }
    }

    @Test
    fun `should add Norwegian language to model from language parameters`() {
        LocaleContextHolder.setLocale(Locale.of("en"))
        val modelAndView = ModelAndView()
        every { httpServletRequestMock.queryString } returns "select=something&lang=no"

        requestInterceptorToTest.postHandle(httpServletRequestMock, httpServletResponseMock, handler, modelAndView)

        val language = modelAndView.model.getOrDefault(
            CHOSEN_LANGUAGE, Language(Locale.of("svada"), "there")
        ) as Language

        assertAll {
            assertThat(language.name).isEqualTo(NORSK)
            assertThat(language.locale).isEqualTo(Locale.of("no"))
        }
    }

    @Test
    fun `should add English language to model when unknown language parameter`() {
        LocaleContextHolder.setLocale(Locale.of("th"))
        val modelAndView = ModelAndView()
        every { httpServletRequestMock.queryString } returns "select=something&lang=fi"

        requestInterceptorToTest.postHandle(httpServletRequestMock, httpServletResponseMock, handler, modelAndView)

        val language = modelAndView.model.getOrDefault(
            CHOSEN_LANGUAGE, Language(Locale.of("svada"), "there")
        ) as Language

        assertAll {
            assertThat(language.name).isEqualTo(ENGLISH)
            assertThat(language.locale).isEqualTo(Locale.of("en"))
        }
    }

    @Test
    fun `should add chosen view to the model`() {
        val modelAndView = ModelAndView()
        every { httpServletRequestMock.requestURI } returns "/user"

        requestInterceptorToTest.postHandle(httpServletRequestMock, httpServletResponseMock, handler, modelAndView)

        val model = modelAndView.model
        val currentRequest = model[CURRENT_REQUEST] ?: throw IllegalStateException("No current request!")

        assertAll {
            assertThat(currentRequest).isInstanceOf(Request::class.java)
            assertThat((currentRequest as Request).chosenView).isEqualTo("user")
        }
    }
}
