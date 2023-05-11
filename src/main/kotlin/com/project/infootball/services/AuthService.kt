package com.project.infootball.services

import com.project.infootball.data_classes.JwtResponse
import com.project.infootball.data_classes.LogInRequest
import com.project.infootball.models.User
import com.project.infootball.repositories.UserRepositoryInterface
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.*
import javax.servlet.http.HttpServletRequest

@Service
class AuthService(
    @Autowired
    private val userRepositoryInterface: UserRepositoryInterface,
    @Value("\${jwt.secret-key}")
    private val key: String? = null,
    @Value("\${jwt.expiration-time}")
    private val expirationNumber: Long? = null
) {
    private var secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key))

    private fun getTokenForUser(user: UserDetails): String {
        return Jwts.builder()
            .signWith(secretKey)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + expirationNumber!!))
            .claim("username", user.username)
            .compact()
    }

    fun verifyTokenAndGetJws(token: String): Jws<Claims> {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
    }

    fun authorizeUserByToken(token: String) {
        val username = verifyTokenAndGetJws(token).body.get("username", String::class.java)
        val user: User = userRepositoryInterface.findByUsername(username)
            ?: throw RuntimeException(String.format("User with username %s in claims of JWT not found"))
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(user, null, user.authorities)
    }

    fun getJwtDtoByAuthRequest(loginRequest: LogInRequest): JwtResponse? {
        val user: User = userRepositoryInterface.findByUsername(loginRequest.username) as User
        return if (BCryptPasswordEncoder(8).matches(loginRequest.password, user.password) && user.isActive) {
            val token = getTokenForUser(user)
            val jws: Jws<Claims> = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
            val zoneId = ZoneId.systemDefault()
            val issuedAt = OffsetDateTime.ofInstant(jws.body.issuedAt.toInstant(), zoneId)
            val expiration = OffsetDateTime.ofInstant(jws.body.expiration.toInstant(), zoneId)
            JwtResponse(token, issuedAt, expiration)
        } else {
            throw RuntimeException("Authentication error!")
        }
    }

    fun getUsernameByRequest(request: HttpServletRequest): String {
        val token: String = request.getHeader(HttpHeaders.AUTHORIZATION)
        return verifyTokenAndGetJws(token).body.get("username", String::class.java)
    }
}