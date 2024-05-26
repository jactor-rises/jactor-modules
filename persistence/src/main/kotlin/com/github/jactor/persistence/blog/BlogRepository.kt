package com.github.jactor.persistence.blog

import java.util.UUID
import org.springframework.data.repository.CrudRepository

interface BlogRepository : CrudRepository<BlogEntity, UUID> {
    fun findBlogsByTitle(title: String?): List<BlogEntity>
}
