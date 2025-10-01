package origin.url.shortener.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import origin.url.shortener.helper.UrlShortenerHelper
import origin.url.shortener.model.UrlShortenerRequest
import origin.url.shortener.service.UrlShortenerService
import kotlin.test.Test


@WebMvcTest
class UrlShortenerControllerUnitTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var urlShortenerService: UrlShortenerService

    @MockkBean
    private lateinit var urlShortenerHelper: UrlShortenerHelper

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `shortenUrl should return shortened URL when input is valid`() {
        val originalUrl = "https://www.originenergy.com.au/electricity-gas/plans.html"
        val shortenedUrl = "http://short.ly/a1B2c3"

        every { urlShortenerHelper.isValidUrl(originalUrl) } returns true
        every { urlShortenerService.shortenUrl(originalUrl) } returns shortenedUrl
        every { urlShortenerService.checkShortUrlExists(originalUrl)} returns null

        val request = UrlShortenerRequest(originalUrl)

        mockMvc.perform(post("/url/shorten")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)
            ))
            .andExpect(status().isOk)
            .andExpect (jsonPath("$.shortUrl").value(shortenedUrl))
    }


    @Test
    fun `shortenUrl should return 400 when input is invalid`() {
        val invalidUrl = "htp:/bad-url"

        every { urlShortenerHelper.isValidUrl(invalidUrl) } returns false

        val request = UrlShortenerRequest(invalidUrl)
        mockMvc.perform(post("/url/shorten")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)
            ))
            .andExpect(status().isBadRequest)

    }

    @Test
    fun `getOriginalUrl should return original URL when code is valid`() {
        val shortenedUrl = "http://short.ly/a1B2c3"
        val code = "a1B2c3"
        val originalUrl = "https://originenergy.com.au/plans.html"

        every { urlShortenerHelper.fetchShortenedUrlCode(any()) } returns code
        every { urlShortenerService.getOriginalUrl(any()) } returns originalUrl

        val request = UrlShortenerRequest(shortenedUrl)

        mockMvc.perform(get("/original")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .queryParam("shortUrl", shortenedUrl))
            .andExpect(status().isOk)
            .andExpect (jsonPath("$.originalUrl").value(originalUrl))
    }

    @Test
    fun `getOriginalUrl should return 404 when code is not found`() {
        val shortenedUrl = "http://short.ly/unknown"
        val code = "unknown"

        every { urlShortenerHelper.fetchShortenedUrlCode(shortenedUrl) } returns code
        every { urlShortenerService.getOriginalUrl(code) } returns null

        val request = UrlShortenerRequest(shortenedUrl)

        mockMvc.perform(get("/original")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .queryParam("shortUrl", shortenedUrl))
            .andExpect(status().isNotFound)

    }

    @Test
    fun `redirect should send 302 to original URL when code is valid`() {
        val code = "a1B2c3"
        val originalUrl = "https://www.originenergy.com.au/electricity-gas/plans.html"

        every { urlShortenerService.getOriginalUrl(code) } returns originalUrl

        mockMvc.perform(get("/$code"))
            .andExpect(status().isFound)
            .andExpect(redirectedUrl(originalUrl))
            .andExpect(header().string("Location", originalUrl))
    }

    @Test
    fun `redirect should return 404 when code is not found`() {
        val code = "unknown"

        every { urlShortenerService.getOriginalUrl(code) } returns null

        mockMvc.perform(get("/$code"))
            .andExpect(status().isNotFound)
    }

}