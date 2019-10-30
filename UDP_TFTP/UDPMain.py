import sys
import socket

PORT = int(sys.argv[1])
TIMEOUT = int(sys.argv[2])

UDPSever = UDPServer(PORT, TIMEOUT)