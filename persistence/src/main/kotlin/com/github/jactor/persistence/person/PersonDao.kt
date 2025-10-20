package com.github.jactor.persistence.person

import com.github.jactor.persistence.PersistentDao
import java.time.LocalDateTime
import java.util.UUID

data class PersonDao(
    override var id: UUID? = null,
    override var createdBy: String = "todo",
    override var timeOfCreation: LocalDateTime = LocalDateTime.now(),
    override var modifiedBy: String = "todo",
    override var timeOfModification: LocalDateTime = LocalDateTime.now(),

    var description: String? = null,
    var firstName: String? = null,
    var locale: String? = null,
    var surname: String,
    var addressId: UUID? = null,
) : PersistentDao<PersonDao> {
    fun toPerson() = Person(
        persistent = toPersistent(),
        addressId = addressId,
        locale = locale,
        firstName = firstName,
        surname = surname,
        description = description
    )

    override fun copyWithoutId(): PersonDao = copy(
        id = null,
        addressId = null,
    )

    override fun modifiedBy(modifier: String): PersonDao {
        modifiedBy = modifier
        timeOfModification = LocalDateTime.now()

        return this
    }
}
