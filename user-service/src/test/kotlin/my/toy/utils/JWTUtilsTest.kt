package my.toy.utils

import mu.KotlinLogging
import my.toy.config.JWTProperties
import my.toy.domain.entity.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

class JWTUtilsTest(
) {

    private val logger = KotlinLogging.logger{}


    @Test
    fun `test mock `() {
        val mock = mock(User::class.java)

        //tdd -> test (fdfd) ,
    }

    @Test
    fun createTokenTest() {


        val jwtClaim = JWTClaim(
            userId = 1,
            email = "dev@gmail.com",
            profileUrl = "dev.jpg",
            username = "developer"
        )

        val properties = JWTProperties(
            issuer = "jara",
            subject = "auth",
            expiresTime = 3600,
            secret = "my-secret",
        )

        val token = JWTUtils.createToken(jwtClaim, properties)

        assertNotNull(token)

        logger.info("token  : ${token}")
    }

    @Test
    fun `검색한 도서의 저자와 isbn 정보를 가져올 수 있다`() {
        val jwtClaim = JWTClaim(
            userId = 1,
            email = "dev@gmail.com",
            profileUrl = "dev.jpg",
            username = "developer"
        )

        val properties = JWTProperties(
            issuer = "jara",
            subject = "auth",
            expiresTime = 3600,
            secret = "my-secret",
        )

        val token = JWTUtils.createToken(jwtClaim, properties)

        val decode = JWTUtils.decode(token, secret = properties.secret, issuer = properties.issuer)

        with(decode) {
            logger.info("claims : ${claims}")

            val userId = claims["userId"]!!.asLong()
            assertEquals(userId, jwtClaim.userId)

            val email = claims["email"].toString()
            assertEquals(email, jwtClaim.email)

            val profileUrl = claims["profileUrl"].toString()
            assertEquals(profileUrl, jwtClaim.profileUrl)

            val username = claims["username"].toString()
            assertEquals(username, jwtClaim.username)
        }
    }
}