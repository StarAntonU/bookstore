package project.bookstore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.bookstore.dto.order.CreateOrderRequestDto;
import project.bookstore.dto.order.OrderResponseDto;
import project.bookstore.service.OrderService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderResponseDto createOrder(
            @RequestBody CreateOrderRequestDto requestDto, Authentication authentication) {
        return orderService.createOrder(requestDto, authentication);
    }
}
