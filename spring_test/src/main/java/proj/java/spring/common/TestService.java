package proj.java.spring.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import proj.java.spring.controllerAdvice.NameNotCorrectException;
import proj.java.spring.controllerAdvice.StringCreator;
import proj.java.spring.controllerAdvice.WrongHeightException;
import proj.java.spring.aop.IDNotCorrectException;
import proj.java.spring.reflection.Billing;

import java.util.Optional;


@Component
public class TestService {

    public String getOptional(String request) {
        Billing requestBilling = buildBilling(request);
        System.out.println("requestBilling Get!~");
        return null;
    }
    public Billing buildBilling(String str){
        return Optional.ofNullable(str)
                .filter(StringUtils::isNotBlank)
                .map(s -> Billing.builder()
                        .id(1)
                        .fee("500")
                        .build())
                .orElse(new Billing());
    }

    public String getBeanDetail(String str) throws NameNotCorrectException{
        System.out.println("poperties testStr為 = "+ str);
        return str.substring(1,3);

    }
    private final StringCreator stringCreator;

    @Autowired
    public TestService(StringCreator stringCreator) {
        this.stringCreator = stringCreator;
    }
    public String what(int id,String name,int height){
        if (id == 0) {
            throw new IDNotCorrectException("ID ERROR");
        }
        if (name.equals("")) {
            throw new NameNotCorrectException("Name ERROR");
        }
        if (height == 0) {
            throw new WrongHeightException("Height ERROR");
        }
        return stringCreator.getDetailInformation(id, name, height);
    }
}

