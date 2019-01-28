package me.lise.CPEN431.Frobber.util;

import ca.NetSysLab.ProtocolBuffers.Message.Msg;
import com.google.protobuf.ByteString;

import java.util.Arrays;
import java.util.zip.CRC32;

public class MessageUtil {
    /**
     * Compute the checksum of arbitrary byte arrays.
     * @param args The byte arrays.
     * @return The checksum.
     */
    public static long getChecksum(byte[] ... args) {
        CRC32 crc32 = new CRC32();
        for (byte[] b : args) {
            crc32.update(b);
        }
        return crc32.getValue();
    }

    /**
     * Create a ready-to-send Msg protobuf.
     * @param id The message's unique id.
     * @param payload The payload.
     * @return The Msg.
     */
    public static Msg makeMessage(byte[] id, byte[] payload) {
        return Msg.newBuilder().setMessageID(ByteString.copyFrom(id))
                               .setPayload(ByteString.copyFrom(payload))
                               .setCheckSum(getChecksum(id, payload))
                               .build();
    }

    /**
     * Check if the message's checksum matches its fields.
     * @param message The message to validate.
     * @return {@code true} if the checksum matches the computed checksum.
     */
    public static boolean validateChecksum(Msg message) {
        long checksum = getChecksum(message.getMessageID().toByteArray(), message.getPayload().toByteArray());
        return checksum == message.getCheckSum();
    }

    /**
     * Check that the message IDs of two byte arrays match. Both arrays have the message ID at the start of the array.
     * @param a The first byte array, must have minimum length {@code numBytes}.
     * @param b The second byte array, must have minimum length {@code numBytes}.
     * @param numBytes Length of ID.
     * @return True if the first {@code numBytes} of each array match.
     */
    public static boolean validateMessageID(byte[] a, byte[] b, int numBytes) {
        return Arrays.equals(Arrays.copyOfRange(a, 0, numBytes),
                             Arrays.copyOfRange(b, 0, numBytes));
    }

    /**
     * Check that the message IDs of two {@link Msg}s match.
     * @param a The first message.
     * @param b The second message.
     * @return True if they match.
     */
    public static boolean validateMessageID(Msg a, Msg b) {
        return a.getMessageID().equals(b.getMessageID());
    }
}
