package proj.java.spring.library.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import proj.java.spring.library.domain.BookCopy;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {

    Page<BookCopy> findByBookId(Long bookId, Pageable pageable);

    long countByBookId(Long bookId);
}
