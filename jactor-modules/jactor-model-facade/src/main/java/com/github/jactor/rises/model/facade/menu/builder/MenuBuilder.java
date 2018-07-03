package com.github.jactor.rises.model.facade.menu.builder;

import com.github.jactor.rises.model.facade.menu.MenuItem;
import com.github.jactor.rises.client.datatype.Name;
import com.github.jactor.rises.model.facade.menu.Menu;

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

    public MenuBuilder withName(String name) {
        nameOfMenu = new Name(name);
        return this;
    }

    public Menu build() {
        return new Menu(nameOfMenu, menuItems);
    }
}
