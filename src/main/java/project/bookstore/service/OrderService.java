package project.bookstore.service;

import java.util.List;
import org.springframework.security.core.Authentication;
import project.bookstore.dto.order.CreateOrderRequestDto;
import project.bookstore.dto.order.OrderDto;
import project.bookstore.dto.order.PatchOrderDto;
import project.bookstore.dto.orderitem.OrderItemDto;

public interface OrderService {
    OrderDto createOrder(CreateOrderRequestDto requestDto,
                         Authentication authentication);

    List<OrderDto> viewOrders(Authentication authentication);

    OrderDto getOrderById(Long orderId, Authentication authentication);

    OrderItemDto getItemByIdInOrder(Long orderId, Long itemId, Authentication authentication);

    OrderDto changedStatus(Long id, PatchOrderDto requestDto, Authentication authentication);
}
