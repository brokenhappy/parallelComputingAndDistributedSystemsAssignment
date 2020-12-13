print(WebsiteScraper(WebsiteDownloadingRepository())
    .scrapeDepthFirst(Domain("guimp.com"))
    .flatMap { it.split("\\P{L}+".toRegex()) }
    .filter { it.isNotEmpty() }
    .groupBy{ it }
    .mapValues { (_, words) -> words.size }
    .entries
    .sortedWith(compareBy { it.value }))
