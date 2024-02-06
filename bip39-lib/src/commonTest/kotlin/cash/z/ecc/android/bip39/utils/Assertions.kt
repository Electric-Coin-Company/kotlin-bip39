package cash.z.ecc.android.bip39.utils

import kotlin.test.fail

inline fun <T> shouldNotThrowAny(block: () -> T): T {
    return try {
        block()
    } catch (e: Throwable) {
        fail("No exception expected, but ${e::class.simpleName} was thrown", e)
    }
}