import kotlin.system.measureNanoTime

println(
    measureNanoTime {
        println(WebsiteScraper(WebsiteDownloadingRepository())
            .scrape(Domain("guimp.com"))
            .flatMap { it.split("\\P{L}+".toRegex()) }
            .filter { it.isNotEmpty() }
            .groupBy{ it }
            .mapValues { (_, words) -> words.size }
            .entries
            .sortedWith(compareBy { it.value }))
    } / 1_000_000_000.0
)
