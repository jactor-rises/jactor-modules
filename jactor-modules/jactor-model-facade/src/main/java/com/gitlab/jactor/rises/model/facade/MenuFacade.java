package com.gitlab.jactor.rises.model.facade;

import com.gitlab.jactor.rises.commons.datatype.Name;
import com.gitlab.jactor.rises.model.facade.menu.MenuItem;

import java.util.List;

public interface MenuFacade {

    /**
     * @param menuName of the menu items to fetch.
     * @return a list of {@link MenuItem} by {@link Name}.
     */
    List<MenuItem> fetchMenuItems(Name menuName);
}
