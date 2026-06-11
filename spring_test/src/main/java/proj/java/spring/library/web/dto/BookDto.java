package proj.java.spring.library.web.dto;

import proj.java.spring.library.domain.Book;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public record BookDto(
        Long id,
        String isbn,
        String title,
        String description,
        LocalDate publishDate,
        List<AuthorDto> authors,
        long copyCount) {

    public static BookDto from(Book book, long copyCount) {
        List<AuthorDto> authors = book.getAuthors().stream()
                .map(AuthorDto::from)
                .sorted(Comparator.comparing(AuthorDto::id))
                .toList();
        return new BookDto(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getDescription(),
                book.getPublishDate(),
                authors,
                copyCount);
    }
}
