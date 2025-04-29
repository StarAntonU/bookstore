package project.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Arrays;
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
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CreateCategoryRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
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
    @Sql(scripts = "classpath:db/category/delete-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void save_ValidCategoryDto_ReturnCategoryDto() throws Exception {
        CreateCategoryRequestDto categoryRequestDto = createCategoryRequestDto();
        CategoryDto expected = createCategoryDto();
        String jsonResult = objectMapper.writeValueAsString(categoryRequestDto);
        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .content(jsonResult)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method findAll with correct data")
    @Sql(scripts = "classpath:db/category/add-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/category/delete-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAll_ValidData_ReturnListCategoryDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/categories")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        byte[] actual = result.getResponse().getContentAsByteArray();
        byte[] expected = createArrayByteCategoriesDto();
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method getCategoryById with correct data")
    @Sql(scripts = "classpath:db/category/add-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/category/delete-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCategoryById_ValidCategoryId_ReturnCategoryDto() throws Exception {
        CategoryDto expected = createUpdateCategory();
        MvcResult result = mockMvc.perform(
                        get("/categories/2")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method deleteById with correct data")
    @Sql(scripts = "classpath:db/category/add-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/category/delete-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void delete_ValidCategoryId_ReturnStatus() throws Exception {
        mockMvc.perform(
                        delete("/categories/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        MvcResult result = mockMvc.perform(
                        get("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
        byte[] actual = result.getResponse().getContentAsByteArray();
        byte[] expected = Arrays.toString(new CategoryDto[]{createUpdateCategory()}).getBytes();
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method updateCategory with correct data")
    @Sql(scripts = "classpath:db/category/add-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/category/delete-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCategory_ValidData_ReturnCategoryDto() throws Exception {
        CreateCategoryRequestDto updateDto = createCategoryRequestDto();
        String jsonResult = objectMapper.writeValueAsString(updateDto);
        MvcResult result = mockMvc.perform(put("/categories/2")
                        .content(jsonResult)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        CategoryDto expected = createCategoryDto();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method getBooksByCategoryId with correct data")
    @Sql(scripts = {"classpath:db/category/add-categories-to-categories-table.sql",
            "classpath:db/book/add-books-to-books-table.sql",
            "classpath:db/bookscategories/add-book-category-to-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/category/delete-categories-from-categories-table.sql",
            "classpath:db/book/delete-book-from-books-table.sql",
            "classpath:db/bookscategories/delete-book-category-from-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBooksByCategoryId_ValidData_ReturnListBookDto() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/categories/2/books")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDtoWithoutCategoryIds[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookDtoWithoutCategoryIds[].class);
        BookDtoWithoutCategoryIds[] expected = createArrayBookDtos();
        Assertions.assertEquals(expected.length, actual.length);
        Assertions.assertEquals(expected[0], actual[0]);
        Assertions.assertEquals(expected[1], actual[1]);
    }

    private CreateCategoryRequestDto createCategoryRequestDto() {
        return new CreateCategoryRequestDto(
                "Fantasy",
                "Good books"
        );
    }

    private CategoryDto createCategoryDto() {
        return new CategoryDto(
                2L,
                "Fantasy",
                "Good books"
        );
    }

    private CategoryDto createUpdateCategory() {
        return new CategoryDto(
                2L,
                "Action",
                "Good good books"
        );
    }

    private byte[] createArrayByteCategoriesDto() {
        CategoryDto categoryOne = new CategoryDto(
                1L,
                "Fantasy",
                "Good books"
        );
        CategoryDto categoryTwo = new CategoryDto(
                2L,
                "Action",
                "Good good books"
        );
        return Arrays.toString(new CategoryDto[]{categoryOne, categoryTwo}).getBytes();
    }

    private BookDtoWithoutCategoryIds[] createArrayBookDtos() {
        BookDtoWithoutCategoryIds bookThree = new BookDtoWithoutCategoryIds(
                3L,
                "Kobzar3",
                "Taras Shevchenko",
                "12345678903",
                BigDecimal.valueOf(33.44),
                "Very good book",
                "Kobzar3"
        );
        BookDtoWithoutCategoryIds bookFour = new BookDtoWithoutCategoryIds(
                4L,
                "Kobzar4",
                "Taras Shevchenko",
                "12345678904",
                BigDecimal.valueOf(44.37),
                "So good book",
                "Kobzar4"
        );
        return new BookDtoWithoutCategoryIds[]{bookThree, bookFour};
    }
}
