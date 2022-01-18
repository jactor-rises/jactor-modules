package com.github.jactor.web.menu

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("A MenuFacade")
internal class MenuFacadeTest {

    @Test
    @DisplayName("should fail if the menu asked for is unknown")
    fun willFailWhenMenuIsUnknown() {
        val menuItem = MenuItem(itemName = "name", target = "target")
        val menuFacadeToTest = MenuFacade(
            listOf(
                Menu("known", mutableListOf(menuItem))
            )
        )

        assertThatIllegalArgumentException().isThrownBy { menuFacadeToTest.fetchMenuItemsByName("unknown.menu") }
            .withMessageContaining("unknown.menu")
            .withMessageContaining("known.menu")
    }

    @Test
    @DisplayName("should find menu items for known Menu")
    fun willFindKnownMenuItems() {
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
