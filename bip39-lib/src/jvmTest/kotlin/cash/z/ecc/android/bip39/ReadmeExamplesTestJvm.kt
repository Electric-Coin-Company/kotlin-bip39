package cash.z.ecc.android.bip39

import cash.z.ecc.android.bip39.Mnemonics.MnemonicCode
import cash.z.ecc.android.bip39.Mnemonics.WordCount
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class ReadmeExamplesTestJvm :
    ShouldSpec({
        context("Example: auto-clear") {
            should("clear the mnemonic when done") {
                val mnemonicCode = MnemonicCode(WordCount.COUNT_24)
                mnemonicCode.use {
                    mnemonicCode.wordCount shouldBe 24
                }

                // content gets automatically cleared after use!
                mnemonicCode.wordCount shouldBe 0
            }
        }
    })
