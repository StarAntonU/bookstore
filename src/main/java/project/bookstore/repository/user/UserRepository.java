package project.bookstore.repository.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.bookstore.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("FROM User u WHERE u.email like :email")
    Optional<User> findByEmail(String email);
}
