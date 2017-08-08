package nu.hjemme.web.menu;

import nu.hjemme.client.datatype.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nu.hjemme.web.menu.Menu.aMenu;
import static nu.hjemme.web.menu.MenuItem.aMenuItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("A DefaultMenuFacade")
class DefaultMenuFacadeTest {

    @DisplayName("should fail if the menus provided are null")
    @Test void willThrowExceptionIfProvidedMenusAreNull() {
        assertThatThrownBy(() -> new DefaultMenuFacade((Menu[]) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Menus must be provided");
    }

    @DisplayName("should fail if there are no provided menus")
    @Test void willThrowExceptionIfProvidedMenusAreEmpty() {
        Menu[] menus = new Menu[]{};
        assertThatThrownBy(() -> new DefaultMenuFacade(menus))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Menus must be provided");
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