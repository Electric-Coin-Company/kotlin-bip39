package cash.z.ecc.android.bip39

import cash.z.ecc.android.bip39.Mnemonics.MnemonicCode
import cash.z.ecc.android.bip39.Mnemonics.WordCount
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class ReadmeExamplesTest : ShouldSpec({
    val validPhrase =
        "still champion voice habit trend flight survey between bitter process artefact blind carbon truly provide dizzy crush flush breeze blouse charge solid fish spread"
    context("Example: Create 24-word mnemonic phrase") {
        val mnemonicCode = MnemonicCode(WordCount.COUNT_24)
        should("result in a valid 24-word phrase") {
            mnemonicCode.wordCount shouldBe 24
        }
        should("result in a valid phrase") {
            shouldNotThrowAny {
                mnemonicCode.validate()
            }
        }
    }
    context("Example: Generate seed") {
        val mnemonicCode = MnemonicCode(WordCount.COUNT_24)
        should("result in a valid 24-word phrase") {
            mnemonicCode.toSeed()
            mnemonicCode.wordCount shouldBe 24
        }
        should("result in a valid phrase") {
            shouldNotThrowAny {
                mnemonicCode.validate()
            }
        }
    }
    context("Example: Generate seed from existing mnemonic chars") {
        val mnemonicCode = MnemonicCode(validPhrase.toCharArray())
        should("result in a valid 24-word phrase") {
            mnemonicCode.toSeed()
            mnemonicCode.wordCount shouldBe 24
        }
        should("result in a valid phrase") {
            shouldNotThrowAny {
                mnemonicCode.validate()
            }
        }
    }
    context("Example: Generate seed from existing mnemonic string") {
        val mnemonicCode = MnemonicCode(validPhrase)
        should("result in a valid 24-word phrase") {
            mnemonicCode.toSeed()
            mnemonicCode.wordCount shouldBe 24
        }
        should("result in a valid phrase") {
            shouldNotThrowAny {
                mnemonicCode.validate()
            }
        }
    }
    context("Example: Generate seed with passphrase") {
        val passphrase = "bitcoin".toCharArray()
        should("'normal way' results in a 64 byte seed") {
            val seed = MnemonicCode(validPhrase).toSeed(passphrase)
            seed.size shouldBe 64
        }
        should("'private way' results in a 64 byte seed") {
            var seed: ByteArray
            charArrayOf('z', 'c', 'a', 's', 'h').let { passphrase ->
                seed = MnemonicCode(validPhrase).toSeed(passphrase)
                passphrase.concatToString() shouldBe "zcash"
                passphrase.fill('0')
                passphrase.concatToString() shouldBe "00000"
            }
            seed.size shouldBe 64
        }
    }
    context("Example: Iterate over mnemonic codes") {
        val mnemonicCode = MnemonicCode(validPhrase)
        should("work in a for loop") {
            var count = 0
            for (word in mnemonicCode) {
                count++
                validPhrase shouldContain word
            }
            count shouldBe 24
        }
        should("work with forEach") {
            var count = 0
            mnemonicCode.forEach { word ->
                count++
                validPhrase shouldContain word
            }
            count shouldBe 24
        }
    }
})
