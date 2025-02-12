package project.bookstore.repository.book;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import project.bookstore.dto.book.BookSearchParametersDto;
import project.bookstore.model.Book;
import project.bookstore.repository.SpecificationBuilder;
import project.bookstore.repository.SpecificationProviderManager;
import project.bookstore.repository.book.spec.AuthorSpecificationProvider;
import project.bookstore.repository.book.spec.IsbnSpecificationProvider;
import project.bookstore.repository.book.spec.TitleSpecificationProvider;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.title() != null && searchParameters.title().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(TitleSpecificationProvider.TITLE)
                    .getSpecification(searchParameters.title()));
        }
        if (searchParameters.author() != null && searchParameters.author().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(AuthorSpecificationProvider.AUTHOR)
                    .getSpecification(searchParameters.author()));
        }
        if (searchParameters.isbn() != null && searchParameters.isbn().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(IsbnSpecificationProvider.ISBN)
                    .getSpecification(searchParameters.isbn()));
        }
        return spec;
    }
}
