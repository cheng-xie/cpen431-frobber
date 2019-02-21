package me.lise.CPEN431.Frobber.test;

import com.google.protobuf.ByteString;

import java.io.*;
import java.net.InetAddress;
import java.util.Arrays;

public abstract class TestSuite {
    protected int numTests = 0;
    protected int numPassed = 0;
    protected int numFailed = 0;

    protected StringBuilder log = new StringBuilder();
    protected InetAddress host;
    protected int port;
    protected TestClient client;
	protected InetAddress[] hosts;
	protected int[] ports;
	
	public enum RequestStatus {
		SUCCESS, FAIL, TIMEOUT;
		public String format() {
			switch(this) {
			case SUCCESS:
				return "SUCCESS";
			case FAIL:
				return "FAIL";
			case TIMEOUT:
				return "TIMEOUT";
			default:
				return "";
			}
		}
	}

    // along with a 4-byte key and 4-byte version number, this makes for a perfect 8 KB and is used in many tests
    static ByteString BIG_VALUE = ByteString.copyFrom(Arrays.copyOf(
            "Ph'nglui mglw'nafh Cthulhu R'lyeh wgah'nagl fhtagn".getBytes(), 8184));

    protected int pass() {
        numTests++;
        numPassed++;
        printLine("PASSED\n");  // extra newline
        return 0;
    }

    protected int fail() {
        numTests++;
        numFailed++;
        printLine("FAILED\n");  // extra newline
        return 1;
    }

    protected int timeout() {
        numTests++;
        printLine("TIMEOUT\n");  // extra newline
        return 1;
    }

    /**
     * Add a line to a log.
     * @param s The line.
     */
    protected void printLine(String s) {
        log.append(s).append("\n");
        // also echo to console so we know if the test is working...
        System.out.println(s);
    }

    TestSuite(InetAddress[] hosts, int[] ports) throws IOException {
    	this.hosts = hosts;
    	this.ports = ports;
        this.host = hosts[0];
        this.port = ports[0];
        this.client = new TestClient(this.host, this.port);
    }

    TestSuite(InetAddress host, int port) throws IOException {
    	this.hosts = null;
    	this.ports = null;
        this.host = host;
        this.port = port;
        this.client = new TestClient(this.host, this.port);
    }

    abstract public void run() throws IOException;

    /**
     * Summarize the results and write everything to an output file.
     * @param output The output file.
     * @throws IOException I/O badness.
     */
    public void closeAndWriteOut(BufferedWriter output) throws IOException {
        client.close();
        output.append(log);
    }
}
