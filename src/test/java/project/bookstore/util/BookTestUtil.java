package project.bookstore.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookSearchParametersDto;
import project.bookstore.dto.book.CreateBookRequestDto;
import project.bookstore.model.Book;
import project.bookstore.model.Category;

@Component
public class BookTestUtil {
    public static CreateBookRequestDto createBookRequestDto(Long categoryId) {
        CreateBookRequestDto createBookDto = new CreateBookRequestDto();
        createBookDto.setTitle("Kobzar");
        createBookDto.setAuthor("Taras Shevchenko");
        createBookDto.setIsbn("1234567890");
        createBookDto.setPrice(BigDecimal.valueOf(123.45));
        createBookDto.setDescription("Good book");
        createBookDto.setCoverImage("Kobzar");
        createBookDto.setCategories(List.of(categoryId));
        return createBookDto;
    }

    public static CreateBookRequestDto createInvalidBookRequestDto() {
        CreateBookRequestDto createBookDto = new CreateBookRequestDto();
        createBookDto.setTitle("A");
        createBookDto.setAuthor("B");
        createBookDto.setIsbn("1234567890");
        createBookDto.setPrice(BigDecimal.valueOf(123.45));
        createBookDto.setDescription("Good book");
        createBookDto.setCoverImage("Kobzar");
        createBookDto.setCategories(List.of(1L));
        return createBookDto;
    }

    public static BookDto createBookDto() {
        BookDto book = new BookDto();
        book.setId(2L);
        book.setTitle("Kobzar2");
        book.setAuthor("Taras Shevchenko");
        book.setIsbn("12345678902");
        book.setPrice(BigDecimal.valueOf(22.34));
        book.setDescription("Good good book");
        book.setCoverImage("Kobzar2");
        book.setCategoriesIds(List.of(1L));
        return book;
    }

    public static BookDto[] createArrayBookDtos() {
        BookDto bookOne = new BookDto();
        bookOne.setId(1L);
        bookOne.setTitle("Kobzar");
        bookOne.setAuthor("Taras Shevchenko");
        bookOne.setIsbn("12345678901");
        bookOne.setPrice(BigDecimal.valueOf(123.45));
        bookOne.setDescription("Good book");
        bookOne.setCoverImage("Kobzar");
        bookOne.setCategoriesIds(List.of(1L));

        BookDto bookTwo = new BookDto();
        bookTwo.setId(2L);
        bookTwo.setTitle("Kobzar2");
        bookTwo.setAuthor("Taras Shevchenko");
        bookTwo.setIsbn("12345678902");
        bookTwo.setPrice(BigDecimal.valueOf(22.34));
        bookTwo.setDescription("Good good book");
        bookTwo.setCoverImage("Kobzar2");
        bookTwo.setCategoriesIds(List.of(1L));

        BookDto bookThree = new BookDto();
        bookThree.setId(3L);
        bookThree.setTitle("Kobzar3");
        bookThree.setAuthor("Taras Shevchenko");
        bookThree.setIsbn("12345678903");
        bookThree.setPrice(BigDecimal.valueOf(33.44));
        bookThree.setDescription("Very good book");
        bookThree.setCoverImage("Kobzar3");
        bookThree.setCategoriesIds(List.of(2L));

        BookDto bookFour = new BookDto();
        bookFour.setId(4L);
        bookFour.setTitle("Kobzar4");
        bookFour.setAuthor("Taras Shevchenko");
        bookFour.setIsbn("12345678904");
        bookFour.setPrice(BigDecimal.valueOf(44.37));
        bookFour.setDescription("So good book");
        bookFour.setCoverImage("Kobzar4");
        bookFour.setCategoriesIds(List.of(2L));
        return new BookDto[]{bookOne, bookTwo, bookThree, bookFour};
    }

    public static BookDto mapCreateBookDtoToBookDto(CreateBookRequestDto book, Long id) {
        BookDto bookDto = new BookDto();
        bookDto.setId(id);
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setCategoriesIds(book.getCategories());
        return bookDto;
    }

    public static Book createBook(Long bookId) {
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Kobzar");
        book.setAuthor("Taras Shevchenko");
        book.setIsbn("1234567890");
        book.setPrice(BigDecimal.valueOf(123.45));
        book.setDescription("Good book");
        book.setCoverImage("Kobzar");
        book.setCategories(Set.of());
        return book;
    }

    public static BookSearchParametersDto createBookSearchParametersDto() {
        return new BookSearchParametersDto(
                new String[1],
                new String[1],
                new String[1]
        );
    }

    public static Book mapCreateBookToBook(CreateBookRequestDto createBookDto, Long categoryId) {
        Book book = new Book();
        book.setId(1L);
        book.setTitle(createBookDto.getTitle());
        book.setAuthor(createBookDto.getAuthor());
        book.setIsbn(createBookDto.getIsbn());
        book.setPrice(createBookDto.getPrice());
        book.setDescription(createBookDto.getDescription());
        book.setCoverImage(createBookDto.getCoverImage());
        book.setCategories(Set.of(new Category(categoryId)));
        return book;
    }

    public static BookDto mapBookToBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setCategoriesIds(List.of(1L));
        return bookDto;
    }
}
