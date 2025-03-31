package project.bookstore.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
