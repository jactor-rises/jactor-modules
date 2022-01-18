package com.github.jactor.web.menu

class MenuItems(
    private val menuItemsByTarget: MutableMap<String?, MenuItem> = HashMap()
) {
    fun addAll(menuItems: List<MenuItem>) {
        menuItems.filter { it.hasTarget() }.associateBy({ it.target }, { it }).filterTo(menuItemsByTarget) { true }
    }

    fun fetchAll(): List<MenuItem> {
        return ArrayList(menuItemsByTarget.values)
    }

    fun hasTarget(target: String): Boolean {
        return menuItemsByTarget.containsKey(target)
    }

    fun isNotEmpty(): Boolean {
        return menuItemsByTarget.isNotEmpty()
    }
}

class MenuFacade(
    private val menusByName: Map<String, Menu>
) {
    constructor(menus: List<Menu>) : this(menus.associate { it.name to it })

    fun fetchMenuItemsByName(name: String): List<MenuItem> {
        return menusByName[name]?.items() ?: throw IllegalArgumentException("$name is an unknown name of a menu. Known menus: " + menusByName.keys)
    }
}

data class Menu(
    val name: String,
    private val menuItems: MutableList<MenuItem>
) {
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
    //    constructor(itemName: String) : this(itemName, null, null, ArrayList())
//    constructor(itemName: String, children: List<MenuItem>) : this(itemName, null, null, children as MutableList<MenuItem>)
    constructor(itemName: String, target: String) : this(itemName, target, null, ArrayList())
//    constructor(itemName: String, target: String, description: String) : this(itemName, target, description, ArrayList())

    fun addChild(child: MenuItem): MenuItem {
        children.add(child)
        return this
    }

    fun hasTarget(): Boolean {
        return target != null
    }

    fun getChildren(): List<MenuItem> {
        return children
    }

    fun hasChildren(): Boolean {
        return children.isNotEmpty()
    }

    fun isNamed(name: String): Boolean {
        return itemName.equals(name)
    }

    fun isChosen(target: String): Boolean {
        return this.target == target
    }

    fun isChildChosen(target: String): Boolean {
        return children.stream().filter { it.isChosen(target) }.findAny().isPresent
    }
}
