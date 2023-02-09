package cash.z.ecc.android.crypto

internal actual class SecretKeyFactoryCommon(private val jvmSecretKeyFactory: javax.crypto.SecretKeyFactory) {

    actual fun generateSecret(pbeKeySpec: PBEKeySpecCommon): SecretKeyCommon =
        SecretKeyCommon(jvmSecretKeyFactory.generateSecret(pbeKeySpec.wrappedPbeKeySpec))

    actual companion object {
        actual fun getInstance(algorithm: String): SecretKeyFactoryCommon =
            SecretKeyFactoryCommon(javax.crypto.SecretKeyFactory.getInstance(algorithm))

        actual fun getInstance(algorithm: String, provider: FallbackProvider): SecretKeyFactoryCommon =
            SecretKeyFactoryCommon(javax.crypto.SecretKeyFactory.getInstance(algorithm))
    }
}
