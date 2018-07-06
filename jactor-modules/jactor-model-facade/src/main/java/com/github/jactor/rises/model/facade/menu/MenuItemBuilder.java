package com.github.jactor.rises.model.facade.menu;

import com.github.jactor.rises.client.datatype.Name;

import java.util.ArrayList;
import java.util.List;

/**
 * a builder of menu items
 */
public class MenuItemBuilder {

    private final List<MenuItem> children = new ArrayList<>();

    private Name itemName;
    private String description;
    private String target;

    public MenuItemBuilder add(MenuItemBuilder childBuilder) {
        return add(childBuilder.build());
    }

    public MenuItem build() {
        return new MenuItem(itemName, description, target).appendChildren(children);
    }

    private MenuItemBuilder add(MenuItem child) {
        children.add(child);
        return this;
    }

    public MenuItemBuilder withTarget(String target) {
        this.target = target;
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

    public MenuItemBuilder addAsChildren(List<String> usernames) {
        usernames.stream()
                .map(this::usernameAsMenuItem)
                .forEach(this::add);

        return this;
    }

    private MenuItem usernameAsMenuItem(String username) {
        return new MenuItemBuilder()
                .withName(username)
                .withTarget("user?choose=" + username)
                .build();
    }
}
