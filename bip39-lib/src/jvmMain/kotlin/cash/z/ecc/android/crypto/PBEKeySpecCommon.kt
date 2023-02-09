package cash.z.ecc.android.crypto

import javax.crypto.spec.PBEKeySpec

actual class PBEKeySpecCommon actual constructor(password: CharArray?, salt: ByteArray?, iterationCount: Int, keyLength: Int) {
    val wrappedPbeKeySpec = PBEKeySpec(password, salt, iterationCount, keyLength)

    actual var password: CharArray? = null
        get() = wrappedPbeKeySpec.password
        private set
    actual var salt: ByteArray? = wrappedPbeKeySpec.salt
        get() = wrappedPbeKeySpec.salt
        private set
    actual var iterationCount: Int = wrappedPbeKeySpec.iterationCount
        get() = wrappedPbeKeySpec.iterationCount
        private set
    actual var keyLength: Int = wrappedPbeKeySpec.keyLength
        get() = wrappedPbeKeySpec.keyLength
        private set

    actual fun clearPassword() {
        wrappedPbeKeySpec.clearPassword()
    }
}
