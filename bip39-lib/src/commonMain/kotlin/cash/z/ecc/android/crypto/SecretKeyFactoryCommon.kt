package cash.z.ecc.android.crypto

internal expect class SecretKeyFactoryCommon {
    fun generateSecret(pbeKeySpec: PBEKeySpecCommon): SecretKeyCommon

    companion object {
        fun getInstance(algorithm: String): SecretKeyFactoryCommon
        fun getInstance(algorithm: String, provider: FallbackProvider): SecretKeyFactoryCommon
    }
}
