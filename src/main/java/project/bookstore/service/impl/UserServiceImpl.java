package project.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.bookstore.dto.user.UserRegistrationRequestDto;
import project.bookstore.dto.user.UserResponseDto;
import project.bookstore.exception.checked.RegistrationException;
import project.bookstore.mapper.UserMapper;
import project.bookstore.model.User;
import project.bookstore.repository.user.UserRepository;
import project.bookstore.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        checkedIfExistUser(requestDto);
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        return userMapper.toResponseDto(userRepository.save(user));
    }

    private void checkedIfExistUser(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(
                    String.format("User with email %s is exist", requestDto.getEmail()));
        }
    }
}
