package cash.z.ecc.android.bip39

import io.kotest.assertions.fail
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class MnemonicsTest : BehaviorSpec({
    Given("a valid, known mnemonic phrase") {
        val mnemonic =
            "still champion voice habit trend flight survey between bitter process artefact blind carbon truly provide dizzy crush flush breeze blouse charge solid fish spread"
        When("it is converted into a seed") {
            val result = mnemonic.toCharArray().toSeed()
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
                    hex shouldBe "f550d5399659396587a59b6ad446eb89da7741ebb1e42f87c22451d20ece8bb1e09ccb3c19f967f37fbf435367bc295c692c0ce000c52f5b991f1ca91169565e"
                }
            }
        }
    }

    Given("a request for entropy") {
        val m = Mnemonics()
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
                    val entropy = m.createEntropy(wordCount)
                    entropy.size shouldBe bitLength
                }
            }
        }
    }

    Given("a supported word length") {
        Mnemonics.WordCount.values().forEach {
            When("a mnemonic phrase is created using the ${it.name} enum value") {
                Then("it has ${it.count - 1} spaces") {
                    fail("not implemented")
                }
            }
        }
    }

    Given("predefined entropy as hex") {
        When("it is converted to a mnemonic phrase") {
            Then("it should match the expected phrase") {
                val m = Mnemonics()
                forAll(
                    row(24, "b893a6b0da8fc9b73d709bda939e818a677aa376c266949378300b65a34b8e52", "review outdoor promote relax wish swear volume beach surround ostrich parrot below jeans faculty swallow error nest orange army bitter focus place deer fat"),
                    row(18, "d5bcbf62dea1a07ab1abb0144b299300137168a7939f3071f112b557", "stick tourist suffer run borrow diary shop invite begin flock gospel ability damage reform oxygen initial corn moon dwarf height image"),
                    row(15, "e06ce21369dc09eb2bda66510a76f65ab3f947cce90fcb10", "there grow luggage squirrel scene void quarter error extra father rural rely display physical crisp capable slam lumber"),
                    row(12, "0b01c3c0b0590faf45fc171da17cfb22", "arch asthma usual gaze movie stumble blood load buffalo armor disagree earth")
                ) { count, entropy, mnemonic ->
                    fail("not implemented")
                }
            }
        }
    }

    // TODO: use test values from the original BIP : https://github.com/trezor/python-mnemonic/blob/master/vectors.json
    Given("The original BIP-0039 test data set") {
        When("each provided entropy is converted to a mnemonic phrase") {
            Then("each result matches the corresponding test phrase") {
                fail("not implemented")
            }
        }
        When("each provided mnemonic phrase is converted into a seed") {
            Then("each result matches the corresponding test seed") {
                fail("not implemented")
            }
        }
    }
})


//
// Test Utilities
//

fun ByteArray.toHex(): String {
    val sb = StringBuilder(size * 2)
    for (b in this)
        sb.append(String.format("%02x", b))
    return sb.toString()
}

fun String.fromHex(): ByteArray {
    val len = length
    val data = ByteArray(len / 2)
    var i = 0
    while (i < len) {
        data[i / 2] =
            ((Character.digit(this[i], 16) shl 4) + Character.digit(this[i + 1], 16)).toByte()
        i += 2
    }
    return data
}
