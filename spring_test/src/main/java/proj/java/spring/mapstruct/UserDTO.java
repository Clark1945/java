package proj.java.spring.mapstruct;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;

    public String toString() {
        return "UserDTO [id=" + id + ", username=" + name + ", email=" + email + "]";
    }
}