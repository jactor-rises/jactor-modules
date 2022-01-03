package com.github.jactor.persistence.time

import java.time.LocalDateTime

open class Now {
    protected open fun nowAsDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }

    companion object {
        private val SYNC = Any()

        @Volatile
        private var instance: Now? = null

        @JvmStatic
        fun asDateTime(): LocalDateTime {
            return instance!!.nowAsDateTime()
        }

        @JvmStatic
        fun reset(instance: Now?) {
            synchronized(SYNC) { Companion.instance = instance }
        }

        init {
            reset(Now())
        }
    }
}
