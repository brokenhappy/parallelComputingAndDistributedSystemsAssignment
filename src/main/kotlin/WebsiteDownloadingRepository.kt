import org.jsoup.Jsoup
import java.io.IOException
import java.net.URI
import java.net.URL

class WebsiteDownloadingRepository : WebsiteRepository {
    override fun findFor(uri: URI): Website {
        try {
            val doc = Jsoup.connect(uri.toString()).get()
            return Website(
                links = doc.select("a[href]")
                    .map { it.attr("href") }
                    .filter { it.isNotEmpty() }
                    .map { URI(URL(URL(uri.toString()), it).toString()) },
                texts = listOf(doc.text()),
            )
        } catch (exception: IOException) {
            throw WebsiteRepository.ServerError("$uri could not be downloaded", exception)
        }
    }
}
