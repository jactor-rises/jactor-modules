package com.github.jactor.rises.persistence.service;

import com.github.jactor.rises.client.dto.NewBlogDto;
import com.github.jactor.rises.client.dto.NewBlogEntryDto;
import com.github.jactor.rises.persistence.entity.blog.BlogEntity;
import com.github.jactor.rises.persistence.entity.blog.BlogEntryEntity;
import com.github.jactor.rises.persistence.repository.BlogEntryRepository;
import com.github.jactor.rises.persistence.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BlogService {

    private final BlogEntryRepository blogEntryRepository;
    private final BlogRepository blogRepository;

    @Autowired
    public BlogService(BlogRepository blogRepository, BlogEntryRepository blogEntryRepository) {
        this.blogEntryRepository = blogEntryRepository;
        this.blogRepository = blogRepository;
    }

    public Optional<NewBlogDto> find(Long id) {
        return blogRepository.findById(id).map(BlogEntity::asDto);
    }

    public Optional<NewBlogEntryDto> findEntryBy(Long blogEntryId) {
        return blogEntryRepository.findById(blogEntryId).map(BlogEntryEntity::asDto);
    }

    public List<NewBlogDto> findBlogsBy(String title) {
        return blogRepository.findBlogsByTitle(title).stream()
                .map(BlogEntity::asDto)
                .collect(Collectors.toList());
    }

    public List<NewBlogEntryDto> findEntriesForBlog(Long blogId) {
        return blogEntryRepository.findByBlog_Id(blogId).stream()
                .map(BlogEntryEntity::asDto)
                .collect(Collectors.toList());
    }

    public NewBlogDto saveOrUpdate(NewBlogDto blogDto) {
        BlogEntity blogEntity = new BlogEntity(blogDto);
        blogRepository.save(blogEntity);

        return blogEntity.asDto();
    }

    public NewBlogEntryDto saveOrUpdate(NewBlogEntryDto blogEntryDto) {
        BlogEntryEntity blogEntryEntity = new BlogEntryEntity(blogEntryDto);
        blogEntryRepository.save(blogEntryEntity);

        return blogEntryEntity.asDto();
    }
}
