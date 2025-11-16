package com.github.jactor.rises.persistence.blog

import org.jetbrains.exposed.v1.jdbc.selectAll
import com.github.jactor.rises.persistence.guestbook.toBlogDao
import com.github.jactor.rises.persistence.guestbook.toBlogEntryDao

object BlogTestRepositoryObject  {
    fun findBlogEntries(): List<BlogEntryDao> = BlogEntries.selectAll()
        .map { it.toBlogEntryDao() }

    fun findBlogs(): List<BlogDao> = Blogs.selectAll()
        .map { it.toBlogDao() }
}
