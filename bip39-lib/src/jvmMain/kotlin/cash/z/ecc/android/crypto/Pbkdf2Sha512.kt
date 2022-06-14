package cash.z.ecc.android.crypto

import java.io.ByteArrayOutputStream
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
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
        ByteArrayOutputStream().use { baos ->
            val dkLenBytes = dkLen / 8
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
                System.arraycopy(baos.toByteArray(), 0, this, 0, size)
            }
        }
    }

    private fun F(p: ByteArray, s: ByteArray, c: Int, i: Int): ByteArray {
        val key = SecretKeySpec(p, "HmacSHA512")
        val mac = Mac.getInstance(key.algorithm).apply { init(key) }
        val bU = ByteArray(s.size + 4)

        // concat s and i into array bU w/o additional allocations
        System.arraycopy(s, 0, bU, 0, s.size)
        repeat(4) { j ->
            bU[s.size + j] = (i shr (24 - 8 * j)).toByte()
        }

        val uXor = mac.doFinal(bU)
        var uLast = uXor
        mac.reset()

        repeat(c - 1) {
            val baU = mac.doFinal(uLast)
            mac.reset()
            uXor.forEachIndexed { k, b ->
                uXor[k] = (b.xor(baU[k]))
            }
            uLast = baU
        }
        return uXor
    }
}
