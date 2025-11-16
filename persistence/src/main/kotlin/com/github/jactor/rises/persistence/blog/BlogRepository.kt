package com.github.jactor.rises.persistence.blog

import java.util.UUID
import org.springframework.stereotype.Repository

interface BlogRepository {
    fun findBlogById(id: UUID): BlogDao?
    fun findBlogsByUserId(id: UUID): List<BlogDao>
    fun findBlogEntryById(id: UUID): BlogEntryDao?
    fun findBlogsByTitle(title: String): List<BlogDao>
    fun findBlogEntriesByBlogId(id: UUID): List<BlogEntryDao>
    fun save(blogDao: BlogDao): BlogDao
    fun save(blogEntryDao: BlogEntryDao): BlogEntryDao
}

@Repository
class BlogRepositoryImpl : BlogRepository by BlogRepositoryObject
