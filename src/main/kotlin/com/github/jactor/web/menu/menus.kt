package com.github.jactor.web.menu

class MenuFacade(private val menusByName: Map<String, Menu>) {
    constructor(menus: List<Menu>) : this(menus.associateBy { it.name })

    fun fetchMenuItemsByName(name: String): List<MenuItem> {
        return menusByName[name]?.items() ?: throw IllegalArgumentException("$name is an unknown name of a menu. Known menus: " + menusByName.keys)
    }
}

data class Menu(val name: String, private val menuItems: MutableList<MenuItem>) {
    constructor(name: String) : this(name, ArrayList())

    fun items(): List<MenuItem> {
        return ArrayList(menuItems)
    }

    fun addItem(menuItem: MenuItem): Menu {
        menuItems.add(menuItem)
        return this
    }
}

data class MenuItem(
    val itemName: String,
    val target: String? = null,
    var description: String? = null,
    private val children: MutableList<MenuItem> = mutableListOf()
) {

    fun addChild(child: MenuItem): MenuItem {
        children.add(child)
        return this
    }

    fun getChildren(): List<MenuItem> {
        return children
    }

    fun hasChildren(): Boolean {
        return children.isNotEmpty()
    }

    fun isNamed(name: String): Boolean {
        return itemName == name
    }

    fun isChosen(target: String): Boolean {
        return this.target == target
    }

    fun isChildChosen(target: String): Boolean {
        return children.stream().filter { it.isChosen(target) }.findAny().isPresent
    }
}
