package cash.z.ecc.android.crypto

internal expect class PBEKeySpecCommon(password: CharArray?, salt: ByteArray, iterationCount: Int, keyLength: Int) {

    var password: CharArray?
        private set
    var salt: ByteArray?
        private set
    var iterationCount: Int
        private set
    var keyLength: Int
        private set

    fun clearPassword()
}
