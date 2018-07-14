package com.github.jactor.rises.model.facade.menu;

import com.github.jactor.rises.client.datatype.Name;
import com.github.jactor.rises.model.facade.MenuFacade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notEmpty;

public class DefaultMenuFacade implements MenuFacade {
    private final Map<Name, Menu> menusByName = new HashMap<>();

    public DefaultMenuFacade(Menu... menus) {
        notEmpty(menus, "Menus must be provided");

        for (Menu menu : menus) {
            Name menuName = menu.getName();
            menusByName.put(menuName, new Menu(menu.getName(), menu.getMenuItems()));
        }
    }

    public @Override List<MenuItem> fetchMenuItems(Name name) {
        isTrue(menusByName.containsKey(name), name + " is an unknown menu. Known menus: " + menusByName.keySet());

        return menusByName.get(name).getMenuItems();
    }
}
