package com.github.jactor.persistence.repository

import com.github.jactor.persistence.entity.BlogEntity
import org.springframework.data.repository.CrudRepository

interface BlogRepository : CrudRepository<BlogEntity, Long> {
    fun findBlogsByTitle(title: String?): List<BlogEntity>
}
