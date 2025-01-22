package malewicz.jakub.user_service.user.repositories

import malewicz.jakub.user_service.user.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<UserEntity, UUID> {
    fun findByEmail(email: String) : UserEntity?
    fun existsByEmail(email: String): Boolean
}