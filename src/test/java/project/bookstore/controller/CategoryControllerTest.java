package project.bookstore.controller;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import project.bookstore.controller.util.CategoryUtilTest;
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CreateCategoryRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryUtilTest categoryUtilTest;

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
        CreateCategoryRequestDto categoryRequestDto = categoryUtilTest.createCategoryRequestDto();
        CategoryDto expected = categoryUtilTest.createCategoryDto();
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
        assertTrue(reflectionEquals(expected, actual, "id"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method save with invalid data")
    public void save_InvalidData_ReturnStatus() throws Exception {
        CreateCategoryRequestDto category = categoryUtilTest.createCategoryRequestDtoInvalidData();
        String jsonResult = objectMapper.writeValueAsString(category);
        mockMvc.perform(
                post("/categories")
                        .content(jsonResult)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
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
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<CategoryDto> actual = objectMapper.readValue(root.get("content").toString(),
                new TypeReference<>() {
                });
        PageImpl<CategoryDto> page = categoryUtilTest.createPageImpl();
        List<CategoryDto> expected = page.get().toList();
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method findAll with incorrect url")
    public void findAll_InvalidData_ReturnStatus() throws Exception {
        mockMvc.perform(
                        get("/category")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method getCategoryById with correct data")
    @Sql(scripts = "classpath:db/category/add-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/category/delete-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCategoryById_ValidCategoryId_ReturnCategoryDto() throws Exception {
        CategoryDto expected = categoryUtilTest.createUpdateCategory();
        MvcResult result = mockMvc.perform(
                        get("/categories/2")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method getCategoryById with incorrect category id")
    @Sql(scripts = "classpath:db/category/add-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/category/delete-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCategoryById_InvalidCategoryId_ReturnStatus() throws Exception {
        mockMvc.perform(
                        get("/categories/12")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method delete with correct data")
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
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<CategoryDto> actual = objectMapper.readValue(root.get("content").toString(),
                new TypeReference<>() {
                });
        Pageable pageable = PageRequest.of(0, 1);
        List<CategoryDto> categories = List.of(categoryUtilTest.createUpdateCategory());
        PageImpl<CategoryDto> page = new PageImpl<>(categories, pageable, categories.size());
        List<CategoryDto> expected = page.get().toList();
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method delete with incorrect category id")
    @Sql(scripts = "classpath:db/category/add-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/category/delete-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void delete_InvalidCategoryId_ReturnStatus() throws Exception {
        mockMvc.perform(
                        delete("/categories/12")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method updateCategory with correct data")
    @Sql(scripts = "classpath:db/category/add-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/category/delete-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCategory_ValidData_ReturnCategoryDto() throws Exception {
        CreateCategoryRequestDto updateDto = categoryUtilTest.createCategoryRequestDto();
        String jsonResult = objectMapper.writeValueAsString(updateDto);
        MvcResult result = mockMvc.perform(
                        put("/categories/2")
                                .content(jsonResult)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        CategoryDto expected = categoryUtilTest.createCategoryDto();
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method updateCategory with incorrect data")
    @Sql(scripts = "classpath:db/category/add-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/category/delete-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCategory_InvalidData_ReturnStatus() throws Exception {
        CreateCategoryRequestDto invalidCategoryDto = categoryUtilTest.createInvalidCategoryDto();
        String jsonResult = objectMapper.writeValueAsString(invalidCategoryDto);
        mockMvc.perform(
                        put("/categories/1")
                                .content(jsonResult)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method updateCategory with incorrect url")
    @Sql(scripts = "classpath:db/category/add-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/category/delete-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCategory_InvalidUrl_ReturnStatus() throws Exception {
        CreateCategoryRequestDto invalidCategoryDto = categoryUtilTest.createCategoryRequestDto();
        String jsonResult = objectMapper.writeValueAsString(invalidCategoryDto);
        mockMvc.perform(
                        put("/categories/12")
                                .content(jsonResult)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
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
        BookDtoWithoutCategoryIds[] expected = categoryUtilTest.createArrayBookDtos();
        assertEquals(expected.length, actual.length);
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify method getBooksByCategoryId with incorrect category id")
    @Sql(scripts = {"classpath:db/category/add-categories-to-categories-table.sql",
            "classpath:db/book/add-books-to-books-table.sql",
            "classpath:db/bookscategories/add-book-category-to-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/category/delete-categories-from-categories-table.sql",
            "classpath:db/book/delete-book-from-books-table.sql",
            "classpath:db/bookscategories/delete-book-category-from-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBooksByCategoryId_InvalidCategoryId_ReturnStatus() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/categories/12/books")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto[] actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(),
                CategoryDto[].class);
        CategoryDto[] expected = new CategoryDto[0];
        assertTrue(reflectionEquals(expected, actual));
    }
}
