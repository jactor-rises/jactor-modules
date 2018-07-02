package com.github.jactor.rises.model.facade.menu.builder;

import com.github.jactor.rises.client.datatype.Name;
import com.github.jactor.rises.model.facade.menu.MenuItem;
import com.github.jactor.rises.model.facade.menu.MenuItemTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * a builder of menu items
 */
public class MenuItemBuilder {
    private final List<MenuItem> children = new ArrayList<>();

    private Name itemName;
    private String description;
    private MenuItemTarget menuItemTarget;

    public MenuItemBuilder add(MenuItemBuilder childBuilder) {
        return add(childBuilder.build());
    }

    public MenuItem build() {
        return new MenuItem(this.itemName, this.description, menuItemTarget).appendChildren(children);
    }

    private MenuItemBuilder add(MenuItem child) {
        children.add(child);
        return this;
    }

    public MenuItemBuilder withTarget(String menuItemTarget) {
        this.menuItemTarget = new MenuItemTarget(menuItemTarget);
        return this;
    }

    public MenuItemBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public MenuItemBuilder withName(String name) {
        itemName = new Name(name);
        return this;
    }
}
