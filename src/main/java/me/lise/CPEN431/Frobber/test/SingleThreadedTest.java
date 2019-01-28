package me.lise.CPEN431.Frobber.test;

import ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest;
import ca.NetSysLab.ProtocolBuffers.KeyValueResponse.KVResponse;
import ca.NetSysLab.ProtocolBuffers.Message.Msg;
import me.lise.CPEN431.Frobber.util.MessageUtil;

import com.google.protobuf.ByteString;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class SingleThreadedTest extends TestSuite {
    public SingleThreadedTest(InetAddress host, int port) throws IOException {
        super(host, port);
    }

    /**
     * Run the tests in this suite.
     */
    @Override
    public void run() throws IOException {
        printLine("---------------------------------------------------");
        printLine(String.format("SINGLE-THREADED SERVER TESTS ON %s:%d", host.getHostName(), port));
        printLine("---------------------------------------------------");

        // simple server tests
        int failures = 0;
        failures += testIsAlive();
        failures += testGetPid();
        failures += testGetMembershipCount();
        failures += testBadCommand();

        // if there are too many failures, then probably don't run anymore tests...
        if (failures > 2) {
            printLine("Too many test failures. Aborting.");
            return;
        }

        // clear the server before the next tests
        client.sendWipeout();

        // simple key-value store tests
        testSimplePutGetRemove();
        testOverwriteValue();
        testNoKey();
        testExtraArgs();
        testLargeKeyValue();
        testKeyValueTooLong();
        testWipeOut();

        // corrupt message tests
        testGarbageRequest();
        testBadChecksum();

        // capacity tests
        failures = 0;
        failures += testServerCapacity();
        failures += testOutOfMemory();

        // stress tests -- these are costly, so only do it if the last two tests passed
        if (failures == 0) {
            testServerCapacityStress();
            testServerMemoryStress();
        } else {
            printLine("Skipping server capacity stress test due to earlier failures...\n");
        }

        // summarize
        printLine(String.format("Completed %d tests: %d passed, %d failed, %d inconclusive\n",
                numTests, numPassed, numFailed, numTests - numFailed - numPassed));
    }

    /**
     * Simple heartbeat test. Server should return OKAY. Check that no other fields are defined.
     */
    private int testIsAlive() throws IOException {
        printLine("[TEST: IsAlive]");
        KVResponse response;

        response = client.sendIsAlive();
        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.OKAY
                || response.hasPid() || response.hasValue() || response.hasVersion()
                || response.hasOverloadWaitTime() || response.hasMembershipCount()) {
            return fail();
        }
        return pass();
    }

    /**
     * Simple GetPid. Server should return OKAY with PID value.
     */
    private int testGetPid() throws IOException {
        printLine("[TEST: GetPid]");
        KVResponse response;

        response = client.sendGetPid();
        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.OKAY || !response.hasPid()
                    || response.hasValue() || response.hasVersion()
                    || response.hasOverloadWaitTime() || response.hasMembershipCount()) {
            return fail();
        }

        return pass();
    }

    /**
     * Simple GetMembershipCount test. Server should return OKAY with count = 1.
     * TODO: Change this test in the future when the server might be permitted to return other counts.
     */
    private int testGetMembershipCount() throws IOException {
        printLine("[TEST: GetMembershipCount]");
        KVResponse response;

        response = client.sendGetMembershipCount();
        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.OKAY || !response.hasMembershipCount()
                || response.getMembershipCount() != 1
                || response.hasPid() || response.hasValue()
                || response.hasVersion() || response.hasOverloadWaitTime()) {
            return fail();
        }

        return pass();
    }

    /**
     * Unknown command test. The server should return an "bad command" error.
     */
    private int testBadCommand() throws IOException {
        printLine("[TEST: Invalid command]");
        KVResponse response;

        response = client.sendReceive(KVRequest.newBuilder().setCommand(-1).build());
        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.BAD_COMMAND
                || response.hasPid() || response.hasValue() || response.hasVersion()
                || response.hasMembershipCount() || response.hasOverloadWaitTime()) {
            return fail();
        }

        return pass();
    }

    /**
     * Put a value. Get the value and make sure it's the same value. Remove the value.
     */
    private int testSimplePutGetRemove() throws IOException {
        printLine("[TEST: Basic PUT -> GET -> REMOVE -> GET, all with same key]");
        KVResponse response;

        // put a key, value
        ByteString key = ByteString.copyFrom("pride".getBytes());
        ByteString value = ByteString.copyFrom("prejudice".getBytes());
        response = client.sendPut(key, value);

        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.OKAY) {
            return fail();
        }

        // get the value
        response = client.sendGet(key);
        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.OKAY
                || !(response.getValue().equals(value))
                || response.getVersion() != 0) {
            return fail();
        }

        // remove the value
        response = client.sendRemove(key);
        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.OKAY) {
            return fail();
        }

        // check that it was really removed
        response = client.sendGet(key);
        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.NO_KEY) {
            return fail();
        }

        return pass();
    }

    private int testNoKey() throws IOException {
        printLine("[TEST: GET & REMOVE on non-existent key]");
        KVResponse response;

        ByteString key = ByteString.copyFrom("Bertrand's teacup".getBytes());
        response = client.sendGet(key);
        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.NO_KEY) {
            return fail();
        }

        response = client.sendRemove(key);
        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.NO_KEY) {
            return fail();
        }
        return pass();
    }

    private int testExtraArgs() throws IOException {
        printLine("[TEST: Extra arguments in GET]");
        KVResponse response;

        // put a key, value
        ByteString key = ByteString.copyFrom("war".getBytes());
        ByteString value = ByteString.copyFrom("peace".getBytes());
        response = client.sendPut(key, value);

        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.OKAY) {
            return fail();
        }

        // GET with value specified
        response = client.sendReceive(
                KVRequest.newBuilder().setCommand(Command.GET).setKey(key).setValue(value).build());
        if (response == null) {
            return timeout();
        } else if (response.getErrCode() == ErrCode.OKAY) {
            return fail();
        }

        // GET with version specified
        response = client.sendReceive(
                KVRequest.newBuilder().setCommand(Command.GET).setKey(key).setVersion(3).build());
        if (response == null) {
            return timeout();
        } else if (response.getErrCode() == ErrCode.OKAY) {
            return fail();
        }
        return pass();
    }

    private int testOverwriteValue() throws IOException {
        printLine("[TEST: Overwrite a previous value]");
        KVResponse response;

        // put a key, value
        ByteString key = ByteString.copyFrom("romeo".getBytes());
        ByteString value = ByteString.copyFrom("rosalind".getBytes());
        response = client.sendPut(key, value, 1);

        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.OKAY) {
            return fail();
        }

        // put a new value
        ByteString newVal = ByteString.copyFrom("juliet".getBytes());
        response = client.sendPut(key, newVal, 2);
        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.OKAY) {
            return fail();
        }

        // get the value
        response = client.sendGet(key);
        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.OKAY
                || response.getVersion() != 2
                || !newVal.equals(response.getValue())) {
            return fail();
        }

        return pass();
    }

    private int testLargeKeyValue() throws IOException {
        printLine("[TEST: PUT with 32 byte key, 10000 byte value]");
        KVResponse response;

        // send a key and value of exactly the max size
        ByteString key = ByteString.copyFrom(Arrays.copyOf("rosencrantz".getBytes(), 32));
        ByteString value = ByteString.copyFrom(Arrays.copyOf("guildenstern".getBytes(), 10000));
        response = client.sendPut(key, value);
        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.OKAY) {
            return fail();
        }

        return pass();
    }

    private int testKeyValueTooLong() throws IOException {
        printLine("[TEST: PUT with 33 byte key, PUT with 10001 byte value]");
        KVResponse response;

        // send a key and value of exactly the max size
        ByteString badKey = ByteString.copyFrom(Arrays.copyOf("rosencrantz".getBytes(), 33));
        ByteString goodVal = ByteString.copyFrom(Arrays.copyOf("guildenstern".getBytes(), 30));
        response = client.sendPut(badKey, goodVal);
        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.BAD_KEY) {
            return fail();
        }

        ByteString goodKey = ByteString.copyFrom(Arrays.copyOf("rosencrantz".getBytes(), 20));
        ByteString badVal = ByteString.copyFrom(Arrays.copyOf("guildenstern".getBytes(), 60000));
        response = client.sendPut(goodKey, badVal);
        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.BAD_VALUE) {
            return fail();
        }

        return pass();
    }

    private int testWipeOut() throws IOException {
        printLine("[TEST: Wipeout command]");
        KVResponse response;

        // put three keys
        ByteString value = ByteString.copyFrom("sadreact".getBytes());
        for (int i = 1; i <= 3; i++) {
            ByteString key = ByteString.copyFrom(ByteBuffer.allocate(4).putInt(i).array());
            response = client.sendPut(key, value);
            if (response == null) {
                return timeout();
            } else if (response.getErrCode() != ErrCode.OKAY) {
                return fail();
            }
        }

        // send wipeout command
        response = client.sendWipeout();
        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.OKAY) {
            return fail();
        }

        // try to get the keys again. They should no longer exist.
        for (int i = 1; i <= 3; i++) {
            ByteString key = ByteString.copyFrom(ByteBuffer.allocate(4).putInt(i).array());
            response = client.sendGet(key);
            if (response == null) {
                return timeout();
            } else if (response.getErrCode() != ErrCode.NO_KEY) {
                return fail();
            }
        }

        return pass();
    }

    /**
     * Test that sending some garbage bytes doesn't crash the server.
     */
    private int testGarbageRequest() throws IOException {
        printLine("[TEST: Send garbage request]");

        byte[] message = "some garbage message".getBytes();
        byte[] buf = new byte[100];
        DatagramSocket socket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(message, message.length, host, port);
        DatagramPacket responsePacket;

        // send 3 times
        for (int i = 0; i < 3; i++) {
            socket.send(packet);

            responsePacket = new DatagramPacket(buf, buf.length);
            try {
                socket.setSoTimeout(500);  // wait 0.5 seconds
                socket.receive(responsePacket);
            } catch (SocketTimeoutException e) {
                continue;
            }

            // there should be no response, so if there was one, fail the test
            fail();
            break;
        }

        // send a heartbeat to make sure the server is still alive
        KVResponse response = client.sendIsAlive();
        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.OKAY) {
            return fail();
        }
        return pass();
    }

    /**
     * Test that a corrupted checksum doesn't crash the server.
     */
    private int testBadChecksum() throws IOException {
        printLine("[TEST: Send request with bad checksum]");

        byte[] messageID = "bad message".getBytes();
        byte[] payload = KVRequest.newBuilder().setCommand(Command.IS_ALIVE).build().toByteArray();
        byte[] message = Msg.newBuilder().setMessageID(ByteString.copyFrom(messageID))
                                         .setPayload(ByteString.copyFrom(payload))
                                         .setCheckSum(MessageUtil.getChecksum(messageID, payload) + 1)
                                         .build()
                                         .toByteArray();

        byte[] buf = new byte[100];
        DatagramSocket socket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(message, message.length, host, port);
        DatagramPacket responsePacket;

        // send 3 times
        for (int i = 0; i < 3; i++) {
            socket.send(packet);

            responsePacket = new DatagramPacket(buf, buf.length);
            try {
                socket.setSoTimeout(500);  // wait 0.5 seconds
                socket.receive(responsePacket);
            } catch (SocketTimeoutException e) {
                continue;
            }

            // there should be no response, so if there was one, fail the test
            fail();
            break;
        }

        // send a heartbeat to make sure the server is still alive
        KVResponse response = client.sendIsAlive();
        if (response == null) {
            return timeout();
        } else if (response.getErrCode() != ErrCode.OKAY) {
            return fail();
        }
        return pass();
    }

    /**
     * Test that the server stores at least 32 MB.
     */
    private int testServerCapacity() throws IOException {
        printLine("[TEST: Server holds at least 32 MB]");
        KVResponse response;

        // make sure to start with a fresh server
        response = client.sendWipeout();
        if (response == null || response.getErrCode() != ErrCode.OKAY) {
            return timeout();
        }

        // a 32MB server should be able to hold 4096 8 KB entries
        for (int i = 1; i <= 4096; i++) {
            ByteString key = ByteString.copyFrom(ByteBuffer.allocate(4).putInt(i).array());
            response = client.sendPut(key, BIG_VALUE);
            if (response == null) {
                return timeout();
            } else if (response.getErrCode() != ErrCode.OKAY) {
                return fail();
            }
        }

        return pass();
    }

    /**
     * Test that the server handles running out of memory gracefully.
     */
    private int testOutOfMemory() throws IOException {
        printLine("[TEST: Server does not fail when asked to store too much data]");
        KVResponse response;

        // fresh server
        response = client.sendWipeout();
        if (response == null || response.getErrCode() != ErrCode.OKAY) {
            return timeout();
        }

        // same as before: each entry is 8 KB
        int numSuccesses = 0;
        for (int i = 1; i <= 10000; i++) {
            ByteString key = ByteString.copyFrom(ByteBuffer.allocate(4).putInt(i).array());
            response = client.sendPut(key, BIG_VALUE);
            if (response != null && response.getErrCode() == ErrCode.OKAY) {
                numSuccesses++;
            } else {
                break;
            }
        }

        // estimate max capacity based on number of successes
        printLine(String.format("Server successfully stored %d KB", numSuccesses * 8));

        // is the server still alive? retry this a few times to make sure we're not failing due to dropped packets
        boolean isAlive = false;
        for (int i = 0; i < 5; i++) {
            response = client.sendIsAlive();
            if (response != null && response.getErrCode() == ErrCode.OKAY) {
                isAlive = true;
                break;
            }
        }

        // if server is not alive: test failed
        if (!isAlive) {
            return fail();
        } else if (numSuccesses < 4096) {
            return timeout();
        }

        // send one last request and check the error code
        response = client.sendPut(ByteString.copyFrom("saveme".getBytes()), BIG_VALUE);
        if (response.getErrCode() != ErrCode.NO_SPACE) {
            return timeout();  // inconclusive
        }

        return pass();
    }

    /**
     * Test server capacity. Then clear the server. Then test it. Then clear it. Many times.
     */
    private int testServerCapacityStress() throws IOException {
        printLine("[TEST: Server capacity stress test]");
        KVResponse response;

        // give the server a cooldown before hammering it
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("Test suite got interrupted?!");
            Thread.currentThread().interrupt();
        }

        int ITERS = 100;

        int currentKey = 1;  // send a unique key each time
        for (int j = 0; j < ITERS; j++) {
            // little bit of output so it doesn't look like the test hanged
            System.out.print(".");

            // clear the server
            response = client.sendWipeout();
            if (response == null || response.getErrCode() != ErrCode.OKAY) {
                return timeout();
            }

            // now fill it up
            for (int i = 0; i < 10000; i++) {
                ByteString key = ByteString.copyFrom(ByteBuffer.allocate(4).putInt(currentKey).array());
                response = client.sendPut(key, BIG_VALUE);
                if (response == null || response.getErrCode() == ErrCode.NO_SPACE) {
                    break;
                }
                currentKey++;  // increment key
            }

            // check that the server is still alive
            boolean isAlive = false;
            for (int i = 0; i < 5; i++) {
                response = client.sendIsAlive();
                if (response != null && response.getErrCode() == ErrCode.OKAY) {
                    isAlive = true;
                    break;
                }
            }

            // if server is not alive: test failed
            if (!isAlive) {
                return fail();
            }
        }

        // clear the server again
        response = client.sendWipeout();
        if (response == null || response.getErrCode() != ErrCode.OKAY) {
            return timeout();
        }

        // check that it is able to take in 32 MB worth of new keys
        for (int i = 0; i < 4096; i++) {
            ByteString key = ByteString.copyFrom(ByteBuffer.allocate(4).putInt(i).array());
            response = client.sendPut(key, BIG_VALUE);
            if (response == null) {
                return timeout();
            } else if (response.getErrCode() != ErrCode.OKAY) {
                return fail();
            }
        }

        return pass();
    }

    /**
     * Fill up the server with large key-value pairs. Then do a ton of GET requests.
     */
    private int testServerMemoryStress() throws IOException {
        printLine("[TEST: big GET requests]");
        KVResponse response;

        // send a clear command
        response = client.sendWipeout();
        if (response == null || response.getErrCode() != ErrCode.OKAY) {
            return timeout();
        }

        // give the server a cooldown before hammering it
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("Test suite got interrupted?!");
            Thread.currentThread().interrupt();
        }

        // fill the store up with 32 MB
        for (int i = 0; i < 4096; i++) {
            ByteString key = ByteString.copyFrom(ByteBuffer.allocate(4).putInt(i).array());
            response = client.sendPut(key, BIG_VALUE);
            if (response == null) {
                return timeout();
            } else if (response.getErrCode() != ErrCode.OKAY) {
                return fail();
            }
        }

        int timeouts = 0;
        int successes = 0;
        int failures = 0;

        // time this test
        long startTime = System.nanoTime();

        // make 8192 get requests (basically, force a ton of object creation at the server)
        for (int i = 0; i < 8192; i++) {
            // do a get request
            KVResponse localResponse = client.sendGet(
                    ByteString.copyFrom(ByteBuffer.allocate(4).putInt(i % 4096).array()));

            if (localResponse == null) {
                timeouts++;
            } else if (localResponse.getErrCode() == ErrCode.OKAY) {
                successes++;
            } else {
                failures++;
            }
        }

        double execTimeMs = (System.nanoTime() - startTime) * 1e-6;

        // log successes, failures, etc
        printLine(String.format("Completed 4096 GET requests in %.3f ms", execTimeMs));
        printLine(String.format("Total successful requests: %d", successes));
        printLine(String.format("Total failed requests: %d", failures));
        printLine(String.format("Total timed out: %d", timeouts));

        printLine("Testing if server is still alive...");

        // heartbeat to see if server is still alive
        for (int i = 0; i < 5; i++) {
            response = client.sendIsAlive();
            if (response != null && response.getErrCode() == ErrCode.OKAY) {
                return pass();
            }
        }

        // no response to heartbeat -- server is probably dead :(
        return fail();
    }
}
