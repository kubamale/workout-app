package malewicz.jakub.user_service.authentication.validators

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [PasswordValidator::class])
annotation class Password(
    val message: String = "must have at least 8 characters, at least one lowercase and one uppercase letter, and at least one special character",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class PasswordValidator : ConstraintValidator<Password, String> {
    private val passwordPattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$"
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return  value?.matches(Regex(passwordPattern)) ?: false
    }
}
