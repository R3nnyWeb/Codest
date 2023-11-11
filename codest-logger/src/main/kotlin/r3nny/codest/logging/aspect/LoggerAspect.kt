package r3nny.codest.logging.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.CodeSignature
import org.slf4j.LoggerFactory
import org.slf4j.MDC

@Aspect
class LoggerAspect {


    @Pointcut("@annotation(LogMethod) && execution(* *(..))")
    fun logMethod() {
    }

    @Around("logMethod()")
    fun logMethod(joinPoint: ProceedingJoinPoint): Any {

        beforeExecution(joinPoint)

        return runCatching {
            joinPoint.proceed()
        }.onSuccess {
            logSuccess(it, joinPoint)
        }.onFailure {
            logError(it, joinPoint)
        }.getOrThrow()

    }

    private fun logError(error: Throwable, joinPoint: ProceedingJoinPoint) {
        val codeSignature = joinPoint.signature as CodeSignature
        val className = getClassName(codeSignature)
        val methodName = codeSignature.name
        val log = LoggerFactory.getLogger(codeSignature.declaringType)

        MDC.put("error", error.toString())
        log.error("${className}.$methodName!")
        MDC.remove("error")

    }

    private fun getClassName(
        codeSignature: CodeSignature,
    ) = codeSignature.declaringTypeName.replace("${codeSignature.declaringType.packageName}.", "")

    private fun logSuccess(result: Any, joinPoint: ProceedingJoinPoint) {
        val codeSignature = joinPoint.signature as CodeSignature
        val className = getClassName(codeSignature)
        val methodName = codeSignature.name
        val log = LoggerFactory.getLogger(codeSignature.declaringType)

        if (result.toString() != "COROUTINE_SUSPENDED") {
            MDC.put("result", result.toString())
            log.info("${className}.${methodName.replace("\$suspendImpl", "")} <")
            MDC.remove("result")
        }

    }

    private fun beforeExecution(joinPoint: ProceedingJoinPoint) {
        val codeSignature = joinPoint.signature as CodeSignature
        val className = getClassName(codeSignature)
        val methodName = codeSignature.name
        val parameterNames = codeSignature.parameterNames.filter { it != "\$completion" }
        val parameterValues = joinPoint.args

        val parameters = mutableMapOf<String, Any?>()
        parameterNames.forEachIndexed { index, name ->
            parameters[name] = parameterValues[index]
        }

        if (!methodName.contains("suspendImpl")) {
            val log = LoggerFactory.getLogger(codeSignature.declaringType)

            if (parameters.isNotEmpty())
                MDC.put("payload", parameters.toString())

            log.info("${className}.$methodName >")

            MDC.remove("payload")
        }

    }
}
