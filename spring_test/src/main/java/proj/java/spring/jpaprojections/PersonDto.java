package proj.java.spring.jpaprojections;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PersonDto {
    private String firstName;
    private String lastName;

    public PersonDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
