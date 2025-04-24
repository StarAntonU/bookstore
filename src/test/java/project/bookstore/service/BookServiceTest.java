package project.bookstore.service;

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
import project.bookstore.dto.book.BookDto;
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

    @Test
    @DisplayName("Verify findBookById with valid data")
    public void findBookById_CorrectBookId_ReturnValidBookDto() {
        long bookId = 1L;
        Book book = createBook(bookId);
        BookDto bookDto = mapBookToBookDto(book);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        BookDto actual = bookService.findBookById(bookId);
        Assertions.assertEquals(bookDto, actual);
    }

    @Test
    @DisplayName("Return exception because book is not exist")
    public void findBookById_IncorrectBookId_ReturnException() {
        long bookId = 1000L;
        String textException = "Can`t find book by id " + bookId;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.findBookById(bookId)
        );
        Assertions.assertEquals(textException, exception.getMessage());
    }

    @Test
    @DisplayName("Verify save book with correct data")
    public void save_CorrectBook_ReturnValidBookDto() {
        long categoryId = 1L;
        CreateBookRequestDto createBook = createBookRequestDto(categoryId);
        Book book = mapCreateBookToBook(createBook, categoryId);
        BookDto bookDto = mapBookToBookDto(book);
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(bookMapper.toModel(createBook)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        BookDto actual = bookService.save(createBook);
        Assertions.assertEquals(bookDto, actual);
    }

    @Test
    @DisplayName("Return exception because category is not exist")
    public void save_IncorrectCategory_ReturnException() {
        long categoryId = 100L;
        String textException = "There categories are not exist " + List.of(categoryId);
        CreateBookRequestDto createBook = createBookRequestDto(categoryId);
        when(categoryRepository.existsById(categoryId)).thenReturn(false);
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.save(createBook));
        Assertions.assertEquals(textException, exception.getMessage());
    }

    @Test
    @DisplayName("Verify findAll when correct data return all books")
    public void findAll_CorrectDate_ReturnValidData() {
        Book book = createBook(1L);
        BookDto bookDto = mapBookToBookDto(book);
        Pageable pageable = PageRequest.of(0,10);
        List<Book> books = List.of(book);
        PageImpl<Book> bookPage = new PageImpl<>(books, pageable, books.size());
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        List<BookDto> bookDtos = bookService.findAll(pageable);
        Assertions.assertEquals(1, bookDtos.size());
        Assertions.assertEquals(bookDto, bookDtos.get(0));
    }

    @Test
    @DisplayName("Verify update when correct data")
    public void update_CorrectBookData_ReturnValidData() {
        long id = 1L;
        CreateBookRequestDto createBook = createBookRequestDto(id);
        Book book = mapCreateBookToBook(createBook, id);
        BookDto bookDto = mapBookToBookDto(book);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        BookDto actual = bookService.update(id, createBook);
        Assertions.assertEquals(bookDto, actual);
    }

    @Test
    @DisplayName("Return exception because book not exist")
    public void update_IncorrectBookId_ReturnException() {
        long id = 100L;
        String textException = "Can`t update book by id " + id;
        CreateBookRequestDto createBook = createBookRequestDto(id);
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.update(id, createBook));
        Assertions.assertEquals(textException, exception.getMessage());
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
        CreateBookRequestDto createBook = new CreateBookRequestDto();
        createBook.setTitle("Kobzar");
        createBook.setAuthor("Taras Shevchenko");
        createBook.setIsbn("1234567890");
        createBook.setPrice(BigDecimal.valueOf(123.45));
        createBook.setDescription("Good book");
        createBook.setCoverImage("Kobzar");
        createBook.setCategories(List.of(categoryId));
        return createBook;
    }

    private Book mapCreateBookToBook(CreateBookRequestDto createBook, Long categoryId) {
        Book book = new Book();
        book.setId(1L);
        book.setTitle(createBook.getTitle());
        book.setAuthor(createBook.getAuthor());
        book.setIsbn(createBook.getIsbn());
        book.setPrice(createBook.getPrice());
        book.setDescription(createBook.getDescription());
        book.setCoverImage(createBook.getCoverImage());
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
