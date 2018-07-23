package com.gitlab.jactor.rises.model.facade.menu;

import com.gitlab.jactor.rises.commons.datatype.Name;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/** A {@link Menu} that contains a collection of {@link MenuItem}s. */
public class Menu {

    private final Name menuName;
    private final List<MenuItem> menuItems = new ArrayList<>();

    Menu(Name menuName, List<MenuItem> menuItems) {
        this.menuName = menuName;
        this.menuItems.addAll(menuItems);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(menuName)
                .append(menuItems)
                .toString();
    }

    public Name getName() {
        return menuName;
    }

    List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public static MenuBuilder aMenu() {
        return new MenuBuilder();
    }
}
