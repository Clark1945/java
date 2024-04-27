package org.example.Optional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaOptionalTest {

    private Optional<String> op = Optional.ofNullable("Clark");
    private Optional<String> opNull = Optional.ofNullable(null);

    public boolean isOpPresent() {
        return op.isPresent();
    }

    public boolean isOpNullPresent() {
        return opNull.isPresent();
    }

    public void isOpPresentThenPrint() {
        op.ifPresent(name -> System.out.println(name));
    }

    public void isOpPresentThenNotPrint() {
        opNull.ifPresent(name -> System.out.println(name));
    }

    public String OpReturnNotExist() {
        return op.orElse("Not Exist");
    }

    public String OpNullReturnNotExist() {
        return opNull.orElse("Not Exist");
    }

    public String OpNullReturnNotExist2() {
        return opNull.orElseGet(() -> "Not Exist");
    }
    public void OpNotNullPrintMessage() {
        opNull.orElse(getMyDefault());
    }

    public String getMyDefault() {
        System.out.println("Getting Default Value");
        return "Default Value";
    }
    public void javaStreamNonDuplicated() {
        // 使用map 處理陣列
        List<String> companyNames = Arrays.asList("paypal", "oracle", "", "microsoft", "", "apple");
        companyNames =companyNames.stream().filter(x -> !x.isEmpty()).collect(Collectors.toList());
        System.out.println("Company　Name" + companyNames);
    }

    public void findLengthExistingNull () {
        List<String> companyNames = Arrays.asList("paypal", "oracle", "", "microsoft", "", "apple");
        Optional<List<String>> listOptional = Optional.ofNullable(companyNames);
        int size = listOptional.map(List::size).orElse(0);
        System.out.println("SIZE = " + size);
    }

    public void Ch502() {
        Optional<String> s = Optional.of("test");
        System.out.println((Optional.of("TEST") + " AND " + s.map(String::toUpperCase)));

        System.out.println(Optional.of(Optional.of("STRING")) +
                " AND " +
                Optional
                    .of("string")
                    .map(st -> Optional.of("STRING")));

        System.out.println(Optional.of("STRING") +
                " AND " +
                Optional
                .of("string")
                .flatMap(st -> Optional.of("STRING")));

        List<String> myList = Stream.of("a", "b")
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println(Arrays.asList("A", "B") + " AND " + myList);

        List<String> words = Arrays.asList("Java", "Stream", "API");

        // 使用map操作，將每個字串轉換為它的長度
        List<Integer> wordLengths = words.stream()
                .map(String::length)
                .collect(Collectors.toList());
        System.out.println("使用 map 操作得到的長度列表: " + wordLengths);

        // 使用flatMap操作，將每個字串拆分為單個字符並平坦化為單獨的流
        List<Character> characters = words.stream()
                .flatMap(word -> word.chars().mapToObj(c -> (char) c))
                .collect(Collectors.toList());
        System.out.println("使用 flatMap 操作得到的字符列表: " + characters);
    }

    public class Person {
        private String name;
        private int age;
        private String password;

        public Person(String name, int i) {
            this.age = i;
            this.name = name;
        }

        public Optional<String> getName() {
            return Optional.ofNullable(name);
        }

        public Optional<Integer> getAge() {
            return Optional.ofNullable(age);
        }

        public Optional<String> getPassword() {
            return Optional.ofNullable(password);
        }

        // normal constructors and setters
    }


    public void OptionalMapFilterTesting() {
        // 利用map進行映射，再用filter處理值
        String password = " password ";
        Optional<String> passOpt = Optional.of(password);
        boolean correctPassword = passOpt
                .filter(pass -> pass.equals("password"))
                .isPresent();
        System.out.println(correctPassword == false ? "Correct":"Failure");

        correctPassword = passOpt
                .map(String::trim)
                .filter(pass -> pass.equals("password"))
                .isPresent();
        System.out.println(correctPassword == true ? "Correct":"Failure");
    }

    public void OptionalMapFilterRangeTesting() {
        Integer year = 2016;
        Optional<Integer> yearOptional = Optional.of(year);
        boolean is2016 = yearOptional.filter(y -> y == 2016).isPresent();

        // 可以使用 function進行值的檢測判斷
        Modem modem2 = new Modem(19.10d);
        System.out.println("Result = " + priceIsInRange2(modem2));
    }

    public boolean priceIsInRange2(Modem modem2) {
        return Optional.ofNullable(modem2)
                .map(Modem::getPrice).
                filter(p -> p >= 10).
                filter(p -> p <= 15).
                isPresent();
    }

    public class Modem {
        private Double price;

        public Modem(Double price) {
            this.price = price;
        }
        // standard getters and setters

        public void setPrice(Double price) {
            this.price = price;
        }

        public Double getPrice() {
            return price;
        }
    }
}
