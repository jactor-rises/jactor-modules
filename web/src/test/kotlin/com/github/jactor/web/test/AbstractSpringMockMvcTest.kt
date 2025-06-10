package com.github.jactor.web.test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.servlet.view.InternalResourceViewResolver
import com.github.jactor.web.client.UserClient
import com.github.jactor.web.i18n.MyMessages
import com.github.jactor.web.menu.MenuFacade
import com.ninjasquad.springmockk.MockkBean

@SpringBootTest
internal abstract class AbstractSpringMockMvcTest {
    protected abstract val initMockMvc: (InternalResourceViewResolver) -> MockMvc

    @Autowired
    protected lateinit var menuFacade: MenuFacade

    @Autowired
    protected lateinit var myMessages: MyMessages

    @MockkBean
    @Qualifier("userConsumer")
    protected lateinit var userClientMock: UserClient

    @Value("\${spring.mvc.view.prefix}")
    private lateinit var prefix: String

    @Value("\${spring.mvc.view.suffix}")
    private lateinit var suffix: String

    @Value("\${server.servlet.context-path}")
    protected lateinit var contextPath: String

    @Suppress("UsePropertyAccessSyntax")
    protected val mockMvc: MockMvc by lazy {
        val internalResourceViewResolver = InternalResourceViewResolver()

        internalResourceViewResolver.setPrefix(prefix)
        internalResourceViewResolver.setSuffix(suffix)

        initMockMvc.invoke(internalResourceViewResolver)
    }
}
