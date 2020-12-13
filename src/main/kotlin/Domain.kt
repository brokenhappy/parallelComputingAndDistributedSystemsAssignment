import org.jetbrains.annotations.Contract
import java.net.URI

class Domain(domain: String) {
    val domain = domain.toLowerCase()

    @Contract(pure = true)
    fun toUri(): URI {
        return URI("http://www.$domain")
    }

    @Contract(pure = true)
    operator fun contains(uri: URI) = uri.host != null
            && domain == uri.host
            .removePrefix("https://")
            .removePrefix("http://")
            .removePrefix("www.")
}