package malewicz.jakub.statistics_service.measurements.mappers

import malewicz.jakub.statistics_service.measurements.dtos.MeasurementDetails
import malewicz.jakub.statistics_service.measurements.entities.MeasurementEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import java.time.LocalDateTime
import java.util.*

@Mapper(componentModel = "spring")
abstract class MeasurementMapper {
    @Mapping(target = "id", ignore = true)
    abstract fun toMeasurementEntity(measurementDetails: MeasurementDetails, userId: UUID): MeasurementEntity

    @Mapping(target = "date", source = "date")
    abstract fun toMeasurementDetails(measurement: MeasurementEntity): MeasurementDetails

    protected fun getDateTime(dateTime: LocalDateTime?): LocalDateTime {
        return dateTime ?: LocalDateTime.now()
    }
}