package proj.java.spring.library.web.dto;

import proj.java.spring.library.domain.Author;

public record AuthorDto(Long id, String name) {

    public static AuthorDto from(Author author) {
        return new AuthorDto(author.getId(), author.getName());
    }
}
