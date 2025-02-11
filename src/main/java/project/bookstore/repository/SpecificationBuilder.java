package project.bookstore.repository;

import org.springframework.data.jpa.domain.Specification;
import project.bookstore.dto.book.BookSearchParametersDto;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParametersDto searchParameters);
}
