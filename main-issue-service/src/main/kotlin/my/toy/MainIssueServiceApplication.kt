package my.toy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class MainIssueServiceApplication

fun main(args: Array<String>) {
    runApplication<MainIssueServiceApplication>(*args)
}