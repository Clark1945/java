package proj.java.spring.library.web.dto;

import proj.java.spring.library.domain.Book;

import java.time.LocalDate;

public record BookSummaryDto(Long id, String isbn, String title, LocalDate publishDate) {

    public static BookSummaryDto from(Book book) {
        return new BookSummaryDto(book.getId(), book.getIsbn(), book.getTitle(), book.getPublishDate());
    }
}
