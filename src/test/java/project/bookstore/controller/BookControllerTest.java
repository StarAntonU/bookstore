package project.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookSearchParametersDto;
import project.bookstore.dto.book.CreateBookRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method save with correct data")
    @Sql(scripts = {"classpath:db/category/add-categories-to-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/category/delete-categories-from-categories-table.sql",
            "classpath:db/book/delete-book-from-books-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void save_ValidRequestDto_ReturnValidDto() throws Exception {
        CreateBookRequestDto createBookDto = createBookRequestDto();
        BookDto expected = mapCreateBookDtoToBookDto(createBookDto, 1L);
        String jsonRequest = objectMapper.writeValueAsString(createBookDto);
        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method findAll with correct data")
    @Sql(scripts = {"classpath:db/category/add-categories-to-categories-table.sql",
            "classpath:db/book/add-books-to-books-table.sql",
            "classpath:db/bookscategories/add-book-category-to-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/category/delete-categories-from-categories-table.sql",
            "classpath:db/book/delete-book-from-books-table.sql",
            "classpath:db/bookscategories/delete-book-category-from-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAll_ValidData_ReturnArrayDto() throws Exception {
        BookDto[] expected = createArrayBookDtos();
        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookDto[].class);
        Assertions.assertEquals(4, actual.length);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method getBookById with correct data")
    @Sql(scripts = {"classpath:db/category/add-categories-to-categories-table.sql",
            "classpath:db/book/add-books-to-books-table.sql",
            "classpath:db/bookscategories/add-book-category-to-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/category/delete-categories-from-categories-table.sql",
            "classpath:db/book/delete-book-from-books-table.sql",
            "classpath:db/bookscategories/delete-book-category-from-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBookById_ValidBookId_ReturnBookDto() throws Exception {
        BookDto expected = createBookDto();
        MvcResult result = mockMvc.perform(
                        get("/books/2")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method update with correct data")
    @Sql(scripts = {"classpath:db/category/add-categories-to-categories-table.sql",
            "classpath:db/book/add-books-to-books-table.sql",
            "classpath:db/bookscategories/add-book-category-to-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/category/delete-categories-from-categories-table.sql",
            "classpath:db/book/delete-book-from-books-table.sql",
            "classpath:db/bookscategories/delete-book-category-from-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void update_ValidBookData_ReturnBookDto() throws Exception {
        CreateBookRequestDto updateBook = createBookRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(updateBook);
        BookDto expected = mapCreateBookDtoToBookDto(updateBook, 2L);
        MvcResult result = mockMvc.perform(
                        put("/books/2")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method delete with correct data")
    @Sql(scripts = {"classpath:db/category/add-categories-to-categories-table.sql",
            "classpath:db/book/add-books-to-books-table.sql",
            "classpath:db/bookscategories/add-book-category-to-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/category/delete-categories-from-categories-table.sql",
            "classpath:db/book/delete-book-from-books-table.sql",
            "classpath:db/bookscategories/delete-book-category-from-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void delete_ValidBookId_ReturnStatus() throws Exception {
        mockMvc.perform(
                        delete("/books/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookDto[].class);
        BookDto expected = createBookDto();
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, actual[0]);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method delete with correct data")
    @Sql(scripts = {"classpath:db/category/add-categories-to-categories-table.sql",
            "classpath:db/book/add-books-to-books-table.sql",
            "classpath:db/bookscategories/add-book-category-to-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/category/delete-categories-from-categories-table.sql",
            "classpath:db/book/delete-book-from-books-table.sql",
            "classpath:db/bookscategories/delete-book-category-from-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void search_ValidData_ReturnListDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/books/search?title=Kobzar2")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto[].class);
        BookDto expected = createBookDto();
        Assertions.assertEquals(1, actual.length);
        Assertions.assertEquals(expected, actual[0]);
    }

    private CreateBookRequestDto createBookRequestDto() {
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

    private BookDto createBookDto() {
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

    private BookDto[] createArrayBookDtos() {
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

    private BookSearchParametersDto createBookSearchParametersDto() {
        return new BookSearchParametersDto(
                new String[]{"Kobzar2"},
                null,
                null
        );
    }

    private BookDto mapCreateBookDtoToBookDto(CreateBookRequestDto book, long id) {
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
