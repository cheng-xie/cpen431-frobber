package me.lise.CPEN431.Frobber;

import ca.NetSysLab.ProtocolBuffers.KeyValueResponse.KVResponse;
import me.lise.CPEN431.Frobber.test.*;

import java.io.*;
import java.net.*;

public class App {
    private static final String OUTPUT_FILE_PATH = "frobber.log";
    private static BufferedWriter writer;

    private static void printLine(String s) throws IOException {
        System.out.println(s);
        writer.append(s).append("\n");
    }

    private static void testShutDown(InetAddress host, int port) throws IOException {
        printLine("[TEST: shutdown server]");

        TestClient client = new TestClient(host, port);
        client.sendShutdown();

        // test if client is alive (it shouldn't be)
        KVResponse response;
        for (int i = 0; i < 5; i++) {
            response = client.sendIsAlive();
            if (response != null) {
                client.close();
                printLine("FAILED -- server is still alive\n");
                return;
            }
        }

        client.close();
        printLine("PASSED");
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java -jar A3_test_client.jar /path/to/servers.txt");
            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        writer = new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH));

        String line = reader.readLine();
        while (line != null) {
            TestSuite testSuite;
            try {
                String[] server = line.split(":");
                if (server.length != 2) {
                    continue;
                }

                InetAddress host = InetAddress.getByName(server[0]);
                int port = Integer.parseInt(server[1]);

                // single threaded tests
                testSuite = new SingleThreadedTest(host, port);
                testSuite.run();
                testSuite.closeAndWriteOut(writer);

                // multithreaded tests
                testSuite = new ThroughputTest(host, port);
                testSuite.run();
                testSuite.closeAndWriteOut(writer);

                // shut down
                testShutDown(host, port);
            } catch (Exception e) {
                // could be from socket error or error parsing the server & port. whatever, just try the next one
                writer.append(String.format("Could not test: %s\n", line));
                continue;
            }

            // next server
            line = reader.readLine();
        }

        System.out.println("Tests complete. Output saved to " + OUTPUT_FILE_PATH);

        reader.close();
        writer.close();

        System.exit(0);
    }
}
