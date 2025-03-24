package project.bookstore.dto.category;

import jakarta.validation.constraints.Positive;

public record UpdateCartItemDto(
        @Positive
        int quantity
) {
}
