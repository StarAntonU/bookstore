package mate.academy;

import java.math.BigDecimal;
import mate.academy.model.Book;
import mate.academy.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookstoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Title");
            book.setPrice(new BigDecimal("1000"));
            book.setIsbn("abc");
            book.setAuthor("Stepan Giga");

            bookService.save(book);

            bookService.findAll().forEach(System.out::println);
        };
    }
}
