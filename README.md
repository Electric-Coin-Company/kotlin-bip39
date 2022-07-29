# kotlin-bip39
[![license](https://img.shields.io/github/license/zcash/kotlin-bip39.svg?maxAge=2592000&style=plastic)](https://github.com/zcash/kotlin-bip39/blob/master/LICENSE)
![maven](https://img.shields.io/maven-central/v/cash.z.ecc.android/kotlin-bip39?color=success&style=plastic)


## Introduction
A concise implementation of [BIP-0039](https://github.com/bitcoin/bips/blob/master/bip-0039.mediawiki) in Kotlin for Android. 

Only about 30kB in total size. For comparison, the entire library is about 3X the size of this README file (because there are no dependencies)!

### Motivation

* There are not many bip-39 implementations for android
* Most that do exist are not Kotlin
  * or they are not idiomatic (because they are direct Java ports to Kotlin)
  * or they have restrictive licenses
* **Most implementations fail to [validate the checksum](https://github.com/zcash/kotlin-bip39/blob/300e25dba95e0d1e3fe94a0f3c0cd7d707cca999/lib/src/test/java/cash/z/ecc/android/bip39/MnemonicsTest.kt#L147-L172), which can easily lead to loss of funds!**
  * validating the checksum prevents: leading/trailing white space, valid words in the wrong order, mistyping a valid word (like `chief` instead of `chef`) and other similar issues that could invalidate a backup or lose funds.
* No other implementation uses [CharArrays](https://stackoverflow.com/a/8881376/178433), from the ground up, for [added security](https://docs.oracle.com/javase/6/docs/technotes/guides/security/crypto/CryptoSpec.html#PBEEx) and lower chances of [accidentally logging](https://stackoverflow.com/a/8885343/178433) sensitive info.

Consequently, this library strives to use both [idiomatic Kotlin](https://kotlinlang.org/docs/reference/idioms.html) and `CharArrays` whenever possible. It also aims to be concise and thoroughly tested. As a pure kotlin library, it probably also works outside of Android but that is not an explicit goal (Update: confirmed to also work on a [Ktor server](https://ktor.io/)).

Plus, it uses a permissive MIT license and no dependencies beyond Kotlin's stdlib!

## Getting Started
### Gradle

Add dependencies (see Maven badge above for latest version number):

```groovy
dependencies {
    implementation "cash.z.ecc.android:kotlin-bip39:${latestVersion}"
}

repository {
    mavenCentral()
}
```
***

## Usage
This library prefers `CharArrays` over `Strings` for [added security](https://stackoverflow.com/a/8881376/178433).  
Note: If strings or lists are desired, it is very easy (but not recommended) to convert to/from a CharArray via `String(charArray)` or `String(charArray).split(' ')`.
* Create new 24-word mnemonic phrase
```kotlin
import cash.z.ecc.android.bip39.Mnemonics.MnemonicCode

val mnemonicCode: MnemonicCode = MnemonicCode(WordCount.COUNT_24)

// assert: mnemonicCode.wordCount == 24, mnemonicCode.languageCode == "en"
```
* Generate seed
```kotlin
val seed: ByteArray = mnemonicCode.toSeed()
```
* Generate seed from existing mnemonic
```kotlin
val preExistingPhraseString = "scheme spot photo card baby mountain device kick cradle pact join borrow"
val preExistingPhraseChars = validPhraseString.toCharArray()

// from CharArray
seed = MnemonicCode(preExistingPhraseChars).toSeed()

// from String
seed = MnemonicCode(preExistingPhraseString).toSeed()
```
* Generate seed with passphrase
```kotlin
// normal way
val passphrase = "bitcoin".toCharArray()
mnemonicCode.toSeed(passphrase)

// more private way (erase at the end)
charArrayOf('z', 'c', 'a', 's', 'h').let { passphrase ->
    mnemonicCode.toSeed(passphrase)
    passphrase.fill('0') // erased!
}
```
* Generate raw entropy for a corresponding word count
```kotlin
val entropy: ByteArray = WordCount.COUNT_18.toEntropy()

// this can be used to directly generate a mnemonic:
val mnemonicCode = MnemonicCode(entropy)

// note: that gives the same result as calling:
MnemonicCode(WordCount.COUNT_18)
```
* Validate pre-existing or user-provided mnemonic  
  (NOTE: mnemonics generated by the library "from scratch" are valid, by definition)
```kotlin
// throws a typed exception when invalid:
//     ChecksumException - when checksum fails, usually meaning words are swapped
//     WordCountException(count) - invalid number of words
//     InvalidWordException(word) - contains a word not found on the list
mnemonicCode.validate()
```
* Iterate over words
```kotlin
// mnemonicCodes are iterable
for (word in mnemonicCode) {
    println(word)
}

mnemonicCode.forEach { word ->
    println(word)
}
```
* Clean up!
```kotlin
mnemonicCode.clear() // code words are deleted and no longer available for attacker
```
#### Advanced Usage
 These generated codes are compatible with kotlin's [scoped resource usage](https://kotlinlang.org/docs/tutorials/kotlin-for-py/scoped-resource-usage.html)
* Leverage `use` to automatically clean-up after use
```kotlin
MnemonicCode(WordCount.COUNT_24).use {
    // Do something with the words (wordCount == 24)
}
// memory has been cleared at this point (wordCount == 0)
```
* Generate original entropy that was used to create the mnemonic
  (or throw exception if the mnemonic is invalid).
  * Note: Calling this function only succeeds when the entropy is valid so it also can be used, indirectly, for validation. In fact, currently, it is called as part of the `MnemonicCode::validate()` function.
```kotlin
val entropy: ByteArray = MnemonicCode(preExistingPhraseString).toEntropy()
```
* Mnemonics generated by the library do not need to be validated while creating the corresponding seed. That step can be skipped for a little added speed and security (because validation generates strings on the heap--which might get improved in a future release).
```kotlin
seed = MnemonicCode(WordCount.COUNT_24).toSeed(validate = false)
```
* Other languages are not yet supported but the API for them is in place. It accepts any `ISO 639-1` language code. For now, using it with anything other than "en" will result in an `UnsupportedOperationException`.
```kotlin
// results in exception, for now
val mnemonicCode = MnemonicCode(WordCount.COUNT_24, languageCode = Locale.GERMAN.language)

// english is the only language that doesn't crash
val mnemonicCode = MnemonicCode(WordCount.COUNT_24, languageCode = Locale.ENGLISH.language)
```

# Known issues
 * When publishing the library, a Gradle warning will be printed. This is a [known issue](https://youtrack.jetbrains.com/issue/KT-46466) in Kotlin Multiplatform and can be safely ignored.

## Credits
* [zcash/ebfull](https://github.com/ebfull) - Zcash core dev and BIP-0039 co-author who inspired creation of this library
* [bitcoinj](https://github.com/bitcoinj/bitcoinj/blob/master/core/src/main/java/org/bitcoinj/crypto/MnemonicCode.java) - Java implementation from which much of this code was adapted
* [Trezor](https://github.com/trezor/python-mnemonic/blob/master/vectors.json) - for their OG [test data set](https://github.com/trezor/python-mnemonic/blob/master/vectors.json) that has excellent edge cases
* [Cole Barnes](http://cryptofreek.org/2012/11/29/pbkdf2-pure-java-implementation/) - whose PBKDF2SHA512 Java implementation is floating around _everywhere_ online
* [Ken Sedgwick](https://github.com/ksedgwic) - who adapted Cole Barnes' work to use SHA-512
