package testDouble.fake

import Website
import WebsiteRepository
import java.net.URI

class WebsiteFactory(localWebBuilder: LocalWebBuilder.() -> Unit) : WebsiteRepository {
    val localWeb = LocalWebBuilder().also(localWebBuilder).build()

    class LocalWebBuilder {
        private val builtWeb = mutableMapOf<String, Website>()

        class UriAdder(private val uri: URI, private val parent: LocalWebBuilder) {
            infix fun fake(website: Website) {
                parent.builtWeb[uri.toString()] = website
            }
        }

        infix fun on(uri: URI) = UriAdder(uri, this)

        internal fun build(): Map<String, Website> = builtWeb
    }

    override fun findFor(uri: URI) = localWeb[uri.toString()]
        ?: throw IllegalStateException("Uri requested that has not been faked: $uri")

}
