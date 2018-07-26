package com.gitlab.jactor.rises.persistence.service;

import com.gitlab.jactor.rises.commons.dto.BlogDto;
import com.gitlab.jactor.rises.commons.dto.BlogEntryDto;
import com.gitlab.jactor.rises.persistence.entity.blog.BlogEntity;
import com.gitlab.jactor.rises.persistence.entity.blog.BlogEntryEntity;
import com.gitlab.jactor.rises.persistence.repository.BlogEntryRepository;
import com.gitlab.jactor.rises.persistence.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogService {

    private final BlogEntryRepository blogEntryRepository;
    private final BlogRepository blogRepository;
    private final UserService userService;

    @Autowired
    public BlogService(BlogRepository blogRepository, BlogEntryRepository blogEntryRepository, UserService userService) {
        this.blogEntryRepository = blogEntryRepository;
        this.blogRepository = blogRepository;
        this.userService = userService;
    }

    public Optional<BlogDto> find(Long id) {
        return blogRepository.findById(id).map(BlogEntity::asDto);
    }

    public Optional<BlogEntryDto> findEntryBy(Long blogEntryId) {
        return blogEntryRepository.findById(blogEntryId).map(BlogEntryEntity::asDto);
    }

    public List<BlogDto> findBlogsBy(String title) {
        return blogRepository.findBlogsByTitle(title).stream()
                .map(BlogEntity::asDto)
                .collect(Collectors.toList());
    }

    public List<BlogEntryDto> findEntriesForBlog(Long blogId) {
        return blogEntryRepository.findByBlog_Id(blogId).stream()
                .map(BlogEntryEntity::asDto)
                .collect(Collectors.toList());
    }

    public BlogDto saveOrUpdate(BlogDto blogDto) {
        userService.find(fetchUsername(blogDto))
                .ifPresent(blogDto::setUser);

        BlogEntity blogEntity = new BlogEntity(blogDto);
        blogRepository.save(blogEntity);

        return blogEntity.asDto();
    }

    public BlogEntryDto saveOrUpdate(BlogEntryDto blogEntryDto) {
        userService.find(fetchUsername(blogEntryDto.getBlog()))
                .ifPresent(userDto -> blogEntryDto.getBlog().setUser(userDto));

        BlogEntryEntity blogEntryEntity = new BlogEntryEntity(blogEntryDto);
        blogEntryRepository.save(blogEntryEntity);

        return blogEntryEntity.asDto();
    }

    private String fetchUsername(BlogDto blogDto) {
        if (blogDto == null || blogDto.getUser() == null) {
            return null;
        }

        return blogDto.getUser().getUsername();
    }
}
