package malewicz.jakub.user_service.exceptions

class ResourceNotFoundException(override val message: String) : RuntimeException(message){
}