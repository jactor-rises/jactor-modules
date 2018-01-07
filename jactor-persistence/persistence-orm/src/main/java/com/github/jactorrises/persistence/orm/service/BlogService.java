package com.github.jactorrises.persistence.orm.service;

import com.github.jactorrises.client.dto.BlogDto;
import com.github.jactorrises.client.dto.BlogEntryDto;
import com.github.jactorrises.persistence.orm.dao.HibernateRepository;
import com.github.jactorrises.persistence.orm.entity.blog.BlogEntity;
import com.github.jactorrises.persistence.orm.entity.blog.BlogEntryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogService {
    private final HibernateRepository hibernateRepository;

    @Autowired
    public BlogService(HibernateRepository hibernateRepository) {
        this.hibernateRepository = hibernateRepository;
    }

    public BlogDto fetch(Long id) {
        return hibernateRepository.fetch(BlogEntity.class, id).asDto();
    }

    public BlogDto saveOrUpdate(BlogDto blogDto) {
        return hibernateRepository.saveOrUpdate(new BlogEntity(blogDto)).asDto();
    }

    public BlogEntryDto saveOrUpdate(BlogEntryDto blogEntryDto) {
        return hibernateRepository.saveOrUpdate(new BlogEntryEntity(blogEntryDto)).asDto();
    }
}
