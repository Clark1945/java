package proj.java.spring.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proj.java.spring.SpringBootApplication;

import java.io.IOException;
import java.util.Properties;

@RestController
public class TestController {
    private final TestService service;

    public TestController(TestService testService) {
        this.service = testService;
    }


    @GetMapping("/bean")
    public String getBeanDetail() throws IOException {
        Properties prop = new Properties();
        prop.load(SpringBootApplication.class.getClassLoader().getResourceAsStream("application-local.properties")); // 可讀取application-poperties的參數
        return prop.getProperty("test.str");
    }

    @GetMapping("/getStr")
    public String getReturnStr(@RequestParam String input) {
        return service.getReturnStr(input);
    }


    @GetMapping("/testErr")
    public ResponseEntity testErr(@RequestParam(value = "id") int id,
                          @RequestParam(value = "str", required = false) String str,
                          @RequestParam(value = "height") int height) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.ifValidateFailThrowException(id, str, height));
    }
}
