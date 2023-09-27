package com.github.jactor.persistence.service

import org.springframework.stereotype.Service
import com.github.jactor.persistence.dto.BlogDto
import com.github.jactor.persistence.dto.BlogEntryDto
import com.github.jactor.persistence.entity.BlogEntity
import com.github.jactor.persistence.entity.BlogEntryEntity
import com.github.jactor.persistence.repository.BlogEntryRepository
import com.github.jactor.persistence.repository.BlogRepository

@Service
class BlogService(
    private val blogRepository: BlogRepository,
    private val blogEntryRepository: BlogEntryRepository,
    private val userService: UserService
) {
    fun find(id: Long): BlogDto? {
        return blogRepository.findById(id)
            .map { it.asDto() }
            .orElse(null)
    }

    fun findEntryBy(blogEntryId: Long): BlogEntryDto? {
        return blogEntryRepository.findById(blogEntryId)
            .map { it.asDto() }
            .orElse(null)
    }

    fun findBlogsBy(title: String?): List<BlogDto> {
        return blogRepository.findBlogsByTitle(title).map { obj: BlogEntity? -> obj?.asDto()!! }
    }

    fun findEntriesForBlog(blogId: Long?): List<BlogEntryDto> {
        return blogEntryRepository.findByBlog_Id(blogId).map { obj: BlogEntryEntity? -> obj?.asDto()!! }
    }

    fun saveOrUpdate(blogDto: BlogDto): BlogDto {
        val userDto = userService.find(username = fetchUsername(blogDto))
        blogDto.userInternal = userDto

        return blogRepository.save(BlogEntity(blogDto)).asDto()
    }

    fun saveOrUpdate(blogEntryDto: BlogEntryDto): BlogEntryDto {
        val userDto = userService.find(username = fetchUsername(blogEntryDto.blog))
        blogEntryDto.blog!!.userInternal = userDto

        val blogEntryEntity = BlogEntryEntity(blogEntryDto)

        return blogEntryRepository.save(blogEntryEntity).asDto()
    }

    private fun fetchUsername(blogDto: BlogDto?): String {
        return blogDto?.userInternal?.username ?: throw IllegalStateException("Unnable to find username in $blogDto")
    }
}
