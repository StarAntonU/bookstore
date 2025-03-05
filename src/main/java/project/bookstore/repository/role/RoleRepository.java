package project.bookstore.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByName(Role.RoleName roleName);
}
