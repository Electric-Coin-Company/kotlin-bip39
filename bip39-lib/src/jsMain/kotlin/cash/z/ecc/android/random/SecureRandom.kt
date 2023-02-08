package cash.z.ecc.android.random

import kotlinx.browser.window
import org.w3c.dom.get

actual class SecureRandom actual constructor() {

    var crypto : dynamic = null

    init {
        try {
            // node
            crypto = js("""require("crypto")""").webcrypto
        } catch (_: Throwable){}
        try {
            // browser
            crypto = window["crypto"]
        } catch (_: Throwable){}
    }

    actual fun nextBytes(bytes: ByteArray) {
        crypto.getRandomValues(bytes)
            println(bytes)
    }
}