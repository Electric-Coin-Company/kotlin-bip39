package cash.z.ecc.android.bip39

import cash.z.ecc.android.bip39.utils.fromHex
import cash.z.ecc.android.bip39.utils.toHex
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class UtilsTest {
    @Test
    fun testByteArrayToHexStringConversion() {
        val byteArray = byteArrayOf(-128, -16, 0, 16, 127)
        assertEquals("80f000107f", byteArray.toHex())
    }

    @Test
    fun testHexStringToByteArrayConversion() {
        val hexString = "80f000107f"
        val expectedByteArray = byteArrayOf(-128, -16, 0, 16, 127)
        assertContentEquals(expectedByteArray, hexString.fromHex())
    }

    @Test
    fun testReturnsOriginalValueWhenConvertingToHexAndThenFromHex() {
        val originalBytes = ByteArray(256) { (it - 128).toByte() }
        val transformedBytes = originalBytes.toHex().fromHex()
        assertContentEquals(originalBytes, transformedBytes)
    }

    @Test
    fun testReturnsOriginalValueWhenConvertingFromHexAndThenToHex() {
        val originalHex = "0008101820283038404850586068707880889098a0a8b0b8c0c8d0d8e0e8f0f8"
        val transformedHex = originalHex.fromHex().toHex()
        assertEquals(originalHex, transformedHex)
    }
}
