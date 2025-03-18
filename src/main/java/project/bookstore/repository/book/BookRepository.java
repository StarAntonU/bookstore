package project.bookstore.repository.book;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    List<BookDtoWithoutCategoryIds> findByCategories_Id(Long categoryId);
}
