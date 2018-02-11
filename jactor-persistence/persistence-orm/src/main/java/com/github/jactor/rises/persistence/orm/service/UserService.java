package com.github.jactorrises.persistence.orm.service;

import com.github.jactor.rises.client.datatype.UserName;
import com.github.jactor.rises.client.dto.UserDto;
import com.github.jactorrises.persistence.orm.dao.HibernateRepository;
import com.github.jactorrises.persistence.orm.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final HibernateRepository hibernateRepository;

    @Autowired
    public UserService(HibernateRepository hibernateRepository) {
        this.hibernateRepository = hibernateRepository;
    }

    public Optional<UserDto> findUsing(UserName userName) {
        return hibernateRepository.findUsing(userName).map(UserEntity::asDto);
    }

    public UserDto fetch(Long id) {
        return hibernateRepository.fetch(UserEntity.class, id).asDto();
    }

    public UserDto saveOrUpdate(UserDto userDto) {
        return hibernateRepository.saveOrUpdate(new UserEntity(userDto)).asDto();
    }
}
