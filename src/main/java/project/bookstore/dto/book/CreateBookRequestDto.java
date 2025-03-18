package project.bookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank
    @Size(min = 3, max = 64)
    private String title;
    @NotBlank
    @Size(min = 3, max = 64)
    private String author;
    @NotBlank
    @Size(min = 10, max = 64)
    private String isbn;
    @NotNull
    @Positive
    private BigDecimal price;
    @Size(min = 1000)
    private String description;
    private String coverImage;
    @NotBlank
    private List<Long> categories;
}
