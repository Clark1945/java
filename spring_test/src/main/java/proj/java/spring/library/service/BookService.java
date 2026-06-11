package proj.java.spring.library.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proj.java.spring.library.domain.Author;
import proj.java.spring.library.domain.Book;
import proj.java.spring.library.repository.AuthorRepository;
import proj.java.spring.library.repository.BookCopyRepository;
import proj.java.spring.library.repository.BookRepository;
import proj.java.spring.library.web.dto.BookCopyDto;
import proj.java.spring.library.web.dto.BookDto;
import proj.java.spring.library.web.dto.BookRequest;
import proj.java.spring.library.web.dto.BookSummaryDto;
import proj.java.spring.library.web.dto.PageResponse;
import proj.java.spring.library.web.error.NotFoundException;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository,
                       BookCopyRepository bookCopyRepository,
                       AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<BookSummaryDto> search(String query, Pageable pageable) {
        Page<Book> page = (query == null || query.isBlank())
                ? bookRepository.findAll(pageable)
                : bookRepository.findByTitleContainingIgnoreCase(query.trim(), pageable);
        return PageResponse.from(page.map(BookSummaryDto::from));
    }

    @Transactional(readOnly = true)
    public BookDto getBook(Long id) {
        Book book = bookRepository.findByIdWithAuthors(id)
                .orElseThrow(() -> new NotFoundException("Book", id));
        return BookDto.from(book, bookCopyRepository.countByBookId(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<BookCopyDto> listCopies(Long bookId, Pageable pageable) {
        if (!bookRepository.existsById(bookId)) {
            throw new NotFoundException("Book", bookId);
        }
        return PageResponse.from(bookCopyRepository.findByBookId(bookId, pageable).map(BookCopyDto::from));
    }

    @Transactional
    public BookDto create(BookRequest request) {
        Book book = new Book(request.isbn(), request.title(), request.description(), request.publishDate());
        applyAuthors(book, request.authorIds());
        bookRepository.save(book);
        return BookDto.from(book, 0);
    }

    @Transactional
    public BookDto update(Long id, BookRequest request) {
        Book book = bookRepository.findByIdWithAuthors(id)
                .orElseThrow(() -> new NotFoundException("Book", id));
        book.setIsbn(request.isbn());
        book.setTitle(request.title());
        book.setDescription(request.description());
        book.setPublishDate(request.publishDate());
        book.getAuthors().clear();
        applyAuthors(book, request.authorIds());
        return BookDto.from(book, bookCopyRepository.countByBookId(id));
    }

    @Transactional
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book", id));
        bookRepository.delete(book);
    }

    private void applyAuthors(Book book, List<Long> authorIds) {
        if (authorIds == null) {
            return;
        }
        for (Long authorId : authorIds) {
            Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new NotFoundException("Author", authorId));
            book.addAuthor(author);
        }
    }
}
