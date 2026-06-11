package proj.java.spring.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proj.java.spring.library.domain.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
