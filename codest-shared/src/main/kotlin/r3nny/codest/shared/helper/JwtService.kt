package r3nny.codest.shared.helper

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import r3nny.codest.shared.exception.DefaultSecurityExceptionCode
import r3nny.codest.shared.exception.throwSecurityException
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


class JwtService(
    private val secret: String,
) {
    private val signKey = getSignKey()
    fun generateToken(userId: UUID): String {
        val expirationDateTime = LocalDateTime.now().plusDays(20)

        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date.from(expirationDateTime.atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(signKey, SignatureAlgorithm.HS256).compact()
    }

    fun getUserId(token: String): UUID {
        return runCatching {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).body.subject.toUUID()
        }.onFailure {
            throwSecurityException(
                DefaultSecurityExceptionCode.UNAUTHORIZED,
            )
        }.getOrThrow()
    }

    fun getSignKey(): Key {
        val keyBytes = Decoders.BASE64.decode(secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}

private fun String.toUUID(): UUID {
  return  UUID.fromString(this)
}
