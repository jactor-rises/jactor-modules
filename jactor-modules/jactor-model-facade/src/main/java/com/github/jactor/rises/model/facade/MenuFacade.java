package com.github.jactor.rises.model.facade;

import com.github.jactor.rises.client.datatype.Name;
import com.github.jactor.rises.model.facade.menu.MenuItem;

import java.util.List;

public interface MenuFacade {

    /**
     * @param menuName of the menu items to fetch.
     * @return a list of {@link MenuItem} by {@link Name}.
     */
    List<MenuItem> fetchMenuItems(Name menuName);
}
