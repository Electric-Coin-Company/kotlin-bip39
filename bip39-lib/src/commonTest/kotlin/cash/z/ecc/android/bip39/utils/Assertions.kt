package cash.z.ecc.android.bip39.utils

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail

inline fun <T> shouldNotThrowAny(block: () -> T): T {
    return try {
        block()
    } catch (e: Throwable) {
        fail("No exception expected, but ${e::class.simpleName} was thrown", e)
    }
}

inline fun <reified T : Throwable> shouldThrow(block: () -> Any?): T {
    val expectedExceptionClass = T::class
    val thrownThrowable =
        try {
            block()
            // Can't throw failure here directly, as it would be caught by the catch clause,
            // and it's an AssertionError, which is a special case
            null
        } catch (thrown: Throwable) {
            thrown
        }

    return when (thrownThrowable) {
        null -> fail("Expected exception ${expectedExceptionClass.simpleName} but no exception was thrown.")
        is T -> {
            // This should be before `is AssertionError`. If the user is purposefully trying to verify
            // `shouldThrow<AssertionError>{}` this will take priority
            thrownThrowable
        }

        is AssertionError -> throw thrownThrowable
        else ->
            fail(
                "Expected exception ${expectedExceptionClass.simpleName} but a ${thrownThrowable::class.simpleName} " +
                    "was thrown instead.",
                thrownThrowable,
            )
    }
}

class ThrowAssertionFunctionsTest {
    @Test
    fun testShouldNotThrowAnyWithNoException() {
        val result =
            shouldNotThrowAny {
                // Block that does not throw any exception
                42
            }
        assertTrue { result == 42 }
    }

    @Test
    fun testShouldNotThrowAnyWithException() {
        try {
            shouldNotThrowAny {
                // Block that throws an exception
                throw IllegalArgumentException("Test exception")
            }
            @Suppress("UNREACHABLE_CODE")
            fail("Expected an exception to be thrown, but it was not.")
        } catch (e: AssertionError) {
            assertTrue {
                e.message?.contains("No exception expected, but IllegalArgumentException was thrown") ?: false
            }
        }
    }

    @Test
    fun testShouldThrowWithExpectedException() {
        val exception =
            shouldThrow<IllegalArgumentException> {
                // Block that throws the expected exception
                throw IllegalArgumentException("Test exception")
            }
        assertTrue { exception.message == "Test exception" }
    }

    @Test
    fun testShouldThrowWithUnexpectedException() {
        try {
            shouldThrow<IllegalArgumentException> {
                // Block that throws a different exception than expected
                throw IllegalStateException("Test exception")
            }
            fail("Expected IllegalArgumentException to be thrown, but IllegalStateException was thrown.")
        } catch (e: AssertionError) {
            assertTrue {
                e.message?.contains(
                    "Expected exception IllegalArgumentException but a IllegalStateException " +
                        "was thrown instead.",
                )
                    ?: false
            }
        }
    }

    @Test
    fun testShouldThrowWithNoException() {
        try {
            shouldThrow<IllegalArgumentException> {
                // Block that does not throw any exception
            }
            fail("Expected an exception to be thrown, but it was not.")
        } catch (e: AssertionError) {
            assertTrue {
                e.message?.contains("Expected exception IllegalArgumentException but no exception was thrown.") ?: false
            }
        }
    }
}
