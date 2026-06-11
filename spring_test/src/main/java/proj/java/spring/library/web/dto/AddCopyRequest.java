package proj.java.spring.library.web.dto;

import jakarta.validation.constraints.NotBlank;
import proj.java.spring.library.domain.BookStatus;

public record AddCopyRequest(@NotBlank String barcode, BookStatus status) {
}
