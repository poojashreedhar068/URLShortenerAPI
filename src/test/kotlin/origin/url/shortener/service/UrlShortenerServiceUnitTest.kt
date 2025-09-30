package origin.url.shortener.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import origin.url.shortener.helper.UrlShortenerHelper
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UrlShortenerServiceUnitTest {

    private val helper = mockk<UrlShortenerHelper>()
    private val store = mockk<InMemoryUrlStorageService>(relaxed = true)
    private val service = UrlShortenerServiceImpl(helper, store)

    @Test
    fun `shortenUrl should generate code and save mapping`() {
        val originalUrl = "https://originenergy.com.au/plans.html"
        val code = "a1B2c3"

        every { helper.generateCode() } returns code

        val result = service.shortenUrl(originalUrl)

        assertEquals("http://short.ly/$code", result)
        verify { store.save(code, originalUrl) }
    }

    @Test
    fun `getOriginalUrl should return original URL when code exists`() {
        val code = "a1B2c3"
        val originalUrl = "https://originenergy.com.au/plans.html"

        every { store.findOriginalUrl(code) } returns originalUrl

        val result = service.getOriginalUrl(code)

        assertEquals(originalUrl, result)
    }

    @Test
    fun `getOriginalUrl should return null when code does not exist`() {
        val code = "unknown"

        every { store.findOriginalUrl(code) } returns null

        val result = service.getOriginalUrl(code)

        assertNull(result)
    }

}