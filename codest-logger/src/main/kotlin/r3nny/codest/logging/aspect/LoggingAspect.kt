package r3nny.codest.logging.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect

@Aspect
class LoggingAspect {

    @Around("@annotation(r3nny.codest.shared.annotation.LogMethod)")
    fun log(join: ProceedingJoinPoint) {
        println("USSS");
    }

}