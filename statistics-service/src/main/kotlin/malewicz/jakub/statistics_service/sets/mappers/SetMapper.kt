package malewicz.jakub.statistics_service.sets.mappers

import malewicz.jakub.statistics_service.sets.dtos.SetDto
import malewicz.jakub.statistics_service.sets.entities.DistanceSetEntity
import malewicz.jakub.statistics_service.sets.entities.TimeSetEntity
import malewicz.jakub.statistics_service.sets.entities.WeightSetEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.NullValueCheckStrategy

@Mapper(componentModel = "spring")
interface SetMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(
    target = "weight",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    defaultExpression = "java(throw new malewicz.jakub.statistics_service.exceptions.throw BadRequestException(\"Weight cannot be null.\"))"
  )
  @Mapping(
    target = "reps",
    defaultExpression = "java(throw new malewicz.jakub.statistics_service.exceptions.throw BadRequestException(\"Reps cannot be null.\"))"
  )
  fun toWeightSetEntity(setDto: List<SetDto>): MutableList<WeightSetEntity>

  @Mapping(target = "id", ignore = true)
  @Mapping(
    target = "distance",
    defaultExpression = "java(throw new malewicz.jakub.statistics_service.exceptions.throw BadRequestException(\"Distance cannot be null.\"))"
  )
  fun toDistanceSetEntity(setDto: List<SetDto>): MutableList<DistanceSetEntity>

  @Mapping(target = "id", ignore = true)
  @Mapping(
    target = "weight",
    defaultValue = "0.0"
  )
  @Mapping(
    target = "time",
    defaultExpression = "java(throw new malewicz.jakub.statistics_service.exceptions.throw BadRequestException(\"Time cannot be null.\"))"
  )
  fun toTimeSetEntity(setDto: List<SetDto>): MutableList<TimeSetEntity>
}