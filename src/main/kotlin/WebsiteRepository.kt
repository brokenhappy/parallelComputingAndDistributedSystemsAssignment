import java.net.URI
import kotlin.jvm.Throws

interface WebsiteRepository {
    class ServerError(message: String, cause: Throwable?): Exception(message, cause)

    @Throws(ServerError::class)
    fun findFor(uri: URI): Website
}