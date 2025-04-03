package project.bookstore.repository.order;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"orderItems", "orderItems.book"})
    List<Order> findByUserId(Long id);
    @EntityGraph(attributePaths = {"orderItems", "orderItems.book"})
    Order findByIdAndUserId(Long orderId, Long userId);
}
