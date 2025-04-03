package project.bookstore.repository.orderitem;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOrderId(Long id);
}
