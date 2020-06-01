package cash.z.ecc.android.bip39

import java.security.SecureRandom

class Mnemonics {
    private val secureRandom = SecureRandom()

    fun createEntropy(words: WordCount): ByteArray = ByteArray(words.bitLength).apply {
        secureRandom.nextBytes(this)
    }

    enum class WordCount(val count: Int) {
        COUNT_12(12), COUNT_15(15), COUNT_18(18), COUNT_21(21), COUNT_24(24);

        val bitLength = count / 3 * 32

        companion object {
            fun valueOf(count: Int): WordCount? {
                values().forEach {
                    if (it.count == count) return it
                }
                return null
            }
        }
    }

    companion object {
        fun toSeed(mnemonic: CharArray): ByteArray {
            return ByteArray(256)
        }
    }
}


//
// Extensions
//

fun CharArray.toSeed(): ByteArray = Mnemonics.toSeed(this)
