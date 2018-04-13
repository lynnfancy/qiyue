package com.qiyue.aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class TimeAspect {
//    @AfterReturning("execution(* com.mycompany.financial.nirvana..*Mapper.*(..))")
//    public void logServiceAccess(JoinPoint joinPoint) {
//        logger.info("Completed: " + joinPoint);
//    }


	 /**
     * 定义一个切入点.
     * 解释下：
     *
     * ~ 第一个 * 代表任意修饰符及任意返回值.
     * ~ 第二个 * 定义在web包或者子包
     * ~ 第三个 * 任意方法
     * ~ .. 匹配任意数量的参数.
     */
    @Pointcut("execution(* com.qiyue.service.announcement..*.*(..))")
    private void pointCutMethod() {
    }

    /**
     * 声明环绕通知
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("pointCutMethod()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long begin = System.nanoTime();
        Object obj = pjp.proceed();
        long end = System.nanoTime();

        log.info("调用service.announcement方法：{},\n参数:{},\n执行耗时:{}纳秒,\r\n耗时:{}毫秒",
                pjp.getSignature().toString(), Arrays.toString(pjp.getArgs()),(end - begin), (end - begin) / 1000000);
        return obj;
    }
}
