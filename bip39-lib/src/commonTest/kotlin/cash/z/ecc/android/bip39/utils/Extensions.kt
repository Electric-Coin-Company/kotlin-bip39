package cash.z.ecc.android.bip39.utils

fun ByteArray.toHex(): String {
    val sb = StringBuilder(size * 2)
    for (b in this) {
        val hexValue = b.let { if (it >= 0) it.toInt() else 256 + it }
            .toString(16)
            .let { if (it.length < 2) "0$it" else it }
        sb.append(hexValue)
    }
    return sb.toString()
}

fun String.fromHex(): ByteArray {
    val len = length
    val data = ByteArray(len / 2)
    var i = 0
    while (i < len) {
        data[i / 2] =
            ((this[i].digitToInt(16) shl 4) + this[i + 1].digitToInt(16)).toByte()
        i += 2
    }
    return data
}

fun String.swap(srcWord: Int, destWord: Int = srcWord + 1): String {
    require(srcWord < destWord) { "srcWord must be less than destWord" }
    require(destWord <= count { it == ' ' }) { "there aren't that many words" }

    return split(' ').let { words ->
        words.reduceIndexed { i, result, word ->
            val next = when (i) {
                srcWord -> words[destWord]
                destWord -> words[srcWord]
                else -> word
            }
            if (srcWord == 0 && i == 1) "${words[destWord]} $next" else "$result $next"
        }
    }
}
