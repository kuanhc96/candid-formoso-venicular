import socket
from threading import Timer

class UDPServer:
    
    HOST = socket.gethostname()
    
    def __init__(self, port, timeout):
        self.port = port
        self.timeout = timeout
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.sock.bind((HOST, self.port))
    
    def receiveRequest(self):
        while True:
            data, addr = self.sock.recvfrom(516)
            data = bytearray(data)
            thread = UDPThread(data, addr)
            thread.run()
            