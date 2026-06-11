package proj.java.spring.library.web.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.List;

/**
 * Payload for creating or updating a book. {@code authorIds} replaces the full
 * set of authors on update; null is treated as "no authors".
 */
public record BookRequest(
        @NotBlank String isbn,
        @NotBlank String title,
        String description,
        LocalDate publishDate,
        List<Long> authorIds) {
}
