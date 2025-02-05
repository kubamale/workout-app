package malewicz.jakub.user_service.user.repositories

import malewicz.jakub.user_service.user.entities.ResetPasswordEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ForgotPasswordRepository : JpaRepository<ResetPasswordEntity, UUID> {
    @EntityGraph(attributePaths = ["user"])
    fun findByToken(token: String): Optional<ResetPasswordEntity>
}