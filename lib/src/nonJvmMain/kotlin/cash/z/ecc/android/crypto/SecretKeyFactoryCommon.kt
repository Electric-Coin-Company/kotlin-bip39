package cash.z.ecc.android.crypto

actual class SecretKeyFactoryCommon() {

    actual fun generateSecret(pbeKeySpec: PBEKeySpecCommon): SecretKeyCommon {
        TODO()
    }

    actual companion object{
        actual fun getInstance(algorithm: String): SecretKeyFactoryCommon = TODO()


        actual fun getInstance(algorithm: String, provider: FallbackProvider): SecretKeyFactoryCommon =  TODO()

    }


}
