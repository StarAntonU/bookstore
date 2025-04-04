package project.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

@Tag(name = "Order", description = "Endpoints for managing orders")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Create order", description = "Create a new order")
    @PostMapping
    public OrderDto createOrder(
            @RequestBody CreateOrderRequestDto requestDto, Authentication authentication) {
        return orderService.createOrder(requestDto, authentication);
    }

    @Operation(summary = "View orders", description = "View all orders")
    @GetMapping
    public List<OrderDto> getOrders(Authentication authentication) {
        return orderService.getOrders(authentication);
    }

    @Operation(summary = "Get order", description = "Get one order by id")
    @GetMapping("/{orderId}/items")
    public OrderDto getOrderById(@PathVariable Long orderId, Authentication authentication) {
        return orderService.getOrderById(orderId, authentication);
    }

    @Operation(summary = "Get item", description = "Get one item in order by id")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getItemByIdInOrder(@PathVariable Long orderId,
                                           @PathVariable Long itemId,
                                           Authentication authentication) {
        return orderService.getItemByIdInOrder(orderId, itemId, authentication);
    }

    @Operation(summary = "Change status", description = "Change a status order")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public OrderDto changedStatus(@PathVariable Long id,
                                      @RequestBody PatchOrderDto requestDto,
                                      Authentication authentication) {
        return orderService.changedStatus(id, requestDto, authentication);
    }
}
