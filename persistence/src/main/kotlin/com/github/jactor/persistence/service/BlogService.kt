package com.github.jactor.persistence.service

import java.util.UUID
import org.springframework.stereotype.Service
import com.github.jactor.persistence.dto.BlogDto
import com.github.jactor.persistence.dto.BlogEntryDto
import com.github.jactor.persistence.entity.BlogEntity
import com.github.jactor.persistence.entity.BlogEntryEntity
import com.github.jactor.persistence.repository.BlogEntryRepository
import com.github.jactor.persistence.repository.BlogRepository

interface BlogService {
    fun find(id: UUID): BlogDto?
    fun findBlogsBy(title: String?): List<BlogDto>
    fun findEntriesForBlog(blogId: UUID?): List<BlogEntryDto>
    fun findEntryBy(blogEntryId: UUID): BlogEntryDto?
    fun saveOrUpdate(blogDto: BlogDto): BlogDto
    fun saveOrUpdate(blogEntryDto: BlogEntryDto): BlogEntryDto
}

@Service
class DefaultBlogService(
    private val blogRepository: BlogRepository,
    private val blogEntryRepository: BlogEntryRepository,
    private val userService: UserService
) : BlogService {
    override fun find(id: UUID): BlogDto? {
        return blogRepository.findById(id)
            .map { it.asDto() }
            .orElse(null)
    }

    override fun findEntryBy(blogEntryId: UUID): BlogEntryDto? {
        return blogEntryRepository.findById(blogEntryId)
            .map { it.asDto() }
            .orElse(null)
    }

    override fun findBlogsBy(title: String?): List<BlogDto> {
        return blogRepository.findBlogsByTitle(title).map { obj: BlogEntity? -> obj?.asDto()!! }
    }

    override fun findEntriesForBlog(blogId: UUID?): List<BlogEntryDto> {
        return blogEntryRepository.findByBlog_Id(blogId).map { obj: BlogEntryEntity? -> obj?.asDto()!! }
    }

    override fun saveOrUpdate(blogDto: BlogDto): BlogDto {
        val userDto = userService.find(username = fetchUsername(blogDto))
        blogDto.userInternal = userDto

        return blogRepository.save(BlogEntity(blogDto)).asDto()
    }

    override fun saveOrUpdate(blogEntryDto: BlogEntryDto): BlogEntryDto {
        val userDto = userService.find(username = fetchUsername(blogEntryDto.blog))
        blogEntryDto.blog!!.userInternal = userDto

        val blogEntryEntity = BlogEntryEntity(blogEntryDto)

        return blogEntryRepository.save(blogEntryEntity).asDto()
    }

    private fun fetchUsername(blogDto: BlogDto?): String {
        return blogDto?.userInternal?.username ?: throw IllegalStateException("Unnable to find username in $blogDto")
    }
}
