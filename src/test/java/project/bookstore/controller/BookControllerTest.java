package project.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @Sql(scripts = {"classpath:db/category/add-category-to-categories-table.sql",
    "classpath:db/book/add-book-to-books-table.sql"},
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/category/delete-category-from-categories-table.sql",
    "classpath:db/book/delete-book-from-books-table.sql"},
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void save_ValidRequestDto_ReturnValidDto() throws Exception {
        CreateBookRequestDto createBookDto = createBookDto();
        BookDto expected = mapCreateBookDtoToBookDto(createBookDto);
        String jsonRequest = objectMapper.writeValueAsString(createBookDto);
        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    private CreateBookRequestDto createBookDto() {
        CreateBookRequestDto createBookDto = new CreateBookRequestDto();
        createBookDto.setTitle("Kobzar");
        createBookDto.setAuthor("Taras Shevchenko");
        createBookDto.setIsbn("12345678901");
        createBookDto.setPrice(BigDecimal.valueOf(123.45));
        createBookDto.setDescription("Good book");
        createBookDto.setCoverImage("Kobzar");
        createBookDto.setCategories(List.of(1L));
        return createBookDto;
    }

    private BookDto mapCreateBookDtoToBookDto(CreateBookRequestDto book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
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
