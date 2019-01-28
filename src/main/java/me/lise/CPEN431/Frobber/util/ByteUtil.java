package me.lise.CPEN431.Frobber.util;

import java.nio.*;

public class ByteUtil {
    /**
     * Parse a 32-bit big endian integer from a byte array.
     * @param bytes The byte array. Must have length of at least <code>offset + 4</code>.
     * @param offset The index of the first byte to parse.
     * @return The integer.
     */
    public static int intFromByteArray(byte[] bytes, int offset) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result <<= 8;
            result += bytes[i + offset];
        }
        return result;
    }

    /**
     * Parse a 32-bit big endian integer from the first 4 bytes of a byte array.
     * @param bytes The byte array. Must have length of at least <code>4</code>.
     * @return The integer.
     */
    public static int intFromByteArray(byte[] bytes) {
        return intFromByteArray(bytes, 0);
    }

    /**
     * Convert a 32-bit little endian integer to a byte array.
     * @param num
     * @return
     */
    public static byte[] intToByteArray(int num) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(num).array();
    }

    /**
     * Convert part of a byte array to a hex string.
     * @param bytes The byte array. Must have length of at least <code>offset + length</code>.
     * @param offset The first byte to convert.
     * @param length The number of bytes to convert.
     * @return The converted string.
     */
    public static String strFromByteArray(byte[] bytes, int offset, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            byte b = bytes[i+offset];
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    /**
     * Concatenate two byte arrays. Will not modify the input arrays.
     * @param a The first byte array.
     * @param b The second byte array.
     * @return The concatenation of {@code a} and {@code b}.
     */
    public static byte[] joinBytes(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
