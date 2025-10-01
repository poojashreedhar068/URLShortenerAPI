package origin.url.shortener.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springdoc.api.ErrorMessage
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import origin.url.shortener.helper.UrlShortenerHelper
import origin.url.shortener.model.OriginalUrlResponse
import origin.url.shortener.model.UrlShortenerRequest
import origin.url.shortener.model.UrlShortenerResponse
import origin.url.shortener.service.UrlShortenerService

@RestController
@RequestMapping
class UrlShortenerController(
    private val urlShortenerService: UrlShortenerService,
    private val urlShortenerHelper: UrlShortenerHelper
) {

    @PostMapping("/url/shorten")
    fun shortenUrl(@RequestBody body: UrlShortenerRequest): ResponseEntity<UrlShortenerResponse> {
        val originalUrl = body.url
        if (!urlShortenerHelper.isValidUrl(originalUrl)) {
            return ResponseEntity.badRequest().body(UrlShortenerResponse(
                errorMessage = ErrorMessage("Invalid URL format, please provide a valid URL.")
            ))
        }

        urlShortenerService.checkShortUrlExists(originalUrl)?.let {
            return ResponseEntity.ok(UrlShortenerResponse(shortUrl = "http://short.ly/$it"))
        }
        val shortenedUrl = urlShortenerService.shortenUrl(originalUrl)
        return ResponseEntity.ok(UrlShortenerResponse(shortUrl = shortenedUrl))
    }

    @GetMapping("/original")
    fun getOriginalUrl(@RequestParam("shortUrl") shortUrl : String): ResponseEntity<OriginalUrlResponse> {
        val code = urlShortenerHelper.fetchShortenedUrlCode(shortUrl)
        val originalUrl = urlShortenerService.getOriginalUrl(code)
        originalUrl?.let {
            return ResponseEntity.ok(OriginalUrlResponse(originalUrl = it))
        } ?: run {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(OriginalUrlResponse(
                errorMessage = ErrorMessage("Original URL not found. Please check the shortened URL.")
            ))
        }
    }

    @GetMapping("/{code}")
    fun redirect(@PathVariable code: String, response: HttpServletResponse) {
        val mapping = urlShortenerService.getOriginalUrl(code)
        mapping?.let {
            response.sendRedirect(it)
        } ?: run {
            response.status = HttpServletResponse.SC_NOT_FOUND
        }
    }


}
