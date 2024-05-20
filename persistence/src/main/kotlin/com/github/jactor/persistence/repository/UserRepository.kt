package com.github.jactor.persistence.repository

import com.github.jactor.persistence.entity.UserEntity
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface UserRepository : CrudRepository<UserEntity, UUID> {
    fun findByUsername(username: String): Optional<UserEntity>
    fun findByUserTypeIn(userType: Collection<UserEntity.UserType>): List<UserEntity>
}
