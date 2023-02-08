package cash.z.ecc.android.bip39

import okio.ByteString.Companion.toByteString

internal actual fun ByteArray.toSha256() : ByteArray = this.toByteString().sha256().toByteArray()