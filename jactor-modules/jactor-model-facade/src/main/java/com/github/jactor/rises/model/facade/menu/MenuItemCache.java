package com.github.jactor.rises.model.facade.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple caching mechanism for a list of {@link MenuItem} by {@link MenuTarget}
 */
class MenuItemCache {

    private final Map<MenuTarget, List<MenuItem>> cachedMenuItems;

    MenuItemCache() {
        cachedMenuItems = new HashMap<>();
    }

    boolean isCached(MenuTarget menuTarget) {
        return cachedMenuItems.containsKey(menuTarget);
    }

    void cache(MenuTarget menuItemTarget, List<MenuItem> listeOfMenuItems) {
        cachedMenuItems.put(menuItemTarget, listeOfMenuItems);
    }

    List<MenuItem> fetchBy(MenuTarget menuItemTarget) {
        return cachedMenuItems.get(menuItemTarget);
    }
}
