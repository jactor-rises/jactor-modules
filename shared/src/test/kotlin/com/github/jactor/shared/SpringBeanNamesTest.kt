package com.github.jactor.shared

import org.junit.jupiter.api.Test
import assertk.assertThat
import assertk.assertions.isEqualTo

class SpringBeanNamesTest {
    private val springBeanNames = SpringBeanNames()

    @Test
    fun `should only add simple bean definition name for the given bean`() {
        springBeanNames.add("com.github.jactor.shared.FooBar")

        assertThat(springBeanNames.names).isEqualTo(listOf("FooBar"))
    }

    @Test
    fun `should retrieve full bean definition if it does not contain package name`() {
        springBeanNames.add("FooBar")

        assertThat(springBeanNames.names).isEqualTo(listOf("FooBar"))
    }

    @Test
    fun `should retrieve 1 line names for every 10 names given`() {
        springBeanNames.add("com.github.jactor.shared.FooBar0")
        springBeanNames.add("com.github.jactor.shared.FooBar1")
        springBeanNames.add("com.github.jactor.shared.FooBar2")
        springBeanNames.add("com.github.jactor.shared.FooBar3")
        springBeanNames.add("com.github.jactor.shared.FooBar4")
        springBeanNames.add("com.github.jactor.shared.FooBar5")
        springBeanNames.add("com.github.jactor.shared.FooBar6")
        springBeanNames.add("com.github.jactor.shared.FooBar7")
        springBeanNames.add("com.github.jactor.shared.FooBar8")
        springBeanNames.add("com.github.jactor.shared.FooBar9")

        assertThat(springBeanNames.names).isEqualTo(
            listOf("FooBar0, FooBar1, FooBar2, FooBar3, FooBar4, FooBar5, FooBar6, FooBar7, FooBar8, FooBar9")
        )
    }

    @Test
    fun `should retrieve 2 line names if there is more than 10 names but less than 20`() {
        springBeanNames.add("com.github.jactor.shared.FooBar0")
        springBeanNames.add("com.github.jactor.shared.FooBar1")
        springBeanNames.add("com.github.jactor.shared.FooBar2")
        springBeanNames.add("com.github.jactor.shared.FooBar3")
        springBeanNames.add("com.github.jactor.shared.FooBar4")
        springBeanNames.add("com.github.jactor.shared.FooBar5")
        springBeanNames.add("com.github.jactor.shared.FooBar6")
        springBeanNames.add("com.github.jactor.shared.FooBar7")
        springBeanNames.add("com.github.jactor.shared.FooBar8")
        springBeanNames.add("com.github.jactor.shared.FooBar9")
        springBeanNames.add("com.github.jactor.shared.FooBarA")
        springBeanNames.add("com.github.jactor.shared.FooBarB")
        springBeanNames.add("com.github.jactor.shared.FooBarC")
        springBeanNames.add("com.github.jactor.shared.FooBarD")

        assertThat(springBeanNames.names).assertAll {
            it named "names" withSize 2

            val line1 = it.firstOrNull()
            line1 named "line 1" equalTo "FooBar0, FooBar1, FooBar2, FooBar3, FooBar4, FooBar5, FooBar6, FooBar7," +
                " FooBar8, FooBar9"

            val line2 = it.lastOrNull()
            line2 named "line 2" equalTo "FooBarA, FooBarB, FooBarC, FooBarD"
        }
    }
}
