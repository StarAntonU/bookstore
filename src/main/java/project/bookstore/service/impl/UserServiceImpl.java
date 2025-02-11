package project.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.bookstore.dto.user.UserRegistrationRequestDto;
import project.bookstore.dto.user.UserResponseDto;
import project.bookstore.mapper.UserMapper;
import project.bookstore.model.User;
import project.bookstore.repository.user.UserRepository;
import project.bookstore.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        User user = userRepository.save(userMapper.toModel(requestDto));
        return userMapper.toResponseDto(user);
    }
}
