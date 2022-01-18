package com.github.jactor.web.controller

import com.github.jactor.web.i18n.MyMessages
import com.github.jactor.web.model.HomePageModel
import junit.framework.AssertionFailedError
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.view.InternalResourceViewResolver

@SpringBootTest
@DisplayName("The HomeController")
internal class HomeControllerTest {
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var myMessages: MyMessages

    @Value("\${spring.mvc.view.prefix}")
    private lateinit var prefix: String

    @Value("\${spring.mvc.view.suffix}")
    private lateinit var suffix: String

    @BeforeEach
    fun `mock mvc with view resolver`() {
        val internalResourceViewResolver = InternalResourceViewResolver()

        internalResourceViewResolver.setPrefix(prefix)
        internalResourceViewResolver.setSuffix(suffix)

        mockMvc = MockMvcBuilders.standaloneSetup(HomeController(myMessages))
            .setViewResolvers(internalResourceViewResolver)
            .build()
    }

    @Test
    fun `should create a homepage dto with my messages`() {
        val modelAndView = mockMvc.perform(
            MockMvcRequestBuilders.get("/home")
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        ).andReturn().modelAndView

        assertAll(
            { assertThat(modelAndView).`as`("modelAndView").isNotNull() },
            {
                val model = modelAndView?.model ?: throw AssertionFailedError("No model to be found!")
                assertThat(model).isNotNull

                val homePageModel = model["homepage"] as HomePageModel?
                assertThat(homePageModel!!.technologies).`as`("technologies").hasSize(10)
            }
        )
    }
}