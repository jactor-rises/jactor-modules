package com.github.jactor.rises.persistence.orm.service;

import com.github.jactor.rises.client.dto.BlogDto;
import com.github.jactor.rises.client.dto.BlogEntryDto;
import com.github.jactor.rises.persistence.orm.dao.HibernateRepository;
import com.github.jactor.rises.persistence.orm.entity.blog.BlogEntity;
import com.github.jactor.rises.persistence.orm.entity.blog.BlogEntryEntity;
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
