package cash.z.ecc.android.random

expect class SecureRandom {
    fun nextBytes(bytes: ByteArray)
}
