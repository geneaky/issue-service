package my.toy.domain.repositroy

import my.toy.domain.entity.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository : CoroutineCrudRepository<User, Long>{

    suspend fun findByEmail(email: String): User?
}