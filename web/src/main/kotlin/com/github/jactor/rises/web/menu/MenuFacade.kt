package com.github.jactor.rises.web.menu

class MenuFacade(
    private val menusByName: Map<String, Menu>,
) {
    constructor(menus: List<Menu>) : this(menus.associateBy { it.name })

    fun fetchMenuItemsByName(name: String): List<MenuItem> =
        menusByName[name]?.items()
            ?: throw IllegalArgumentException("$name is an unknown name of a menu. Known menus: " + menusByName.keys)
}
