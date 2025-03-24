package project.bookstore.dto.cartitem;

import jakarta.validation.constraints.Positive;

public record UpdateCartItemDto(
        @Positive
        int quantity
) {
}
