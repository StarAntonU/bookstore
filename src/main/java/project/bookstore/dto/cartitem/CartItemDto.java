package project.bookstore.dto.cartitem;

public record CartItemDto(
        Long id,
        int bookId,
        String bookTitle,
        int quantity
) {
}
