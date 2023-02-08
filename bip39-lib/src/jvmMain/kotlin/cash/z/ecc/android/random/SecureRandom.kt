package cash.z.ecc.android.random

actual class SecureRandom actual constructor() {
    val secureRandom = java.security.SecureRandom()

    actual fun nextBytes(bytes: ByteArray) {
        secureRandom.nextBytes(bytes)
    }
}