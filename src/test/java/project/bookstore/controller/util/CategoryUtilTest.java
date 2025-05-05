package project.bookstore.controller.util;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CreateCategoryRequestDto;

@Component
public class CategoryUtilTest {
    public CreateCategoryRequestDto createInvalidCategoryDto() {
        return new CreateCategoryRequestDto(
                "",
                ""
        );
    }

    public PageImpl<CategoryDto> createPageImpl() {
        List<CategoryDto> categoriesDtos = createListCategoriesDto();
        Pageable pageable = PageRequest.of(0, 2);
        return new PageImpl<>(categoriesDtos, pageable, categoriesDtos.size());
    }

    public CreateCategoryRequestDto createCategoryRequestDto() {
        return new CreateCategoryRequestDto(
                "Fantasy",
                "Good books"
        );
    }

    public CreateCategoryRequestDto createCategoryRequestDtoInvalidData() {
        return new CreateCategoryRequestDto(
                "",
                ""
        );
    }

    public CategoryDto createCategoryDto() {
        return new CategoryDto(
                2L,
                "Fantasy",
                "Good books"
        );
    }

    public CategoryDto createUpdateCategory() {
        return new CategoryDto(
                2L,
                "Action",
                "Good good books"
        );
    }

    public List<CategoryDto> createListCategoriesDto() {
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

    public BookDtoWithoutCategoryIds[] createArrayBookDtos() {
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
