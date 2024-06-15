package proj.java.spring.mapstruct;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long user_id;
    private String user_name;
    private String email;
    // Getters and setters
}