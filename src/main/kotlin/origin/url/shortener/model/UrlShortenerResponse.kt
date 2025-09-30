package origin.url.shortener.model

import org.springdoc.api.ErrorMessage
import java.time.OffsetDateTime

data class UrlShortenerResponse(
    val shortUrl: String ? = null,
    val errorMessage : ErrorMessage ? = null,
    val created : OffsetDateTime = OffsetDateTime.now()
)