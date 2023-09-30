package com.github.jactor.web.menu

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.PropertySource
import com.github.jactor.web.JactorWebBeans
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue

@SpringBootTest
@PropertySource("classpath:application.properties")
internal class MenuFacadeIntegrationTest {
    @Autowired
    private lateinit var testMenuFacade: MenuFacade

    @Test
    fun `should fail when fetching items for an unknown menu`() {
        assertThrows<IllegalArgumentException> { testMenuFacade.fetchMenuItemsByName("unknown") }
    }

    @Test
    fun `should fetch user menu items and reveal chosen item or child`() {
        val target = "user?choose=jactor"
        val menuItems = testMenuFacade.fetchMenuItemsByName(JactorWebBeans.USERS_MENU_NAME)
            .flatMap { it.getChildren() }

        assertAll {
            for (menuItem in menuItems) {
                if (menuItem.hasChildren() && menuItem.isNamed("menu.users.default")) {
                    assertThat(menuItem.isChildChosen(target)).isTrue()
                } else if (menuItem.hasChildren()) {
                    assertThat(menuItem.isChildChosen(target)).isTrue()
                } else {
                    assertThat(menuItem.isChosen(target)).isEqualTo("jactor" == menuItem.itemName)
                }
            }
        }
    }
}
