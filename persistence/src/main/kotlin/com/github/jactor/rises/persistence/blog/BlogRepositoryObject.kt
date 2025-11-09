package com.github.jactor.rises.persistence.blog

import java.util.UUID
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update

object BlogRepositoryObject : BlogRepository {
    override fun findBlogById(id: UUID): BlogDao? = Blogs.selectAll()
        .andWhere { Blogs.id eq id }
        .map { it.toBlogDao() }
        .singleOrNull()

    override fun findBlogsByUserId(id: UUID): List<BlogDao> = Blogs.selectAll()
        .andWhere { Blogs.userId eq id }
        .map { it.toBlogDao() }

    override fun findBlogEntries(): List<BlogEntryDao> = BlogEntries.selectAll()
        .map { it.toBlogEntryDao() }

    override fun findBlogEntryById(id: UUID): BlogEntryDao? = BlogEntries.selectAll()
        .andWhere { BlogEntries.id eq id }
        .map { it.toBlogEntryDao() }
        .singleOrNull()

    override fun findBlogs(): List<BlogDao> = Blogs.selectAll()
        .map { it.toBlogDao() }

    override fun findBlogsByTitle(title: String): List<BlogDao> = Blogs.selectAll()
        .andWhere { Blogs.title eq title }
        .map { it.toBlogDao() }

    override fun findBlogEntriesByBlogId(id: UUID): List<BlogEntryDao> = BlogEntries.selectAll()
        .andWhere { BlogEntries.blogId eq id }
        .map { it.toBlogEntryDao() }

    override fun save(blogDao: BlogDao): BlogDao = when (blogDao.isNotPersisted) {
        true -> insert(blogDao)
        false -> update(blogDao)
    }

    private fun insert(blogDao: BlogDao): BlogDao = Blogs.insertAndGetId {
        it[Blogs.created] = blogDao.created
        it[Blogs.createdBy] = blogDao.createdBy
        it[Blogs.modifiedBy] = blogDao.modifiedBy
        it[Blogs.timeOfCreation] = blogDao.timeOfCreation
        it[Blogs.timeOfModification] = blogDao.timeOfModification
        it[Blogs.title] = blogDao.title
        it[Blogs.userId] = requireNotNull(blogDao.userId) { "A blog must belong to a user" }
    }.value.let { blogDao.copy(id = it) }

    private fun update(blogDao: BlogDao): BlogDao = Blogs.update(
        where = { Blogs.id eq blogDao.id }
    ) { update ->
        update[Blogs.modifiedBy] = blogDao.modifiedBy
        update[Blogs.timeOfModification] = blogDao.timeOfModification
        update[Blogs.created] = blogDao.created
        update[Blogs.title] = blogDao.title
        update[Blogs.userId] = requireNotNull(blogDao.userId) { "A blog must belong to a user" }
    }.let { blogDao }

    override fun save(blogEntryDao: BlogEntryDao): BlogEntryDao = when (blogEntryDao.isNotPersisted) {
        true -> insert(blogEntryDao)
        false -> update(blogEntryDao)
    }

    private fun insert(blogEntryDao: BlogEntryDao): BlogEntryDao = BlogEntries.insertAndGetId { insert ->
        insert[blogId] = requireNotNull(blogEntryDao.blogId) { "A blog entry must belong to a blog" }
        insert[createdBy] = blogEntryDao.createdBy
        insert[creatorName] = blogEntryDao.creatorName
        insert[entry] = blogEntryDao.entry
        insert[modifiedBy] = blogEntryDao.modifiedBy
        insert[timeOfCreation] = blogEntryDao.timeOfCreation
        insert[timeOfModification] = blogEntryDao.timeOfModification
    }.value.let { blogEntryDao.copy(id = it) }

    private fun update(blogEntryDao: BlogEntryDao): BlogEntryDao = BlogEntries.update(
        where = { BlogEntries.id eq blogEntryDao.id },
    ) { update ->
        update[blogId] = requireNotNull(blogEntryDao.blogId) { "A blog entry must belong to a blog" }
        update[createdBy] = blogEntryDao.createdBy
        update[creatorName] = blogEntryDao.creatorName
        update[modifiedBy] = blogEntryDao.modifiedBy
        update[timeOfCreation] = blogEntryDao.timeOfCreation
        update[timeOfModification] = blogEntryDao.timeOfModification
    }.let { blogEntryDao }

    private fun ResultRow.toBlogDao(): BlogDao = BlogDao(
        id = this[Blogs.id].value,
        created = this[Blogs.created],
        createdBy = this[Blogs.createdBy],
        modifiedBy = this[Blogs.modifiedBy],
        timeOfCreation = this[Blogs.timeOfCreation],
        timeOfModification = this[Blogs.timeOfModification],
        title = this[Blogs.title],
        userId = this[Blogs.userId],
    )

    private fun ResultRow.toBlogEntryDao(): BlogEntryDao = BlogEntryDao(
        id = this[BlogEntries.id].value,
        createdBy = this[BlogEntries.createdBy],
        timeOfCreation = this[BlogEntries.timeOfCreation],
        modifiedBy = this[BlogEntries.modifiedBy],
        timeOfModification = this[BlogEntries.timeOfModification],
        creatorName = this[BlogEntries.creatorName],
        entry = this[BlogEntries.entry],
        blogId = this[BlogEntries.blogId]
    )
}
