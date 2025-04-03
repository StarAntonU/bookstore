package project.bookstore.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.bookstore.dto.order.CreateOrderRequestDto;
import project.bookstore.dto.order.OrderDto;
import project.bookstore.dto.order.PatchOrderDto;
import project.bookstore.dto.orderitem.OrderItemDto;
import project.bookstore.service.OrderService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderDto createOrder(
            @RequestBody CreateOrderRequestDto requestDto, Authentication authentication) {
        return orderService.createOrder(requestDto, authentication);
    }

    @GetMapping
    public List<OrderDto> viewOrders(Authentication authentication) {
        return orderService.viewOrders(authentication);
    }

    @GetMapping("/{orderId}/items")
    public OrderDto getOrderById(@PathVariable Long orderId, Authentication authentication) {
        return orderService.getOrderById(orderId, authentication);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getItemByIdInOrder(@PathVariable Long orderId,
                                           @PathVariable Long itemId,
                                           Authentication authentication) {
        return orderService.getItemByIdInOrder(orderId, itemId, authentication);
    }

    @PatchMapping("/{id}")
    public OrderDto changedStatus(@PathVariable Long id,
                                      @RequestBody PatchOrderDto requestDto,
                                      Authentication authentication) {
        return orderService.changedStatus(id, requestDto, authentication);
    }
}
