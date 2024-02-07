package cash.z.ecc.android.random

import org.khronos.webgl.ArrayBufferView
import org.khronos.webgl.Uint8Array

external interface Crypto {
    fun <T : ArrayBufferView> getRandomValues(array: T): T
}

actual class SecureRandom {
    private val crypto: Crypto = getCrypto()

    actual fun nextBytes(bytes: ByteArray) {
        crypto.getRandomValues(bytes.unsafeCast<Uint8Array>())
    }
}

// https://github.com/whyoleg/cryptography-kotlin/blob/d524143a0719e6926b0ae190977a7341673fa718/cryptography-random/src/jsMain/kotlin/CryptographyRandom.js.kt
//language=JavaScript
private fun getCrypto(): Crypto {
    return js(
        code = """
    
        var isNodeJs = typeof process !== 'undefined' && process.versions != null && process.versions.node != null
        if (isNodeJs) {
            return (eval('require')('node:crypto').webcrypto);
        } else {
            return (window ? (window.crypto ? window.crypto : window.msCrypto) : self.crypto);
        }
    
               """
    ).unsafeCast<Crypto>()
}
