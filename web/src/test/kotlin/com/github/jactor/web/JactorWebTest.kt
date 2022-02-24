package com.github.jactor.web

import com.github.jactor.web.controller.AboutController
import com.github.jactor.web.controller.HomeController
import com.github.jactor.web.controller.UserController
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [JactorWeb::class])
internal class JactorWebTest {

  @Autowired
  private val homeController: HomeController? = null

  @Autowired
  private val aboutController: AboutController? = null

  @Autowired
  private val userController: UserController? = null

  @Test
  fun `should fetch controllers from spring context`() {
    assertThat(homeController).`as`("HomeController").isNotNull
    assertThat(aboutController).`as`("AboutController").isNotNull
    assertThat(userController).`as`("UserController").isNotNull
  }
}