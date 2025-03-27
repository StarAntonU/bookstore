package project.bookstore.service;

import org.springframework.security.core.Authentication;
import project.bookstore.dto.cartitem.CreateCartItemRequestDto;
import project.bookstore.dto.cartitem.UpdateCartItemDto;
import project.bookstore.dto.shoppingcart.ShoppingCartDto;
import project.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto addItem(CreateCartItemRequestDto requestDto, Authentication authentication);

    ShoppingCartDto updateCartItem(Long id,
                                   UpdateCartItemDto requestDto,
                                   Authentication authentication);

    ShoppingCartDto getShoppingCart(Authentication authentication);

    void delete(Long id);

    void createNewShoppingCart(User user);
}
