package com.github.jactor.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import javax.servlet.http.HttpServletRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class RequestManagerTest {

    @MockkBean
    private lateinit var httpServletRequestMock: HttpServletRequest

    @Value("\${server.servlet.context-path}")
    private lateinit var contextPath: String

    @Test
    fun `should fetch currentUrl and attach it to the model`() {
        every { httpServletRequestMock.requestURI } returns "$contextPath/user"
        every { httpServletRequestMock.queryString } returns "choose=jactor"

        assertThat(RequestManager(contextPath, httpServletRequestMock).fetchCurrentUrl())
            .isEqualTo("/user?choose=jactor")
    }

    @Test
    fun `should not add query string to currentUrl if query string is blank`() {
        every { httpServletRequestMock.requestURI } returns "$contextPath/user"
        every { httpServletRequestMock.queryString } returns ""

        assertThat(RequestManager(contextPath, httpServletRequestMock).fetchCurrentUrl()).isEqualTo("/user")
    }

    @Test
    fun `should not add parameter called lang`() {
        every { httpServletRequestMock.requestURI } returns "$contextPath/home"
        every { httpServletRequestMock.queryString } returns "lang=en"

        val languageParam = RequestManager(contextPath, httpServletRequestMock).fetchCurrentUrl()

        every { httpServletRequestMock.requestURI } returns "$contextPath/user"
        every { httpServletRequestMock.queryString } returns "lang=no&choose=tip"

        val langAndOtherParam = RequestManager(contextPath, httpServletRequestMock).fetchCurrentUrl()

        assertAll(
            { assertThat(languageParam).`as`("only language param").isEqualTo("/home") },
            { assertThat(langAndOtherParam).`as`("one language and one user params").isEqualTo("/user?choose=tip") }
        )
    }

    @Test
    fun `should not add context-path to current url`() {
        every { httpServletRequestMock.requestURI } returns "$contextPath/home"
        every { httpServletRequestMock.queryString } returns null

        assertThat(RequestManager(contextPath, httpServletRequestMock).fetchCurrentUrl()).isEqualTo("/home")
    }

    @Test
    fun `should not add centext-path to the view name`() {
        every { httpServletRequestMock.requestURI } returns "$contextPath/someView"
        every { httpServletRequestMock.queryString } returns null

        assertThat(RequestManager(contextPath, httpServletRequestMock).fetchChosenView()).isEqualTo("someView")
    }
}
