package com.github.jactor.rises.model.facade;

import com.github.jactor.rises.model.facade.menu.MenuItem;
import com.github.jactor.rises.model.facade.menu.MenuTarget;
import com.github.jactor.rises.model.facade.menu.MenuTargetRequest;

import java.util.List;

/** Finds {@link MenuItem}s by {@link MenuTarget} */
public interface MenuFacade {

    /**
     * @param menuTargetRequest determine the menu where the menu item target belongs.
     * @return a list of {@link MenuItem} according to the request.
     */
    List<MenuItem> fetchMenuItem(MenuTargetRequest menuTargetRequest);
}
