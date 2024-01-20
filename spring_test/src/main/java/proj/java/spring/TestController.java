//package proj.java.spring;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class TestController {
//    private final TestService service;
//    @Autowired
//    public TestController(TestService testService) {
//        this.service=testService;
//    }
//    @GetMapping("/aop")
//    public String getUserDesc(@RequestParam("id") int id,
//                         @RequestParam("name") String name,
//                         @RequestParam("height") int height){
//        return service.getUserDesc(id,name,height);
//    }
//
////    @PostMapping("/optional")
////    public String getUserDescription(@RequestParam("id") int id){
////        return service.getOptional(id);
////    }
////        try {
//
////        } catch (Throwable e) {
////            return e.getMessage();
////        }
////    @GetMapping("/bean")
////    public String getBeanDetail() {
//////        Properties prop = new Properties();
//////        prop.load(SpringBootApplication.class.getClassLoader().getResourceAsStream("application-local.properties"));
////
//////        TestService service1 = new TestService();
////        return service.getBeanDetail();
////    }
//}
