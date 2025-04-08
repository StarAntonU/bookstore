package project.bookstore.service.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.bookstore.dto.order.CreateOrderRequestDto;
import project.bookstore.dto.order.OrderDto;
import project.bookstore.dto.order.PatchOrderDto;
import project.bookstore.dto.orderitem.OrderItemDto;
import project.bookstore.exception.unchecked.EntityNotFoundException;
import project.bookstore.exception.unchecked.OrderProcessingException;
import project.bookstore.mapper.OrderMapper;
import project.bookstore.model.CartItem;
import project.bookstore.model.Order;
import project.bookstore.model.OrderItem;
import project.bookstore.model.ShoppingCart;
import project.bookstore.model.User;
import project.bookstore.repository.order.OrderRepository;
import project.bookstore.repository.shoppingcart.ShoppingCartRepository;
import project.bookstore.repository.user.UserRepository;
import project.bookstore.service.OrderService;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public OrderDto createOrder(
            CreateOrderRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find user bu id " + userId)
        );
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId);
        if (cart.getCartItems().isEmpty()) {
            throw new OrderProcessingException("Cart is empty");
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
    public OrderItemDto getItemByIdInOrder(Long orderId, Long itemId, Long userId) {
        OrderDto order = getOrderById(orderId, userId);
        return order.orderItems()
                .stream()
                .filter(item -> item.id().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Can`t find order %s or item %s", orderId, itemId)));
    }

    @Override
    public OrderDto changedStatus(Long id, PatchOrderDto requestDto) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can`t find order by order id %s and user id %s" + id));
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
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
