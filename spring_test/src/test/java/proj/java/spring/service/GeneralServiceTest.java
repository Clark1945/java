package proj.java.spring.service;


import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import proj.java.spring.reflection.Person;
import proj.java.spring.reflection.PersonException;
import proj.java.spring.reflection.PersonService;

import java.io.IOException;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GeneralServiceTest {
    @Autowired
    private PersonService personService;
    @Test
    public void testAuth() throws IOException, JSONException {
        Person person = new Person("kay", 25);
        String jsonString = personService.getValidatedPersonAsJson(person);
        // --> {"name":"kay","age":"25"}
        Assertions.assertEquals("{\"name\":\"kay\",\"age\":\"25\"}", jsonString);
    }

    @Test
    public void test_Fail_NumberNegativeThenThrow() {
        Person person = new Person("kay", -10);
        // com.kaiyee0.reflection.exception.PersonException: Field age value -10 is not positive
        Assertions.assertThrows(PersonException.class, () -> personService.getValidatedPersonAsJson(person));
    }
}


