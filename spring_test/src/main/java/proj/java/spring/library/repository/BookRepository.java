package proj.java.spring.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proj.java.spring.library.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
