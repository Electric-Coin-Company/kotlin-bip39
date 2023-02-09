package cash.z.ecc.android.crypto

/**
 *
 * This is a clean-room implementation of PBKDF2 using RFC 2898 as a reference.
 *
 *
 * RFC 2898: http://tools.ietf.org/html/rfc2898#section-5.2
 *
 *
 * This code passes all RFC 6070 test vectors: http://tools.ietf.org/html/rfc6070
 *
 *
 * http://cryptofreek.org/2012/11/29/pbkdf2-pure-java-implementation/<br></br>
 * Modified to use SHA-512 - Ken Sedgwick ken@bonsai.com
 * Modified to for Kotlin - Kevin Gorham anothergmale@gmail.com
 */
expect object Pbkdf2Sha512 {

    /**
     * Generate a derived key from the given parameters.
     *
     * @param p the password
     * @param s the salt
     * @param c the iteration count
     * @param dkLen the key length in bits
     */
    fun derive(p: CharArray, s: ByteArray, c: Int, dkLen: Int): ByteArray

    internal fun F(p: ByteArray, s: ByteArray, c: Int, i: Int): ByteArray
}
