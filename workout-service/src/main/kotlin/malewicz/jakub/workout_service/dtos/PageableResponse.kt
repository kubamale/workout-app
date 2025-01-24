package malewicz.jakub.workout_service.dtos

data class PageableResponse<T>(
    val totalElements: Long,
    val page: Int,
    val size: Int,
    val hasNextPage: Boolean,
    val totalPages: Int,
    val results: List<T>
)
