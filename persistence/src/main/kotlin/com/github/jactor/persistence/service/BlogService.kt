package com.github.jactor.persistence.service

import java.util.UUID
import org.springframework.stereotype.Service
import com.github.jactor.persistence.dto.BlogModel
import com.github.jactor.persistence.dto.BlogEntryModel
import com.github.jactor.persistence.entity.BlogEntity
import com.github.jactor.persistence.entity.BlogEntryEntity
import com.github.jactor.persistence.repository.BlogEntryRepository
import com.github.jactor.persistence.repository.BlogRepository

interface BlogService {
    fun find(id: UUID): BlogModel?
    fun findBlogsBy(title: String?): List<BlogModel>
    fun findEntriesForBlog(blogId: UUID?): List<BlogEntryModel>
    fun findEntryBy(blogEntryId: UUID): BlogEntryModel?
    fun saveOrUpdate(blogModel: BlogModel): BlogModel
    fun saveOrUpdate(blogEntryModel: BlogEntryModel): BlogEntryModel
}

@Service
class DefaultBlogService(
    private val blogRepository: BlogRepository,
    private val blogEntryRepository: BlogEntryRepository,
    private val userService: UserService
) : BlogService {
    override fun find(id: UUID): BlogModel? {
        return blogRepository.findById(id)
            .map { it.asDto() }
            .orElse(null)
    }

    override fun findEntryBy(blogEntryId: UUID): BlogEntryModel? {
        return blogEntryRepository.findById(blogEntryId)
            .map { it.asDto() }
            .orElse(null)
    }

    override fun findBlogsBy(title: String?): List<BlogModel> {
        return blogRepository.findBlogsByTitle(title).map { obj: BlogEntity? -> obj?.asDto()!! }
    }

    override fun findEntriesForBlog(blogId: UUID?): List<BlogEntryModel> {
        return blogEntryRepository.findByBlog_Id(blogId).map { obj: BlogEntryEntity? -> obj?.asDto()!! }
    }

    override fun saveOrUpdate(blogModel: BlogModel): BlogModel {
        val userDto = userService.find(username = fetchUsername(blogModel))
        blogModel.userInternal = userDto

        return blogRepository.save(BlogEntity(blogModel)).asDto()
    }

    override fun saveOrUpdate(blogEntryModel: BlogEntryModel): BlogEntryModel {
        blogEntryModel.blog?.also {
            it.id ?: error("An entry must belong to a persistent blog!")
        } ?: error("An entry must belong to a blog!")

        val blogEntryEntity = BlogEntryEntity(blogEntryModel)

        return blogEntryRepository.save(blogEntryEntity).asDto()
    }

    private fun fetchUsername(blogModel: BlogModel?): String {
        return blogModel?.userInternal?.username ?: throw IllegalStateException("Unnable to find username in $blogModel")
    }
}
