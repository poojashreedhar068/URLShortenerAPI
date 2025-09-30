package origin.url.shortener.service

import org.springframework.stereotype.Component

@Component
interface UrlShortenerService {

    fun shortenUrl(originalUrl: String): String
    fun getOriginalUrl(code: String): String?
    fun checkShortUrlExists(originalUrl: String): String?

}