package com.github.jactor.persistence.repository

import java.util.UUID
import com.github.jactor.persistence.entity.BlogEntity
import org.springframework.data.repository.CrudRepository

interface BlogRepository : CrudRepository<BlogEntity, UUID> {
    fun findBlogsByTitle(title: String?): List<BlogEntity>
}
