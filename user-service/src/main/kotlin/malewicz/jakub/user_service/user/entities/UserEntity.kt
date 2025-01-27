package malewicz.jakub.user_service.user.entities

import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    var email: String,
    var firstName: String,
    var lastName: String,
    var password: String,
    var dateOfBirth: LocalDate,
    @Enumerated(EnumType.ORDINAL)
    var weightUnits: WeightUnits,
    var active: Boolean = false,
)

enum class WeightUnits {
    KG, LB
}