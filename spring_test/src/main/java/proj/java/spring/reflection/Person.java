package proj.java.spring.reflection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@JsonObject
@AllArgsConstructor
public class Person {
    private String name;

    @PositiveNumber
    private Integer age;

}
