package malewicz.jakub.workout_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class WorkoutServiceApplication

fun main(args: Array<String>) {
	runApplication<WorkoutServiceApplication>(*args)
}
