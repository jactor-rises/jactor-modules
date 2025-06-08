package com.github.jactor.persistence.blog

import java.util.UUID
import org.springframework.data.repository.CrudRepository

interface BlogEntryRepository : CrudRepository<BlogEntryEntity, UUID> {
    fun findByBlogId(blogId: UUID?): List<BlogEntryEntity?>
}
