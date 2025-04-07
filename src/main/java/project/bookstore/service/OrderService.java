package project.bookstore.service;

import java.util.List;
import org.springframework.security.core.Authentication;
import project.bookstore.dto.order.CreateOrderRequestDto;
import project.bookstore.dto.order.OrderDto;
import project.bookstore.dto.order.PatchOrderDto;

public interface OrderService {
    OrderDto createOrder(CreateOrderRequestDto requestDto,
                         Authentication authentication);

    List<OrderDto> getOrders(Long userId);

    OrderDto getOrderById(Long orderId, Long userId);

    OrderDto changedStatus(Long id, PatchOrderDto requestDto, Long userId);
}
