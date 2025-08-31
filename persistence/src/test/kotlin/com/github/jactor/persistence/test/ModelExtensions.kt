package com.github.jactor.persistence.test

import java.util.UUID
import com.github.jactor.persistence.Address
import com.github.jactor.persistence.AddressEntity
import com.github.jactor.persistence.common.Persistent
import com.github.jactor.persistence.common.PersistentDataEmbeddable

fun Address.toEntityWithId() = AddressEntity().apply {
    id = UUID.randomUUID()
    persistentDataEmbeddable = this@toEntityWithId.persistent.copy(id = id).toEmbeddable()
    addressLine1 = this@toEntityWithId.addressLine1
    addressLine2 = this@toEntityWithId.addressLine2
    addressLine3 = this@toEntityWithId.addressLine3
    city = this@toEntityWithId.city
    country = this@toEntityWithId.country
    zipCode = this@toEntityWithId.zipCode
}

fun Persistent.toEmbeddable() = PersistentDataEmbeddable(persistent = this)
fun Address.withId() = this.copy(persistent = this.persistent.copy(id = UUID.randomUUID()))
