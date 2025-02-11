package project.bookstore.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
