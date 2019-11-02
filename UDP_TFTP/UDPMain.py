import sys
import socket
import UDPServer as udps

PORT = int(sys.argv[1])
TIMEOUT = int(sys.argv[2])

# PORT = 8080
# TIMEOUT = 1000

UDPServer = udps.UDPServer(PORT, TIMEOUT)
UDPServer.receiveRequest()
