//package proj.java.spring;
//
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//@ControllerAdvice
//public class GlobalExceptionHandler extends Throwable {
//
//    @ResponseBody
//    @ExceptionHandler(ExceptionCollecter.class)
//    public String handleExceptionCollecter(ExceptionCollecter ex) {
//        // 处理NullPointerException的逻辑
//        return "error-page"; // 返回错误页面或其他适当的响应
//    }
//
//    @ResponseBody
//    @ExceptionHandler(NullPointerException.class)
//    public String handleNullPointerException(NullPointerException ex) {
//        // 处理NullPointerException的逻辑
//        return "Null-Pointer"; // 返回错误页面或其他适当的响应
//    }
//}
