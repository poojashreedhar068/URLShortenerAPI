package origin.url.shortener.model

import org.springdoc.api.ErrorMessage
import java.time.OffsetDateTime

data class OriginalUrlResponse(
    val originalUrl: String? = null,
    val errorMessage : ErrorMessage? = null,
    val created : OffsetDateTime = OffsetDateTime.now()
)