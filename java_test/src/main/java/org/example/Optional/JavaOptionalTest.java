package org.example.Optional;

import java.util.Optional;

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

}
