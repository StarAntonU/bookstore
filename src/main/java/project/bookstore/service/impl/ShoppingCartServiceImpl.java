package project.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.dto.cartitem.CreateCartItemRequestDto;
import project.bookstore.dto.cartitem.UpdateCartItemDto;
import project.bookstore.dto.shoppingcart.ShoppingCartDto;
import project.bookstore.exception.unchecked.EntityNotFoundException;
import project.bookstore.mapper.CartItemMapper;
import project.bookstore.mapper.ShoppingCartMapper;
import project.bookstore.model.CartItem;
import project.bookstore.model.ShoppingCart;
import project.bookstore.model.User;
import project.bookstore.repository.book.BookRepository;
import project.bookstore.repository.cartitem.CartItemRepository;
import project.bookstore.repository.shoppingcart.ShoppingCartRepository;
import project.bookstore.service.ShoppingCartService;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartDto addItem(
            CreateCartItemRequestDto requestDto, Authentication authentication) {
        Long userId = findUser(authentication);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
        CartItem cartItem = cartItemRepository.findByShoppingCartIdAndBookId(
                shoppingCart.getId(), requestDto.bookId()
        );
        if (cartItem != null) {
            UpdateCartItemDto cartItemDto = cartItemMapper.updateQuantity(requestDto);
            cartItemDto.setQuantity(cartItem.getQuantity() + requestDto.quantity());
            return updateCartItem(cartItem.getId(),
                    cartItemDto,
                    authentication);
        }
        cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(bookRepository.findById(requestDto.bookId()).orElseThrow(
                () -> new EntityNotFoundException("Can`t find book by id " + requestDto.bookId())
        ));
        cartItemRepository.saveAndFlush(cartItem);
        return shoppingCartMapper.toDto(shoppingCartRepository.findByUserId(userId));
    }

    @Override
    public ShoppingCartDto updateCartItem(Long id,
                                          UpdateCartItemDto requestDto,
                                          Authentication authentication) {
        Long userId = findUser(authentication);
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id, userId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find cart item by id " + id)
        );
        cartItem.setQuantity(requestDto.getQuantity());
        cartItemRepository.saveAndFlush(cartItem);
        return shoppingCartMapper.toDto(shoppingCartRepository.findByUserId(userId));
    }

    @Override
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void delete(Long id) {
        if (!cartItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Can`t find item by id " + id);
        }
        cartItemRepository.deleteById(id);
    }

    public ShoppingCart createNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCart;
    }

    private Long findUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
