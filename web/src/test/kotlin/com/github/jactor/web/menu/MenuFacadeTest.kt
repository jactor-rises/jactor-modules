package com.github.jactor.web.menu

import org.junit.jupiter.api.Test
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isNotNull
import assertk.fail

internal class MenuFacadeTest {

    @Test
    fun `should fail if the menu asked for is unknown`() {
        val menuItem = MenuItem(itemName = "name", target = "target")
        val menuFacadeToTest = MenuFacade(
            listOf(
                Menu("known", mutableListOf(menuItem))
            )
        )

        runCatching { menuFacadeToTest.fetchMenuItemsByName("unknown.menu") }
            .onSuccess { fail("Test was supposed to fail!") }
            .onFailure {
                assertThat(it.message).isNotNull().contains("unknown.menu")
                assertThat(it.message).isNotNull().contains("known.menu")
            }
    }

    @Test
    fun `should find menu items for known Menu`() {
        val menuItem = MenuItem(itemName = "name", target = "target")
        val menuFacadeToTest = MenuFacade(
            listOf(
                Menu("known", mutableListOf(menuItem))
            )
        )

        val menuItems = menuFacadeToTest.fetchMenuItemsByName("known")

        assertThat(menuItems).contains(menuItem)
    }
}
