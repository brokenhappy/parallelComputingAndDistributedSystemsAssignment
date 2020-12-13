import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.net.URI
import kotlin.test.assertEquals

class WebsiteDownloadingRepositoryTest {
    @Tag("internetAccesTest")
    @Test
    fun `test guimp`() {
        assertEquals(
            Website(
                links = listOf(URI("http://www.guimp.com/home.html")),
                texts = listOf("smallest website in the world! guimp.com"),
            ),
            WebsiteDownloadingRepository().findFor(URI("http://www.guimp.com/"))
        )
    }
}