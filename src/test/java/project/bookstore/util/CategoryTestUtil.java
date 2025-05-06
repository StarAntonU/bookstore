package project.bookstore.util;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CreateCategoryRequestDto;
import project.bookstore.model.Category;

@Component
public class CategoryTestUtil {
    public static CreateCategoryRequestDto createInvalidCategoryDto() {
        return new CreateCategoryRequestDto(
                "",
                ""
        );
    }

    public static PageImpl<CategoryDto> createPageImpl() {
        List<CategoryDto> categoriesDtos = createListCategoriesDto();
        Pageable pageable = PageRequest.of(0, 2);
        return new PageImpl<>(categoriesDtos, pageable, categoriesDtos.size());
    }

    public static CreateCategoryRequestDto createCategoryRequestDto() {
        return new CreateCategoryRequestDto(
                "Fantasy",
                "Good books"
        );
    }

    public static CreateCategoryRequestDto createCategoryRequestDtoInvalidData() {
        return new CreateCategoryRequestDto(
                "",
                ""
        );
    }

    public static CategoryDto createCategoryDto() {
        return new CategoryDto(
                2L,
                "Fantasy",
                "Good books"
        );
    }

    public static CategoryDto createUpdateCategory() {
        return new CategoryDto(
                2L,
                "Action",
                "Good good books"
        );
    }

    public static List<CategoryDto> createListCategoriesDto() {
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
        return List.of(categoryOne, categoryTwo);
    }

    public static BookDtoWithoutCategoryIds[] createArrayBookDtos() {
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

    public static Category creaateCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Fantasy");
        category.setDescription("Good book");
        return category;
    }

    public static BookDtoWithoutCategoryIds createBookDtoWithoutCategoryIds(long id) {
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

    public static Category mapCreateCategoryRequestDtoToCategory(
            CreateCategoryRequestDto createCategory) {
        Category category = new Category();
        category.setId(1L);
        category.setName(createCategory.name());
        category.setDescription(createCategory.description());
        return category;
    }

    public static CategoryDto mapCategoryToCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
