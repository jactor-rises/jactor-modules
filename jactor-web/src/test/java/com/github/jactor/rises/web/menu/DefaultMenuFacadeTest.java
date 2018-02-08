package com.github.jactor.rises.web.menu;

import com.github.jactor.rises.client.datatype.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.List;

import static com.github.jactor.rises.web.menu.Menu.aMenu;
import static com.github.jactor.rises.web.menu.MenuItem.aMenuItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(JUnitPlatform.class)
@DisplayName("A DefaultMenuFacade")
class DefaultMenuFacadeTest {

    @DisplayName("should fail if the menus provided are null")
    @Test void willThrowExceptionIfProvidedMenusAreNull() {
        assertThatNullPointerException().isThrownBy(() -> new DefaultMenuFacade((Menu[]) null))
                .withMessage("Menus must be provided");
    }

    @DisplayName("should fail if there are no provided menus")
    @Test void willThrowExceptionIfProvidedMenusAreEmpty() {
        Menu[] menus = new Menu[]{};
        assertThatIllegalArgumentException().isThrownBy(() -> new DefaultMenuFacade(menus))
                .withMessage("Menus must be provided");
    }

    @DisplayName("should fail if the menu asked for is unknown")
    @Test void willFailWhenMenuIsUnknown() {
        MenuItemTarget somewhere = new MenuItemTarget("somewhere");
        DefaultMenuFacade defaultMenuFacadeToTest = new DefaultMenuFacade(aMenu().withName("known.menu").add(aMenuItem()).build());

        assertThatThrownBy(() -> defaultMenuFacadeToTest.fetchMenuItemBy(new MenuTargetRequest(new MenuTarget(somewhere, new Name("unknown.menu")))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("unknown menu")
                .hasMessageContaining("known.menu");
    }

    @DisplayName("should find menu items for known MenuTarget")
    @Test void willFindKnownMenuItems() {
        MenuItemTarget somewhere = new MenuItemTarget("somewhere");
        MenuItem menuItem = aMenuItem().build();
        DefaultMenuFacade defaultMenuFacadeToTest = new DefaultMenuFacade(aMenu().withName("known.menu").add(menuItem).build());

        List<MenuItem> menuItems = defaultMenuFacadeToTest.fetchMenuItemBy(new MenuTargetRequest(new MenuTarget(somewhere, new Name("known.menu"))));

        assertThat(menuItems).contains(menuItem);
    }
}
