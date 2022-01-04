package com.github.jactor.persistence.entity

import java.time.LocalDateTime

interface PersistentData {
    val createdBy: String?
    val timeOfCreation: LocalDateTime?
    val modifiedBy: String?
    val timeOfModification: LocalDateTime?
}
