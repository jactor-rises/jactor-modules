package com.github.jactor.persistence.blog

import java.util.UUID
import org.springframework.data.repository.CrudRepository

interface BlogEntryRepository : CrudRepository<BlogEntryEntity, UUID> {
    @Suppress("FunctionName") // the underscore in determined by spring jpa
    fun findByBlog_Id(blogId: UUID?): List<BlogEntryEntity?>
}
