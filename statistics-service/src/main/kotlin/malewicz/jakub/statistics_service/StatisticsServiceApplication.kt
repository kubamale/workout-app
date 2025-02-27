package malewicz.jakub.statistics_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class StatisticsServiceApplication

fun main(args: Array<String>) {
	runApplication<StatisticsServiceApplication>(*args)
}
