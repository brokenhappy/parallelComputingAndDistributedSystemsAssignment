import org.junit.jupiter.api.Test
import testDouble.fake.WebsiteFactory
import java.net.URI
import kotlin.test.assertEquals

class WebsiteScraperTest {
    @Test
    fun `test that it iterates over no items if the website contains no links`() {
        val scraper = WebsiteScraper(WebsiteFactory {
            on(URI("http://www.fake.com")) fake Website(
                links = listOf(),
                texts = listOf(),
            )
        })

        assertEquals(listOf(), scraper.scrapeDepthFirst(Domain("fake.com")).toList())
    }

    @Test
    fun `test that texts on index page are scraped`() {
        val scraper = WebsiteScraper(WebsiteFactory {
            on(URI("http://www.john.doe")) fake Website(
                links = listOf(),
                texts = listOf("foo", "bar"),
            )
        })

        assertEquals(
            listOf("foo", "bar").sorted(),
            scraper.scrapeDepthFirst(Domain("john.doe")).toList().sorted()
        )
    }

    @Test
    fun `test that underlying external redirects are skipped`() {
        val scraper = WebsiteScraper(WebsiteFactory {
            on(URI("http://www.john.doe")) fake Website(
                links = listOf(URI("http://www.cornhub.com/corny"), URI("http://www.jikes.no")),
                texts = listOf("foo", "bar"),
            )
            on(URI("http://www.cornhub.com/corny")) fake Website(
                links = listOf(),
                texts = listOf("these should", "not be shown"),
            )
        })

        assertEquals(
            listOf("foo", "bar").sorted(),
            scraper.scrapeDepthFirst(Domain("john.doe")).toList().sorted()
        )
    }

    @Test
    fun `test that underlying internal redirects are scraped too`() {
        val scraper = WebsiteScraper(WebsiteFactory {
            on(URI("http://www.john.doe")) fake Website(
                links = listOf(URI("http://www.john.doe/faq")),
                texts = listOf("foo", "bar"),
            )
            on(URI("http://www.john.doe/faq")) fake Website(
                links = listOf(URI("http://www.john.doe/memes")),
                texts = listOf("how are you so", "extremely kewl"),
            )
            on(URI("http://www.john.doe/memes")) fake Website(
                links = listOf(),
                texts = listOf("lol", "lel"),
            )
        })

        assertEquals(
            listOf("foo", "bar", "how are you so", "extremely kewl", "lol", "lel").sorted(),
            scraper.scrapeDepthFirst(Domain("john.doe")).toList().sorted()
        )
    }

    @Test
    fun `test that recursive references are handled too`() {
        val scraper = WebsiteScraper(WebsiteFactory {
            on(URI("http://www.john.doe")) fake Website(
                links = listOf(URI("http://www.john.doe/faq")),
                texts = listOf("foo", "bar"),
            )
            on(URI("http://www.john.doe/faq")) fake Website(
                links = listOf(URI("http://www.john.doe")),
                texts = listOf("baz"),
            )
        })

        assertEquals(
            listOf("foo", "bar", "baz").sorted(),
            scraper.scrapeDepthFirst(Domain("john.doe")).toList().sorted()
        )
    }
}