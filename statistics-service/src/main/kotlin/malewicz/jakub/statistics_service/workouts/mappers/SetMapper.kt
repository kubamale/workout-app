package malewicz.jakub.statistics_service.workouts.mappers

import malewicz.jakub.statistics_service.exceptions.BadRequestException
import malewicz.jakub.statistics_service.workouts.dtos.SetDto
import malewicz.jakub.statistics_service.workouts.dtos.SetType
import malewicz.jakub.statistics_service.workouts.entities.DistanceSetEntity
import malewicz.jakub.statistics_service.workouts.entities.SetEntity
import malewicz.jakub.statistics_service.workouts.entities.TimeSetEntity
import malewicz.jakub.statistics_service.workouts.entities.WeightSetEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
abstract class SetMapper {
    fun toSetEntity(setDto: SetDto): SetEntity {
        return when (setDto.setType) {
            SetType.TIME -> TimeSetEntity(
                setDto.time ?: throw BadRequestException("Time cannot be null."),
                setDto.weight ?: 0.0,
            )

            SetType.WEIGHT -> WeightSetEntity(
                setDto.reps ?: throw BadRequestException("Reps cannot be null."),
                setDto.weight ?: throw BadRequestException("Weight cannot be null."),
            )

            SetType.DISTANCE -> DistanceSetEntity(
                setDto.distance ?: throw BadRequestException("Distance cannot be null.")
            )
        }
    }
}