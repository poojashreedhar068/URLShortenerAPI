package origin.url.shortener.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.stereotype.Component
import java.io.File
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemoryUrlStorageService {
    private val urlToCode = ConcurrentHashMap<String, String>()
    private val file = File("url_store.json")
    private val objectMapper = jacksonObjectMapper()

    @PostConstruct
    fun loadStoreFromFile() {
        if (file.exists()) {
            val map: Map<String, String> = objectMapper.readValue(file)
            urlToCode.putAll(map)
        }
    }

    @PreDestroy
    fun persistStoreToFile() {
        val json = objectMapper.writeValueAsString(urlToCode)
        file.writeText(json)
    }

    fun save(code: String, originalUrl: String) {
        urlToCode[code] = originalUrl
    }

    fun findOriginalUrl(code: String): String? = urlToCode[code]

    fun findCodeByOriginalUrl(originalUrl: String): String? {
        return urlToCode.entries.find { it.value == originalUrl }?.key
    }

}