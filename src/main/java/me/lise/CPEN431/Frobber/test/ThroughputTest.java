package me.lise.CPEN431.Frobber.test;

import ca.NetSysLab.ProtocolBuffers.KeyValueResponse.KVResponse;

import com.google.protobuf.ByteString;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThroughputTest extends TestSuite {
    private ExecutorService executor;

    public ThroughputTest(InetAddress host, int port) throws IOException {
        super(host, port);
        executor = Executors.newCachedThreadPool();
    }

    @Override
    public void run() throws IOException {
        printLine("---------------------------------------------------");
        printLine(String.format("MULTI-THREADED THROUGHPUT TESTS ON %s:%d", host.getHostName(), port));
        printLine("---------------------------------------------------");

        // throughput tests with varying numbers of clients
        int testFailure = 0;
        testFailure += testThroughput(1);
        testFailure += testThroughput(4);
        testFailure += testThroughput(10);

        if (testFailure > 0) {
            printLine("Too many failures, skipping stress test");
            return;
        }

        // 100-client throughput test
        testThroughput(100);
    }

    /**
     * Test and record the throughput and success rate of sending 100 PUT and GET requests from arbitrary clients.
     * This isn't an exact test, since throughput will be affected by interleaving of clients.
     */
    private int testThroughput(int numClients) throws IOException {
        printLine(String.format("[TEST: %d clients, 100 PUTs and GETs per client]", numClients));

        // send a clear command
        KVResponse response = client.sendWipeout();
        if (response == null || response.getErrCode() != ErrCode.OKAY) {
            return timeout();
        }

        AtomicInteger totalSuccesses = new AtomicInteger(0);
        AtomicInteger totalTimeouts = new AtomicInteger(0);
        AtomicInteger totalFailures = new AtomicInteger(0);

        List<Future<Integer>> futures = new ArrayList<>();
        ByteString value = ByteString.copyFrom("oooo".getBytes());

        long startTime = System.nanoTime();

        for (int i = 0; i < numClients; i++) {
            final int clientId = i;  // make a final copy for use within the anonymous function
            futures.add(executor.submit(() -> {
                // record time
                try {
                    TestClient localClient = new TestClient(host, port);

                    int successes = 0;
                    int timeouts = 0;
                    int failures = 0;

                    for (int j = 0; j < 100; j++) {
                        // do a put request
                        ByteString key = ByteString.copyFrom(ByteBuffer.allocate(4).putInt(j + clientId * 100).array());
                        KVResponse localResponse = localClient.sendPut(key, value);
                        if (localResponse == null) {
                            timeouts++;
                        } else if (localResponse.getErrCode() == ErrCode.OKAY) {
                            successes++;
                        } else {
                            failures++;
                        }

                        // do a get request
                        localResponse = localClient.sendGet(key);
                        if (localResponse == null) {
                            timeouts++;
                        } else if (localResponse.getErrCode() == ErrCode.OKAY) {
                            successes++;
                        } else {
                            failures++;
                        }
                    }

                    // update values, if need be
                    if (successes > 0) {
                        totalSuccesses.addAndGet(successes);
                    }
                    if (timeouts > 0) {
                        totalTimeouts.addAndGet(timeouts);
                    }
                    if (failures > 0) {
                        totalFailures.addAndGet(failures);
                    }

                    // close the client
                    localClient.close();
                    return 0;
                } catch (IOException e) {
                    // shrug?
                    return 0;
                }
            }));
        }

        // collect the execution times
        long totalExecTime = 0;
        for (Future<Integer> f: futures) {
            try {
                f.get();  // this will not complete until thread finishes
            } catch (InterruptedException | ExecutionException e) {
                // no idea what could throw these
                printLine("Caught an exception while running throughput test:");
                printLine(e.getMessage());
            }
        }

        long execTime = System.nanoTime() - startTime;

        // log results
        double totalTimeMs = execTime * 1e-6;
        printLine(String.format("Test completed in %.3f milliseconds", totalTimeMs));

        int goodResponses = totalSuccesses.get();
        int totalResponses = goodResponses + totalFailures.get();
        printLine(String.format("Total responses received: %d", totalResponses));
        printLine(String.format("Total successful requests: %d", goodResponses));
        printLine(String.format("Total failed requests: %d", totalFailures.get()));
        printLine(String.format("Total timed out: %d", totalTimeouts.get()));

        // calculate performance
        printLine(String.format("Average time per request: %.3f ms", totalTimeMs / totalResponses));
        printLine(String.format("Throughput: %.2f requests per second", totalResponses * 1000 / totalTimeMs));
        printLine(String.format("Goodput: %.2f requests per second\n", goodResponses * 1000 / totalTimeMs));

        // return "success" if at least 3/4 of the requests were successful
        return goodResponses > (numClients * 150) ? 0 : 1;
    }
}
