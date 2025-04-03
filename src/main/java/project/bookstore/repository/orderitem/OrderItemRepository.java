package project.bookstore.repository.orderitem;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Set<OrderItem> findAllByOrderId(Long id);
}
