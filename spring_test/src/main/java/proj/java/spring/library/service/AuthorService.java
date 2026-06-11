package proj.java.spring.library.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proj.java.spring.library.domain.Author;
import proj.java.spring.library.domain.Book;
import proj.java.spring.library.repository.AuthorRepository;
import proj.java.spring.library.web.dto.AuthorDto;
import proj.java.spring.library.web.dto.AuthorRequest;
import proj.java.spring.library.web.dto.PageResponse;
import proj.java.spring.library.web.error.NotFoundException;

import java.util.HashSet;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<AuthorDto> list(Pageable pageable) {
        return PageResponse.from(authorRepository.findAll(pageable).map(AuthorDto::from));
    }

    @Transactional(readOnly = true)
    public AuthorDto get(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author", id));
        return AuthorDto.from(author);
    }

    @Transactional
    public AuthorDto create(AuthorRequest request) {
        Author author = new Author(request.name());
        authorRepository.save(author);
        return AuthorDto.from(author);
    }

    @Transactional
    public AuthorDto update(Long id, AuthorRequest request) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author", id));
        author.setName(request.name());
        return AuthorDto.from(author);
    }

    @Transactional
    public void delete(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author", id));
        // Book owns the join; detach this author from every book before removing it.
        for (Book book : new HashSet<>(author.getBooks())) {
            book.getAuthors().remove(author);
        }
        authorRepository.delete(author);
    }
}
