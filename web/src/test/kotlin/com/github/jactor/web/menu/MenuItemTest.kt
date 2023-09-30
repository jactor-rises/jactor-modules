package com.github.jactor.web.menu

import org.junit.jupiter.api.Test
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue

internal class MenuItemTest {

    companion object {
        private const val HIT_DEAD_CENTER = "hit?dead=center"
    }

    @Test
    fun `should not be chosen when the target is unknown`() {
        val testMenuItem = MenuItem(itemName = "an item", target = "miss")
        assertThat(testMenuItem.isChosen(HIT_DEAD_CENTER)).isFalse()
    }

    @Test
    fun `should be chosen when the target is known`() {
        val testMenuItem = MenuItem(itemName = "an item", target = HIT_DEAD_CENTER)
        assertThat(testMenuItem.isChosen(HIT_DEAD_CENTER)).isTrue()
    }

    @Test
    fun `should not have chosen child when the target is unknown`() {
        val testMenuItem = MenuItem(itemName = "an item", target = HIT_DEAD_CENTER)
            .addChild(MenuItem(itemName = "a child", target = "miss"))

        assertThat(testMenuItem.isChildChosen(HIT_DEAD_CENTER)).isFalse()
    }

    @Test
    fun `should have chosen child when the target is known`() {
        val testMenuItem = MenuItem(itemName = "an item", target = "miss")
            .addChild(MenuItem(itemName = "an item", target = HIT_DEAD_CENTER))

        assertThat(testMenuItem.isChildChosen(HIT_DEAD_CENTER)).isTrue()
    }
}
