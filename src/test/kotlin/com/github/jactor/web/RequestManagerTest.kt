package com.github.jactor.web

import javax.servlet.http.HttpServletRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
@DisplayName("RequestManager")
internal class RequestManagerTest {

    @MockBean
    private lateinit var httpServletRequestMock: HttpServletRequest

    @Value("\${server.servlet.context-path}")
    private lateinit var contextPath: String

    @Test
    fun `should fetch currentUrl and attach it to the model`() {
        whenever(httpServletRequestMock.requestURI).thenReturn("$contextPath/user")
        whenever(httpServletRequestMock.queryString).thenReturn("choose=jactor")

        assertThat(RequestManager(contextPath, httpServletRequestMock).fetchCurrentUrl()).isEqualTo("/user?choose=jactor")
    }

    @Test
    fun `should not add query string to currentUrl if query string is blank`() {
        whenever(httpServletRequestMock.requestURI).thenReturn("$contextPath/user")
        whenever(httpServletRequestMock.queryString).thenReturn("")

        assertThat(RequestManager(contextPath, httpServletRequestMock).fetchCurrentUrl()).isEqualTo("/user")
    }

    @Test
    @DisplayName("should not add parameter called lang")
    fun shouldNotAddLangParameter() {
        whenever(httpServletRequestMock.requestURI).thenReturn("$contextPath/home")
        whenever(httpServletRequestMock.queryString).thenReturn("lang=en")

        val languageParam = RequestManager(contextPath, httpServletRequestMock).fetchCurrentUrl()

        whenever(httpServletRequestMock.requestURI).thenReturn("$contextPath/user")
        whenever(httpServletRequestMock.queryString).thenReturn("lang=no&choose=tip")

        val langAndOtherParam = RequestManager(contextPath, httpServletRequestMock).fetchCurrentUrl()

        assertAll(
            { assertThat(languageParam).`as`("only language param").isEqualTo("/home") },
            { assertThat(langAndOtherParam).`as`("one language and one user params").isEqualTo("/user?choose=tip") }
        )
    }

    @Test
    @DisplayName("should not add context-path to current url")
    fun shouldNotAddContextPath() {
        whenever(httpServletRequestMock.requestURI).thenReturn("$contextPath/home")

        assertThat(RequestManager(contextPath, httpServletRequestMock).fetchCurrentUrl()).isEqualTo("/home")
    }

    @Test
    @DisplayName("should not add centext-path to the view name")
    fun shouldNotAddViewNameToTheModel() {
        whenever(httpServletRequestMock.requestURI).thenReturn("$contextPath/someView")

        assertThat(RequestManager(contextPath, httpServletRequestMock).fetchChosenView()).isEqualTo("someView")
    }
}
