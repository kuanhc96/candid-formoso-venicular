This is a tool that transfers files securely over UDP. Files that are saved in the same directory as this tool can be accessed remotely from a client; other files can be sent to the directory in which the tool is located. These tasks can be done as follows:

Place the UDPMain.py, UDPServer.py, and UDPThread.py in the same folder with any text file. Open a terminal and navigate to this folder and run 
$ python3 UDPMain.py <port #> <timeout (ms)>
<port #> can be any # that is not a privileged # (0 - 1023). 
<timeout (ms)> is the amount of time the UDPServer will wait for the client's request for data or data to write before the UDPServer resends data or aknowledgements to the client.
   
Open another terminal, save another file that is to be transferred, and enter the following commands:

To request file from server:

$ tftp
tftp> connect <host name> <port #> # <host name> can be localhost or linux1, linux2, linux3 if run on U Chicago linux systems
tftp> binary
tftp> verbose
Verbose mode on.
tftp> get <file1> <file2> # get <file1>, which is the file that is in the same directory as the UDPServer, and <file2> is what the client wants to name <file1> as once it is obtained from the server
getting from <host name>:<file1> to <file2> [octet]
Received X bytes in Y seconds [Z bits/sec]
tftp> quit

To send file to server:
$ tftp
tftp> connect <host name>
tftp> binary
tftp> verbose
Verbose mode on.
tftp> put <file1>
putting <file1> to <host name>:<file1> [octet]
Sent X bytes in Y seconds [Z bits/sec]
tftp> quit

To test whether file transfer is affected by dropping packets, download udpproxy.jar and run
$ java -jar udpproxy.jar <port # + 1> <host name> <port #> <drop rate>

Then:
$ tftp
tftp> connect <hostname> <port #>
tftp> binary
tftp> verbose
Verbose mode on.
tftp> put <file1>
putting <file1> to <host name>:<file1> [octet]
Sent X bytes in Y seconds [Z bits/sec]

