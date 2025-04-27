package project.bookstore.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookSearchParametersDto;
import project.bookstore.dto.book.CreateBookRequestDto;
import project.bookstore.exception.unchecked.EntityNotFoundException;
import project.bookstore.mapper.BookMapper;
import project.bookstore.model.Book;
import project.bookstore.model.Category;
import project.bookstore.repository.book.BookRepository;
import project.bookstore.repository.book.BookSpecificationBuilder;
import project.bookstore.repository.category.CategoryRepository;
import project.bookstore.service.impl.BookServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @Mock
    private Specification specification;

    @Test
    @DisplayName("Verify method findBookById with correct data")
    public void findBookById_CorrectBookId_ReturnValidBookDto() {
        long bookId = 1L;
        Book book = createBook(bookId);
        BookDto expected = mapBookToBookDto(book);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.findBookById(bookId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName(""" 
            Verify method findBookById with incorrect data.
             Book with the id not exist
            """)
    public void findBookById_IncorrectBookId_ReturnException() {
        long bookId = 1000L;
        String expected = "Can`t find book by id " + bookId;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        Exception actual = Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.findBookById(bookId)
        );
        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Verify method save with correct data")
    public void save_CorrectBook_ReturnValidBookDto() {
        long categoryId = 1L;
        CreateBookRequestDto createBookDto = createBookRequestDto(categoryId);
        Book book = mapCreateBookToBook(createBookDto, categoryId);
        BookDto expected = mapBookToBookDto(book);
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(bookMapper.toModel(createBookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.save(createBookDto);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName(""" 
            Verify method save with incorrect data.
             Category with the id not exist
            """)
    public void save_IncorrectCategory_ReturnException() {
        long categoryId = 100L;
        String expected = "There categories are not exist " + List.of(categoryId);
        CreateBookRequestDto createBookDto = createBookRequestDto(categoryId);
        when(categoryRepository.existsById(categoryId)).thenReturn(false);
        Exception actual = Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.save(createBookDto));
        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Verify method findAll with correct data")
    public void findAll_CorrectDate_ReturnValidData() {
        Book book = createBook(1L);
        BookDto expected = mapBookToBookDto(book);
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        PageImpl<Book> bookPage = new PageImpl<>(books, pageable, books.size());
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(expected);
        List<BookDto> actual = bookService.findAll(pageable);
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(expected, actual.get(0));
    }

    @Test
    @DisplayName("Verify method update with correct data")
    public void update_CorrectBookData_ReturnValidData() {
        long id = 1L;
        CreateBookRequestDto createBookDto = createBookRequestDto(id);
        Book book = mapCreateBookToBook(createBookDto, id);
        BookDto expected = mapBookToBookDto(book);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.update(id, createBookDto);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName(""" 
            Verify method update with incorrect data.
             Book with the id not exist
            """)
    public void update_IncorrectBookId_ReturnException() {
        long id = 100L;
        String expected = "Can`t update book by id " + id;
        CreateBookRequestDto createBookDto = createBookRequestDto(id);
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        Exception actual = Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.update(id, createBookDto));
        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Verify method deleteById with correct data")
    public void deleteById_CorrectDta_NoReturn() {
        long id = 1L;
        when(bookRepository.existsById(id)).thenReturn(true);
        bookService.deleteById(id);
        verify(bookRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName(""" 
            Verify method delete with incorrect data.
             Book with the id not exist
            """)
    public void deleteById_IncorrectData_ReturnException() {
        long id = 1L;
        String expected = "Can`t delete book by id " + id;
        when(bookRepository.existsById(id)).thenReturn(false);
        Exception actual = Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.deleteById(id));
        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Verify method search with correct data")
    public void search_CorrectData_ReturnValidData() {
        BookSearchParametersDto params = createBookSearchParametersDto();
        specification = mock(Specification.class);
        Book book = createBook(1L);
        BookDto expected = mapBookToBookDto(book);
        when(bookSpecificationBuilder.build(params)).thenReturn(specification);
        when(bookRepository.findAll(specification)).thenReturn(List.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);
        List<BookDto> actual = bookService.search(params);
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(expected, actual.get(0));
    }

    private Book createBook(Long bookId) {
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

    private CreateBookRequestDto createBookRequestDto(Long categoryId) {
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

    private BookSearchParametersDto createBookSearchParametersDto() {
        return new BookSearchParametersDto(
                new String[1],
                new String[1],
                new String[1]
        );
    }

    private Book mapCreateBookToBook(CreateBookRequestDto createBookDto, Long categoryId) {
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

    private BookDto mapBookToBookDto(Book book) {
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
