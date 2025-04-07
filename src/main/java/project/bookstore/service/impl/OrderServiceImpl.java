package project.bookstore.service.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import project.bookstore.dto.order.CreateOrderRequestDto;
import project.bookstore.dto.order.OrderDto;
import project.bookstore.dto.order.PatchOrderDto;
import project.bookstore.exception.unchecked.EntityNotFoundException;
import project.bookstore.mapper.OrderMapper;
import project.bookstore.model.CartItem;
import project.bookstore.model.Order;
import project.bookstore.model.OrderItem;
import project.bookstore.model.ShoppingCart;
import project.bookstore.model.User;
import project.bookstore.repository.order.OrderRepository;
import project.bookstore.repository.shoppingcart.ShoppingCartRepository;
import project.bookstore.service.OrderService;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    @Override
    public OrderDto createOrder(
            CreateOrderRequestDto requestDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ShoppingCart cart = shoppingCartRepository.findByUserId(user.getId());
        if (cart.getCartItems().isEmpty()) {
            throw new EntityNotFoundException("Cart is empty");
        }
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.NEW);
        order.setShippingAddress(requestDto.shippingAddress());
        Set<OrderItem> orderItem = createOrderItem(cart.getCartItems(), order);
        cart.clearCart();
        order.setOrderItems(orderItem);
        order.setTotal(countTotalPrice(order.getOrderItems()));
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> getOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderDto getOrderById(Long orderId, Long userId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        "Can`t find order by order id %s or user id %s", orderId, userId)));
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto changedStatus(Long id, PatchOrderDto requestDto, Long userId) {
        Order order = orderRepository.findByIdAndUserId(id, userId).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        "Can`t find order by order id %s and user id %s", id, userId)));
        order.setStatus(requestDto.status());
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    private Set<OrderItem> createOrderItem(Set<CartItem> cartItems, Order order) {
        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
            cartItem.setShoppingCart(null);
        }
        return orderItems;
    }

    private BigDecimal countTotalPrice(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
