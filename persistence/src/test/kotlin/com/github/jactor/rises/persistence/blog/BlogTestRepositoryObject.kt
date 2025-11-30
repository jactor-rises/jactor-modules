package com.github.jactor.rises.persistence.blog

import com.github.jactor.rises.persistence.util.toBlogDao
import com.github.jactor.rises.persistence.util.toBlogEntryDao
import org.jetbrains.exposed.v1.jdbc.selectAll

object BlogTestRepositoryObject {
    fun findBlogEntries(): List<BlogEntryDao> = BlogEntries.selectAll()
        .map { it.toBlogEntryDao() }

    fun findBlogs(): List<BlogDao> = Blogs.selectAll()
        .map { it.toBlogDao() }
}
