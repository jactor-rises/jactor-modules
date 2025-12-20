package com.github.jactor.rises.persistence

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.jactor.rises.persistence.test.initAddressDao
import com.github.jactor.rises.persistence.test.initBlogDao
import com.github.jactor.rises.persistence.test.initBlogEntryDao
import com.github.jactor.rises.persistence.test.initGuestBookDao
import com.github.jactor.rises.persistence.test.initGuestBookEntryDao
import com.github.jactor.rises.persistence.test.initPersonDao
import com.github.jactor.rises.persistence.test.initUserDao
import com.github.jactor.rises.shared.test.countSecondsUntilNow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

internal class PersistenceHandlerTest {
    private val oneMinuteAgo = LocalDateTime.now().minusMinutes(1)
    private val persistenceHandler = PersistenceHandler()

    @Test
    fun `should modify timestamp on address when used`() = runTest {
        val withId = initAddressDao(id = UUID.randomUUID(), timeOfModification = oneMinuteAgo)
        val withoutId = initAddressDao(id = null, timeOfModification = oneMinuteAgo)

        assertAll {
            persistenceHandler.modifyAndSave(withId) { dao ->
                assertThat(dao.timeOfModification.countSecondsUntilNow(), "with id").isEqualTo(0)
                return@modifyAndSave dao
            }

            persistenceHandler.modifyAndSave(withoutId) { dao ->
                assertThat(dao.timeOfModification.countSecondsUntilNow(), "without id").isEqualTo(60)
                return@modifyAndSave dao
            }
        }
    }

    @Test
    fun `should modify timestamp on blog when used`() = runTest {
        val withId = initBlogDao(id = UUID.randomUUID(), timeOfModification = oneMinuteAgo)
        val withoutId = initBlogDao(id = null, timeOfModification = oneMinuteAgo)

        assertAll {
            persistenceHandler.modifyAndSave(withId) { dao ->
                assertThat(dao.timeOfModification.countSecondsUntilNow(), "with id").isEqualTo(0)
                return@modifyAndSave dao
            }

            persistenceHandler.modifyAndSave(withoutId) { dao ->
                assertThat(dao.timeOfModification.countSecondsUntilNow(), "without id").isEqualTo(60)
                return@modifyAndSave dao
            }
        }
    }

    @Test
    fun `should modify timestamp on blogEntry when used`() = runTest {
        val withId = initBlogEntryDao(id = UUID.randomUUID(), timeOfModification = oneMinuteAgo)
        val withoutId = initBlogEntryDao(id = null, timeOfModification = oneMinuteAgo)

        assertAll {
            persistenceHandler.modifyAndSave(withId) { dao ->
                assertThat(dao.timeOfModification.countSecondsUntilNow(), "with id").isEqualTo(0)
                return@modifyAndSave dao
            }

            persistenceHandler.modifyAndSave(withoutId) { dao ->
                assertThat(dao.timeOfModification.countSecondsUntilNow(), "without id").isEqualTo(60)
                return@modifyAndSave dao
            }
        }
    }

    @Test
    fun `should modify timestamp on guestBook when used`() = runTest {
        val withId = initGuestBookDao(id = UUID.randomUUID(), timeOfModification = oneMinuteAgo)
        val withoutId = initGuestBookDao(id = null, timeOfModification = oneMinuteAgo)

        assertAll {
            persistenceHandler.modifyAndSave(withId) { dao ->
                assertThat(dao.timeOfModification.countSecondsUntilNow(), "with id").isEqualTo(0)
                return@modifyAndSave dao
            }

            persistenceHandler.modifyAndSave(withoutId) { dao ->
                assertThat(dao.timeOfModification.countSecondsUntilNow(), "without id").isEqualTo(60)
                return@modifyAndSave dao
            }
        }
    }

    @Test
    fun `should modify timestamp on guestBookEntry when used`() = runTest {
        val withId = initGuestBookEntryDao(id = UUID.randomUUID(), timeOfModification = oneMinuteAgo)
        val withoutId = initGuestBookEntryDao(id = null, timeOfModification = oneMinuteAgo)

        assertAll {
            persistenceHandler.modifyAndSave(withId) { dao ->
                assertThat(dao.timeOfModification.countSecondsUntilNow(), "with id").isEqualTo(0)
                return@modifyAndSave dao
            }

            persistenceHandler.modifyAndSave(withoutId) { dao ->
                assertThat(dao.timeOfModification.countSecondsUntilNow(), "without id").isEqualTo(60)
                return@modifyAndSave dao
            }
        }
    }

    @Test
    fun `should modify timestamp on person when used`() = runTest {
        val withId = initPersonDao(id = UUID.randomUUID(), timeOfModification = oneMinuteAgo)
        val withoutId = initPersonDao(id = null, timeOfModification = oneMinuteAgo)

        assertAll {
            persistenceHandler.modifyAndSave(withId) { dao ->
                assertThat(dao.timeOfModification.countSecondsUntilNow(), "with id").isEqualTo(0)
                return@modifyAndSave dao
            }

            persistenceHandler.modifyAndSave(withoutId) { dao ->
                assertThat(dao.timeOfModification.countSecondsUntilNow(), "without id").isEqualTo(60)
                return@modifyAndSave dao
            }
        }
    }

    @Test
    fun `should modify timestamp on user when used`() = runTest {
        val withId = initUserDao(id = UUID.randomUUID(), timeOfModification = oneMinuteAgo)
        val withoutId = initUserDao(id = null, timeOfModification = oneMinuteAgo)

        assertAll {
            persistenceHandler.modifyAndSave(withId) { dao ->
                assertThat(dao.timeOfModification.countSecondsUntilNow(), "with id").isEqualTo(0)
                return@modifyAndSave dao
            }

            persistenceHandler.modifyAndSave(withoutId) { dao ->
                assertThat(dao.timeOfModification.countSecondsUntilNow(), "without id").isEqualTo(60)
                return@modifyAndSave dao
            }
        }
    }
}
