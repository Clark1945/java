package proj.java.spring.library.web.dto;

import jakarta.validation.constraints.NotNull;
import proj.java.spring.library.domain.BookStatus;

public record UpdateCopyStatusRequest(@NotNull BookStatus status) {
}
