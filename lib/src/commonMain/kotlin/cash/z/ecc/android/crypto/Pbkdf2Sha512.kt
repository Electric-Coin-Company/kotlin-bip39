package cash.z.ecc.android.crypto

import okio.Buffer
import okio.ByteString.Companion.toByteString
import okio.use
import kotlin.experimental.xor
import kotlin.math.ceil

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
object Pbkdf2Sha512 {

    /**
     * Generate a derived key from the given parameters.
     *
     * @param p the password
     * @param s the salt
     * @param c the iteration count
     * @param dkLen the key length in bits
     */
    fun derive(p: CharArray, s: ByteArray, c: Int, dkLen: Int): ByteArray {
        Buffer().use { baos ->
            val dkLenBytes = dkLen/8
            val pBytes = p.foldIndexed(ByteArray(p.size)) { i, acc, c ->
                acc.apply { this[i] = c.code.toByte() }
            }
            val hLen = 20.0
            // note: dropped length check because it's redundant, given the size of an int in kotlin
            val l = ceil(dkLenBytes / hLen).toInt()
            for (i in 1..l) {
                F(pBytes, s, c, i).let { Tn ->
                    baos.write(Tn)
                }
            }
            return ByteArray(dkLenBytes).apply {
                baos.read(this)
            }
        }
    }

    private fun F(p: ByteArray, s: ByteArray, c: Int, i: Int): ByteArray {
        val key = p.toByteString()
        val bU = ByteArray(s.size + 4)

        // concat s and i into array bU w/o additional allocations
        s.copyInto(bU, 0, 0, s.size)
        repeat(4) { j ->
            bU[s.size + j] = (i shr (24 - 8 * j)).toByte()
        }

        var uXor = bU.toByteString().hmacSha512(key).toByteArray()
        var uLast = uXor

        repeat(c - 1) {
            val baU = uLast.toByteString().hmacSha512(key).toByteArray()
            uXor.forEachIndexed { k, b ->
                uXor[k] = (b.xor(baU[k]))
            }
            uLast = baU
        }
        return uXor
    }

}
