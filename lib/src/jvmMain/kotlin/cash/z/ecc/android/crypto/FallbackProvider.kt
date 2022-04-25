package cash.z.ecc.android.crypto

import java.security.Provider
import java.security.spec.KeySpec
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.SecretKeyFactorySpi
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * A provider to use in the event that the necessary cryptographic algorithm is not available in the
 * service provider. This provides a bridge to a commonly used Java implementation that has been
 * moderately adapted to Kotlin.
 */
// Constructor was deprecated in Java 9, but for compatibility with Android (Java 8, effectively) the old constructor
// must continue to be used.
@Suppress("DEPRECATION")
class FallbackProvider : Provider(
    "FallbackProvider",
    1.0,
    "Provides a bridge to a default implementation of the PBKDF2WithHmacSHA512 algorithm" +
        " to use when one is not already available on the device."
) {
    override fun getService(type: String?, algorithm: String?): Service? {
        return ServiceProvider().takeIf {
            SecretKeyFactory::class.java.simpleName.equals(type, true) &&
                Pbkdf2KeyFactory.algorithm.equals(algorithm, true)
        }
    }

    inner class ServiceProvider : Provider.Service(
        this@FallbackProvider,
        SecretKeyFactory::class.java.simpleName,
        Pbkdf2KeyFactory.algorithm,
        ServiceProvider::class.java.simpleName,
        null,
        null
    ) {
        override fun newInstance(unused: Any?): Any {
            return Pbkdf2KeyFactory()
        }
    }
}

/**
 * A simple interface to bridge to a fallback algorithm implementation.
 *
 * This service provider interface supplies the implementation of a secret-key factory for
 * the Password-Based Key Derivation Function 2 (PBKDF2) with Hash-based Message Authentication Code
 * (HMAC) and Secure Hash Algorithm (SHA-512). Most modern devices already have this algorithm
 * available so this is used to bridge to a popular java implementation simply as a fallback.
 */
class Pbkdf2KeyFactory : SecretKeyFactorySpi() {

    override fun engineGenerateSecret(keySpec: KeySpec): SecretKey {
        return (keySpec as PBEKeySpec).run {
            SecretKeySpec(Pbkdf2Sha512.derive(password, salt, iterationCount, keyLength), algorithm)
        }
    }

    override fun engineGetKeySpec(s: SecretKey, p: Class<*>) = throw UnsupportedOperationException()
    override fun engineTranslateKey(s: SecretKey?) = throw UnsupportedOperationException()

    companion object {
        const val algorithm = "PBKDF2WithHmacSHA512"
    }
}
