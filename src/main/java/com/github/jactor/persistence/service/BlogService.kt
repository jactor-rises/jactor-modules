package com.github.jactor.persistence.service

import com.github.jactor.persistence.dto.BlogDto
import com.github.jactor.persistence.dto.BlogEntryDto
import com.github.jactor.persistence.dto.UserInternalDto
import com.github.jactor.persistence.entity.BlogEntity
import com.github.jactor.persistence.entity.BlogEntryEntity
import com.github.jactor.persistence.repository.BlogEntryRepository
import com.github.jactor.persistence.repository.BlogRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class BlogService(
    private val blogRepository: BlogRepository,
    private val blogEntryRepository: BlogEntryRepository,
    private val userService: UserService
) {
    fun find(id: Long): Optional<BlogDto> {
        return blogRepository.findById(id).map { blog: BlogEntity? -> blog?.asDto() }
    }

    fun findEntryBy(blogEntryId: Long): Optional<BlogEntryDto> {
        return blogEntryRepository.findById(blogEntryId).map { entry: BlogEntryEntity? -> entry?.asDto() }
    }

    fun findBlogsBy(title: String?): List<BlogDto> {
        return blogRepository.findBlogsByTitle(title)?.map { obj: BlogEntity? -> obj?.asDto()!! } ?: emptyList()
    }

    fun findEntriesForBlog(blogId: Long?): List<BlogEntryDto> {
        return blogEntryRepository.findByBlog_Id(blogId)?.map { obj: BlogEntryEntity? -> obj?.asDto()!! } ?: emptyList()
    }

    fun saveOrUpdate(blogDto: BlogDto?): BlogDto {
        userService.find(fetchUsername(blogDto))
            .ifPresent { userDto: UserInternalDto? -> blogDto!!.userInternal = userDto }

        val blogEntity = BlogEntity(blogDto)

        return blogRepository.save(blogEntity).asDto()
    }

    fun saveOrUpdate(blogEntryDto: BlogEntryDto): BlogEntryDto {
        userService.find(fetchUsername(blogEntryDto.blog))
            .ifPresent { userDto: UserInternalDto? -> blogEntryDto.blog!!.userInternal = userDto }

        val blogEntryEntity = BlogEntryEntity(blogEntryDto)

        return blogEntryRepository.save(blogEntryEntity).asDto()
    }

    private fun fetchUsername(blogDto: BlogDto?): String? {
        return if (blogDto?.userInternal != null) blogDto.userInternal!!.username else null
    }
}
