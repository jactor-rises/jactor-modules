package com.github.jactor.rises.web.menu

@JvmRecord
data class Menu(
    val name: String,
    private val menuItems: MutableList<MenuItem>,
) {
    constructor(name: String) : this(name, ArrayList())

    fun items(): List<MenuItem> = ArrayList(menuItems)

    fun addItem(menuItem: MenuItem): Menu {
        menuItems.add(menuItem)
        return this
    }
}
