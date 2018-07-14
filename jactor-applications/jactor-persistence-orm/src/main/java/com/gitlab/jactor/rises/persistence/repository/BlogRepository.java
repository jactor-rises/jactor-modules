package com.gitlab.jactor.rises.persistence.repository;

import com.gitlab.jactor.rises.persistence.entity.blog.BlogEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BlogRepository extends CrudRepository<BlogEntity, Long> {
    List<BlogEntity> findBlogsByTitle(String title);
}
