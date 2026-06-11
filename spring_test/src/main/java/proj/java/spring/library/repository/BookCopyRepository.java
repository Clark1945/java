package proj.java.spring.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proj.java.spring.library.domain.BookCopy;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
}
