package my.toy.service

import my.toy.config.JWTProperties
import my.toy.domain.entity.User
import my.toy.domain.repositroy.UserRepository
import my.toy.exception.InvalidJwtTokenException
import my.toy.exception.PasswordNotMatchedException
import my.toy.exception.UserExistsException
import my.toy.exception.UserNotFoundException
import my.toy.model.SignInRequest
import my.toy.model.SignInResponse
import my.toy.model.SignUpRequest
import my.toy.utils.BCryptUtils
import my.toy.utils.JWTClaim
import my.toy.utils.JWTUtils
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class UserService(
    private val userRepository: UserRepository,
    private val jwtProperties: JWTProperties,
    private val cacheManager: CoroutineCacheManager<User>
) {

    companion object {
        private val CACHE_TTL = Duration.ofMinutes(1)
    }

    suspend fun signUp(signUpRequest: SignUpRequest) {
        with(signUpRequest) {
            userRepository.findByEmail(email)?.let{
                throw UserExistsException()
            }

            val user = User(
                email = email,
                password = BCryptUtils.hash(password),
                username = username,
            )

            userRepository.save(user)
        }
    }

    suspend fun signIn(request: SignInRequest): SignInResponse {
        return with(userRepository.findByEmail(request.email) ?: throw UserNotFoundException()){
            val verified = BCryptUtils.verify(request.password, password)
            if(!verified) {
                throw PasswordNotMatchedException()
            }

            val jwtClaim = JWTClaim(
                userId = id!!,
                email = email,
                profileUrl = profileUrl,
                username = username,
            )

            val token = JWTUtils.createToken(claim = jwtClaim, properties = jwtProperties)

            cacheManager.awaitPut(key = token, value = this, ttl = CACHE_TTL)

            SignInResponse(email, username, token)
        }
    }

    suspend fun logout(token: String) {
        cacheManager.awaitEvict(token)
    }

    suspend fun getByToken(token: String): User {
        return cacheManager.awaitGetOrPut(key = token, ttl = CACHE_TTL) {
            val decodedJWT = JWTUtils.decode(token, jwtProperties.secret, jwtProperties.issuer)

            val userId = decodedJWT.claims["userId"]?.asLong() ?: throw InvalidJwtTokenException()

            get(userId)
        }
    }

    private suspend fun get(userId: Long): User {
        return userRepository.findById(userId) ?: throw UserNotFoundException()
    }
}