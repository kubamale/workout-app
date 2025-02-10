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
    @Enumerated(EnumType.ORDINAL)
    var lengthUnits: LengthUnits,
    var active: Boolean = false,
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    var resetPasswords: MutableList<ResetPasswordEntity> = mutableListOf(),
)

enum class WeightUnits {
    KG, LB
}

enum class LengthUnits {
    CM, IN
}