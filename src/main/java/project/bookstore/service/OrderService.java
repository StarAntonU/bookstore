package project.bookstore.service;

import java.util.List;
import project.bookstore.dto.order.CreateOrderRequestDto;
import project.bookstore.dto.order.OrderDto;
import project.bookstore.dto.order.PatchOrderDto;
import project.bookstore.dto.orderitem.OrderItemDto;

public interface OrderService {
    OrderDto createOrder(CreateOrderRequestDto requestDto,
                         Long userId);

    List<OrderDto> getOrders(Long userId);

    OrderDto getOrderById(Long orderId, Long userId);

    OrderItemDto getItemByIdInOrder(Long orderId, Long itemId, Long userId);

    OrderDto changedStatus(Long id, PatchOrderDto requestDto);
}
