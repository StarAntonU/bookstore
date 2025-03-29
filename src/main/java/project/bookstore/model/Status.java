package project.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "statuses")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private StatusName status;

    public String getStatusName() {
        return status.name();
    }

    public enum StatusName {
        NEW,
        PAID,
        PACKED,
        IS_DELIVERED,
        DELIVERED,
        CANCELLED
    }
}
