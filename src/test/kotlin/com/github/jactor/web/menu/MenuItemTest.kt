package com.github.jactor.web.menu

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("A MenuItem")
internal class MenuItemTest {

    companion object {
        private const val HIT_DEAD_CENTER = "hit?dead=center"
    }

    @Test
    @DisplayName("should not be chosen when the target is unknown")
    fun shouldNotBeChosenWhenTheTargetIsUnknown() {
        val testMenuItem = MenuItem(itemName = "an item", target = "miss")
        assertThat(testMenuItem.isChosen(HIT_DEAD_CENTER)).isEqualTo(false)
    }

    @Test
    @DisplayName("should be chosen when the target is known")
    fun shouldBeChosenWhenTheTargetIsKnown() {
        val testMenuItem = MenuItem(itemName = "an item", target = HIT_DEAD_CENTER)
        assertThat(testMenuItem.isChosen(HIT_DEAD_CENTER)).isEqualTo(true)
    }

    @Test
    @DisplayName("should not have chosen child when the target is unknown")
    fun shouldNotBeChosenChildWhenMenuTargetOnChildIsUnknown() {
        val testMenuItem = MenuItem(itemName = "an item", target = HIT_DEAD_CENTER)
            .addChild(MenuItem(itemName = "a child", target = "miss"))

        assertThat(testMenuItem.isChildChosen(HIT_DEAD_CENTER)).isEqualTo(false)
    }

    @Test
    @DisplayName("should have chosen child when the target is known")
    fun shouldBeChosenChildWhenMenuTargetOnChildIsKnown() {
        val testMenuItem = MenuItem(itemName = "an item", target = "miss")
            .addChild(MenuItem(itemName = "an item", target = HIT_DEAD_CENTER))

        assertThat(testMenuItem.isChildChosen(HIT_DEAD_CENTER)).isEqualTo(true)
    }
}
