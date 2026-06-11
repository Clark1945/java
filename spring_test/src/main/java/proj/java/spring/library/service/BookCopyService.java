package proj.java.spring.library.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proj.java.spring.library.domain.Book;
import proj.java.spring.library.domain.BookCopy;
import proj.java.spring.library.domain.BookStatus;
import proj.java.spring.library.repository.BookCopyRepository;
import proj.java.spring.library.repository.BookRepository;
import proj.java.spring.library.web.dto.BookCopyDto;
import proj.java.spring.library.web.error.NotFoundException;

@Service
public class BookCopyService {

    private final BookCopyRepository bookCopyRepository;
    private final BookRepository bookRepository;

    public BookCopyService(BookCopyRepository bookCopyRepository, BookRepository bookRepository) {
        this.bookCopyRepository = bookCopyRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public BookCopyDto get(Long id) {
        BookCopy copy = bookCopyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BookCopy", id));
        return BookCopyDto.from(copy);
    }

    @Transactional
    public BookCopyDto addCopy(Long bookId, String barcode, BookStatus status) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book", bookId));
        BookCopy copy = new BookCopy(barcode, status == null ? BookStatus.AVAILABLE : status);
        book.addCopy(copy);
        bookCopyRepository.save(copy);
        return BookCopyDto.from(copy);
    }

    @Transactional
    public BookCopyDto updateStatus(Long id, BookStatus status) {
        BookCopy copy = bookCopyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BookCopy", id));
        copy.setStatus(status);
        return BookCopyDto.from(copy);
    }

    @Transactional
    public void delete(Long id) {
        BookCopy copy = bookCopyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BookCopy", id));
        bookCopyRepository.delete(copy);
    }
}
