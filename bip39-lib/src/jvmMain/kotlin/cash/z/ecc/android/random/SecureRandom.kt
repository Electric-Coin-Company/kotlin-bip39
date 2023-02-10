package cash.z.ecc.android.random

actual class SecureRandom {
    private val jvmSecureRandom = java.security.SecureRandom()

    actual fun nextBytes(bytes: ByteArray) {
        jvmSecureRandom.nextBytes(bytes)
    }
}
