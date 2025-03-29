package project.bookstore.repository.cartitem;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.bookstore.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("FROM CartItem  WHERE shoppingCart.id = :shoppingCartId AND book.id = :bookId")
    CartItem findByShoppingCartIdAndBookId(Long shoppingCartId, Long bookId);

    Optional<CartItem> findByIdAndShoppingCartId(Long cartItemId, Long shoppingCartId);
}
