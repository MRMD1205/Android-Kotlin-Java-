package com.dynasty.util

import android.util.Base64

import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.security.GeneralSecurityException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionUtil @Throws(OnkoutSecurityException::class)
private constructor() {

    private val TRANSFORMATION = "AES/CBC/PKCS7PADDING"
    private val SECRET_KEY_HASH_TRANSFORMATION: String
//    private val CHARSET = "UTF-8"
    private val charset: Charset = Charsets.UTF_8
    private val writer: Cipher
    private val reader: Cipher

    private val iv: IvParameterSpec
        get() {
            val iv = ByteArray(writer.blockSize)
            System.arraycopy("ABCDEFGHIJKLMNOP".toByteArray(), 0,
                    iv, 0, writer.blockSize)
            return IvParameterSpec(iv)
        }

    class OnkoutSecurityException internal constructor(e: Throwable) : RuntimeException(e)

    init {
        try {
            this.writer = Cipher.getInstance(TRANSFORMATION)
            this.reader = Cipher.getInstance(TRANSFORMATION)

            initCiphers("(!Jack%Sparrow_Black{Pearl})~20@3")

        } catch (e: GeneralSecurityException) {
            throw OnkoutSecurityException(e)
        } catch (e: UnsupportedEncodingException) {
            throw OnkoutSecurityException(e)
        }

        this.SECRET_KEY_HASH_TRANSFORMATION = "SHA-256"
    }

    @Throws(UnsupportedEncodingException::class, NoSuchAlgorithmException::class, InvalidKeyException::class, InvalidAlgorithmParameterException::class)
    private fun initCiphers(secureKey: String) {
        val ivSpec = iv
        val secretKey = getSecretKey(secureKey)

        Logger.e("SecureKey: ", secureKey)
        Logger.e("SecretKey: ", secretKey.format + " - " + secretKey.algorithm + " - " + secretKey.hashCode().toString() + " - " + secretKey.toString())

        writer.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
        reader.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
    }

    @Throws(UnsupportedEncodingException::class, NoSuchAlgorithmException::class)
    private fun getSecretKey(key: String): SecretKeySpec {
        val keyBytes = createKeyBytes(key)
        return SecretKeySpec(keyBytes, TRANSFORMATION)
    }

    @Throws(UnsupportedEncodingException::class, NoSuchAlgorithmException::class)
    private fun createKeyBytes(key: String): ByteArray {
        val md = MessageDigest.getInstance("SHA-256")
        md.reset()
        return md.digest(key.toByteArray(charset))
    }

    @Throws(OnkoutSecurityException::class)
    private fun encrypt(value: String, writer: Cipher): String {
        val secureValue: ByteArray
        try {
            secureValue = convert(writer, value.toByteArray(charset))
        } catch (e: UnsupportedEncodingException) {
            throw OnkoutSecurityException(e)
        }

        return Base64.encodeToString(secureValue, Base64.NO_WRAP)
    }

    private fun decrypt(securedEncodedValue: String): String {
        val securedValue = Base64
                .decode(securedEncodedValue, Base64.NO_WRAP)
        val value = convert(reader, securedValue)
        try {
            return String(value, charset)
        } catch (e: UnsupportedEncodingException) {
            throw OnkoutSecurityException(e)
        }

    }

    fun getEncodedValue(data: String): String {
        val secureValueEncoded = encrypt(data, writer)
        Logger.e("Encrypted String: ", secureValueEncoded)
        return secureValueEncoded
    }

    @Throws(OnkoutSecurityException::class)
    fun getDecodedValue(data: String): String {
        val originalText = decrypt(data)
        Logger.e("Decrypted String: ", originalText)
        return originalText
    }

    companion object {

        private var instance: EncryptionUtil? = null

        fun getInstance(): EncryptionUtil {
            if (instance == null) {
                instance = EncryptionUtil()
            }
            return instance!!
        }

        @Throws(OnkoutSecurityException::class)
        private fun convert(cipher: Cipher, bs: ByteArray): ByteArray {
            try {
                return cipher.doFinal(bs)
            } catch (e: Exception) {
                throw OnkoutSecurityException(e)
            }

        }
    }
}
