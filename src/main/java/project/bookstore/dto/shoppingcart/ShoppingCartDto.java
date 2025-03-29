package project.bookstore.dto.shoppingcart;

import java.util.Set;
import project.bookstore.dto.cartitem.CartItemDto;

public record ShoppingCartDto(
        Long id,
        Long userId,
        Set<CartItemDto> cartItems
) {
}
