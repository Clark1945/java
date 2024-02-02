package proj.java.spring.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
public class LoggerAop {

    // 定義 ThreadLocal 變數來存儲開始時間
    private ThreadLocal<Long> startTime = new ThreadLocal<>();


    @Pointcut("execution(public * proj.java.spring.aop.controller.TxController.*(..))")
    public void pointcutController() {
    }

    @Pointcut("execution(public * proj.java.spring.aop.controller.TxController.getTx(..))")
    public void pointcutGetTx() {
    }

    @Pointcut("pointcutController() && !pointcutGetTx()")
    public void pointcutAdmin() {
    }

    @Before(value = "pointcutController()")
    public void roleVerify(JoinPoint point) {

        startTime.set(System.currentTimeMillis());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String role = request.getHeader("role"); // 抓取Header

        if (StringUtils.isEmpty(role)) {
            throw new IDNotCorrectException("不明確的來源訪問");
        }
    }
    @Before(value = "pointcutController()")
    public void aroleVerify(JoinPoint point) {

        startTime.set(System.currentTimeMillis());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String role = request.getHeader("role"); // 抓取Header

        if (StringUtils.isEmpty(role)) {
            throw new IDNotCorrectException("不明確的來源訪問");
        }
    }

    @Before(value = "pointcutAdmin()")
    public void adminVerify(JoinPoint point) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String role = request.getHeader("role") != null ? request.getHeader("role") : ""; // 抓取Header

        if (!role.equals("admin")) {
            throw new IDNotCorrectException("僅管理者可以訪問");
        }
    }
    @After("pointcutController()")
    public void logResponse() { // 紀錄 Response
        log.info("執行結束");
    }
    @AfterThrowing(pointcut = "pointcutController()",throwing = "ex")
    public void logErrResponse(JoinPoint point,Throwable ex) { // 紀錄 Response
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String role = request.getHeader("role"); // 抓取Header

        log.info("發生異常，使用者：" + role + ", Message = "+ex.getMessage());
    }

    @AfterReturning(pointcut = "pointcutController()", returning = "response")
    public void logReturnResponse(Object response) { // 紀錄 Response

        log.info("執行成功結束，結果 = " + response);

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime.get();

        log.info("執行結束，消耗時間：" + elapsedTime + " 毫秒");

        // 清除 ThreadLocal 變數，避免內存洩漏
        startTime.remove();
    }

    @Around("pointcutController()")
    public Object aroundAdvice(ProceedingJoinPoint pjp) {
        Object[] params = pjp.getArgs();
        int id = Integer.parseInt(String.valueOf(pjp.getArgs()[0]));
        String name = (String) pjp.getArgs()[1];
        int height = Integer.parseInt(String.valueOf(pjp.getArgs()[2]));
        String methodName = pjp.getSignature().getName();
        Object proceed = null;

        try {
//            @Before
            log.info("Request User ID = " + id + " name = " + name);
            if (id <= 0) {
                throw new IDNotCorrectException("ID start from zero");
            }
            //目標方法invoke
            proceed = pjp.proceed(params);
            log.info("Response = "+proceed);
        } catch (Throwable e) {
        // @AfterThrowing
            log.info("Excpetion Happen! message = "+ e.getMessage());
            if(e instanceof IDNotCorrectException)
                proceed = "ID設定不正確";
        } finally {
        // @After
            log.info("Call API Complete");
        }
        // @AfterReturning
        return proceed;
    }
}

