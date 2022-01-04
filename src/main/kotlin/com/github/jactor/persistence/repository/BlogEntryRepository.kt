package com.github.jactor.persistence.repository

import com.github.jactor.persistence.entity.BlogEntryEntity
import org.springframework.data.repository.CrudRepository

interface BlogEntryRepository : CrudRepository<BlogEntryEntity?, Long?> {
    fun findByBlog_Id(blogId: Long?): List<BlogEntryEntity?>? // the underscore in determined by spring jpa
}
