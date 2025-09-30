package origin.url.shortener.helper

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class UrlShortenerHelperTest {

    @Autowired
    private val helper: UrlShortenerHelper = UrlShortenerHelper()

    @Test
    fun `isValidUrl should return true for valid http and https URLs`() {
        assertTrue(helper.isValidUrl("http://example.com"))
        assertTrue(helper.isValidUrl("https://www.originenergy.com.au/plans.html"))
    }

    @Test
    fun `isValidUrl should return false for invalid URLs`() {
        assertFalse(helper.isValidUrl("htp:/bad-url"))
        assertFalse(helper.isValidUrl("ftp://example.com"))
        assertFalse(helper.isValidUrl("just-text"))
        assertFalse(helper.isValidUrl(""))
    }

    @RepeatedTest(10)
    fun `generateCode should return 6-character alphanumeric string`() {
        val code = helper.generateCode()
        assertEquals(6, code.length)
        assertTrue(code.all { it.isLetterOrDigit() })
    }

    @Test
    fun `fetchShortenedUrlCode should extract code from shortened URL`() {
        val shortenedUrl = "http://short.ly/a1B2c3"
        val code = helper.fetchShortenedUrlCode(shortenedUrl)
        assertEquals("a1B2c3", code)
    }

    @Test
    fun `fetchShortenedUrlCode should return full string if delimiter not found`() {
        val malformedUrl = "http://example.com/a1B2c3"
        val code = helper.fetchShortenedUrlCode(malformedUrl)
        assertEquals("http://example.com/a1B2c3", code)
    }
}