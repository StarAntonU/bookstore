package project.bookstore.service;

import java.util.List;
import org.springframework.security.core.Authentication;
import project.bookstore.dto.order.CreateOrderRequestDto;
import project.bookstore.dto.order.OrderDto;

public interface OrderService {
    OrderDto createOrder(CreateOrderRequestDto requestDto,
                         Authentication authentication);

    List<OrderDto> viewOrders(Authentication authentication);
}
