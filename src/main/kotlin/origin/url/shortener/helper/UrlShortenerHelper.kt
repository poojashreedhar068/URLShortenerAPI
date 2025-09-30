package origin.url.shortener.helper

import org.springframework.stereotype.Component
import java.net.MalformedURLException
import java.net.URL
import java.security.SecureRandom

@Component
class UrlShortenerHelper {

    private val characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    private val codeLength = 6
    private val random = SecureRandom()

    fun generateCode(): String {
        return (1..codeLength)
            .map { characters[random.nextInt(characters.length)] }
            .joinToString("")
    }

    fun isValidUrl(url: String): Boolean {
        return try {
            val parsed = URL(url)
            parsed.protocol in listOf("http", "https")
        } catch (e: MalformedURLException) {
            false
        }
    }

    fun fetchShortenedUrlCode(shortenedUrl: String): String {
        return shortenedUrl.substringAfterLast("short.ly/")
    }



}