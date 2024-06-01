package proj.java.spring.jpaprojections;

import org.springframework.data.repository.Repository;

public interface PersonRepository extends Repository<Person, Long> {
    PersonView findByLastName(String lastName);

    PersonDto findByFirstName(String firstName);
}