package com.github.jactor.persistence.blog

import java.time.LocalDateTime
import java.util.UUID
import org.springframework.stereotype.Service

interface BlogService {
    suspend fun create(createBlogEntry: CreateBlogEntry): BlogEntry
    suspend fun find(id: UUID): Blog?
    suspend fun findBlogsBy(title: String): List<Blog>
    suspend fun findEntriesForBlog(blogId: UUID): List<BlogEntry>
    suspend fun findEntryBy(blogEntryId: UUID): BlogEntry?
    suspend fun saveOrUpdate(blog: Blog): Blog
    suspend fun saveOrUpdate(blogEntry: BlogEntry): BlogEntry
    suspend fun update(updateBlogTitle: UpdateBlogTitle): Blog
}

@Service
class BlogServiceImpl(private val blogRepository: BlogRepository) : BlogService {
    override suspend fun create(createBlogEntry: CreateBlogEntry): BlogEntry {
        val blogEntryDao = BlogEntryDao(
            id = null,
            createdBy = createBlogEntry.creatorName,
            creatorName = createBlogEntry.creatorName,
            blogId = createBlogEntry.blogId,
            entry = createBlogEntry.entry,
            modifiedBy = createBlogEntry.creatorName,
            timeOfCreation = LocalDateTime.now(),
            timeOfModification = LocalDateTime.now(),
        )

        return blogRepository.save(blogEntryDao = blogEntryDao).toBlogEntry()
    }

    override suspend fun find(id: UUID): Blog? = blogRepository.findBlogById(id)?.toBlog()
    override suspend fun findEntryBy(blogEntryId: UUID): BlogEntry? {
        return blogRepository.findBlogEntryById(blogEntryId)?.toBlogEntry()
    }

    override suspend fun findBlogsBy(title: String): List<Blog> = blogRepository.findBlogsByTitle(title)
        .map { it.toBlog() }

    override suspend fun findEntriesForBlog(blogId: UUID): List<BlogEntry> {
        return blogRepository.findBlogEntriesByBlogId(blogId).map { it.toBlogEntry() }
    }

    override suspend fun saveOrUpdate(blog: Blog): Blog = blogRepository.save(blog.toBlogDao()).toBlog()
    override suspend fun saveOrUpdate(blogEntry: BlogEntry): BlogEntry {
        return blogRepository.save(blogEntryDao = blogEntry.toBlogEntryDao()).toBlogEntry()
    }

    override suspend fun update(updateBlogTitle: UpdateBlogTitle): Blog {
        val blog = requireNotNull(blogRepository.findBlogById(updateBlogTitle.blogId)) { "Cannot find blog to update" }
            .apply { this.title = updateBlogTitle.title }

        return blogRepository.save(blogDao = blog).toBlog()
    }
}

