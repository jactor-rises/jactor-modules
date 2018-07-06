package com.github.jactor.rises.persistence.repository;

import com.github.jactor.rises.persistence.entity.blog.BlogEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BlogRepository extends CrudRepository<BlogEntity, Long> {
    List<BlogEntity> findBlogsByTitle(String title);
}
