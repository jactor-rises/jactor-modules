package com.github.jactor.web.menu

import com.github.jactor.web.JactorWebBeans
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.PropertySource

@SpringBootTest
@PropertySource("classpath:application.properties")
internal class MenuFacadeIntegrationTest {
    @Autowired
    private lateinit var testMenuFacade: MenuFacade

    @Test
    fun `should fail when fetching items for an unknown menu`() {
        assertThatIllegalArgumentException().isThrownBy { testMenuFacade.fetchMenuItemsByName("unknown") }
    }

    @Test
    fun `should fetch user menu items and reveal chosen item or child`() {
        val target = "user?choose=jactor"
        val menuItems = testMenuFacade.fetchMenuItemsByName(JactorWebBeans.USERS_MENU_NAME).flatMap { menuItem: MenuItem ->
            menuItem.getChildren()
        }

        assertSoftly { softly: SoftAssertions ->
            for (menuItem in menuItems) {
                if (menuItem.hasChildren() && menuItem.isNamed("menu.users.default")) {
                    softly.assertThat(menuItem.isChildChosen(target)).`as`("'menu.users.default' should have chosen child").isTrue
                } else if (menuItem.hasChildren()) {
                    softly.assertThat(menuItem.isChildChosen(target)).`as`("%s should not have chosen child", menuItem.itemName).isTrue
                } else {
                    softly.assertThat(menuItem.isChosen(target)).`as`("expected jactor to be chosen, not %s", menuItem.itemName)
                        .isEqualTo("jactor" == menuItem.itemName)
                }
            }

            softly.assertAll()
        }
    }
}
