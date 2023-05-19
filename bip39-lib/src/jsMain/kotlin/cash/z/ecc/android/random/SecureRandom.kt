package cash.z.ecc.android.random

import kotlinx.browser.window
import org.w3c.dom.get

actual class SecureRandom {

    var crypto: dynamic = null

    init {
        crypto = try {
            // browser
            window["crypto"]
        } catch (_: Throwable) {
            null
        }
        if (crypto == null) {
            try {
                // node
                crypto = js("""require("crypto")""").webcrypto
            } catch (_: Throwable) {
            }
        }
        requireNotNull(crypto) { "Could not get crypto from window or node" }
    }

    actual fun nextBytes(bytes: ByteArray) {
        crypto.getRandomValues(bytes)
    }
}
