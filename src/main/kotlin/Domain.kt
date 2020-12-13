import java.net.URI

class Domain(domain: String) {
    val domain = domain.toLowerCase()

    fun toUri(): URI {
        return URI("http://www.$domain")
    }

    operator fun contains(uri: URI) = uri.host != null
            && domain == uri.host
            .removePrefix("https://")
            .removePrefix("http://")
            .removePrefix("www.")
}