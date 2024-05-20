package com.github.jactor.web

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import com.github.jactor.web.controller.AboutController
import com.github.jactor.web.controller.HomeController
import com.github.jactor.web.controller.UserController
import com.github.jactor.web.test.AbstractNoDirtySpringContextTest
import assertk.assertThat
import assertk.assertions.isNotNull

internal class JactorWebTest: AbstractNoDirtySpringContextTest() {

    @Autowired
    private val homeController: HomeController? = null

    @Autowired
    private val aboutController: AboutController? = null

    @Autowired
    private val userController: UserController? = null

    @Test
    fun `should fetch controllers from spring context`() {
        assertThat(homeController).isNotNull()
        assertThat(aboutController).isNotNull()
        assertThat(userController).isNotNull()
    }
}