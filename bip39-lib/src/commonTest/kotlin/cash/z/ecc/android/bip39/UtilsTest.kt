package cash.z.ecc.android.bip39

import cash.z.ecc.android.bip39.utils.fromHex
import cash.z.ecc.android.bip39.utils.toHex
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class UtilsTest :  ShouldSpec({
    should("convert ByteArray to hex String") {
        val byteArray = byteArrayOf(-128, -16, 0, 16, 127)
        byteArray.toHex() shouldBe "80f000107f"
    }

    should("convert hex String to ByteArray") {
        val hexString = "80f000107f"
        val expectedByteArray = byteArrayOf(-128, -16, 0, 16, 127)
        hexString.fromHex() shouldBe expectedByteArray
    }

    should("return original value, when converting to hex and then from hex") {
        val originalBytes = ByteArray(256) { (it - 128).toByte() }
        val transformedBytes = originalBytes.toHex().fromHex()
        transformedBytes shouldBe originalBytes
    }

    should("return original value, when converting from hex and then to hex") {
        val originalHex = "0008101820283038404850586068707880889098a0a8b0b8c0c8d0d8e0e8f0f8"
        val transformedHex = originalHex.fromHex().toHex()
        transformedHex shouldBe originalHex
    }
})
