package project.bookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import project.bookstore.model.Order;

public record PatchOrderDto(
        @NotNull
        Order.Status status
) {
}
