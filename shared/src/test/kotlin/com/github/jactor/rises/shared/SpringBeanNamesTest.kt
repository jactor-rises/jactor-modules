package com.github.jactor.rises.shared

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.jactor.rises.shared.test.all
import com.github.jactor.rises.shared.test.equals
import com.github.jactor.rises.shared.test.named
import com.github.jactor.rises.shared.test.sized
import org.junit.jupiter.api.Test

class SpringBeanNamesTest {
    private val springBeanNames = SpringBeanNames()

    @Test
    fun `should only add simple bean definition name for the given bean`() {
        springBeanNames.add("com.github.jactor.rises.shared.FooBar")

        assertThat(springBeanNames.names).isEqualTo(listOf("FooBar"))
    }

    @Test
    fun `should retrieve full bean definition if it does not contain package name`() {
        springBeanNames.add("FooBar")

        assertThat(springBeanNames.names).isEqualTo(listOf("FooBar"))
    }

    @Test
    fun `should retrieve 1 line names for every 4 names given`() {
        springBeanNames.add("com.github.jactor.rises.shared.FooBar0")
        springBeanNames.add("com.github.jactor.rises.shared.FooBar1")
        springBeanNames.add("com.github.jactor.rises.shared.FooBar2")
        springBeanNames.add("com.github.jactor.rises.shared.FooBar3")

        assertThat(springBeanNames.names).isEqualTo(
            listOf("FooBar0, FooBar1, FooBar2, FooBar3")
        )
    }

    @Test
    fun `should retrieve 4 line names if there is more than 12 names but less than 16`() {
        springBeanNames.add("com.github.jactor.rises.shared.FooBar0")
        springBeanNames.add("com.github.jactor.rises.shared.FooBar1")
        springBeanNames.add("com.github.jactor.rises.shared.FooBar2")
        springBeanNames.add("com.github.jactor.rises.shared.FooBar3")
        springBeanNames.add("com.github.jactor.rises.shared.FooBar4")
        springBeanNames.add("com.github.jactor.rises.shared.FooBar5")
        springBeanNames.add("com.github.jactor.rises.shared.FooBar6")
        springBeanNames.add("com.github.jactor.rises.shared.FooBar7")
        springBeanNames.add("com.github.jactor.rises.shared.FooBar8")
        springBeanNames.add("com.github.jactor.rises.shared.FooBar9")
        springBeanNames.add("com.github.jactor.rises.shared.FooBarA")
        springBeanNames.add("com.github.jactor.rises.shared.FooBarB")
        springBeanNames.add("com.github.jactor.rises.shared.FooBarC")
        springBeanNames.add("com.github.jactor.rises.shared.FooBarD")

        assertThat(springBeanNames.names).all {
            this named "names" sized 4

            firstOrNull() named "line 1" equals "FooBar0, FooBar1, FooBar2, FooBar3"
            getOrNull(index = 1) named "line 2" equals "FooBar4, FooBar5, FooBar6, FooBar7"
            getOrNull(index = 2) named "line 3" equals "FooBar8, FooBar9, FooBarA, FooBarB"
            lastOrNull() named "line 4" equals "FooBarC, FooBarD"
        }
    }
}
