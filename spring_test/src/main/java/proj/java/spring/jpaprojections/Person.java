package proj.java.spring.jpaprojections;

import javax.persistence.*;

@Entity
public class Person {

    @Id
    private Long id;

    private String firstName;

    private String lastName;

    @OneToOne(mappedBy = "person")
    private Address address;

    // getters and setters
}
