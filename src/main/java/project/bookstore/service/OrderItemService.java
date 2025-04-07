package project.bookstore.service;

import project.bookstore.dto.orderitem.OrderItemDto;

public interface OrderItemService {
    OrderItemDto getItemByIdInOrder(Long orderId, Long itemId, Long userId);
}
