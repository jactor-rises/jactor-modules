package com.github.jactor.persistence.time

import java.time.LocalDateTime

open class Now {
    protected open fun nowAsDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }

    companion object {
        private val SYNC = Any()

        @Volatile
        private lateinit var instance: Now

        fun asDateTime(): LocalDateTime {
            return instance.nowAsDateTime()
        }

        private fun reset(instance: Now) {
            synchronized(SYNC) { Companion.instance = instance }
        }

        init {
            reset(Now())
        }
    }
}
