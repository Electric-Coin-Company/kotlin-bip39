package cash.z.ecc.android.random

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import platform.posix.O_RDONLY
import platform.posix.open
import platform.posix.read

actual class SecureRandom {
    /**
     * Implementation based on:
     * https://stackoverflow.com/a/2572373/1363742
     */
    @OptIn(UnsafeNumber::class, ExperimentalForeignApi::class)
    actual fun nextBytes(bytes: ByteArray) =
        memScoped {
            val randomData = open("/dev/urandom", O_RDONLY)
            if (randomData < 0) {
                throw UnsupportedOperationException("Could not open /dev/urandom")
            } else {
                val result =
                    bytes.usePinned {
                        read(randomData, it.addressOf(0), bytes.size.convert())
                    }
                // Warning: Operator '==' cannot be applied to 'ssize_t /* = Long */' and 'Int'
                // Converting both sides to Long to ensure they fit in the range
                check(result.toLong() == bytes.size.toLong()) {
                    "Could not get random number from /dev/urandom"
                }
            }
        }
}
