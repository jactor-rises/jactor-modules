package com.github.jactor.persistence.guestbook

import java.util.UUID
import com.github.jactor.persistence.entity.UserEntity
import org.springframework.data.repository.CrudRepository

interface GuestBookRepository : CrudRepository<GuestBookEntity, UUID> {
    fun findByUser(userEntity: UserEntity): GuestBookEntity?
}
