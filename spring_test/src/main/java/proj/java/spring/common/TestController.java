package proj.java.spring.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import proj.java.spring.SpringBootApplication;

import java.io.IOException;
import java.util.Properties;

@RestController
public class TestController {
    private final TestService service;
    @Autowired
    public TestController(TestService testService) {
        this.service=testService;
    }

    @PostMapping("/optional")
    public String getUserDescription(@RequestParam("id") String request){ // Optional練習
        return service.getOptional(request);
    }

    @GetMapping("/bean")
    public String getBeanDetail() throws IOException {
        Properties prop = new Properties();
        prop.load(SpringBootApplication.class.getClassLoader().getResourceAsStream("application-local.properties")); // 可讀取application-poperties的參數

        return service.getBeanDetail(prop.getProperty("test.str"));
    }

    @GetMapping("/testErr")
    public String testErr(@RequestParam("id") int id,
                          @RequestParam("str") String str,
                          @RequestParam("height") int height) {
        return service.what(id,str,height);
    }
}
