package cash.z.ecc.android.crypto

import javax.crypto.SecretKey

internal actual class SecretKeyCommon(generatedSecret: SecretKey) {

    actual val encoded = generatedSecret.encoded
}
