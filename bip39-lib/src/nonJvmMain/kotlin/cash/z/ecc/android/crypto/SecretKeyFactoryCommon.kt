package cash.z.ecc.android.crypto

actual class SecretKeyFactoryCommon() {

    actual fun generateSecret(pbeKeySpec: PBEKeySpecCommon): SecretKeyCommon {
        val encoded = pbeKeySpec.run {
            Pbkdf2Sha512.derive(password!!, salt!!, iterationCount, keyLength)
        }
        return SecretKeyCommon(encoded)
    }

    actual companion object{
        actual fun getInstance(algorithm: String): SecretKeyFactoryCommon = TODO()


        actual fun getInstance(algorithm: String, provider: FallbackProvider): SecretKeyFactoryCommon = SecretKeyFactoryCommon()

    }


}
