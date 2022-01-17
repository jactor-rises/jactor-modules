package com.github.jactor.web.menu;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.github.jactor.web.JactorWebBeans;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

@SpringBootTest
@PropertySource("classpath:application.properties")
@DisplayName("The MenuFacade")
class MenuFacadeIntegrationTest {

  @Autowired
  private MenuFacade testMenuFacade;

  @Test
  @DisplayName("should fail when fetching items for an unknown menu")
  void shouldFailWhenFetchingItemsForAnUnknownMenu() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> testMenuFacade.fetchMenuItemsByName("unknown"));
  }

  @Test
  @DisplayName("should fetch user menu items and reveal chosen item/child")
  void shouldFetchMenuItemsForMenuAndRevealChoosenChild() {
    String target = "user?choose=jactor";

    var menuItems = testMenuFacade.fetchMenuItemsByName(JactorWebBeans.USERS_MENU_NAME).stream()
        .flatMap(menuItem -> menuItem.getChildren().stream())
        .toList();

    assertSoftly(
        softly -> {
          for (MenuItem menuItem : menuItems) {
            if (menuItem.hasChildren() && menuItem.isNamed("menu.users.default")) {
              softly.assertThat(menuItem.isChildChosen(target)).as("'menu.users.default' should have chosen child").isEqualTo(true);
            } else if (menuItem.hasChildren()) {
              softly.assertThat(menuItem.isChildChosen(target)).as("%s should not have chosen child", menuItem.getItemName()).isEqualTo(false);
            } else {
              softly.assertThat(menuItem.isChosen(target)).as("expected jactor to be chosen, not %s", menuItem.getItemName())
                  .isEqualTo("jactor".equals(menuItem.getItemName()));
            }
          }
        }
    );
  }
}
