package com.github.jactor.rises.web.test

import com.github.jactor.rises.web.JactorWebBeans
import com.ninjasquad.springmockk.MockkBean
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.PropertySource
import org.springframework.web.client.RestTemplate

@PropertySource("classpath:application.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
internal abstract class AbstractNoDirtySpringContextTest {
    @Autowired
    protected lateinit var jactorWebUriTemplateHandler: JactorWebBeans.JactorWebUriTemplateHandler

    @Autowired
    protected lateinit var testRestTemplate: TestRestTemplate

    @MockkBean
    protected lateinit var httpServletRequestMockk: HttpServletRequest

    @MockkBean
    protected lateinit var httpServletResponseMockk: HttpServletResponse

    @Value("\${server.servlet.context-path}")
    protected lateinit var contextPath: String

    protected val restTemplate: RestTemplate by lazy {
        RestTemplate().apply { uriTemplateHandler = jactorWebUriTemplateHandler.uriTemplateHandler }
    }
}
