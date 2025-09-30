package origin.url.shortener.model

import org.jetbrains.annotations.NotNull

data class UrlShortenerRequest(
    @get:NotNull
    val url: String
)