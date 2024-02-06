package cash.z.ecc.android.crypto

internal expect object Pbkdf2Sha512 {
    /**
     * Generate a derived key from the given parameters.
     *
     * @param p the password
     * @param s the salt
     * @param c the iteration count
     * @param dkLen the key length in bits
     */
    fun derive(
        p: CharArray,
        s: ByteArray,
        c: Int,
        dkLen: Int
    ): ByteArray
}
