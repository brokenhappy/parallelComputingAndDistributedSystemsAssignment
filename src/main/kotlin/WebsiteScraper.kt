import java.net.URI

class WebsiteScraper(private val websiteFactory: WebsiteRepository) {
    class ScrapingContext(val domain: Domain) {
        val visitedUris = mutableSetOf<URI>()
    }

    fun scrapeDepthFirst(domain: Domain) = scrapeDepthFirst(domain.toUri(), ScrapingContext(domain))

    private fun scrapeDepthFirst(uri: URI, context: ScrapingContext): Sequence<String> = sequence {
        if (uri in context.visitedUris)
            return@sequence
        context.visitedUris.add(uri)

        try {
            val site = websiteFactory.findFor(uri)
            site.links
                .filter { it in context.domain }
                .forEach { yieldAll(scrapeDepthFirst(it, context)) }
            yieldAll(site.texts)
        } catch (exception: WebsiteRepository.ServerError) {
            // Lets ignore yeet
        }
    }
}
