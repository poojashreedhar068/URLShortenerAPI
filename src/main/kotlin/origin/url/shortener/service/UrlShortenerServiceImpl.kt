package origin.url.shortener.service

import origin.url.shortener.helper.UrlShortenerHelper
import org.springframework.stereotype.Service

@Service
class UrlShortenerServiceImpl(
    private val urlShortenerHelper : UrlShortenerHelper,
    private val inMemoryUrlStorageService: InMemoryUrlStorageService
) : UrlShortenerService {

    override fun shortenUrl(originalUrl: String): String {
        val code = urlShortenerHelper.generateCode()
        inMemoryUrlStorageService.save(code, originalUrl)
        return "http://short.ly/$code"

    }

    override fun getOriginalUrl(code: String): String? {
        inMemoryUrlStorageService.findOriginalUrl(code)?.let {
            return it
        } ?: run {
            return null
        }
    }

    override fun checkShortUrlExists(originalUrl: String): String? {
        inMemoryUrlStorageService.findCodeByOriginalUrl(originalUrl)?.let {
            return it
        } ?: run {
            return null
        }
    }

}