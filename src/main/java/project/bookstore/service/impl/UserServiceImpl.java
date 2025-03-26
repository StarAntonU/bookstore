package project.bookstore.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.dto.user.UserRegistrationRequestDto;
import project.bookstore.dto.user.UserResponseDto;
import project.bookstore.exception.checked.RegistrationException;
import project.bookstore.exception.unchecked.EntityNotFoundException;
import project.bookstore.mapper.UserMapper;
import project.bookstore.model.Role;
import project.bookstore.model.ShoppingCart;
import project.bookstore.model.User;
import project.bookstore.repository.role.RoleRepository;
import project.bookstore.repository.shoppingcart.ShoppingCartRepository;
import project.bookstore.repository.user.UserRepository;
import project.bookstore.service.ShoppingCartService;
import project.bookstore.service.UserService;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartService shoppingCartService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        checkIfUserExists(requestDto);
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role role = roleRepository.findByRole(Role.RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cen`t find role " + Role.RoleName.USER));
        user.setRoles(Set.of(role));
        ShoppingCart shoppingCart = shoppingCartService.createNewShoppingCart();
        shoppingCart.setUser(user);
        user = userRepository.save(user);
        shoppingCartRepository.save(shoppingCart);
        return userMapper.toResponseDto(user);
    }

    private void checkIfUserExists(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(
                    String.format("User with email %s is exist", requestDto.getEmail()));
        }
    }
}
