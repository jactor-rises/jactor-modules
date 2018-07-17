package com.gitlab.jactor.rises.model.facade.menu;

import com.gitlab.jactor.rises.client.datatype.Name;

import java.util.ArrayList;
import java.util.List;

public class MenuBuilder {

    private Name nameOfMenu;
    private final List<MenuItem> menuItems = new ArrayList<>();

    public MenuBuilder add(MenuItemBuilder menuItemBuilder) {
        return add(menuItemBuilder.build());
    }

    public MenuBuilder add(MenuItem menuItemDomain) {
        menuItems.add(menuItemDomain);
        return this;
    }

    MenuBuilder withName(String name) {
        return withName(new Name(name));
    }

    public MenuBuilder withName(Name name) {
        nameOfMenu = name;
        return this;
    }

    public Menu build() {
        return new Menu(nameOfMenu, menuItems);
    }
}
