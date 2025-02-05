package malewicz.jakub.user_service.user.entities

import jakarta.persistence.*
import java.time.ZonedDateTime
import java.util.*

@Entity(name = "reset_passwords")
class ResetPasswordEntity(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID?,
    @Column(nullable = false, unique = true)
    var token: String,
    @Column(columnDefinition = "TIMESTAMP")
    var expires: ZonedDateTime,
    @ManyToOne(fetch = FetchType.LAZY)
    var user: UserEntity,
) {
    constructor(token: String, expires: ZonedDateTime, user: UserEntity) : this(null, token, expires, user)
}