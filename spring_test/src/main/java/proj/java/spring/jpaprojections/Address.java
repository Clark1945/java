package proj.java.spring.jpaprojections;

import javax.persistence.*;

@Entity
public class Address {

    @Id
    private Long id;

    @OneToOne
    private Person person;

    private String state;

    private String city;

    private String street;

    private String zipCode;

}

