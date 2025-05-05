package project.bookstore.controller;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import project.bookstore.controller.util.BookUtilTest;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.CreateBookRequestDto;

@Sql(scripts = {"classpath:db/category/add-categories-to-categories-table.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"classpath:db/category/delete-categories-from-categories-table.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookUtilTest bookUtilTest;

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
    @Sql(scripts = {"classpath:db/book/delete-book-from-books-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void save_ValidRequestDto_ReturnValidDto() throws Exception {
        CreateBookRequestDto createBookDto = bookUtilTest.createBookRequestDto();
        BookDto expected = bookUtilTest.mapCreateBookDtoToBookDto(createBookDto, 1L);
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
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(reflectionEquals(expected, actual, "id"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method save with category is not exist")
    public void save_InvalidCategory_ReturnStatus() throws Exception {
        CreateBookRequestDto book = bookUtilTest.createInvalidBookRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(book);
        mockMvc.perform(
                post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method findAll with correct data")
    @Sql(scripts = {"classpath:db/book/add-books-to-books-table.sql",
            "classpath:db/bookscategories/add-book-category-to-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/book/delete-book-from-books-table.sql",
            "classpath:db/bookscategories/delete-book-category-from-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAll_ValidData_ReturnArrayDto() throws Exception {
        BookDto[] expected = bookUtilTest.createArrayBookDtos();
        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookDto[].class);
        assertEquals(4, actual.length);
        assertTrue(reflectionEquals(expected, actual));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method findAll with incorrect url")
    @Sql(scripts = {"classpath:db/book/add-books-to-books-table.sql",
            "classpath:db/bookscategories/add-book-category-to-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/book/delete-book-from-books-table.sql",
            "classpath:db/bookscategories/delete-book-category-from-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAll_InvalidData_ReturnStatus() throws Exception {
        mockMvc.perform(
                        get("/book")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method getBookById with correct data")
    @Sql(scripts = {"classpath:db/book/add-books-to-books-table.sql",
            "classpath:db/bookscategories/add-book-category-to-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/book/delete-book-from-books-table.sql",
            "classpath:db/bookscategories/delete-book-category-from-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBookById_ValidBookId_ReturnBookDto() throws Exception {
        BookDto expected = bookUtilTest.createBookDto();
        MvcResult result = mockMvc.perform(
                        get("/books/2")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method getBookById with book is not exist")
    public void getBookById_InvalidBookId_ReturnStatus() throws Exception {
        mockMvc.perform(
                        get("/books/5")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method update with correct data")
    @Sql(scripts = {"classpath:db/book/add-books-to-books-table.sql",
            "classpath:db/bookscategories/add-book-category-to-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/book/delete-book-from-books-table.sql",
            "classpath:db/bookscategories/delete-book-category-from-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void update_ValidBookData_ReturnBookDto() throws Exception {
        CreateBookRequestDto updateBook = bookUtilTest.createBookRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(updateBook);
        BookDto expected = bookUtilTest.mapCreateBookDtoToBookDto(updateBook, 2L);
        MvcResult result = mockMvc.perform(
                        put("/books/2")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify update with incorrect book data")
    @Sql(scripts = {"classpath:db/book/add-books-to-books-table.sql",
            "classpath:db/bookscategories/add-book-category-to-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/book/delete-book-from-books-table.sql",
            "classpath:db/bookscategories/delete-book-category-from-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void update_InvalidBook_ReturnStatus() throws Exception {
        CreateBookRequestDto book = bookUtilTest.createInvalidBookRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(book);
        mockMvc.perform(
                        put("/books/1")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify update with incorrect url")
    @Sql(scripts = {"classpath:db/book/add-books-to-books-table.sql",
            "classpath:db/bookscategories/add-book-category-to-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/book/delete-book-from-books-table.sql",
            "classpath:db/bookscategories/delete-book-category-from-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void update_InvalidUrl_ReturnStatus() throws Exception {
        CreateBookRequestDto book = bookUtilTest.createBookRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(book);
        mockMvc.perform(
                        put("/books/12")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method delete with correct data")
    @Sql(scripts = {"classpath:db/book/add-books-to-books-table.sql",
            "classpath:db/bookscategories/add-book-category-to-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/book/delete-book-from-books-table.sql",
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
        BookDto expected = bookUtilTest.createBookDto();
        assertEquals(3, actual.length);
        assertEquals(expected, actual[0]);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method delete with incorrect book id")
    @Sql(scripts = {"classpath:db/book/add-books-to-books-table.sql",
            "classpath:db/bookscategories/add-book-category-to-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/book/delete-book-from-books-table.sql",
            "classpath:db/bookscategories/delete-book-category-from-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void delete_InvalidBookId_ReturnStatus() throws Exception {
        mockMvc.perform(
                        delete("/books/15")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method delete with correct data")
    @Sql(scripts = {"classpath:db/book/add-books-to-books-table.sql",
            "classpath:db/bookscategories/add-book-category-to-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/book/delete-book-from-books-table.sql",
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
        BookDto expected = bookUtilTest.createBookDto();
        assertEquals(1, actual.length);
        assertEquals(expected, actual[0]);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Invalid search data method search")
    @Sql(scripts = {"classpath:db/book/add-books-to-books-table.sql",
            "classpath:db/bookscategories/add-book-category-to-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/book/delete-book-from-books-table.sql",
            "classpath:db/bookscategories/delete-book-category-from-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void search_InvalidData_ReturnStatus() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/books/search?title=Kobzar12")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto[].class);
        assertEquals(0, actual.length);
    }
}
