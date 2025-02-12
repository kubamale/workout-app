package malewicz.jakub.exercise_service.exceptions

class BadRequestException(override val message: String) : RuntimeException(message)