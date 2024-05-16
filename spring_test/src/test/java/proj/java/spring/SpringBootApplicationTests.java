package proj.java.spring;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import proj.java.spring.reflection.Billing;
import proj.java.spring.reflection.SetterObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class SpringBootApplicationTests {

    @Test
    public void testReflection() throws NoSuchFieldException {
        Person person = Person.of(1,"Majesty");
        System.out.println(person.getId() + " + " + person.getName());

        Class<Person> personClass = Person.class;

        Field field = personClass.getDeclaredField("id");
        Field[] fields = personClass.getDeclaredFields();
        for (Field i : fields) {
            System.out.println("Field = "+i.getName());
        }
        System.out.println(personClass.getCanonicalName()); //路徑+名稱
        System.out.println(personClass.getSimpleName());//名稱
        System.out.println(personClass.getPackage().getName());//路徑
        System.out.println(Modifier.toString(personClass.getModifiers()));

        Method[] methods = personClass.getMethods(); // 包含預設的方法
        Method[] methods2 = personClass.getDeclaredMethods(); //宣告的方法
        for (Method m : methods) {
            System.out.println("Method = " + m.getName());
        }
        for (Method m : methods2) {
            System.out.println("Method2 = " + m.getName());
        }

    }

    @Test
    public void testReflectionAdvanced() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException { // 使用refelction 建立 Billing物件
        Billing billing = new Billing();
        int id = 1;
        String user = "Clark";
        String amount = "30";

        Billing billingAfterSetId = setObjectValue(billing,"id",int.class,id);
        System.out.println(billingAfterSetId.getId());

        SetterObject setterObject = new SetterObject();
        setterObject.setColName("user");
        setterObject.setColValue("Clark");
        setterObject.setColType(String.class);

        SetterObject setterObject2 = new SetterObject();
        setterObject2.setColName("fee");
        setterObject2.setColValue("30");
        setterObject2
                .setColType(String.class);
        List<SetterObject> arr = new ArrayList();
        arr.add(setterObject);
        arr.add(setterObject2);
        List<Class> listClass = new ArrayList<>();
        listClass.add(String.class);
        listClass.add(String.class);
        Billing billingAfterSetIdNew = setObjectValues(billing,arr);
        System.out.println(billingAfterSetIdNew.getUser());
        System.out.println(billingAfterSetIdNew.getFee());
    }

    private <T> T setObjectValue(Object object,String colName,Class colNameType,Object colVal) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> aClass = object.getClass(); // 取得類別
        Method method = aClass.getDeclaredMethod("set"+StringUtils.capitalize(colName),colNameType); // 取得指定值的set方法
        method.invoke(object,colVal);
        return (T) object;
    }

    private <T> T setObjectValues(Object object, List<SetterObject> arr) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> aClass = object.getClass(); // 取得類別
        for (int i = 0;i<arr.size();i++) {
            Method method = aClass.getDeclaredMethod("set"+StringUtils.capitalize(arr.get(i).getColName()),arr.get(i).getColType()); // 取得指定值的set方法
            method.invoke(object,arr.get(i).getColValue());
        }
        return (T) object;
    }

    
}




