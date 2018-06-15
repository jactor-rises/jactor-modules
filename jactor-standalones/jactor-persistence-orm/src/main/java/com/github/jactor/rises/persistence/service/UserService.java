package com.github.jactor.rises.persistence.service;

import com.github.jactor.rises.client.dto.UserDto;
import com.github.jactor.rises.persistence.entity.user.UserEntity;
import com.github.jactor.rises.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserDto> find(String username) {
        return userRepository.findByUsername(username).map(UserEntity::asDto);
    }

    public Optional<UserDto> find(Long id) {
        return userRepository.findById(id).map(UserEntity::asDto);
    }

    public UserDto saveOrUpdate(UserDto userDto) {
        UserEntity userEntity = new UserEntity(userDto);
        userRepository.save(userEntity);

        return userEntity.asDto();
    }
}
