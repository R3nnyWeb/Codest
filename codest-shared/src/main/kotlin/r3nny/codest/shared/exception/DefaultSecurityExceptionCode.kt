package r3nny.codest.shared.exception

enum class DefaultSecurityExceptionCode(
    override val errorCode: String,
    override val message: String
): ExceptionCode<SecurityException>
{
    UNAUTHORIZED("Unauthorized", "Unauthorized"),
    FORBIDDEN("Forbidden", "Недостаточно прав")
}