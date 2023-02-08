package cash.z.ecc.android.random

import kotlinx.cinterop.*
import platform.windows.BCRYPT_USE_SYSTEM_PREFERRED_RNG
import platform.windows.BCryptGenRandom
import platform.windows.CMC_STATUS_SUCCESS

actual class SecureRandom actual constructor() {
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
            if (result != CMC_STATUS_SUCCESS)
            {
                throw Error("Could not get random number from BCryptGenRandom")
            }

    }
}