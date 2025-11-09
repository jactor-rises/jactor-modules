package com.github.jactor.rises.web

import assertk.assertThat
import assertk.assertions.isNotNull
import com.github.jactor.rises.web.controller.AboutController
import com.github.jactor.rises.web.controller.HomeController
import com.github.jactor.rises.web.controller.UserController
import com.github.jactor.rises.web.test.AbstractNoDirtySpringContextTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class JactorWebTest @Autowired constructor(
    private val homeController: HomeController?,
    private val aboutController: AboutController?,
    private val userController: UserController?
) : AbstractNoDirtySpringContextTest() {

    @Test
    fun `should fetch controllers from spring context`() {
        assertThat(homeController).isNotNull()
        assertThat(aboutController).isNotNull()
        assertThat(userController).isNotNull()
    }
}