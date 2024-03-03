package proj.java.spring.controllerAdvice;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import proj.java.spring.aop.IDNotCorrectException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Component
public class GlobalExceptionHandler extends Throwable {

    @ResponseBody
    @ExceptionHandler(value = NameNotCorrectException.class)
    public String handleExceptionCollecter(HttpServletRequest req, Exception e) {
        // 处理NameNotCorrectException的逻辑
        return "nameNotCorrect"; // 返回错误页面或其他适当的响应
    }

    @ResponseBody
    @ExceptionHandler(value = IDNotCorrectException.class)
    public String handleNullPointerException(HttpServletRequest req, Exception e) {
        // 处理NullPointerException的逻辑
        return "ID NotCorrect"; // 返回错误页面或其他适当的响应
    }

    @ResponseBody
    @ExceptionHandler(value = WrongHeightException.class)
    public String handleWrongHeightException(HttpServletRequest req, Exception e) {
        // 处理NullPointerException的逻辑
        return "Height NotCorrect"; // 返回错误页面或其他适当的响应
    }

    public ResponseEntity<Object> defaultErrorHandler(HttpServletRequest request, Exception exception) {
        return ResponseEntity.badRequest().build();
    }
}
