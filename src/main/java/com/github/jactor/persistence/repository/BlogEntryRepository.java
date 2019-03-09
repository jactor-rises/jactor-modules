package com.github.jactor.persistence.repository;

import com.github.jactor.persistence.entity.blog.BlogEntryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BlogEntryRepository extends CrudRepository<BlogEntryEntity, Long> {
    @SuppressWarnings("all") List<BlogEntryEntity> findByBlog_Id(Long blogId); // the underscore in determined by spring jpa
}
