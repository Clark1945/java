package proj.java.spring;

import lombok.Data;

import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
@Data
class Person {
    int id;
    String name;

    public static Person of(int id, String name) {
        checkArgument(id >= 0);
        checkArgument(name != null);
        return new Person(id, name);
    }

    private Person(int id, String name) {
        this.id = id;
        this.name = name;
    }
}