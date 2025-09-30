package origin.url.shortener.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InMemoryUrlStorageServiceUnitTest {
    private lateinit var service: InMemoryUrlStorageService
    private val testFile = File("test_url_store.json")

    @BeforeEach
    fun setup() {
        service = InMemoryUrlStorageService().apply {
            // Inject test file and objectMapper manually
            val fieldFile = this::class.java.getDeclaredField("file")
            fieldFile.isAccessible = true
            fieldFile.set(this, testFile)

            val fieldMap = this::class.java.getDeclaredField("urlToCode")
            fieldMap.isAccessible = true
            fieldMap.set(this, java.util.concurrent.ConcurrentHashMap<String, String>())
        }
    }

    @AfterEach
    fun cleanup() {
        if (testFile.exists()) testFile.delete()
    }

    @Test
    fun `save should store mapping and findOriginalUrl should retrieve it`() {
        val code = "abc123"
        val originalUrl = "https://originenergy.com.au/plans.html"

        service.save(code, originalUrl)
        val result = service.findOriginalUrl(code)

        assertEquals(originalUrl, result)
    }

    @Test
    fun `findOriginalUrl should return null for unknown code`() {
        val result = service.findOriginalUrl("unknown")
        assertNull(result)
    }

    @Test
    fun `persistStoreToFile should write mappings to file`() {
        val code = "xyz789"
        val originalUrl = "https://example.com"

        service.save(code, originalUrl)
        service.persistStoreToFile()

        val content = testFile.readText()
        val map = jacksonObjectMapper().readValue(content, Map::class.java)

        assertEquals(originalUrl, map[code])
    }

    @Test
    fun `loadStoreFromFile should restore mappings from file`() {
        val code = "load123"
        val originalUrl = "https://load.com"
        val map = mapOf(code to originalUrl)

        testFile.writeText(jacksonObjectMapper().writeValueAsString(map))

        service.loadStoreFromFile()
        val result = service.findOriginalUrl(code)

        assertEquals(originalUrl, result)
    }

}