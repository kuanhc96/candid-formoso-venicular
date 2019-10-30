import threading
import socket
import time
import math

class myThread(threading.Thread):
    
    HOST = socket.gethostname()
    
    def __init__(self, clientData, clientAddr): # Client data is a byte array and is only first transmission
        self.clientData = clientData
        self.clientAddr = clientAddr
        self.byteFile = bytearray()
        self.sockThread = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.sockThread.bind((HOST, 0))
        
    def run(self): # need to implement the recv for subsequent transmissions
        self.sockThread.settimeout(self.timeout / 1000) # timeouts for recv not for handling data
        while True:
        
            opcode = self.clientData[1]
            if (opcode == 1):
                # RRQ
                sendData(self.clientData, self.clinetAddr)
            elif (opcode == 2):
                # WRQ
                sendAck(self.clientData, self.clientAddr)
            elif (opcode == 3):
                # DATA
                sendAck(self.clientData, self.clientAddr)
            elif (opcode == 4):
                # ACK
                sendData(self.clientData, self.clientAddr)
            elif (opcode == 5):
                # ERR -- end transfer? -- TODO
                printError(self.clientData)
                break
            else:
                sockThread.sendto('Error: Invalid Opcode'.encode(), addr)
            
            timeoutCounter = 0
            try:
                # In case client did not receive final ACK from thread, continue to listen for DATA.
                # If client did not receive ACK, DATA will be transmitted again and the loop will restart.
                # In case the client did not receive final DATA from thread, continue to listen for ACK.
                # If the client did not receive DATA, ACK will be retransmitted again and the loop will restart.
                self.clientData, self.clientAddr = sockThread.recvfrom(516)
            except sockThread.timeout:
                timeoutCounter = timeoutCounter + 1
                if (timeoutCounter > 100):
                    # Client closed connection
                    # -- client may have received data and closed, but final ACK from client was lost
                    # -- or client received final ACK from thread and closed without retransmitting data
                    break 
                else:
                    continue
    
    # sendAck handles the WRQ's and DATA sent from the clients
    # void return type. sends acknowledgements in response to WRQ or DATA sent from client
    # data is in bytes and represents the data packet received from the client
    # addr is the address of the client
    def sendAck(self, data, addr): 
        opcode = data[1]
        ack = bytearray()
        ack.append(0)
        ack.append(4)
        fileName = getFileName(data)
        file = open(fileName, 'w+')
        if (opcode == 2):
            # WRQ -- respond to WRITER. Block # starts at 0
            ack.append(0)
            ack.append(0)
        else:
            # ACK -- respond to WRITER. Block # continues
            ack.append(data[2])
            ack.append(data[3])
        
        self.sockThread.sendto(ack, addr)
        
        # Decode bytes into string and write to file
        for i in data[4:]:
            file.write(i.decode())
        
        # if (len(data[4:]) < 512):
            # last message -- TODO
        
        file.close()
    
    # sendData handles the RRQ's and the ACK's from the client
    # void return type. sends data requested or next packet of data expected to the socket.
    # data is in bytes and represents the data packet received from the client
    # addr is the address of the client
    def sendData(self, data, addr): 
        opcode = data[1] # opcode identifies the packet type
        toSend = bytearray()
        toSend.append(0)
        toSend.append(3) # Header -- DATA
        fileName = getFileName(data) # Converts filename field in packet from bytes to string
        file = open(fileName, 'rb') # open the file so that it can be read as bytes
        
        # converts file into a bytearray. 
        # Class field self.byteFile is now a filled bytearray representing the file.
        # Section of file to be sent can now be specified.
        generateByteFile(file) 

        if (opcode == 1): # RRQ
            toSend.append(0)
            toSend.append(1) # sequence number starts at 1
            getFileSection(toSend, 0) # access first 512 bytes of the file, from 0 to 511
            
        else: # opcode == 4 -- client acknowledges receiving the DATA
            
            bytesSeq = bytearray() # bytesSeq is used to convert the sequence number from the client (in bytes) to int
            bytesSeq.append(data[2]) # append sequence numbers from client to bytesSeq
            bytesSeq.append(data[3])
            intSeq = int.from_bytes(bytesSeq, "big") # convert sequence number in bytes to int
            intSeq = intSeq + 1 # increment sequence, indicating that data sent is the next segment of the file
            newBytesSeq = int2bytes(intSeq) #convert the sequence number in int back to bytes to append to the packet
            
            toSend.append(newBytesSeq[0]) # append sequence number in bytes to the packet to be sent
            toSend.append(newBytesSeq[1])
            
            # append file in bytes to packet. access the file starting at intSeq - 1
            # -1 because sequence starts at one, while the array starts at index 0
            getFileSection(toSend, intSeq - 1) 
            
        self.sockThread.sendto(toSend, addr) # send packet to the client's address
        file.close()
    
################################################################### HELPER METHODS ############################################################
        
    # returns string that is the name of the file to be read.
    # takes filename in bytes in RRQ and returns filename as a string 
    def getFileName(self, data):
        i = 2
        fileName = ''
        while (data[i] != 0): # loop through filename field in RRQ
            fileName = filenName + data[i]
            i = i + 1
        
        return fileName.decode() #convert byte filename into string
    
    # Returns bytearray that represents the sequence number in bytes
    def int2bytes(self, integer):
        x = bytearray()
        x.append(math.floor(integer/256)) # First byte
        x.append(integer%256) # second byte
        return x
    
    # void return type. Edits the byteFile array if no file has been read yet.
    # Will not be called when WRQ or DATA is sent to thread.
    # Only called when RRQ or ACK is sent to thread.
    def generateByteFile(self, file): 
        if (len(self.byteFile) == 0):
            # generate bytearray that represents file
            byte = file.read(1)# read first byte
            while (byte != ""):
                self.byteFile.append(byte) # append bytes in file to bytearray
                byte = file.read() # read next byte
    
    # void return type. appends elements to the toSend array
    def getFileSection(self, toSend, startSeq):
        if (len(self.byteFile[startSeq:]) > 512):
            # just send next chunck of data
            for i in list(range(512)):
                toSend.append(self.byteFile[startSeq + i])
        else:
            # read data till end
            i = startSeq
            while (i < len(self.byteFile)):
                toSend.append(self.byteFile[i])
                i = i + 1
                
    def printError(self, data):
        i = 4 # Only care about the error message, which starts at the 5th byte
        errorMessage = '' # error message is a string
        while (data[i] != 0):
            errorMessage = errorMessage + data[i]
            i = i + 1
        print('ERROR: ' + errorMessage.decode())