package proj.java.spring.library.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import proj.java.spring.library.service.BookCopyService;
import proj.java.spring.library.web.dto.BookCopyDto;
import proj.java.spring.library.web.dto.UpdateCopyStatusRequest;

@RestController
@RequestMapping("/api/copies")
public class BookCopyController {

    private final BookCopyService bookCopyService;

    public BookCopyController(BookCopyService bookCopyService) {
        this.bookCopyService = bookCopyService;
    }

    @GetMapping("/{id}")
    public BookCopyDto get(@PathVariable Long id) {
        return bookCopyService.get(id);
    }

    @PatchMapping("/{id}/status")
    public BookCopyDto updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateCopyStatusRequest request) {
        return bookCopyService.updateStatus(id, request.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookCopyService.delete(id);
    }
}
