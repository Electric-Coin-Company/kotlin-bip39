package cash.z.ecc.android.random

import kotlin.random.Random

actual class SecureRandom {

    actual fun nextBytes(bytes: ByteArray){
        println("Warning: Using kotlin.random.Random.nextBytes, not a SecureRandom generator")
        Random.nextBytes(bytes)
    }

}