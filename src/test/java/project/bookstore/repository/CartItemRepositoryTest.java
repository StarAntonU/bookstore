package project.bookstore.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import project.bookstore.model.CartItem;
import project.bookstore.repository.cartitem.CartItemRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartItemRepositoryTest {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    @DisplayName("Verify custom sql query findByShoppingCartIdAndBookId with correct data")
    @Sql(scripts = "classpath:database/cartitem/add-cart_item-to-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/cartitem/delete-cart_item-from-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findByShoppingCartIdAndBookId_CorrectData_ReturnCartItem() {
        CartItem actual = cartItemRepository.findByShoppingCartIdAndBookId(1L, 1L);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(1L, actual.getShoppingCart().getId());
        Assertions.assertEquals(1L, actual.getBook().getId());
        Assertions.assertEquals(2, actual.getQuantity());
    }
}
