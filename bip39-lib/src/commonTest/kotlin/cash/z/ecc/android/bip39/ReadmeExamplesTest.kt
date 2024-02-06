package cash.z.ecc.android.bip39

import cash.z.ecc.android.bip39.Mnemonics.MnemonicCode
import cash.z.ecc.android.bip39.Mnemonics.WordCount
import cash.z.ecc.android.bip39.utils.shouldNotThrowAny
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ReadmeExamplesTest {

    private val validPhrase =
        "still champion voice habit trend flight survey between bitter process artefact blind carbon truly provide dizzy crush flush breeze blouse charge solid fish spread"

    @Test
    fun testCreate24WordMnemonicPhrase() {
        val mnemonicCode = MnemonicCode(WordCount.COUNT_24)
        assertEquals(24, mnemonicCode.wordCount, "Result is not a valid 24-word phrase")
        // should result in a valid phrase
        shouldNotThrowAny {
            mnemonicCode.validate()
        }
    }

    @Test
    fun testGenerateSeed() {
        val mnemonicCode = MnemonicCode(WordCount.COUNT_24)
        mnemonicCode.toSeed()
        assertEquals(24, mnemonicCode.wordCount, "Result is not a valid 24-word phrase")
        // should result in a valid phrase
        shouldNotThrowAny {
            mnemonicCode.validate()
        }
    }

    @Test
    fun testGenerateSeedFromExistingMnemonicChars() {
        val mnemonicCode = MnemonicCode(validPhrase.toCharArray())
        mnemonicCode.toSeed()
        assertEquals(24, mnemonicCode.wordCount, "Result is not a valid 24-word phrase")
        // should result in a valid phrase
        shouldNotThrowAny {
            mnemonicCode.validate()
        }
    }

    @Test
    fun testGenerateSeedFromExistingMnemonicString() {
        val mnemonicCode = MnemonicCode(validPhrase)
        mnemonicCode.toSeed()
        assertEquals(24, mnemonicCode.wordCount, "Result is not a valid 24-word phrase")
        // should result in a valid phrase
        shouldNotThrowAny {
            mnemonicCode.validate()
        }
    }

    @Test
    fun testGenerateSeedWithPassphraseNormalWay() {
        val passphrase = "bitcoin".toCharArray()
        val seed = MnemonicCode(validPhrase).toSeed(passphrase)
        assertEquals(64, seed.size, "'normal way' does not result in a 64 byte seed")
    }

    @Test
    fun testGenerateSeedWithPassphrasePrivateWay() {
        val seed: ByteArray
        charArrayOf('z', 'c', 'a', 's', 'h').let { passphrase ->
            seed = MnemonicCode(validPhrase).toSeed(passphrase)
            assertEquals("zcash", passphrase.concatToString())
            passphrase.fill('0')
            assertEquals("00000", passphrase.concatToString())
        }
        assertEquals(64, seed.size, "'private way' does not result in a 64 byte seed")
    }

    @Test
    fun testIterateOverMnemonicCodesWithForLoop() {
        val mnemonicCode = MnemonicCode(validPhrase)
        var count = 0
        for (word in mnemonicCode) {
            count++
            assertContains(validPhrase, word)
        }
        assertEquals(24, count)
    }

    @Test
    fun testIterateOverMnemonicCodesWithForEachLoop() {
        val mnemonicCode = MnemonicCode(validPhrase)
        var count = 0
        mnemonicCode.forEach { word ->
            count++
            assertContains(validPhrase, word)
        }
        assertEquals(24, count)
    }
}