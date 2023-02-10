package cash.z.ecc.android.bip39

import java.security.MessageDigest

internal actual fun ByteArray.toSha256(): ByteArray = MessageDigest.getInstance("SHA-256").digest(this)
