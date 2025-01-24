package malewicz.jakub.workout_service.exceptions

class BadRequestException(override val message: String) : RuntimeException(message)