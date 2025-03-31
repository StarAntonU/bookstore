package project.bookstore.service;

import org.springframework.security.core.Authentication;
import project.bookstore.dto.order.CreateOrderRequestDto;
import project.bookstore.dto.order.OrderResponseDto;

public interface OrderService {
    OrderResponseDto createOrder(CreateOrderRequestDto requestDto,
                                 Authentication authentication);
}
