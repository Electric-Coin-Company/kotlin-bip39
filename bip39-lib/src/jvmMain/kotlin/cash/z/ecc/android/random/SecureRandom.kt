package cash.z.ecc.android.random

actual class SecureRandom {
    private val secureRandom = java.security.SecureRandom()

    actual fun nextBytes(bytes: ByteArray) {
        secureRandom.nextBytes(bytes)
    }
}
