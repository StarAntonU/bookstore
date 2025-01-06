package mate.academy.repository.book;

import mate.academy.model.Book;
import mate.academy.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    private final String AUTHOR = "author";
    @Override
    public String getKey() {
        return AUTHOR;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get(AUTHOR).in(Arrays.stream(params).toArray());
    }
}
