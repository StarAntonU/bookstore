package project.bookstore.repository.status;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.model.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByStatus(Status.StatusName status);
}
