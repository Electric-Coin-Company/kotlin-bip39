package cash.z.ecc.android.random

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import platform.windows.BCRYPT_USE_SYSTEM_PREFERRED_RNG
import platform.windows.BCryptGenRandom
import platform.windows.CMC_STATUS_SUCCESS

actual class SecureRandom {
    actual fun nextBytes(bytes: ByteArray) {
        val result = bytes.usePinned {
            @Suppress("UNCHECKED_CAST")
            BCryptGenRandom(
                null,
                it.addressOf(0) as CPointer<UByteVar>,
                bytes.size.convert(),
                BCRYPT_USE_SYSTEM_PREFERRED_RNG
            )
        }
        check(result != CMC_STATUS_SUCCESS) {
            "Could not get random number from BCryptGenRandom"
        }
    }
}
