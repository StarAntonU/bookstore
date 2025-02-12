package project.bookstore.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookSearchParametersDto;
import project.bookstore.dto.book.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findBookById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto params);
}
