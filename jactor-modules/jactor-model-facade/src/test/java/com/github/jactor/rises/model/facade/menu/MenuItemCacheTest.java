package com.github.jactor.rises.model.facade.menu;

import com.github.jactor.rises.client.datatype.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("A MenuItemCache")
class MenuItemCacheTest {
    private MenuItemCache testMenuItemCache;
    private MenuTarget somewhereOnTheMenu;
    private MenuTarget somewhereElseOnTheMenu;

    @BeforeEach void initForTest() {
        testMenuItemCache = new MenuItemCache();
    }

    @BeforeEach void initSomewhereOnTheMenu() {
        somewhereOnTheMenu = new MenuTarget(new MenuItemTarget("somewhere"), new Name("main.menu"));
    }

    @BeforeEach void initSomewhereElseOnTheMenu() {
        somewhereElseOnTheMenu = new MenuTarget(new MenuItemTarget("somewhere else"), new Name("main.menu"));
    }

    @DisplayName("should not find cached menu items when cache is empty")
    @Test void sholdTellIfChosenItemIsCached() {
        assertThat(testMenuItemCache.isCached(somewhereOnTheMenu)).isEqualTo(false);
    }

    @DisplayName("should not find cached menu items when menu target is unknown")
    @Test void sholdTellIfNotChosenItemIsCached() {
        testMenuItemCache.cache(somewhereOnTheMenu, Collections.emptyList());
        assertThat(testMenuItemCache.isCached(somewhereElseOnTheMenu)).isEqualTo(false);
    }

    @DisplayName("should find cached menu items when menu target is cached")
    @Test void shouldCacheChosenMenuItem() {
        testMenuItemCache.cache(somewhereOnTheMenu, Collections.emptyList());
        assertThat(testMenuItemCache.isCached(somewhereOnTheMenu)).isEqualTo(true);
    }

    @DisplayName("should find cached menu items when menu target is cached")
    @Test void shouldCacheMenuItemsBasedOnMenuTarget() {
        @SuppressWarnings("unchecked") final List<MenuItem> eiListeAvMenuItems = Collections.emptyList();
        @SuppressWarnings("unchecked") final List<MenuItem> eiAnnenListeAvMenuItems = Collections.emptyList();

        testMenuItemCache.cache(somewhereOnTheMenu, eiListeAvMenuItems);
        testMenuItemCache.cache(somewhereElseOnTheMenu, eiAnnenListeAvMenuItems);

        assertAll(
                () -> assertThat(testMenuItemCache.fetchBy(somewhereOnTheMenu)).isEqualTo(eiListeAvMenuItems),
                () -> assertThat(testMenuItemCache.fetchBy(somewhereElseOnTheMenu)).isEqualTo(eiAnnenListeAvMenuItems)
        );
    }
}
