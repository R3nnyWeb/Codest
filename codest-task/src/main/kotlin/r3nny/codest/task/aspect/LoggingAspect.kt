package r3nny.codest.task.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class LogMethod()

@Aspect
class LoggingAspect {

    @Pointcut("execution(* r3nny.codest.task.MainKt.*())")
    fun deepThought() {
        println("gagas")
    }

    @Around("deepThought()")
    fun logging(join: ProceedingJoinPoint): Any = println(join.toString())

}