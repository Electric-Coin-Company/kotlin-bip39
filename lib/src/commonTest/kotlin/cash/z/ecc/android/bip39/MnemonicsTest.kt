package cash.z.ecc.android.bip39

import cash.z.ecc.android.bip39.Mnemonics.MnemonicCode
import io.fluidsonic.locale.Locale
import io.kotest.assertions.asClue
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.serialization.SerialName
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.decodeHex
import okio.ByteString.Companion.toByteString


class MnemonicsTest : BehaviorSpec({
    val validPhrase = "void come effort suffer camp survey warrior heavy shoot primary clutch crush open amazing screen patrol group space point ten exist slush involve unfold"
    val lang = Locale.forLanguage("en").language!!

    Given("a valid, known mnemonic phrase") {
        When("it is converted into a seed") {
            val result = MnemonicCode(validPhrase).toSeed()
            Then("it should not be null") {
                result shouldNotBe null
            }
            Then("it should not be empty") {
                result.size shouldNotBe 0
            }
            Then("it should be the expected length") {
                result.size shouldBe 64
            }
            And("when that seed is converted to hex") {
                val hex = result.toHex()
                Then("it should be the expected length") {
                    hex.length shouldBe 128
                }
                Then("it should equal the expected value") {
                    hex shouldBe "b873212f885ccffbf4692afcb84bc2e55886de2dfa07d90f5c3c239abc31c0a6ce047e30fd8bf6a281e71389aa82d73df74c7bbfb3b06b4639a5cee775cccd3c"
                }
            }
        }
    }

    Given("a request for entropy") {
        When("each supported word count is requested") {
            Then("ensure each result has the correct bit length") {
                forAll(
                    row(12, 128),
                    row(15, 160),
                    row(18, 192),
                    row(21, 224),
                    row(24, 256)
                ) { count, bitLength ->
                    val wordCount = Mnemonics.WordCount.valueOf(count)
                    wordCount shouldNotBe null
                    bitLength shouldBe wordCount!!.bitLength
                    wordCount.toEntropy().let { entropy ->
                        entropy.size * 8 shouldBe bitLength
                    }
                }
            }
        }
    }

    Given("a supported word count") {
        Mnemonics.WordCount.values().forEach { wordCount ->
            When("a mnemonic phrase is created using the ${wordCount.name} enum value") {
                MnemonicCode(wordCount).let { phrase ->
                    phrase.chars.concatToString().asClue { phraseString ->
                        Then("it has ${wordCount.count - 1} spaces") {
                            phrase.chars.count { it == ' ' } shouldBe wordCount.count - 1
                        }
                        And("when that is converted to a list of CharArrays") {
                            phrase.words.map { it.concatToString() }.asClue { words ->
                                Then("It has ${wordCount.count} elements") {
                                    words.size shouldBe wordCount.count
                                }
                                Then("Each word is present in the original phrase") {
                                    phraseString.split(' ').let { correctWords ->
                                        words.shouldContainAll(correctWords)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    Given("predefined entropy as hex") {
        When("it is converted to a mnemonic phrase") {
            Then("it should match the expected phrase") {
                forAll(
                    row(24, "b893a6b0da8fc9b73d709bda939e818a677aa376c266949378300b65a34b8e52", "review outdoor promote relax wish swear volume beach surround ostrich parrot below jeans faculty swallow error nest orange army bitter focus place deer fat"),
                    row(18, "d5bcbf62dea1a07ab1abb0144b299300137168a7939f3071f112b557", "stick tourist suffer run borrow diary shop invite begin flock gospel ability damage reform oxygen initial corn moon dwarf height image"),
                    row(15, "e06ce21369dc09eb2bda66510a76f65ab3f947cce90fcb10", "there grow luggage squirrel scene void quarter error extra father rural rely display physical crisp capable slam lumber"),
                    row(12, "0b01c3c0b0590faf45fc171da17cfb22", "arch asthma usual gaze movie stumble blood load buffalo armor disagree earth")
                ) { count, entropy, mnemonic ->
                    val code = MnemonicCode(entropy.fromHex())
                    code.chars.concatToString() shouldBe mnemonic
                }
            }
        }
    }

    // uses test values from the original BIP : https://github.com/trezor/python-mnemonic/blob/master/vectors.json
    Given("The original BIP-0039 test data set") {
        val testData: TestDataSet? = testData
        testData shouldNotBe null
        When("each provided entropy is converted to a mnemonic phrase [entropy -> mnemonic]") {
            Then("each result matches the corresponding test mnemonic phrase") {
                testData!!.values.forEach {
                    val entropy = it[0].fromHex()
                    val mnemonic = it[1]
                    MnemonicCode(entropy).chars.concatToString() shouldBe mnemonic
                }
            }
        }
        When("each provided mnemonic phrase is reverted to entropy [mnemonic -> entropy]") {
            Then("each result matches the corresponding test entropy") {
                testData!!.values.forEach {
                    val entropy = it[0]
                    val mnemonic = it[1]
                    MnemonicCode(mnemonic).toEntropy().toHex() shouldBe entropy
                }
            }
        }
        When("each provided mnemonic phrase is converted into a seed [mnemonic -> seed]") {
            Then("each result matches the corresponding test seed") {
                testData!!.values.forEach {
                    val mnemonic = it[1].toCharArray()
                    val seed = it[2]
                    val passphrase = "TREZOR".toCharArray()
                    MnemonicCode(mnemonic, lang).toSeed(passphrase).toHex() shouldBe seed
                }
            }
        }
    }

    Given("an invalid mnemonic") {
        When("it was created by swapping two words in a valid mnemonic") {
            // swapped "trend" and "flight"
            validPhrase.swap(4, 5).asClue { mnemonicPhrase ->
                Then("validate() fails with a checksum error") {
                    shouldThrow<Mnemonics.ChecksumException> {
                        MnemonicCode(mnemonicPhrase).validate()
                    }
                }
                Then("toEntropy() fails with a checksum error") {
                    shouldThrow<Mnemonics.ChecksumException> {
                        MnemonicCode(mnemonicPhrase).toEntropy()
                    }
                }
                Then("toSeed() fails with a checksum error") {
                    shouldThrow<Mnemonics.ChecksumException> {
                        MnemonicCode(mnemonicPhrase).toSeed()
                    }
                }
                Then("toSeed(validate=false) succeeds!!") {
                    shouldNotThrowAny {
                        MnemonicCode(mnemonicPhrase).toSeed(validate = false)
                    }
                }
            }
        }
        When("it contains an invalid word") {
            val mnemonicPhrase = validPhrase.split(' ').let { words ->
                validPhrase.replace(words[23], "convincee")
            }
            mnemonicPhrase.asClue {
                Then("validate() fails with a word validation error") {
                    shouldThrow<Mnemonics.InvalidWordException> {
                        MnemonicCode(mnemonicPhrase).validate()
                    }
                }
                Then("toEntropy() fails with a word validation error") {
                    shouldThrow<Mnemonics.InvalidWordException> {
                        MnemonicCode(mnemonicPhrase).toEntropy()
                    }
                }
                Then("toSeed() fails with a word validation error") {
                    shouldThrow<Mnemonics.InvalidWordException> {
                        MnemonicCode(mnemonicPhrase).toSeed()
                    }
                }
                Then("toSeed(validate=false) succeeds!!") {
                    shouldNotThrowAny {
                        MnemonicCode(mnemonicPhrase).toSeed(validate = false)
                    }
                }
            }
        }
        When("it contains an unsupported number of words") {
            val mnemonicPhrase = "$validPhrase still"
            Then("it fails with a word count error") {
                shouldThrow<Mnemonics.WordCountException> {
                    MnemonicCode(mnemonicPhrase).validate()
                }
                shouldThrow<Mnemonics.WordCountException> {
                    MnemonicCode(mnemonicPhrase).toEntropy()
                }
                shouldThrow<Mnemonics.WordCountException> {
                    MnemonicCode(mnemonicPhrase).toSeed()
                }
                shouldNotThrowAny {
                    MnemonicCode(mnemonicPhrase).toSeed(validate = false)
                }
            }
        }
    }
})


//
// Test Utilities
//

@kotlinx.serialization.Serializable
data class TestDataSet (
    @SerialName("english") val values: List<List<String>>
)

fun ByteArray.toHex(): String {
    return toByteString().hex()
}

fun String.fromHex(): ByteArray {
    return decodeHex().toByteArray()
}

fun String.swap(srcWord: Int, destWord: Int = srcWord + 1): String {
    if (srcWord >= destWord) throw IllegalArgumentException("srcWord must be less than destWord")
    if (destWord > count { it == ' '}) throw IllegalArgumentException("there aren't that many words")
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

val testData: TestDataSet = Json.decodeFromString(BIP0039TestValues)

