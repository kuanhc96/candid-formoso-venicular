import socket
from threading import Timer
import UDPThread as udpt

class UDPServer:
    
#     HOST = socket.gethostname()
    
    def __init__(self, port, timeout):
        HOST = socket.gethostname()
        self.port = port
        self.timeout = timeout
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.sock.bind((HOST, self.port))
    
    # void return type: start receiving data and create separate thread for interacting with client
    def receiveRequest(self):
        while True:
            data, addr = self.sock.recvfrom(516)
            data = bytearray(data)
            thread = udpt.UDPThread(data, self.timeout, addr)
            thread.run()
            