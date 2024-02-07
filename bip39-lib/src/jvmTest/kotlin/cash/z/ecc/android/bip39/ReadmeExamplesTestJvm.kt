package cash.z.ecc.android.bip39

import cash.z.ecc.android.bip39.Mnemonics.MnemonicCode
import cash.z.ecc.android.bip39.Mnemonics.WordCount
import kotlin.test.Test
import kotlin.test.assertEquals

class ReadmeExamplesTestJvm {
    @Test
    fun testMnemonicAutoClearedWhenDone() {
        val mnemonicCode = MnemonicCode(WordCount.COUNT_24)
        mnemonicCode.use {
            assertEquals(24, mnemonicCode.wordCount)
        }

        // content gets automatically cleared after use!
        assertEquals(0, mnemonicCode.wordCount)
    }
}
