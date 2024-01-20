//package proj.java.spring;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//
//@Component
//public class TestService {
//    @Value("${test.str}")
//    private String testStr;
//
//    public String getUserDesc(int id, String name, int height) {
//        return "User " + name + " is " + height + " tall (ID=" + id + ")";
//    }
//
//
////    public String getOptional(int id) {
////        Optional<RequestObject> request = buildReqObject(null);
////        request.ifPresent(System.out::println);
//////        if(request.isPresent()){
//////            System.out.println(request.get());
//////        }
////
//////        nickOptional.orElse("Openhome Reader");
//////        nickOptional.orElseThrow("Openhome Reader");
//////        nickOptional.orElseGet("Openhome Reader");
////        return "User";
////    }
////    public Optional<RequestObject> buildReqObject(String str){
////        return StringUtils.isBlank(str) ? Optional.empty() : Optional.ofNullable(RequestObject.builder()
////                .PointMerchant(1)
////                .SourceType(4)
////                .SourceAccount("King")
////                .PointCardType(4)
////                .PointCardID("GID001")
////                .SourceMerchantId("First One").build());
////    }
////
////    public String getBeanDetail(){
////        String str = null;
////        return str.substring(1,3);
//////        throw new ExceptionCollecter("STR","ERROR");
////    }
//}
//
//
////    private final StringCreator stringCreator;
////
////    @Autowired
////    public TestService(StringCreator stringCreator) {
////        this.stringCreator = stringCreator;
////    }
//
//
//
////        try {
////
////        return stringCreator.getDetailInformation(id,name,height);
////        }catch (IDNotCorrectException e) {
////            return e.getMessage();
////        }catch (NameNotCorrectException e) {
////            return e.getMessage();
////        }catch (WrongHeightException e) {
////            return e.getMessage();
////        }
////    }
