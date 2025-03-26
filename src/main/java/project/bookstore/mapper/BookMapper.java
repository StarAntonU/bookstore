package project.bookstore.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import project.bookstore.config.MapperConfig;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.CreateBookRequestDto;
import project.bookstore.model.Book;
import project.bookstore.model.Category;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoriesIds", ignore = true)
    BookDto toDto(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        List<Long> categoriesIds = book.getCategories().stream()
                .map(Category::getId)
                .toList();
        bookDto.setCategoriesIds(categoriesIds);
    }

    @Mapping(target = "categories", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    @Mapping(target = "categories", ignore = true)
    void updateBookFromDto(@MappingTarget Book book, CreateBookRequestDto requestDto);

    @AfterMapping
    default void setCategories(@MappingTarget Book book, CreateBookRequestDto requestDto) {
        Set<Category> categories = requestDto.getCategories().stream()
                .map(Category::new)
                .collect(Collectors.toSet());
        book.setCategories(categories);
    }
}
