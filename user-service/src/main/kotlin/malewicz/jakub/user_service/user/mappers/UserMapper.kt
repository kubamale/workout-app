package malewicz.jakub.user_service.user.mappers

import malewicz.jakub.user_service.authentication.dtos.RegistrationRequest
import malewicz.jakub.user_service.user.entities.UserEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface UserMapper {

    @Mapping(target = "resetPasswords", expression = "java(new java.util.ArrayList())")
    fun toUserEntity(user: RegistrationRequest): UserEntity
}