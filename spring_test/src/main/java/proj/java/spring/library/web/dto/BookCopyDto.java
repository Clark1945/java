package proj.java.spring.library.web.dto;

import proj.java.spring.library.domain.BookCopy;
import proj.java.spring.library.domain.BookStatus;

public record BookCopyDto(Long id, String barcode, BookStatus status, Long bookId) {

    public static BookCopyDto from(BookCopy copy) {
        return new BookCopyDto(copy.getId(), copy.getBarcode(), copy.getStatus(), copy.getBook().getId());
    }
}
