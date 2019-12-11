This is a tool created to test the reachability of a host on an IP network. Ping messages will be sent to a host server remotely to measure roundtrip time, and loss rate. Both of these statistics can give the user insight into how congested or secure the network is between the client and the desired server. To test to see how secure the network is between a client and a desired host, follow the following instructions:

Place the PingClientMain.py, PingClient.py, and Ping.py files in the same folder. Open two terminals, one running pingserver.jar, the other running PingClientMain.py.
First start the pinngserver as follows:
$ java -jar pingserver.jar --port=<port> # <port> is the port number over which Ping/Pong messages will be sent
[--loss_rate=<rate>] # optional argument for the server (used for debugging purposes). <rate> dictates the rate at which messages are lost.
[--bit_error_rate=<rate>] # Optional argument for the server (used for debugging purposes). <rate> is the rate at which incorrect checksums are calculated for the pong messages that are replied to the client
[--avg_delay=<delay>] # Optional argument foe the server (used for debugging purposes). <delay> is the average delay in time in which pong messages are replied to the client.

Next, start the pingclient as follows:
$ python3 PingClientMain.py
[--server_ip=<server ip address>] # optional argument for the client. <server ip address> is the IP address of host where the server is located. (Default: localhost)
[--server_port=<server port>] # optional argument for the client. <server port> is the port number over which Ping/Pong messages will be sent. Note that it should match the <port> field that is input to the server. (Default: 56789)
[--count=<number of pings to send>] # optional argument for the client. <number of pings to sent> is the number of ping messages to be sent to the server. (Default: 10)
[--period=<wait interval>] # optional argument for the client. <wait interval> is the amount of time each ping message is separated by in milliseconds. (Default: 60000)

The server will sit in an infinte loop, waiting to receive messages from clients until the user terminates the program. On the other hand, at the end of each client session, aggregate statistics of the session will be calculated to inform the user details regarding the session. The following information will be printed:
1. total number of packets transmitted
2. total number of packets received
3. percentage of packet loss
4. cumulative elapsed time (in milliseconds)
5. minimum, average, and maximum round trip times (in milliseconds)

