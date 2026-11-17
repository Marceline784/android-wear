package com.example.wear.core.security

import kotlin.random.Random

/** Хешування паролів: пароль ніколи не зберігається у відкритому вигляді. */
interface PasswordHasher {
    /** Нова випадкова сіль (hex). */
    fun newSalt(): String

    /** Хеш пароля із сіллю (hex). Однакові пароль і сіль дають однаковий результат. */
    fun hash(password: String, salt: String): String
}

/**
 * Сіль + SHA-256, повторений ITERATIONS разів (уповільнює підбір).
 * Реалізація на чистому Kotlin, тому однаково працює на Android і Desktop.
 * Це локальне сховище для навчального застосунку: у реальному проєкті автентифікацію виконує сервер.
 */
class Sha256PasswordHasher : PasswordHasher {

    override fun newSalt(): String = Random.Default.nextBytes(SALT_BYTES).toHex()

    override fun hash(password: String, salt: String): String {
        val saltBytes = salt.encodeToByteArray()
        var digest = Sha256.digest(saltBytes + password.encodeToByteArray())
        repeat(ITERATIONS) { digest = Sha256.digest(digest + saltBytes) }
        return digest.toHex()
    }

    private companion object {
        const val SALT_BYTES = 16
        const val ITERATIONS = 5_000
    }
}

internal fun ByteArray.toHex(): String {
    val chars = "0123456789abcdef"
    val sb = StringBuilder(size * 2)
    for (b in this) {
        val v = b.toInt() and 0xFF
        sb.append(chars[v shr 4]).append(chars[v and 0x0F])
    }
    return sb.toString()
}

/** SHA-256 за стандартом FIPS 180-4. */
internal object Sha256 {

    private val K = uintArrayOf(
        0x428a2f98u, 0x71374491u, 0xb5c0fbcfu, 0xe9b5dba5u, 0x3956c25bu, 0x59f111f1u, 0x923f82a4u, 0xab1c5ed5u,
        0xd807aa98u, 0x12835b01u, 0x243185beu, 0x550c7dc3u, 0x72be5d74u, 0x80deb1feu, 0x9bdc06a7u, 0xc19bf174u,
        0xe49b69c1u, 0xefbe4786u, 0x0fc19dc6u, 0x240ca1ccu, 0x2de92c6fu, 0x4a7484aau, 0x5cb0a9dcu, 0x76f988dau,
        0x983e5152u, 0xa831c66du, 0xb00327c8u, 0xbf597fc7u, 0xc6e00bf3u, 0xd5a79147u, 0x06ca6351u, 0x14292967u,
        0x27b70a85u, 0x2e1b2138u, 0x4d2c6dfcu, 0x53380d13u, 0x650a7354u, 0x766a0abbu, 0x81c2c92eu, 0x92722c85u,
        0xa2bfe8a1u, 0xa81a664bu, 0xc24b8b70u, 0xc76c51a3u, 0xd192e819u, 0xd6990624u, 0xf40e3585u, 0x106aa070u,
        0x19a4c116u, 0x1e376c08u, 0x2748774cu, 0x34b0bcb5u, 0x391c0cb3u, 0x4ed8aa4au, 0x5b9cca4fu, 0x682e6ff3u,
        0x748f82eeu, 0x78a5636fu, 0x84c87814u, 0x8cc70208u, 0x90befffau, 0xa4506cebu, 0xbef9a3f7u, 0xc67178f2u
    )

    private val INITIAL = uintArrayOf(
        0x6a09e667u, 0xbb67ae85u, 0x3c6ef372u, 0xa54ff53au, 0x510e527fu, 0x9b05688cu, 0x1f83d9abu, 0x5be0cd19u
    )

    fun digest(message: ByteArray): ByteArray {
        // Доповнення: біт 1, нулі, довжина повідомлення в бітах (64 біти, big-endian)
        val bitLength = message.size.toLong() * 8
        val paddedSize = ((message.size + 9 + 63) / 64) * 64
        val padded = ByteArray(paddedSize)
        message.copyInto(padded)
        padded[message.size] = 0x80.toByte()
        for (i in 0 until 8) {
            padded[paddedSize - 1 - i] = (bitLength ushr (8 * i)).toByte()
        }

        val h = INITIAL.copyOf()
        val w = UIntArray(64)

        for (chunk in 0 until paddedSize step 64) {
            for (t in 0 until 16) {
                val i = chunk + t * 4
                w[t] = ((padded[i].toUInt() and 0xFFu) shl 24) or
                    ((padded[i + 1].toUInt() and 0xFFu) shl 16) or
                    ((padded[i + 2].toUInt() and 0xFFu) shl 8) or
                    (padded[i + 3].toUInt() and 0xFFu)
            }
            for (t in 16 until 64) {
                val s0 = rotr(w[t - 15], 7) xor rotr(w[t - 15], 18) xor (w[t - 15] shr 3)
                val s1 = rotr(w[t - 2], 17) xor rotr(w[t - 2], 19) xor (w[t - 2] shr 10)
                w[t] = w[t - 16] + s0 + w[t - 7] + s1
            }

            var a = h[0]; var b = h[1]; var c = h[2]; var d = h[3]
            var e = h[4]; var f = h[5]; var g = h[6]; var hh = h[7]

            for (t in 0 until 64) {
                val bigS1 = rotr(e, 6) xor rotr(e, 11) xor rotr(e, 25)
                val ch = (e and f) xor (e.inv() and g)
                val temp1 = hh + bigS1 + ch + K[t] + w[t]
                val bigS0 = rotr(a, 2) xor rotr(a, 13) xor rotr(a, 22)
                val maj = (a and b) xor (a and c) xor (b and c)
                val temp2 = bigS0 + maj
                hh = g; g = f; f = e; e = d + temp1
                d = c; c = b; b = a; a = temp1 + temp2
            }

            h[0] += a; h[1] += b; h[2] += c; h[3] += d
            h[4] += e; h[5] += f; h[6] += g; h[7] += hh
        }

        val out = ByteArray(32)
        for (i in 0 until 8) {
            out[i * 4] = (h[i] shr 24).toByte()
            out[i * 4 + 1] = (h[i] shr 16).toByte()
            out[i * 4 + 2] = (h[i] shr 8).toByte()
            out[i * 4 + 3] = h[i].toByte()
        }
        return out
    }

    private fun rotr(x: UInt, n: Int): UInt = (x shr n) or (x shl (32 - n))
}
