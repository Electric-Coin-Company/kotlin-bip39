package cash.z.ecc.android.bip39

import cash.z.ecc.android.bip39.Mnemonics.MnemonicCode
import cash.z.ecc.android.bip39.Mnemonics.WordCount
import java.io.Closeable
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or

/**
 * Encompasses all mnemonic functionality, which helps keep everything concise and in one place.
 */
object Mnemonics {

    internal val secureRandom = SecureRandom()
    internal var cachedList = WordList()

    fun getCachedWords(languageCode: String): List<String> {
        if (cachedList.languageCode != languageCode) {
            cachedList = WordList(languageCode)
        }
        return cachedList.words
    }


    //
    // Inner Classes
    //

    class MnemonicCode(val chars: CharArray, val languageCode: String = Locale.ENGLISH.language) :
        Closeable, Iterable<String> {

        constructor(
            phrase: String,
            languageCode: String = Locale.ENGLISH.language
        ) : this(phrase.toCharArray(), languageCode)

        constructor(
            entropy: ByteArray,
            languageCode: String = Locale.ENGLISH.language
        ) : this(fromEntropy(entropy), languageCode)

        constructor(
            wordCount: WordCount,
            languageCode: String = Locale.ENGLISH.language
        ) : this(fromEntropy(wordCount.toEntropy()), languageCode)

        override fun close() = clear()

        val wordCount get() = chars.count { it == ' ' }.let { if (it == 0) it else it + 1 }

        val words: List<CharArray> get() {
            val wordList = mutableListOf<CharArray>()
            var cursor = 0
            repeat(chars.size) { i ->
                val isSpace = chars[i] == ' '
                if (isSpace || i == (chars.size - 1)) {
                    val wordSize = i - cursor + if (isSpace) 0 else 1
                    wordList.add(CharArray(wordSize).apply {
                        repeat(wordSize) {
                            this[it] = chars[cursor + it]
                        }
                    })
                    cursor = i + 1
                }
            }
            return wordList
        }

        fun clear() = chars.fill(0.toChar())

        fun isEmpty() = chars.isEmpty()


        override fun iterator(): Iterator<String> = object : Iterator<String> {
            var cursor: Int = 0
            override fun hasNext() = cursor < chars.size - 1

            override fun next(): String {
                var nextSpaceIndex = nextSpaceIndex()
                val word = String(chars, cursor, nextSpaceIndex - cursor)
                cursor = nextSpaceIndex + 1
                return word
            }

            private fun nextSpaceIndex(): Int {
                var i = cursor
                while (i < chars.size - 1) {
                    if (chars[i].isWhitespace()) return i else i++
                }
                return chars.size
            }
        }

        fun validate() {
            // verify: word count is supported
            wordCount.let { wordCount ->
                if (WordCount.values().none { it.count == wordCount }) {
                    throw WordCountException(wordCount)
                }
            }

            // verify: all words are on the list
            var sublist = getCachedWords(languageCode)
            var nextLetter = 0
            chars.forEachIndexed { i, c ->
                // filter down, by character, ensuring that there are always matching words.
                // per BIP39, we could stop checking each word after 4 chars but we check them all,
                // for completeness
                if (c == ' ') {
                    sublist = getCachedWords(languageCode)
                    nextLetter = 0
                } else {
                    sublist = sublist.filter { it.length > nextLetter && it[nextLetter] == c }
                    if (sublist.isEmpty()) throw InvalidWordException(i)
                    nextLetter++
                }
            }

            // verify: checksum (this function contains a checksum validation)
            toEntropy()
        }

        /**
         * Convert this mnemonic word list to its original entropy value.
         */
        fun toEntropy(): ByteArray {
            wordCount.let { wordCount ->
                if (wordCount % 3 > 0) throw WordCountException(wordCount)
            }
            if (isEmpty()) throw RuntimeException("Word list is empty.")

            // Look up all the words in the list and construct the
            // concatenation of the original entropy and the checksum.
            //
            val concatLenBits = wordCount * 11
            val concatBits = BooleanArray(concatLenBits)
            var wordindex = 0

            // TODO: iterate by characters instead of by words, for a little added security
            forEach { word ->
                // Find the words index in the wordlist.
                val ndx = getCachedWords(languageCode).binarySearch(word)
                if (ndx < 0) throw InvalidWordException(word)

                // Set the next 11 bits to the value of the index.
                for (ii in 0..10) concatBits[wordindex * 11 + ii] =
                    ndx and (1 shl 10 - ii) != 0
                ++wordindex
            }
            val checksumLengthBits = concatLenBits / 33
            val entropyLengthBits = concatLenBits - checksumLengthBits

            // Extract original entropy as bytes.
            val entropy = ByteArray(entropyLengthBits / 8)
            for (ii in entropy.indices)
                for (jj in 0..7)
                    if (concatBits[ii * 8 + jj]) {
                        entropy[ii] = entropy[ii] or (1 shl 7 - jj).toByte()
                    }

            // Take the digest of the entropy.
            val hash: ByteArray = entropy.toSha256()
            val hashBits = hash.toBitArray()

            // Check all the checksum bits.
            for (i in 0 until checksumLengthBits)
                if (concatBits[entropyLengthBits + i] != hashBits[i]) throw ChecksumException
            return entropy
        }

        companion object {

            /**
             * Utility function to create a mnemonic code as a character array from the given
             * entropy. Typically, new mnemonic codes are created starting with a WordCount
             * instance, rather than entropy, to ensure that the entropy has been created correctly.
             * This function is more useful during testing, when you want to validate that known
             * input produces known output.
             *
             * @param entropy the entropy to use for creating the mnemonic code. Typically, this
             * value is created via WordCount.toEntropy.
             * @param languageCode the language code to use. Typically, `en`.
             *
             * @return an array of characters for the mnemonic code that corresponds to the given
             * entropy.
             *
             * @see WordCount.toEntropy
             */
            private fun fromEntropy(
                entropy: ByteArray,
                languageCode: String = Locale.ENGLISH.language
            ): CharArray {

                /* From SPEC:
                First, an initial entropy of ENT bits is generated.
                A checksum is generated by taking the first ENT / 32 bits of its SHA256 hash.
                This checksum is appended to the end of the initial entropy.
                ---
                Next, these concatenated bits are split into groups of 11 bits,
                each encoding a number from 0-2047, serving as an index into a wordlist.
                ---
                Finally, we convert these numbers into words and use the joined words as a mnemonic
                 sentence.
             */
                val hash: ByteArray = entropy.toSha256()
                val hashBits = hash.toBitArray()

                val entropyBits = entropy.toBitArray()
                val checksumLengthBits = entropyBits.size / 32

                // We append these bits to the end of the initial entropy.
                val concatBits = BooleanArray(entropyBits.size + checksumLengthBits)
                System.arraycopy(entropyBits, 0, concatBits, 0, entropyBits.size)
                System.arraycopy(hashBits, 0, concatBits, entropyBits.size, checksumLengthBits)


                // Next we take these concatenated bits and split them into
                // groups of 11 bits. Each group encodes number from 0-2047
                // which is a position in a wordlist.  We convert numbers into
                // words and use joined words as mnemonic sentence.
                val words = ArrayList<String>()
                val nwords = concatBits.size / 11
                for (i in 0 until nwords) {
                    var index = 0
                    for (j in 0..10) {
                        index = index shl 1
                        if (concatBits[i * 11 + j]) index = index or 0x1
                    }
                    words += Mnemonics.getCachedWords(languageCode)[index]
                }

                // for added security, convert to one array, without adding string objects to the heap
                var result = CharArray(words.sumBy { it.length } + words.size - 1)
                var cursor = 0
                // TODO: use the right separator for japanese, once that language is supported by this lib
                var wordSeparator = ' '
                words.forEach { word ->
                    repeat(word.length) { i ->
                        result[cursor++] = word[i]
                    }
                    if (cursor < result.size) result[cursor++] = wordSeparator
                }
                words.clear()

                return result
            }
        }
    }

    /**
     * The supported word counts that can be used for creating entropy.
     *
     * @param count the number of words in the resulting mnemonic
     */
    enum class WordCount(val count: Int) {

        COUNT_12(12), COUNT_15(15), COUNT_18(18), COUNT_21(21), COUNT_24(24);

        /**
         * The bit length of the entropy necessary to create a mnemonic with the given word count.
         */
        val bitLength = count / 3 * 32

        companion object {

            /**
             * Convert a count into an instance of [WordCount].
             */
            fun valueOf(count: Int): WordCount? {
                values().forEach {
                    if (it.count == count) return it
                }
                return null
            }
        }
    }


    //
    // Typed Exceptions
    //

    object ChecksumException :
        RuntimeException(
            "Error: The checksum failed. Verify that none of the words have been transposed."
        )

    class WordCountException(count: Int) :
        RuntimeException("Error: $count is an invalid word count.")

    class InvalidWordException : RuntimeException {
        constructor(index: Int) : super("Error: invalid word encountered at index $index.")
        constructor(word: String) : super("Error: <$word> was not found in the word list.")
    }
}


//
// Public Extensions
//

/**
 * Given a mnemonic, create a seed per BIP-0039.
 *
 * Per the proposal, "A user may decide to protect their mnemonic with a passphrase. If a
 * passphrase is not present, an empty string "" is used instead. To create a binary seed from
 * the mnemonic, we use the PBKDF2 function with a mnemonic sentence (in UTF-8 NFKD) used as the
 * password and the string "mnemonic" + passphrase (again in UTF-8 NFKD) used as the salt. The
 * iteration count is set to 2048 and HMAC-SHA512 is used as the pseudo-random function. The
 * length of the derived key is 512 bits (= 64 bytes).
 *
 * @param mnemonic the mnemonic to convert into a seed
 * @param passphrase an optional password to protect the phrase. Defaults to an empty string.
 * This gets added to the salt.
 * @param validate true to validate the mnemonic before attempting to generate the seed. This
 * can add a bit of extra time to the calculation and is mainly only necessary when the seed is
 * provided by user input. Meaning, in most cases, this can be false but we default to `true` to
 * be "safe by default."
 */
fun MnemonicCode.toSeed(
    passphrase: CharArray = charArrayOf(),
    validate: Boolean = true
): ByteArray {
    if (validate) validate()
    val salt = "mnemonic".toCharArray() + passphrase
    return Pbkdf2Sha256.derive(chars, salt, 2048, 64)
}

fun WordCount.toEntropy(): ByteArray = ByteArray(bitLength / 8).apply {
    Mnemonics.secureRandom.nextBytes(this)
}


//
// Private Extensions
//

private fun ByteArray?.toSha256() = MessageDigest.getInstance("SHA-256").digest(this)

private fun ByteArray.toBitArray(): BooleanArray {
    val bits = BooleanArray(size * 8)
    val zero = 0.toByte()
    repeat(size) { i ->
        repeat(8) { j ->
            val tmp1 = 1 shl (7 - j)
            val tmp2 = this[i] and tmp1.toByte()

            bits[i * 8 + j] = tmp2 != zero
        }
    }
    return bits
}
