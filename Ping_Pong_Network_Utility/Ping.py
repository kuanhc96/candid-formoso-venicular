import socket
import os
import time
import threading

class Ping(threading.Thread):
    
    def __init__(self, queue, serverIP, serverPort, seqNumber, timeout):
        super(Ping,self).__init__()
################################# class constants ################################
        self.START_TIME = time.time() # units are in seconds
################################# class variables ################################
        self.queue = queue
        self.serverIP = serverIP
        self.serverPort = serverPort
        self.seqNumber = seqNumber
        self.timeout = timeout
################################# additional variables ###########################
        self.request = bytearray()
        self.timeElapsed = 0
################################# Socket setup ###################################
        self.sockPing = socket.socket(socket.AF_INET6, socket.SOCK_DGRAM)
        self.sockPing.settimeout(self.timeout / 1000) # close Ping if no echo reply received from the server after timeout
       
    # Run all relevant programs
    def run(self):
        ## first construct message (ICMP), then send to host
        self.constructMessage()
        ## second, recv new message, then checksum.
        self.readMessage()
        ## If no message received, give up.
        ## If message received but header incorrect, drop.
        ## If message received but checksum incorrect, drop.
        self.queue.put(self.timeElapsed) # if relply never received, or if checksum field of reply incorrect, then put 0 into the queue (???)
        
    
    # construct message to be sent to the server
    def constructMessage(self):
        self.request.append(8) # echo message header = 8 and code field = 0 (1 + 1 = 2 bytes)
        self.request.append(0)
        self.request.append(0) # filler, will be replaced by checksum lateron (2 bytes)
        self.request.append(0)
        
        pid = os.getpid() 
        if (pid >= 2 ** 16): # consider case when pid is greater than three bytes
            pid = pid % (2 ** 16)
        
        # identifier chosen to be process id (2 bytes)
        self.request.append(pid >> 8)
        self.request.append(pid % 256)
        
        # sequence number field (2 bytes)
        self.request.append(self.seqNumber >> 8)
        self.request.append(self.seqNumber % 256)
        
        # last 6 bytes is the time stamp: unsigned int representing time that has elapsed since epoch
        timestamp = int(time.time() * 1000)
        self.request.append(timestamp >> 40)
        self.request.append(timestamp % (2 ** 40) >> 32)
        self.request.append(timestamp % (2 ** 32) >> 24)
        self.request.append(timestamp % (2 ** 24) >> 16)
        self.request.append(timestamp % (2 ** 16) >> 8)
        self.request.append(timestamp % (2**8)) # converting timestamp into 6 byte integer
        self.calculateChecksum()
        self.sockPing.sendto(self.request, (self.serverIP, self.serverPort, 0, 0))
        
        
    # receive messages from server to be analyzed
    def readMessage(self):
        try: # Check if recv times out
            echoReply, self.serverIP = self.sockPing.recvfrom(14 * 8) # Size of IMCP message is 14 bytes, which is 14 * 8 bits
            if (self.checkChecksum(echoReply)): # check if checksum field is correct
                # checksum field is correct
                self.timeElapsed = time.time() - self.START_TIME # calculate time elapsed. this is later put into the queue that keeps track of elapsed time. units in seconds
                print("PONG " + self.serverIP[0] + ": seq=" + str(self.seqNumber) + " time=" + str(int(self.timeElapsed * 1000)) + " ms")
            else: # checksum field is incorrect: print checksum verification failed
                print("Checksum verification failed for echo reply seqno=" + str(self.seqNumber))
            
        except socket.timeout as e:
            pass # Do nothing

    # check if the checksum field of the reply from the server is correct
    def checkChecksum(self, echoReply):
        leftWordSum = 0
        rightWordSum = 0
        for i in range(0, len(echoReply), 2):
            leftWordSum = leftWordSum + echoReply[i] # summing even bytes
            rightWordSum = rightWordSum + echoReply[i + 1] # summing odd bytes
            if (rightWordSum > 255):
                leftWordSum = leftWordSum + 1
                rightWordSum = rightWordSum - 256
            if (leftWordSum > 255):
                leftWordSum = leftWordSum - 256
                rightWordSum = rightWordSum + 1
        return rightWordSum == 255 and leftWordSum == 255 # checksum and other fields must sum to 11111111 11111111
        
    # calculate the checksum field that is to be sent to the server
    def calculateChecksum(self): # Use bit math
        leftWordSum = 0 # even bytes
        rightWordSum = 0 # odd bytes
        for i in range(0, len(self.request), 2):
            leftWordSum = leftWordSum + self.request[i] # summing even bytes
            rightWordSum = rightWordSum + self.request[i + 1] # summing odd bytes
            if (rightWordSum > 255):
                # carry on to 9th binary digit
                rightWordSum = rightWordSum - 256
                leftWordSum = leftWordSum + 1 
            if (leftWordSum > 255):
                #1's complement
                rightWordSum = rightWordSum + 1
                leftWordSum = leftWordSum - 256
        checksumLeft = 255 - leftWordSum
        checksumRight = 255 - rightWordSum
        self.request[2] = checksumLeft
        self.request[3] = checksumRight