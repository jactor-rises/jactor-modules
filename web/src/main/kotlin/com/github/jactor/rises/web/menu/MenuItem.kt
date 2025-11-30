package com.github.jactor.rises.web.menu

data class MenuItem(
    val itemName: String,
    val target: String? = null,
    var description: String? = null,
    private val children: MutableList<MenuItem> = mutableListOf(),
) {
    fun addChild(child: MenuItem): MenuItem {
        children.add(child)
        return this
    }

    fun getChildren(): List<MenuItem> = children

    fun hasChildren(): Boolean = children.isNotEmpty()

    fun isNamed(name: String): Boolean = itemName == name

    fun isChosen(target: String): Boolean = this.target == target

    fun isChildChosen(target: String): Boolean =
        children
            .stream()
            .filter { it.isChosen(target) }
            .findAny()
            .isPresent
}
