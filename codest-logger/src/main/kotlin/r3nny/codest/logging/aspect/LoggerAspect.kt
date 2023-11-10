package r3nny.codest.logging.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext

@Aspect
class LoggerAspect {


    @Pointcut("@annotation(LogMethod) && execution(* *(..))")
    fun logMethod() {
    }

    @Around("logMethod()")
    fun logMethod(joinPoint: ProceedingJoinPoint): Any {
        val clazz = joinPoint.signature.declaringType
        val logger: Logger = LoggerFactory.getLogger(clazz)
        val methodName = joinPoint.signature.name

        var args = ""
        for (arg in joinPoint.args.copyOf(joinPoint.args.size - 1)) {
            args += " $arg"
        }

        if (!methodName.contains("suspendImpl"))
            logger.info("method: $methodName > payload = $args")

        return runCatching {
            joinPoint.proceed()
        }.onSuccess {
            if (it.toString() != "COROUTINE_SUSPENDED")
                logger.info("method: ${methodName.replace("suspendImpl", "")} < result = $it}")

        }.onFailure {
            logger.error("method: $methodName! error = $it")
        }.getOrThrow()

    }

}
