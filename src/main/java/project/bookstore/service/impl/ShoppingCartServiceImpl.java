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
        User user = findUser(authentication);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        CartItem cartItem = cartItemRepository.findByShoppingCartIdAndBookId(
                shoppingCart.getId(), requestDto.bookId()
        );
        if (cartItem != null) {
            return updateCartItem(cartItem.getId(),
                    cartItemMapper.updateQuantity(requestDto),
                    authentication);
        }
        cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(bookRepository.findById(requestDto.bookId()).orElseThrow(
                () -> new EntityNotFoundException("Can`t find book by id " + requestDto.bookId())
        ));
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCartRepository.findByUserId(user.getId()));
    }

    @Override
    public ShoppingCartDto updateCartItem(Long id,
                                          UpdateCartItemDto requestDto,
                                          Authentication authentication) {
        User user = findUser(authentication);
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find cart item by id " + id)
        );
        cartItem.setQuantity(cartItem.getQuantity() + requestDto.quantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCartRepository.findByUserId(user.getId()));
    }

    @Override
    public ShoppingCartDto findAll(Authentication authentication) {
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

    private User findUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
