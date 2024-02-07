package cash.z.ecc.android.bip39

import cash.z.ecc.android.bip39.Mnemonics.MnemonicCode
import cash.z.ecc.android.bip39.utils.*
import kotlin.test.*

private const val DEFAULT_LANGUAGE_CODE = "en"

class MnemonicsTest {
    val validPhrase =
        "void come effort suffer camp survey warrior heavy shoot primary clutch crush open amazing" +
            " screen patrol group space point ten exist slush involve unfold"

    @Test
    fun testSeedGeneratedFromKnownValidPhraseIsValid() {
        val result = MnemonicCode(validPhrase).toSeed()
        assertNotNull(result, "Seed should not be null")
        assertNotEquals(0, result.size, "Seed should not be empty")
        assertEquals(64, result.size, "Seed should be the expected length")
        //
        val hex = result.toHex()
        assertEquals(128, hex.length, "Seed should be the expected length when the seed is converted to hex")
        assertEquals(
            "b873212f885ccffbf4692afcb84bc2e55886de2dfa07d90f5c3c239abc31c0a6ce047e30fd8bf6a281e71389aa82d73df74c7bbfb3b06b4639a5cee775cccd3c",
            hex,
            "Seed should equal the expected value when the seed is converted to hex"
        )
    }

    @Test
    fun testBitLengthOfEntropyCorrectForAllSupportedWordCounts() {
        data class CountToExpectedBitLength(val count: Int, val expectedBitLength: Int)

        val countToExpectedBitLengths = listOf(
            CountToExpectedBitLength(12, 128),
            CountToExpectedBitLength(15, 160),
            CountToExpectedBitLength(18, 192),
            CountToExpectedBitLength(21, 224),
            CountToExpectedBitLength(24, 256)
        )
        for ((count, bitLength) in countToExpectedBitLengths) {
            val wordCount = Mnemonics.WordCount.valueOf(count)
            assertNotNull(wordCount)
            assertEquals(bitLength, wordCount.bitLength)
            wordCount.toEntropy().let { entropy ->
                assertEquals(bitLength, entropy.size * 8)
            }
        }
    }


    @Test
    fun testMnemonicPhraseGenerationAndValidation() {
        Mnemonics.WordCount.values().forEach { wordCount ->
            val mnemonicCode = MnemonicCode(wordCount)
            val phraseString = mnemonicCode.chars.concatToString()

            // Test that it has the correct number of spaces
            val expectedSpaces = wordCount.count - 1
            val actualSpaces = phraseString.count { it == ' ' }
            assertEquals(
                expectedSpaces,
                actualSpaces,
                "Expected $expectedSpaces spaces for ${wordCount.name}, found $actualSpaces"
            )

            val words = mnemonicCode.words.map { it.concatToString() }
            // Test that the list has the correct number of elements when it is converted from a list of CharArrays
            assertEquals(
                wordCount.count,
                words.size,
                "Expected ${wordCount.count} elements for ${wordCount.name}, found ${words.size}"
            )

            // Test that each word is present in the original phrase
            val correctWords = phraseString.split(' ')
            assertTrue(
                words.all { it in correctWords },
                "Not all words from generated phrase are present in the original phrase for ${wordCount.name}"
            )
        }
    }

    @Test
    fun testConvertingHexEntropyToMnemonicPhraseMatchesExpected() {
        data class ExpectedMnemonicData(val wordCount: Int, val entropy: String, val mnemonic: String)

        val expected = listOf(
            ExpectedMnemonicData(
                24,
                "b893a6b0da8fc9b73d709bda939e818a677aa376c266949378300b65a34b8e52",
                "review outdoor promote relax wish swear volume beach surround ostrich parrot below jeans" + " faculty swallow error nest orange army bitter focus place deer fat"
            ), ExpectedMnemonicData(
                18,
                "d5bcbf62dea1a07ab1abb0144b299300137168a7939f3071f112b557",
                "stick tourist suffer run borrow diary shop invite begin flock gospel ability damage reform" + " oxygen initial corn moon dwarf height image"
            ), ExpectedMnemonicData(
                15,
                "e06ce21369dc09eb2bda66510a76f65ab3f947cce90fcb10",
                "there grow luggage squirrel scene void quarter error extra father rural rely display" + " physical crisp capable slam lumber"
            ), ExpectedMnemonicData(
                12,
                "0b01c3c0b0590faf45fc171da17cfb22",
                "arch asthma usual gaze movie stumble blood load buffalo armor disagree earth"
            )
        )

        expected.forEach { (_, entropy, mnemonic) ->
            val code = MnemonicCode(entropy.fromHex())
            assertEquals(mnemonic, code.chars.concatToString())
        }
    }

    // uses test values from the original BIP : https://github.com/trezor/python-mnemonic/blob/master/vectors.json
    @Test
    fun testEntropyToMnemonicConversion() {
        englishTestData.forEach {
            val entropy = it[0].fromHex()
            val expectedMnemonic = it[1]
            val actualMnemonic = MnemonicCode(entropy).chars.concatToString()
            assertEquals(expectedMnemonic, actualMnemonic, "Failed conversion for entropy: ${it[0]}")
        }
    }

    // uses test values from the original BIP : https://github.com/trezor/python-mnemonic/blob/master/vectors.json
    @Test
    fun testMnemonicToEntropyConversion() {
        englishTestData.forEach {
            val expectedEntropy = it[0]
            val mnemonic = it[1]
            val actualEntropy = MnemonicCode(mnemonic).toEntropy().toHex()
            assertEquals(expectedEntropy, actualEntropy, "Failed conversion for mnemonic: $mnemonic")
        }
    }

    // uses test values from the original BIP : https://github.com/trezor/python-mnemonic/blob/master/vectors.json
    @Test
    fun testMnemonicToSeedConversion() {
        englishTestData.forEach {
            val mnemonic = it[1].toCharArray()
            val expectedSeed = it[2]
            val passphrase = "TREZOR".toCharArray()
            val actualSeed = MnemonicCode(mnemonic, DEFAULT_LANGUAGE_CODE).toSeed(passphrase).toHex()
            assertEquals(expectedSeed, actualSeed, "Failed seed generation for mnemonic: $mnemonic")
        }
    }

    @Test
    fun testMnemonicValidationFailsWithSwappedWords() {
        val swappedPhrase = validPhrase.swap(4, 5)
        // "validate() fails with a checksum error"
        shouldThrow<Mnemonics.ChecksumException> { MnemonicCode(swappedPhrase).validate() }
        // "toEntropy() fails with a checksum error"
        shouldThrow<Mnemonics.ChecksumException> { MnemonicCode(swappedPhrase).toEntropy() }
        // "toSeed() fails with a checksum error"
        shouldThrow<Mnemonics.ChecksumException> { MnemonicCode(swappedPhrase).toSeed() }

        // "toSeed(validate=false) succeeds!!"
        shouldNotThrowAny { MnemonicCode(swappedPhrase).toSeed(validate = false) }
    }

    @Test
    fun testMnemonicValidationFailsWhenContainingAnInvalidWord() {
        val mnemonicPhrase = validPhrase.split(' ').let { words ->
            validPhrase.replace(words[23], "convincee")
        }

        // "validate() fails with a word validation error"
        shouldThrow<Mnemonics.InvalidWordException> { MnemonicCode(mnemonicPhrase).validate() }
        // "toEntropy() fails with a word validation error"
        shouldThrow<Mnemonics.InvalidWordException> { MnemonicCode(mnemonicPhrase).toEntropy() }
        // "toSeed() fails with a word validation error"
        shouldThrow<Mnemonics.InvalidWordException> { MnemonicCode(mnemonicPhrase).toSeed() }

        // "toSeed(validate=false) succeeds!!"
        shouldNotThrowAny { MnemonicCode(mnemonicPhrase).toSeed(validate = false) }
    }

    @Test
    fun testMnemonicValidationFailsWithUnsupportedWordCount() {
        val mnemonicPhrase = "$validPhrase still"

        // "validate() fails with a word count error"
        shouldThrow<Mnemonics.WordCountException> { MnemonicCode(mnemonicPhrase).validate() }
        // "toEntropy() fails with a word count error"
        shouldThrow<Mnemonics.WordCountException> { MnemonicCode(mnemonicPhrase).toEntropy() }
        // "toEntropy() fails with a word count error"
        shouldThrow<Mnemonics.WordCountException> { MnemonicCode(mnemonicPhrase).toSeed() }

        // "toSeed(validate=false) succeeds!!"
        shouldNotThrowAny { MnemonicCode(mnemonicPhrase).toSeed(validate = false) }
    }

}