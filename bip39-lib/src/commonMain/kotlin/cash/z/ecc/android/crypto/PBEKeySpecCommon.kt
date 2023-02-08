package cash.z.ecc.android.crypto

expect class PBEKeySpecCommon(password: CharArray?, salt: ByteArray?, iterationCount: Int, keyLength: Int) {

    var password: CharArray?
    var salt: ByteArray?
    var iterationCount: Int
    var keyLength: Int

    fun clearPassword()
}
