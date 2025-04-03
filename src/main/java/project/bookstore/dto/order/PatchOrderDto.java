package project.bookstore.dto.order;

import project.bookstore.model.Order;

public record PatchOrderDto(
        Order.Status status
) {
}
