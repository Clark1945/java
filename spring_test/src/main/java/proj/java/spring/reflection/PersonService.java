package proj.java.spring.reflection;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
@Service
public class PersonService {
    @SneakyThrows
    public String getValidatedPersonAsJson(Object object) {
        if (Objects.isNull(object)) {
            throw new PersonException("object can not be null");
        }
        Class<?> clazz = object.getClass();
        if (!clazz.isAnnotationPresent(JsonObject.class)) { // 有Annotation才做判斷
            throw new PersonException(String.format("%s is not annotated with @JsonObject", clazz.getSimpleName()));
        }
        Map<String, Object> jsonObjectMaps = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(PositiveNumber.class)) { // 有Annotation才做判斷
                String numStr = field.get(object).toString();
                if (Integer.parseInt(numStr) <= 0) {
                    throw new PersonException(String.format("Field %s value %s is not positive", field.getName(), numStr));
                }
            }
            jsonObjectMaps.put(field.getName(), field.get(object));
        }

        String jsonString = jsonObjectMaps.entrySet().stream()
                .map(entry -> "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"")
                .collect(Collectors.joining(","));
        return "{" + jsonString + "}";
    }
}
