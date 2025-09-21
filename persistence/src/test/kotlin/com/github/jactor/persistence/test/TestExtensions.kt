package com.github.jactor.persistence.test

import java.util.UUID
import com.github.jactor.persistence.Address
import com.github.jactor.persistence.Blog
import com.github.jactor.persistence.common.Persistent

fun Address.withId() = copy(persistent = Persistent(id = UUID.randomUUID()))
fun Blog.withId() = copy(persistent = persistent.copy(id = UUID.randomUUID()))
