package cash.z.ecc.android.crypto

internal actual class PBEKeySpecCommon {

    /**
     * Returns the iteration count or 0 if not specified.
     *
     * @return the iteration count.
     */
    actual var iterationCount: Int = 0
        private set

    /**
     * Returns the to-be-derived key length or 0 if not specified.
     *
     *
     *  Note: this is used to indicate the preference on key length
     * for variable-key-size ciphers. The actual key size depends on
     * each provider's implementation.
     *
     * @return the to-be-derived key length.
     */
    actual var keyLength = 0
        private set

    /**
     * Constructor that takes a password. An empty char[] is used if
     * null is specified.
     *
     *
     *  Note: `password` is cloned before it is stored in
     * the new `PBEKeySpec` object.
     *
     * @param password the password.
     */
    constructor(password: CharArray?) {
        if (password == null || password.isEmpty()) {
            this.password = CharArray(0)
        } else {
            this.password = password.copyOf()
        }
    }

    /**
     * Constructor that takes a password, salt, iteration count, and
     * to-be-derived key length for generating PBEKey of variable-key-size
     * PBE ciphers.  An empty char[] is used if null is specified for
     * `password`.
     *
     *
     *  Note: the `password` and `salt`
     * are cloned before they are stored in
     * the new `PBEKeySpec` object.
     *
     * @param password the password.
     * @param salt the salt.
     * @param iterationCount the iteration count.
     * @param keyLength the to-be-derived key length.
     * @exception IllegalArgumentException if `salt` is empty,
     * i.e. 0-length, `iterationCount` or
     * `keyLength` is not positive.
     */
    actual constructor(
        password: CharArray?,
        salt: ByteArray,
        iterationCount: Int,
        keyLength: Int
    ) {
        if (password == null || password.isEmpty()) {
            this.password = CharArray(0)
        } else {
            this.password = password.copyOf()
        }
        require(salt.isNotEmpty()) {
            "the salt parameter must not be empty"
        }
        this.salt = salt.copyOf()
        require(iterationCount > 0) { "invalid iterationCount value" }
        require(keyLength > 0) { "invalid keyLength value" }
        this.iterationCount = iterationCount
        this.keyLength = keyLength
    }

    /**
     * Constructor that takes a password, salt, iteration count for
     * generating PBEKey of fixed-key-size PBE ciphers. An empty
     * char[] is used if null is specified for `password`.
     *
     *
     *  Note: the `password` and `salt`
     * are cloned before they are stored in the new
     * `PBEKeySpec` object.
     *
     * @param password the password.
     * @param salt the salt.
     * @param iterationCount the iteration count.
     * @exception IllegalArgumentException if `salt` is empty,
     * i.e. 0-length, or `iterationCount` is not positive.
     */
    constructor(password: CharArray?, salt: ByteArray, iterationCount: Int) {
        if (password == null || password.isEmpty()) {
            this.password = CharArray(0)
        } else {
            this.password = password.copyOf()
        }
        require(salt.isNotEmpty()) {
            "the salt parameter must not be empty"
        }
        this.salt = salt.copyOf()
        require(iterationCount > 0) { "invalid iterationCount value" }
        this.iterationCount = iterationCount
    }

    /**
     * Clears the internal copy of the password.
     *
     */
    actual fun clearPassword() {
        password?.fill(' ')
        password = null
    }

    /**
     * Returns a copy of the password.
     *
     *
     *  Note: this method returns a copy of the password. It is
     * the caller's responsibility to zero out the password information after
     * it is no longer needed.
     *
     * @exception IllegalStateException if password has been cleared by
     * calling `clearPassword` method.
     * @return the password.
     */
    actual var password: CharArray?
        get() {
            checkNotNull(field) { "password has been cleared" }
            return field!!.copyOf()
        }

    /**
     * Returns a copy of the salt or null if not specified.
     *
     *
     *  Note: this method should return a copy of the salt. It is
     * the caller's responsibility to zero out the salt information after
     * it is no longer needed.
     *
     * @return the salt.
     */
    actual var salt: ByteArray? = null
        get() {
            return if (field != null) {
                field!!.copyOf()
            } else {
                null
            }
        }
}
