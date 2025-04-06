package project.bookstore.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import project.bookstore.dto.order.CreateOrderRequestDto;
import project.bookstore.dto.order.OrderDto;
import project.bookstore.dto.order.PatchOrderDto;
import project.bookstore.dto.orderitem.OrderItemDto;
import project.bookstore.exception.unchecked.EntityNotFoundException;
import project.bookstore.mapper.OrderItemMapper;
import project.bookstore.mapper.OrderMapper;
import project.bookstore.model.CartItem;
import project.bookstore.model.Order;
import project.bookstore.model.OrderItem;
import project.bookstore.model.ShoppingCart;
import project.bookstore.model.User;
import project.bookstore.repository.order.OrderRepository;
import project.bookstore.repository.orderitem.OrderItemRepository;
import project.bookstore.repository.shoppingcart.ShoppingCartRepository;
import project.bookstore.service.OrderService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderDto createOrder(
            CreateOrderRequestDto requestDto, Authentication authentication) {
        User user = findUser(authentication);
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
        OrderDto dto = orderMapper.toDto(orderRepository.save(order));
        dto.setOrderItems(changedOrderItToOrderItDto(orderItem));
        return dto;
    }

    @Override
    public List<OrderDto> getOrders(Authentication authentication) {
        User user = findUser(authentication);
        List<Order> orders = orderRepository.findByUserId(user.getId());
        List<OrderDto> orderDtos = orders
                .stream()
                .map(orderMapper::toDto)
                .toList();
        for (OrderDto orderDto : orderDtos) {
            Set<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderDto.getId());
            orderDto.setOrderItems(changedOrderItToOrderItDto(orderItems));
        }
        return orderDtos;
    }

    @Override
    public OrderDto getOrderById(Long orderId, Authentication authentication) {
        User user = findUser(authentication);
        List<Order> orders = orderRepository.findByUserId(user.getId());
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                OrderDto dto = orderMapper.toDto(order);
                Set<OrderItem> orderItems = orderItemRepository.findAllByOrderId((orderId));
                dto.setOrderItems(changedOrderItToOrderItDto(orderItems));
                return dto;
            }
        }
        throw new EntityNotFoundException("Can`t find order by id " + orderId);
    }

    @Override
    public OrderItemDto getItemByIdInOrder(
            Long orderId, Long itemId, Authentication authentication) {
        OrderDto order = getOrderById(orderId, authentication);
        return order.getOrderItems()
                .stream()
                .filter(o -> o.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t find order item by id " + itemId));
    }

    @Override
    public OrderDto changedStatus(
            Long id, PatchOrderDto requestDto, Authentication authentication) {
        User user = findUser(authentication);
        Order order = orderRepository.findByIdAndUserId(id, user.getId());
        order.setStatus(requestDto.status());
        orderRepository.save(order);
        Set<OrderItem> orderItems = orderItemRepository.findAllByOrderId(id);
        OrderDto dto = orderMapper.toDto(order);
        dto.setOrderItems(changedOrderItToOrderItDto(orderItems));
        return dto;
    }

    private Set<OrderItemDto> changedOrderItToOrderItDto(Set<OrderItem> orderItems) {
        return orderItems
                .stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
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

    private User findUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
