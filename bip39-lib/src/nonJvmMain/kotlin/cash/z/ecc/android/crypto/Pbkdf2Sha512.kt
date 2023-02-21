package cash.z.ecc.android.crypto

import okio.ByteString.Companion.toByteString
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
 * Modified to for Kotlin Multiplatform w/ okio - Luca Spinazzola anothergmale@gmail.com
 */
internal actual object Pbkdf2Sha512 {

    /**
     * The size of Tn in bytes. Which will always be 64 bytes, as it is the xor of hmacSha512.
     */
    private const val tnLen = 64

    /**
     * Generate a derived key from the given parameters.
     *
     * @param p the password
     * @param s the salt
     * @param c the iteration count
     * @param dkLen the key length in bits
     */
    actual fun derive(p: CharArray, s: ByteArray, c: Int, dkLen: Int): ByteArray {
        val dkLenBytes = dkLen / 8
        val pBytes = p.foldIndexed(ByteArray(p.size)) { i, acc, cc ->
            acc.apply { this[i] = cc.code.toByte() }
        }
        val hLen = 20.0
        // note: dropped length check because it's redundant, given the size of an int in kotlin
        val l = ceil(dkLenBytes / hLen).toInt()
        val baos = ByteArray(l * tnLen)
        for (i in 1..l) {
            f(pBytes, s, c, i).let { Tn ->
                Tn.copyInto(baos, (i - 1) * tnLen)
            }
        }
        return baos.sliceArray(0 until dkLenBytes)
    }

    private fun f(
        p: ByteArray,
        s: ByteArray,
        c: Int,
        i: Int
    ): ByteArray {
        val key = p.toByteString()
        val bU = ByteArray(s.size + 4)

        // concat s and i into array bU w/o additional allocations
        s.copyInto(bU, 0, 0, s.size)
        repeat(4) { j ->
            bU[s.size + j] = (i shr (24 - 8 * j)).toByte()
        }

        val uXor = bU.toByteString().hmacSha512(key).toByteArray()
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
