package origin.url.shortener

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["origin.url.shortener.*"])
class UrlShortenerApplication
//http://localhost:8080/swagger-ui.html
fun main(args: Array<String>) {
	runApplication<UrlShortenerApplication>(*args)
}
