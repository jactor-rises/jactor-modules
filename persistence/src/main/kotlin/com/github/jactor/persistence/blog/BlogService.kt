package com.github.jactor.persistence.blog

import java.util.UUID
import org.springframework.stereotype.Service
import com.github.jactor.persistence.repository.BlogEntryRepository
import com.github.jactor.persistence.repository.BlogRepository
import com.github.jactor.persistence.service.UserService

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
            .map { it.toModel() }
            .orElse(null)
    }

    override fun findEntryBy(blogEntryId: UUID): BlogEntryModel? {
        return blogEntryRepository.findById(blogEntryId)
            .map { it.toModel() }
            .orElse(null)
    }

    override fun findBlogsBy(title: String?): List<BlogModel> {
        return blogRepository.findBlogsByTitle(title).map { obj: BlogEntity? -> obj?.toModel()!! }
    }

    override fun findEntriesForBlog(blogId: UUID?): List<BlogEntryModel> {
        return blogEntryRepository.findByBlog_Id(blogId).map { obj: BlogEntryEntity? -> obj?.toModel()!! }
    }

    override fun saveOrUpdate(blogModel: BlogModel): BlogModel {
        val userModel = userService.find(username = fetchUsername(blogModel))

        return blogRepository.save(
            BlogEntity(blogModel.copy(user = userModel))
        ).toModel()
    }

    override fun saveOrUpdate(blogEntryModel: BlogEntryModel): BlogEntryModel {
        blogEntryModel.blog?.also {
            it.id ?: error("An entry must belong to a persistent blog!")
        } ?: error("An entry must belong to a blog!")

        val blogEntryEntity = BlogEntryEntity(blogEntryModel)

        return blogEntryRepository.save(blogEntryEntity).toModel()
    }

    private fun fetchUsername(blogModel: BlogModel?): String {
        return blogModel?.user?.username
            ?: throw IllegalStateException("Unnable to find username in $blogModel")
    }
}
