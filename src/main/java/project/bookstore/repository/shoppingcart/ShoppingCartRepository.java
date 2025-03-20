package project.bookstore.repository.shoppingcart;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.model.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
}
