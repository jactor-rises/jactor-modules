package com.github.jactor.rises.model.facade.menu;

import com.github.jactor.rises.client.datatype.Name;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

import static java.util.Objects.hash;

public class MenuTarget {
    private static final ThreadLocal<MenuItemTarget> REQUESTED_BY_THREAD = new ThreadLocal<>();

    private final MenuItemTarget menuItemTarget;
    private final Name menuName;

    public MenuTarget(MenuItemTarget menuItemTarget, Name menuName) {
        this.menuItemTarget = menuItemTarget;
        this.menuName = menuName;
        REQUESTED_BY_THREAD.set(menuItemTarget);
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MenuTarget menuTarget = (MenuTarget) o;

        return Objects.equals(menuItemTarget, menuTarget.menuItemTarget) && Objects.equals(menuName, menuTarget.menuName);
    }

    @Override public int hashCode() {
        return hash(menuItemTarget, menuName);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(menuName)
                .append(menuItemTarget)
                .toString();
    }

    public MenuItemTarget getMenuItemTarget() {
        return menuItemTarget;
    }

    public Name getMenuName() {
        return menuName;
    }
}
