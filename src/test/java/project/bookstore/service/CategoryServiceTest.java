package project.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static project.bookstore.util.CategoryTestUtil.creaateCategory;
import static project.bookstore.util.CategoryTestUtil.createBookDtoWithoutCategoryIds;
import static project.bookstore.util.CategoryTestUtil.createCategoryRequestDto;
import static project.bookstore.util.CategoryTestUtil.mapCategoryToCategoryDto;
import static project.bookstore.util.CategoryTestUtil.mapCreateCategoryRequestDtoToCategory;

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
        CategoryDto expected = mapCategoryToCategoryDto(category);

        when(categoryMapper.toModel(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryDto actual = categoryService.save(categoryRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify method findAll with correct data")
    public void findAll_CorrectData_ReturnAllCategoriesDto() {
        Category category = creaateCategory();
        CategoryDto expected = mapCategoryToCategoryDto(category);
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = List.of(category);
        PageImpl<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(expected);
        Page<CategoryDto> pages = categoryService.findAll(pageable);
        List<CategoryDto> actual = pages.get().toList();

        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0));
    }

    @Test
    @DisplayName("Verify method findCategoryById with correct data")
    public void findCategoryById_CorrectData_ReturnValidCategoryDto() {
        Category category = creaateCategory();
        CategoryDto expected = mapCategoryToCategoryDto(category);

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryDto actual = categoryService.findCategoryById(category.getId());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName(""" 
            Verify method findCategoryById with incorrect data.
             Category with the id not exist
            """)
    public void findCategoryById_IncorrectData_ReturnException() {
        Category category = creaateCategory();

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.empty());
        Exception actual = Assertions.assertThrows(EntityNotFoundException.class,
                () -> categoryService.findCategoryById(category.getId())
        );

        String expected = "Can`t find category by id " + category.getId();
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Verify method update with correct data")
    public void update_CorrectData_ReturnValidCategoryDto() {
        CreateCategoryRequestDto createCategoryDto = createCategoryRequestDto();
        Category category = mapCreateCategoryRequestDtoToCategory(createCategoryDto);
        CategoryDto expected = mapCategoryToCategoryDto(category);

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryDto actual = categoryService.update(category.getId(), createCategoryDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName(""" 
            Verify method update with incorrect data.
             Category with the id not exist
            """)
    public void update_IncorrectData_ReturnException() {
        long id = 1L;
        CreateCategoryRequestDto createCategoryDto = createCategoryRequestDto();

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        Exception actual = Assertions.assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(id, createCategoryDto)
        );

        String expected = "Can`t find category by id " + id;
        assertEquals(expected, actual.getMessage());
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
        when(categoryRepository.existsById(id)).thenReturn(false);
        Exception actual = Assertions.assertThrows(EntityNotFoundException.class,
                () -> categoryService.deleteById(id));

        String expected = "Can`t delete category by id " + id;
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Verify method getBooksByCategoryId with correct data")
    public void getBooksByCategoryId_CorrectData_ReturnValidCategoryDto() {
        long id = 1L;
        BookDtoWithoutCategoryIds expected = createBookDtoWithoutCategoryIds(id);

        when(bookRepository.findByCategoriesId(id)).thenReturn(List.of(expected));
        List<BookDtoWithoutCategoryIds> actual = categoryService.getBooksByCategoryId(id);

        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0));
    }
}
