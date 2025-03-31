package project.bookstore.repository.orderitem;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
