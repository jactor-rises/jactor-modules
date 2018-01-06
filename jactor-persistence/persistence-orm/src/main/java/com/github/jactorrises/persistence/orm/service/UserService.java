package com.github.jactorrises.persistence.orm.service;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.dto.UserDto;
import com.github.jactorrises.persistence.orm.dao.HibernateRepository;
import com.github.jactorrises.persistence.orm.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final HibernateRepository hibernateRepository;

    @Autowired
    public UserService(HibernateRepository hibernateRepository) {
        this.hibernateRepository = hibernateRepository;
    }

    @Transactional public Optional<UserDto> findUsing(UserName userName) {
        return hibernateRepository.findUsing(userName).map(UserEntity::asDto);
    }
}
