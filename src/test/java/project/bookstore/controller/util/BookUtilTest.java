package project.bookstore.controller.util;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.CreateBookRequestDto;

@Component
public class BookUtilTest {
    public CreateBookRequestDto createBookRequestDto() {
        CreateBookRequestDto createBookDto = new CreateBookRequestDto();
        createBookDto.setTitle("Kobzar");
        createBookDto.setAuthor("Taras Shevchenko");
        createBookDto.setIsbn("1234567890");
        createBookDto.setPrice(BigDecimal.valueOf(123.45));
        createBookDto.setDescription("Good book");
        createBookDto.setCoverImage("Kobzar");
        createBookDto.setCategories(List.of(1L));
        return createBookDto;
    }

    public CreateBookRequestDto createInvalidBookRequestDto() {
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

    public BookDto createBookDto() {
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

    public BookDto[] createArrayBookDtos() {
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

    public BookDto mapCreateBookDtoToBookDto(CreateBookRequestDto book, Long id) {
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
}
