package com.github.jactor.rises.persistence.aop

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.jactor.rises.persistence.Persistent
import com.github.jactor.rises.persistence.test.initAddressDao
import com.github.jactor.rises.persistence.test.initBlogDao
import com.github.jactor.rises.persistence.test.initBlogEntryDao
import com.github.jactor.rises.persistence.test.initGuestBookDao
import com.github.jactor.rises.persistence.test.initGuestBookEntryDao
import com.github.jactor.rises.persistence.test.initPersonDao
import com.github.jactor.rises.persistence.test.initUserDao
import com.github.jactor.rises.shared.test.countSecondsUntilNow
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.aspectj.lang.JoinPoint
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class ModifierAspectTest {
    private val oneMinuteAgo = LocalDateTime.now().minusMinutes(1)
    private val modifierAspect = ModifierAspect()
    private val persistent = Persistent(
        createdBy = "na",
        id = null,
        modifiedBy = "na",
        timeOfCreation = oneMinuteAgo,
        timeOfModification = oneMinuteAgo,
    )

    @MockK
    private lateinit var joinPointMock: JoinPoint

    @Test
    fun `should modify timestamp on address when used`() {
        val withId = initAddressDao(id = UUID.randomUUID(), timeOfModification = oneMinuteAgo)
        val withoutId = initAddressDao(id = null, timeOfModification = oneMinuteAgo)

        every { joinPointMock.args } returns arrayOf<Any>(withId, withoutId)

        modifierAspect.modifyPersistentDao(joinPointMock)

        val idNoOfSeconds = withId.timeOfModification.countSecondsUntilNow()
        val noIdNoOfSeconds = withoutId.timeOfModification.countSecondsUntilNow()

        assertAll {
            assertThat(idNoOfSeconds, "with id").isEqualTo(0)
            assertThat(noIdNoOfSeconds, "without id").isEqualTo(60)
        }
    }

    @Test
    fun `should modify timestamp on blog when used`() {
        val withId = initBlogDao(id = UUID.randomUUID(), timeOfModification = oneMinuteAgo)
        val withoutId = initBlogDao(id = null, timeOfModification = oneMinuteAgo)

        every { joinPointMock.args } returns arrayOf<Any>(withId, withoutId)

        modifierAspect.modifyPersistentDao(joinPointMock)

        val idNoOfSeconds = withId.timeOfModification.countSecondsUntilNow()
        val noIdNoOfSeconds = withoutId.timeOfModification.countSecondsUntilNow()

        assertAll {
            assertThat(idNoOfSeconds, "with id").isEqualTo(0)
            assertThat(noIdNoOfSeconds, "without id").isEqualTo(60)
        }
    }

    @Test
    fun `should modify timestamp on blogEntry when used`() {
        val withId = initBlogEntryDao(id = UUID.randomUUID(), timeOfModification = oneMinuteAgo)
        val withoutId = initBlogEntryDao(id = null, timeOfModification = oneMinuteAgo)

        every { joinPointMock.args } returns arrayOf<Any>(withId, withoutId)

        modifierAspect.modifyPersistentDao(joinPointMock)

        val idNoOfSeconds = withId.timeOfModification.countSecondsUntilNow()
        val noIdNoOfSeconds = withoutId.timeOfModification.countSecondsUntilNow()

        assertAll {
            assertThat(idNoOfSeconds, "with id").isEqualTo(0)
            assertThat(noIdNoOfSeconds, "without id").isEqualTo(60)
        }
    }

    @Test
    fun `should modify timestamp on guestBook when used`() {
        val withId = initGuestBookDao(id = UUID.randomUUID(), timeOfModification = oneMinuteAgo)
        val withoutId = initGuestBookDao(id = null, timeOfModification = oneMinuteAgo)

        every { joinPointMock.args } returns arrayOf<Any>(withId, withoutId)

        modifierAspect.modifyPersistentDao(joinPointMock)

        val idNoOfSeconds = withId.timeOfModification.countSecondsUntilNow()
        val noIdNoOfSeconds = withoutId.timeOfModification.countSecondsUntilNow()

        assertAll {
            assertThat(idNoOfSeconds, "with id").isEqualTo(0)
            assertThat(noIdNoOfSeconds, "without id").isEqualTo(60)
        }
    }

    @Test
    fun `should modify timestamp on guestBookEntry when used`() {
        val withId = initGuestBookEntryDao(id = UUID.randomUUID(), timeOfModification = oneMinuteAgo)
        val withoutId = initGuestBookEntryDao(id = null, timeOfModification = oneMinuteAgo)

        every { joinPointMock.args } returns arrayOf<Any>(withId, withoutId)

        modifierAspect.modifyPersistentDao(joinPointMock)

        val idNoOfSeconds = withId.timeOfModification.countSecondsUntilNow()
        val noIdNoOfSeconds = withoutId.timeOfModification.countSecondsUntilNow()

        assertAll {
            assertThat(idNoOfSeconds, "with id").isEqualTo(0)
            assertThat(noIdNoOfSeconds, "without id").isEqualTo(60)
        }
    }

    @Test
    fun `should modify timestamp on person when used`() {
        val withId = initPersonDao(id = UUID.randomUUID(), timeOfModification = oneMinuteAgo)
        val withoutId = initPersonDao(id = null, timeOfModification = oneMinuteAgo)

        every { joinPointMock.args } returns arrayOf<Any>(withId, withoutId)

        modifierAspect.modifyPersistentDao(joinPointMock)

        val idNoOfSeconds = withId.timeOfModification.countSecondsUntilNow()
        val noIdNoOfSeconds = withoutId.timeOfModification.countSecondsUntilNow()

        assertAll {
            assertThat(idNoOfSeconds, "with id").isEqualTo(0)
            assertThat(noIdNoOfSeconds, "without id").isEqualTo(60)
        }
    }

    @Test
    fun `should modify timestamp on user when used`() {
        val withId = initUserDao(id = UUID.randomUUID(), timeOfModification = oneMinuteAgo)
        val withoutId = initUserDao(id = null, timeOfModification = oneMinuteAgo)

        every { joinPointMock.args } returns arrayOf<Any>(withId, withoutId)

        modifierAspect.modifyPersistentDao(joinPointMock)

        val idNoOfSeconds = withId.timeOfModification.countSecondsUntilNow()
        val noIdNoOfSeconds = withoutId.timeOfModification.countSecondsUntilNow()

        assertAll {
            assertThat(idNoOfSeconds, "with id").isEqualTo(0)
            assertThat(noIdNoOfSeconds, "without id").isEqualTo(60)
        }
    }
}
