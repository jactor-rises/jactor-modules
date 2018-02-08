package com.github.jactorrises.persistence.orm.service;

import com.github.jactor.rises.client.dto.GuestBookDto;
import com.github.jactor.rises.client.dto.GuestBookEntryDto;
import com.github.jactorrises.persistence.orm.dao.HibernateRepository;
import com.github.jactorrises.persistence.orm.entity.guestbook.GuestBookEntity;
import com.github.jactorrises.persistence.orm.entity.guestbook.GuestBookEntryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuestBookService {
    private final HibernateRepository hibernateRepository;

    @Autowired
    public GuestBookService(HibernateRepository hibernateRepository) {
        this.hibernateRepository = hibernateRepository;
    }

    public GuestBookDto fetch(Long id) {
        return hibernateRepository.fetch(GuestBookEntity.class, id).asDto();
    }

    public GuestBookDto saveOrUpdate(GuestBookDto guestBookDto) {
        return hibernateRepository.saveOrUpdate(new GuestBookEntity(guestBookDto)).asDto();
    }

    public GuestBookEntryDto saveOrUpdate(GuestBookEntryDto guestBookEntryDto) {
        return hibernateRepository.saveOrUpdate(new GuestBookEntryEntity(guestBookEntryDto)).asDto();
    }
}
