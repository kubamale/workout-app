package malewicz.jakub.user_service.user.mappers

import malewicz.jakub.user_service.authentication.dtos.RegistrationRequest
import malewicz.jakub.user_service.user.entities.UserEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface UserMapper {
    fun toUserEntity(user: RegistrationRequest): UserEntity
}