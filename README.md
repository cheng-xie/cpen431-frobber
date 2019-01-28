# The CPEN 431 Frobber

The Frobber is a test client for [CPEN 431](https://docs.google.com/document/d/1AZT4t1pOaNDvTnWNwRPXVjkp8bOotOCFOH5X52-pfLQ/edit): Design of Distributed Software Systems.

It contains a full suite of correctness and performance tests to help test the correctness of your server implementation for Assignment 3 and Assignment 4.

## Running the client

A jar file is included. You can run the tests via the jar file as follows:

```
java -jar TheFrobber.jar ./servers.txt
```

Where `./servers.txt` is the path to a file that contains the servers to test, one on each line, formated as `hostname:port`. An example `servers.txt` file is included in the directory.

The results of each test is printed to stdout and saved to `frobber.log`. A sample log file is included in the respository.

## Tests Performed

For all of the tests, dropped packages may result in a `timeout` result.

The test client should be run on a freshly booted server, and the test client should be the sole user of the server for the duration of the test. False negatives occur otherwise.

### Basic Server Tests

- `testIsAlive`: an `IS_ALIVE` command is sent to the server. This test passes if the server responds with error code `0`.
- `testGetPid`: a `GET_PID` command is sent to the server. This test passes if the server responds with error code `0`, and the `pid` field is set.
- `testGetMembership`: a `GET_MEMBERSHIP_COUNT` command is sent to the server. This test passes if the server responds with error code `0`, and the `membershipCount` field is set to 1.
- `testBadCommand`: an unknown command `-1` is sent to the server. This test passes if the server responds with error code `0x05`.

### Basic Key-value Store Tests

- `testSimplePutGetRemove`: with the same key specified, a sequence of commands, `PUT(value) -> GET -> REMOVE -> GET` are sent to the server. This test passes if the first three commands succeed with error code `0` and the first `GET` command retrieves the value sent in the `PUT`. The second `GET` should fail with error code `0x1`.
- `testOverwriteValue`: two `PUT` commands are sent with the same key, but different values and version numbers. The test succeeds if the `PUT` commands succeed with error code `0`, and a subsequent `GET` request returns with error code `0` and the _second_ value and version sent.
- `testNoKey`: a `GET` and `REMOVE` command are sent with a key that has not been put in the server. They should both return error code `0x1` (key does not exist).
- `testExtraArgs`: a `PUT` request is used to insert a key-value pair. This should succeed with error code `0`. Two `GET` requests are then sent for the key: one with an extraneous value specified, and one with an extraneous version specified. Both `GET` requests should result in an error code other than `0`.
- `testLargeKeyValue`: a `PUT` request is sent with a 32-byte key and a 10000-byte value. Both should succeed.
- `testKeyValueTooLong`: a `PUT` request is sent with a 33-byte key and a small value. A second `PUT` request is sent with a short key and a 60000-byte value. These should fail with error codes `0x6` (bad key) and `0x7` (bad value), respectively.
- `testWipeout`: three key-value pairs are `PUT` into the server. A `WIPEOUT` command is sent. The test succeeds if the subsequent `GET` requests fail with error code `0x1` (key does not exist).
- `testGarbageRequest`: a garbled string of bytes is sent to the server, followed by an `IS_ALIVE` to check if the server has crashed. This test succeeds if no response is received to the first request, and an error code `0` is received for the second.
- `testBadChecksum`: a message is sent with an invalid checksum, followed by an `IS_ALIVE` to check if the server has crashed. This test succeeds if no response is received to the first request, and an error code `0` is received for the second.

### Server Capacity Tests

- `testServerCapacity`: `PUT` requests totalling exactly 32MB (counting the key, value, and version sizes) are sent to the server. This test passes if all the `PUT` requests succeed with error code `0`.
- `testOutOfMemory`: `PUT` requests totalling more than 64MB are sent to the server. This test succeeds if the final `PUT` request returns an error code `0x2` (out of memory).

### Stress tests

These tests only run if the earlier capacity tests succeeded.

#### Capacity stress test

The purpose of this test is to check for memory leaks.

This test begins by sending a `WIPEOUT` command and waiting 5 seconds, to ensure that it is starting with a fresh server.

`PUT` requests totalling greater than 64 MB is sent to the server, followed by a `WIPEOUT` command. This repeats 100 times.

Finally, `PUT` requests totalling 32 MB is sent to the server. The test succeeds if all of these `PUT` requests succeed with error code `0`.

#### Big GET test

The purpose of this test is to check that the caching strategy does not cause the server to crash under large request volumes.

This test begins by sending a `WIPEOUT` command and waiting 5 seconds, to ensure that it is starting with a fresh server.

`PUT` requests totalling exactly 32 MB are sent to the server to max out the key-value store's capacity. Each `PUT` request stores an 8 KB key-value pair.

Then, 8192 separate `GET` requests are sent. 

Finally, an `IS_ALIVE` command is sent. The test passes if the request succeeds with error code `0`.

The test runner will also report the number of `GET` requests that were successfully fulfilled. Not all `GET` requests need to succeed -- it is okay for the server to return an error code for some of them.

### Server Throughput test

Request throughput is tested by sending repeated `PUT` and `GET` requests with small (24 byte) key-value pairs. Each test thread sends 100 `PUT` and 100 `GET` requests.

This test is performed with 1, 4, 10, and (if the first three succeeded) 100 clients. For each test, the test runner will report the number of successful, failed, and timed out requests, as well as the throughput and goodput of the server.

### Shutdown test

After all the tests complete, a `SHUTDOWN` command is sent to the server. This final test passes if subsequent `IS_ALIVE` commands fail to obtain a response.

## About

I'm going to try to continue to make improvements to this test suite as this course progresses and we continue to get more assignments with more requirements.

Please let me know (send me a message through Piazza or Github, or grab me after class) if you find any errors in my test suite, or have any suggestions for things to test.

If you'd like to make a pull request to add more tests, that would be amazing! Let's work together to make our year the best year of CPEN 431 yet.
