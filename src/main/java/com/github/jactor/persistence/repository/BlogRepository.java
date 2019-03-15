package com.github.jactor.persistence.repository;

import com.github.jactor.persistence.entity.BlogEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BlogRepository extends CrudRepository<BlogEntity, Long> {
    List<BlogEntity> findBlogsByTitle(String title);
}
