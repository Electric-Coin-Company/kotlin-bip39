package cash.z.ecc.android.random

actual class SecureRandom {
    val secureRandom = java.security.SecureRandom()

    actual fun nextBytes(bytes: ByteArray) = secureRandom.nextBytes(bytes)

}