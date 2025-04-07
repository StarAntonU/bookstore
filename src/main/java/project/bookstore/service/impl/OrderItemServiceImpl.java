package project.bookstore.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.bookstore.dto.order.OrderDto;
import project.bookstore.dto.orderitem.OrderItemDto;
import project.bookstore.exception.unchecked.EntityNotFoundException;
import project.bookstore.service.OrderItemService;
import project.bookstore.service.OrderService;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderService orderService;

    @Override
    public OrderItemDto getItemByIdInOrder(Long orderId, Long itemId, Long userId) {
        OrderDto order = orderService.getOrderById(orderId, userId);
        return order.orderItems()
                .stream()
                .filter(o -> o.id().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Can`t find order %s or item %s", orderId, itemId)));
    }
}
