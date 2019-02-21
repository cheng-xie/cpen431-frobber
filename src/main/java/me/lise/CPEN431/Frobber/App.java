package me.lise.CPEN431.Frobber;

import ca.NetSysLab.ProtocolBuffers.KeyValueResponse.KVResponse;
import me.lise.CPEN431.Frobber.test.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

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
            System.out.println("Usage: java -jar A6_test_client.jar /path/to/servers.txt");
            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        writer = new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH));
        
        List<InetAddress> hosts = new ArrayList<>();
        List<Integer> ports = new ArrayList<>();

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
            } catch (Exception e) {
                // could be from socket error or error parsing the server & port. whatever, just try the next one
                writer.append(String.format("Could not test: %s\n", line));
                continue;
            }

            // next server
            line = reader.readLine();
        }
        
        InetAddress[] hostArray = new InetAddress[hosts.size()];
		TestSuite testSuite = new MultiNodeTest(hosts.toArray(hostArray), ports.stream().mapToInt(i->i).toArray());
		testSuite.run();
		testSuite.closeAndWriteOut(writer);

        System.out.println("Tests complete. Output saved to " + OUTPUT_FILE_PATH);

        reader.close();
        writer.close();

        System.exit(0);
    }
}
