package com.example.wear

import com.example.wear.core.security.Sha256
import com.example.wear.core.security.Sha256PasswordHasher
import com.example.wear.core.security.toHex
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class Sha256Test {

    private fun sha(text: String) = Sha256.digest(text.encodeToByteArray()).toHex()

    @Test
    fun emptyString() {
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", sha(""))
    }

    @Test
    fun abc() {
        assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad", sha("abc"))
    }

    @Test
    fun twoBlocks() {
        assertEquals(
            "248d6a61d20638b8e5c026930c3e6039a33ce45964ff2167f6ecedd419db06c1",
            sha("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq")
        )
    }

    @Test
    fun hasherIsDeterministicAndSaltDependent() {
        val hasher = Sha256PasswordHasher()
        val salt = hasher.newSalt()
        assertEquals(32, salt.length)
        assertEquals(hasher.hash("Secret123", salt), hasher.hash("Secret123", salt))
        assertNotEquals(hasher.hash("Secret123", salt), hasher.hash("Secret124", salt))
        assertNotEquals(hasher.hash("Secret123", salt), hasher.hash("Secret123", hasher.newSalt()))
        assertEquals(64, hasher.hash("Secret123", salt).length)
    }
}
