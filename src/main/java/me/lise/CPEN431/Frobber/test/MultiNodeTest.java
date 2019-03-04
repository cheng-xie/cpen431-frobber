package me.lise.CPEN431.Frobber.test;

import ca.NetSysLab.ProtocolBuffers.KeyValueResponse.KVResponse;
import me.lise.CPEN431.Frobber.test.helper.TestResult;
import me.lise.CPEN431.Frobber.util.ByteStringGenerator;

import com.google.protobuf.ByteString;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class MultiNodeTest extends TestSuite {
    private ExecutorService executor;
    // Each node has one dedicated client which is responsible for cleanups for example
	private TestClient[] dedicatedClients;
	private final int NUM_THREADS = 8;

    public MultiNodeTest(InetAddress[] hosts, int[] ports) throws IOException {
        super(hosts, ports);
        printLine(String.format("Running with %d threads", NUM_THREADS));
        executor = Executors.newFixedThreadPool(NUM_THREADS);
        this.dedicatedClients = new TestClient[this.hosts.length];
        for (int i = 0; i < this.hosts.length; i++) {
        	this.dedicatedClients[i] = new TestClient(this.hosts[i], this.ports[i]);
        }
    }
    
    protected boolean wipeNodes() {
        List<Future<Boolean>> futures = new ArrayList<>();
    	for (TestClient client: this.dedicatedClients) {
            futures.add(executor.submit(() -> {
				KVResponse response = client.sendWipeout();
				if (response == null || response.getErrCode() != ErrCode.OKAY) {
					return false; 
				}
				return true;
            }));
    	}
        for (Future<Boolean> f: futures) {
            try {
            	if (!f.get()) {
            		return false;
            	}
            } catch (InterruptedException | ExecutionException e) {
                // no idea what could throw these
                printLine("Caught an exception while running multinode test:");
                printLine(e.getMessage());
				return false;
            }
        }
    	return true;
    }
    
    @Override
    public void run() throws IOException {
        printLine("---------------------------------------------------");
        printLine(String.format("MULTI-THREADED THROUGHPUT TESTS ON %s:%d", host.getHostName(), port));
        printLine("---------------------------------------------------");
		printLine(testSimplePutGetRemove(10).format());
    }

    /**
     * Test and record the throughput and success rate of sending 100 PUT and GET requests from arbitrary clients.
     * This isn't an exact test, since throughput will be affected by interleaving of clients.
     */
    private TestResult testSimplePutGetRemove(int keysPerNode) throws IOException {
        printLine(String.format("[TEST: Put and Then Get %d keysPerNode with %d threads]", keysPerNode, NUM_THREADS));

        // send a clear command
        if (!this.wipeNodes()) {
        	// if not successful, report undecided test 
        	return TestResult.undecided("Could not perform pre-test wipeout. Server stati uknown.");
        }

        // put some kvs (equal to the number of nodes times keysPerNode) 
        ByteStringGenerator byteGen = new ByteStringGenerator();
        Map<ByteString, ByteString> referenceKVMap = new ConcurrentHashMap<ByteString, ByteString>();
        List<Future<TestResult>> testFutures = new ArrayList<>();
		for (int j = 0; j < keysPerNode; j++) {
			final int keyIdx = j;
			for (int i = 0; i < this.hosts.length; i++) {
				final int nodeIdx = i;  // make a final copy for use within the anonymous function
				testFutures.add(executor.submit(() -> {
					try {
						TestClient localClient = new TestClient(this.hosts[nodeIdx], this.ports[nodeIdx]);
						// generate unique key and values
						ByteString key = byteGen.getNextByteString(8);
						ByteString value = byteGen.getNextByteString(16);
						
						// initial put
						KVResponse localResponse = localClient.sendPut(key, value);
						localClient.close();
						if (localResponse == null) {
							return TestResult.undecided("Initial Put Request Timeout");
						} else if (localResponse.getErrCode() == ErrCode.OKAY) {
							// on success remember the put and make sure we didnt overwrite anything
							printLine(value.toString());
							assert(referenceKVMap.put(key, value) == null);
						} else {
							return TestResult.failed(String.format("Initial Put Request Error"));
						}

						// now try to get the value on all of the nodes
						for(int k = 0; k < this.hosts.length; k++) {
							TestClient getClient = new TestClient(this.hosts[k], this.ports[k]);
							localResponse = getClient.sendGet(key);
							getClient.close();
							if (localResponse == null) {
								return TestResult.undecided("Get1 Request Timeout");
							} else if (localResponse.getErrCode() == ErrCode.OKAY) {
								printLine(localResponse.getValue().toString() + '|' + referenceKVMap.get(key).toString());
								if(referenceKVMap.get(key).equals(localResponse.getValue())) {
									return TestResult.failed("Get1 Returned Incorrect Value");
								}
							} else {
								return TestResult.failed("Get1 Request Error");
							}
						}

						// now remove the key on a different node
						final int otherNodeIdx = (nodeIdx + keyIdx) % this.hosts.length;
						TestClient otherClient = new TestClient(this.hosts[otherNodeIdx], this.ports[otherNodeIdx]);
						localResponse = otherClient.sendRemove(key);
						otherClient.close();
						if (localResponse == null) {
							return TestResult.undecided("Remove Request Timeout");
						} else if (localResponse.getErrCode() == ErrCode.OKAY) {
							// on success update our reference kvmap 
							assert(referenceKVMap.remove(key) != null);
						} else {
							return TestResult.failed(String.format("Remove Request Error"));
						}

						// now make sure the key is gone on all nodes
						for(int k = 0; k < this.hosts.length; k++) {
							TestClient getClient = new TestClient(this.hosts[k], this.ports[k]);
							localResponse = getClient.sendGet(key);
							getClient.close();
							if (localResponse == null) {
								return TestResult.undecided("Get2 Request Timeout");
							} else if (localResponse.getErrCode() == ErrCode.NO_KEY) {
								// This is expected
							} else if (localResponse.getErrCode() == ErrCode.OKAY) {
								return TestResult.failed("Get2 Request Error");
							} else {
								return TestResult.failed("Get2 Request Error");
							}
						}
						return TestResult.passed("");
					} catch (IOException e) {
						// shrug?
						printLine("Unexpected IOException");
						printLine(e.getMessage());
						return TestResult.failed("Unexpected IOException");
					}
				}));
			}
        }
        int failed = 0;
        int passed = 0;
        int undecided = 0;
        for (Future<TestResult> f: testFutures) {
            try {
				TestResult result = f.get();
            	switch(result.status) {
            	case PASSED:
            		passed++;
            		break;
            	case FAILED:
            		failed++;
            		printLine(result.getMsg());
            		break;
            	case UNDECIDED:
            		undecided++;
            		break;
            	}
            } catch (InterruptedException | ExecutionException e) {
                // no idea what could throw these
                printLine("Caught an exception while running throughput test:");
                printLine(e.getMessage());
				return TestResult.failed("Unexpected exception while joining threads.");
            }
        }

        int totalTests = this.hosts.length*keysPerNode;
        printLine(String.format("Number of sequences passed   : %d/%d", passed, totalTests));
        printLine(String.format("Number of sequences undecided: %d/%d", undecided, totalTests));
        printLine(String.format("Number of sequences failed   : %d/%d", failed, totalTests));

        // fail if a single request failed
        if (failed > 0) {
        	return TestResult.failed("At least one test failed.");
        }
        else if (undecided > totalTests*5/100) {
        	return TestResult.undecided("More than 5% of the tests were undecided.");
        }
        else if (undecided > totalTests*5/100) {
        	return TestResult.passed("Less than 5% of the tests were undecided.");
        }
        return null;
    }

    @Override
    public void closeAndWriteOut(BufferedWriter output) throws IOException {
    	for(TestClient client: this.dedicatedClients) {
			client.close();
    	}
        output.append(log);
    }
}