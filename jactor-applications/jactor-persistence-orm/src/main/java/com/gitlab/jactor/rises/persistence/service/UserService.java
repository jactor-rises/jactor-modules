package com.gitlab.jactor.rises.persistence.service;

import com.gitlab.jactor.rises.commons.dto.UserDto;
import com.gitlab.jactor.rises.persistence.entity.user.UserEntity;
import com.gitlab.jactor.rises.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<String> findUsernamesOnActiveUsers() {
        return userRepository.findByInactiveOrderByUsername(false).stream()
                .map(UserRepository.UsernameProjection::getUsername)
                .collect(Collectors.toList());
    }
}
