package project.bookstore.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CreateCategoryRequestDto;
import project.bookstore.exception.unchecked.EntityNotFoundException;
import project.bookstore.mapper.CategoryMapper;
import project.bookstore.model.Category;
import project.bookstore.repository.book.BookRepository;
import project.bookstore.repository.category.CategoryRepository;
import project.bookstore.service.impl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private BookRepository bookRepository;

    @Test
    @DisplayName("Verify method save with correct data")
    public void save_CorrectCategory_ReturnValidCategoryDto() {
        CreateCategoryRequestDto categoryRequestDto = createCategoryRequestDto();
        Category category = mapCreateCategoryRequestDtoToCategory(categoryRequestDto);
        CategoryDto categoryDto = mapCategoryToCategoryDto(category);
        when(categoryMapper.toModel(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        CategoryDto actual = categoryService.save(categoryRequestDto);
        Assertions.assertEquals(categoryDto, actual);
    }

    @Test
    @DisplayName("Verify method findAll with correct data")
    public void findAll_CorrectData_ReturnAllCategoriesDto() {
        Category category = creaateCategory();
        CategoryDto categoryDto = mapCategoryToCategoryDto(category);
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = List.of(category);
        PageImpl<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        Page<CategoryDto> categoryDtos = categoryService.findAll(pageable);
        Assertions.assertEquals(1, categoryDtos.getTotalElements());
    }

    @Test
    @DisplayName("Verify method findCategoryById with correct data")
    public void findCategoryById_CorrectData_ReturnValidCategoryDto() {
        Category category = creaateCategory();
        CategoryDto categoryDto = mapCategoryToCategoryDto(category);
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        CategoryDto actual = categoryService.findCategoryById(category.getId());
        Assertions.assertEquals(categoryDto, actual);
    }

    @Test
    @DisplayName(""" 
            Verify method findCategoryById with incorrect data.
             Category with the id not exist
            """)
    public void findCategoryById_IncorrectData_ReturnException() {
        Category category = creaateCategory();
        String testException = "Can`t find category by id " + category.getId();
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> categoryService.findCategoryById(category.getId())
        );
        Assertions.assertEquals(testException, exception.getMessage());
    }

    @Test
    @DisplayName("Verify method update with correct data")
    public void update_CorrectData_ReturnValidCategoryDto() {
        CreateCategoryRequestDto createCategoryDto = createCategoryRequestDto();
        Category category = mapCreateCategoryRequestDtoToCategory(createCategoryDto);
        CategoryDto categoryDto = mapCategoryToCategoryDto(category);
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        CategoryDto actual = categoryService.update(category.getId(), createCategoryDto);
        Assertions.assertEquals(categoryDto, actual);
    }

    @Test
    @DisplayName(""" 
            Verify method update with incorrect data.
             Category with the id not exist
            """)
    public void update_IncorrectData_ReturnException() {
        long id = 1L;
        CreateCategoryRequestDto createCategoryDto = createCategoryRequestDto();
        String testException = "Can`t find category by id " + id;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(id, createCategoryDto)
        );
        Assertions.assertEquals(testException, exception.getMessage());
    }

    @Test
    @DisplayName("Verify method deleteById with correct data")
    public void deleteById_CorrectData_NoReturn() {
        long id = 1L;
        when(categoryRepository.existsById(id)).thenReturn(true);
        categoryService.deleteById(id);
        verify(categoryRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName(""" 
            Verify method delete with incorrect data.
             Category with the id not exist
            """)
    public void deleteById_IncorrectData_ReturnException() {
        long id = 1L;
        String expected = "Can`t delete category by id " + id;
        when(categoryRepository.existsById(id)).thenReturn(false);
        Exception actual = Assertions.assertThrows(EntityNotFoundException.class,
                () -> categoryService.deleteById(id));
        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Verify method getBooksByCategoryId with correct data")
    public void getBooksByCategoryId_CorrectData_ReturnValidCategoryDto() {
        long id = 1L;
        BookDtoWithoutCategoryIds bookDto = createBookDtoWithoutCategoryIds(id);
        when(bookRepository.findByCategoriesId(id)).thenReturn(List.of(bookDto));
        List<BookDtoWithoutCategoryIds> bookDtos = categoryService.getBooksByCategoryId(id);
        Assertions.assertEquals(1, bookDtos.size());
        Assertions.assertEquals(bookDto, bookDtos.get(0));
    }

    private Category creaateCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Fantasy");
        category.setDescription("Good book");
        return category;
    }

    private CreateCategoryRequestDto createCategoryRequestDto() {
        return new CreateCategoryRequestDto(
                "Fantasy",
                "Good book"
        );
    }

    private BookDtoWithoutCategoryIds createBookDtoWithoutCategoryIds(long id) {
        return new BookDtoWithoutCategoryIds(
                id,
                "Kobzar",
                "Taras Shevchenko",
                "1234567890",
                BigDecimal.valueOf(123.45),
                "Good book",
                "Kobzar"
        );
    }

    private Category mapCreateCategoryRequestDtoToCategory(
            CreateCategoryRequestDto createCategory) {
        Category category = new Category();
        category.setId(1L);
        category.setName(createCategory.name());
        category.setDescription(createCategory.description());
        return category;
    }

    private CategoryDto mapCategoryToCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
