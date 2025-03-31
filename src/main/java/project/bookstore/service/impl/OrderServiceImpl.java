package project.bookstore.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import project.bookstore.dto.order.CreateOrderRequestDto;
import project.bookstore.dto.order.OrderResponseDto;
import project.bookstore.exception.unchecked.DataProcessingException;
import project.bookstore.exception.unchecked.EntityNotFoundException;
import project.bookstore.mapper.OrderItemMapper;
import project.bookstore.mapper.OrderMapper;
import project.bookstore.model.Order;
import project.bookstore.model.OrderItem;
import project.bookstore.model.ShoppingCart;
import project.bookstore.model.Status;
import project.bookstore.model.User;
import project.bookstore.repository.order.OrderRepository;
import project.bookstore.repository.orderitem.OrderItemRepository;
import project.bookstore.repository.shoppingcart.ShoppingCartRepository;
import project.bookstore.repository.status.StatusRepository;
import project.bookstore.service.OrderService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final StatusRepository statusRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderResponseDto createOrder(
            CreateOrderRequestDto requestDto, Authentication authentication) {
        User user = findUser(authentication);
        ShoppingCart cart = shoppingCartRepository.findByUserId(user.getId());
        final Order order = new Order();
        List<OrderItem> orderItems = cart.getCartItems()
                .stream()
                .map(orderItemMapper::toOrderItem)
                .peek(o -> o.setId(null))
                .peek(o -> o.setOrder(order))
                .peek(order::addOrderItemToOrder)
                .toList();
        return orderMapper.toDto(
                orderRepository.save(buildOrder(user, orderItems, requestDto, order)));
    }

    private User findUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    private Order buildOrder(
            User user, List<OrderItem> orderItem,
            CreateOrderRequestDto requestDto, Order order) {
        order.setUser(user);
        Status status = statusRepository.findByStatus(Status.StatusName.NEW)
                .orElseThrow(() -> new EntityNotFoundException("Can`t find status "
                        + Status.StatusName.NEW));
        order.setStatus(status);
        order.setTotal(countTotalPrice(orderItem));
        order.setOrderDate(LocalDateTime.now());
        order.setOrderItems(new HashSet<>(orderItem));
        order.setShippingAddress(requestDto.shippingAddress());
        return order;
    }

    private BigDecimal countTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(o -> o.getPrice().multiply(new BigDecimal(o.getQuantity())))
                .reduce(BigDecimal::add)
                .orElseThrow(() -> new DataProcessingException("List order items is empty"));
    }
}
