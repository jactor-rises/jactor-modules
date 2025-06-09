package com.github.jactor.web.controller

import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.view.InternalResourceViewResolver
import com.github.jactor.web.model.HomePageModel
import com.github.jactor.web.test.AbstractSpringMockMvcTest
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isNotNull

internal class HomeControllerTest : AbstractSpringMockMvcTest() {
    override val initMockMvc: (InternalResourceViewResolver) -> MockMvc = {
        MockMvcBuilders.standaloneSetup(HomeController(myMessages)).setViewResolvers(it).build()
    }

    @Test
    fun `should create a homepage dto with my messages`() {
        val modelAndView = mockMvc.perform(
            MockMvcRequestBuilders.get("/home")
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        ).andReturn().modelAndView

        assertAll {
            assertThat(modelAndView).isNotNull()
            val model = modelAndView?.model ?: throw AssertionFailedError("No model to be found!")
            assertThat(model).isNotNull()

            val homePageModel = model["homepage"] as HomePageModel?
            assertThat(homePageModel!!.technologies).hasSize(10)
        }
    }
}