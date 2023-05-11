package com.project.infootball

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("com.project")
class InfootballApplication

fun main(args: Array<String>) {
	runApplication<InfootballApplication>(*args)
}