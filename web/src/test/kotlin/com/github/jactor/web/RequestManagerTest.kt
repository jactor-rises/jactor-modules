package com.github.jactor.web

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.jactor.web.test.AbstractNoDirtySpringContextTest
import io.mockk.every
import org.junit.jupiter.api.Test

internal class RequestManagerTest : AbstractNoDirtySpringContextTest() {
    @Test
    fun `should fetch currentUrl and attach it to the model`() {
        every { httpServletRequestMockk.requestURI } returns "$contextPath/user"
        every { httpServletRequestMockk.queryString } returns "choose=jactor"

        assertThat(RequestManager(contextPath, httpServletRequestMockk).fetchCurrentUrl())
            .isEqualTo("/user?choose=jactor")
    }

    @Test
    fun `should not add query string to currentUrl if query string is blank`() {
        every { httpServletRequestMockk.requestURI } returns "$contextPath/user"
        every { httpServletRequestMockk.queryString } returns ""

        assertThat(RequestManager(contextPath, httpServletRequestMockk).fetchCurrentUrl()).isEqualTo("/user")
    }

    @Test
    fun `should not add parameter called lang`() {
        every { httpServletRequestMockk.requestURI } returns "$contextPath/home"
        every { httpServletRequestMockk.queryString } returns "lang=en"

        val languageParam = RequestManager(contextPath, httpServletRequestMockk).fetchCurrentUrl()

        every { httpServletRequestMockk.requestURI } returns "$contextPath/user"
        every { httpServletRequestMockk.queryString } returns "lang=no&choose=tip"

        val langAndOtherParam = RequestManager(contextPath, httpServletRequestMockk).fetchCurrentUrl()

        assertAll {
            assertThat(languageParam).isEqualTo("/home")
            assertThat(langAndOtherParam).isEqualTo("/user?choose=tip")
        }
    }

    @Test
    fun `should not add context-path to current url`() {
        every { httpServletRequestMockk.requestURI } returns "$contextPath/home"
        every { httpServletRequestMockk.queryString } returns null

        assertThat(RequestManager(contextPath, httpServletRequestMockk).fetchCurrentUrl()).isEqualTo("/home")
    }

    @Test
    fun `should not add centext-path to the view name`() {
        every { httpServletRequestMockk.requestURI } returns "$contextPath/someView"
        every { httpServletRequestMockk.queryString } returns null

        assertThat(RequestManager(contextPath, httpServletRequestMockk).fetchChosenView()).isEqualTo("someView")
    }
}
