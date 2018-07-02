package com.github.jactor.rises.model.facade.menu;

import com.github.jactor.rises.client.datatype.Name;

public final class MenuTargetRequest {
    private static final ThreadLocal<MenuItemTarget> REQUESTED_BY_THREAD = new ThreadLocal<>();
    private final Name menuName;

    public MenuTargetRequest(MenuTarget requestedTarget) {
        menuName = requestedTarget.getMenuName();
        REQUESTED_BY_THREAD.set(requestedTarget.getMenuItemTarget());
    }

    Name getMenuName() {
        return menuName;
    }

    static boolean isRequestFor(MenuItemTarget menuItemTarget) {
        return menuItemTarget != null && menuItemTarget.equals(REQUESTED_BY_THREAD.get());
    }
}
