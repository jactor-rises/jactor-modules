package com.github.jactor.web

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import com.ninjasquad.springmockk.MockkBean
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.every
import jakarta.servlet.http.HttpServletRequest

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

        assertAll {
            assertThat(languageParam).isEqualTo("/home")
            assertThat(langAndOtherParam).isEqualTo("/user?choose=tip")
        }
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
