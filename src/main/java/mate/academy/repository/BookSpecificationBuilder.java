package mate.academy.repository;

import lombok.RequiredArgsConstructor;
import mate.academy.dto.BookSearchParametersDto;
import mate.academy.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;
    private final String TITLE = "title";
    private final String AUTHOR = "author";
    private final String PRICE = "price";

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.title() != null && searchParameters.title().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(TITLE)
                    .getSpecification(searchParameters.title()));
        }
        if (searchParameters.author() != null && searchParameters.author().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(AUTHOR)
                    .getSpecification(searchParameters.author()));
        }
        if (searchParameters.price() != null && searchParameters.price().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(PRICE)
                    .getSpecification((String[]) Arrays.stream(
                            searchParameters.price())
                    .map(BigDecimal::toPlainString)
                            .toArray()));
        }
        return spec;
    }
}
