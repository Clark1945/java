package proj.java.spring.controllerAdvice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

// 一般來說只要使用 @Component 即可，但因為我們要實現 @RequestMapping，所以改用 @RestController
@RestController
public class CustomErrorController implements ErrorController { // 處理filter曾錯誤
    // 這個就是上面定義的 GlobalExceptionHandler，我們將用 DI 來注入
    private final GlobalExceptionHandler globalExceptionHandler;
    @Autowired
    public CustomErrorController(GlobalExceptionHandler globalExceptionHandler) {
        this.globalExceptionHandler = globalExceptionHandler;
    }

    // 宣告一個 @RequestMapping，並直接將 Exception 導向 GlobalExceptionHandler
    @RequestMapping("/error")
    public ResponseEntity<Object> handleError(HttpServletRequest request) {
        Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        return globalExceptionHandler.defaultErrorHandler(request, exception);
    }

    @Override
    public String getErrorPath() {
        // 這個 String 要和上面的 @RequestMapping 相同
        return "/error";
    }
}
