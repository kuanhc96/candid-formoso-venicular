import threading
import socket
import time
import math

class UDPThread(threading.Thread):
    
    def __init__(self, clientData, timeout, clientAddr): # Client data is a byte array and is only first transmission
############################### class constant ############################################        
        HOST = socket.gethostname()
############################### class variables ###########################################        
        self.clientData = clientData
        self.timeout = timeout
        self.clientAddr = clientAddr
        self.byteFile = bytearray()
        self.file = None
        self.timeoutCounter = 0
        self.previousSeq1 = -1
        self.previousSeq2 = -1
############################### Socket setup ##############################################        
        self.sockThread = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.sockThread.bind((HOST, 0))
        self.sockThread.settimeout(self.timeout / 1000) # timeouts for recv not for handling data
        
    def run(self): # need to implement the recv for subsequent transmissions
        print("Thread created. Connection established.")
        while True:
        
            opcode = self.clientData[1]
            if (opcode == 1):
                # RRQ
                self.sendData(self.clientData, self.clientAddr)
            elif (opcode == 2):
                # WRQ
                self.sendAck(self.clientData, self.clientAddr)
            elif (opcode == 3):
                # DATA
                self.sendAck(self.clientData, self.clientAddr)
            elif (opcode == 4):
                # ACK
                self.sendData(self.clientData, self.clientAddr)
            elif (opcode == 5):
                # ERR -- end transfer? -- TODO
                self.printError(self.clientData)
                break
            else:
                self.sockThread.sendto('Error: Invalid Opcode'.encode(), addr)
            
            if (self.file == None):
                print('Transfer completed. Terminating connection')
                break
            
            try:
                # In case client did not receive final ACK from thread, continue to listen for DATA.
                # If client did not receive ACK, DATA will be transmitted again and the loop will restart.
                # In case the client did not receive final DATA from thread, continue to listen for ACK.
                # If the client did not receive DATA, ACK will be retransmitted again and the loop will restart.
                self.clientData, self.clientAddr = self.sockThread.recvfrom(516)
            except socket.timeout as e:
                self.timeoutCounter = self.timeoutCounter + 1
                if (self.timeoutCounter > 10):
                    # Client closed connection
                    # -- client may have received data and closed, but final ACK from client was lost
                    # -- or client received final ACK from thread and closed without retransmitting data
                    print("Transfer completed. Terminating connection.")
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
        
        if (opcode == 2):
            # WRQ -- respond to WRITER. Block # starts at 0
            ack.append(0)
            ack.append(0)
            fileName = self.getFileName(data)
            self.file = open(fileName, 'w+')
            print("opening: " + fileName)
        else:
            # ACK -- respond to WRITER. Block # continues
            ack.append(data[2])
            ack.append(data[3])

            # Decode bytes into string and write to file
            if self.previousSeq1 != ack[2] or self.previousSeq2 != ack[3]:
                self.previousSeq1 = ack[2]
                self.previousSeq2 = ack[3]
                for i in data[4:]:
                    self.file.write(self.int2bytes(i).hex())
                
            
            if (len(data[4:]) < 512):
                print('closing file')
                print(data)
                self.file.close()
                self.file = None
        
        self.sockThread.sendto(ack, addr)

    # sendData handles the RRQ's and the ACK's from the client
    # void return type. sends data requested or next packet of data expected to the socket.
    # data is in bytes and represents the data packet received from the client
    # addr is the address of the client
    def sendData(self, data, addr): 
        opcode = data[1] # opcode identifies the packet type
        toSend = bytearray()
        toSend.append(0)
        toSend.append(3) # Header -- DATA
        if (self.file == None):
            fileName = self.getFileName(data) # Converts filename field in packet from bytes to string
            print('opening ' + fileName)
            self.file = open(fileName, 'rb') # open the file so that it can be read as bytes
            
            # converts file into a bytearray. 
            # Class field self.byteFile is now a filled bytearray representing the file.
            # Section of file to be sent can now be specified.
            self.generateByteFile(self.file)
            self.file.close()

        if (opcode == 1): # RRQ
            toSend.append(0)
            toSend.append(1) # sequence number starts at 1
            self.getFileSection(toSend, 0) # access first 512 bytes of the file, from 0 to 511
            
        else: # opcode == 4 -- client acknowledges receiving the DATA
            
            bytesSeq = bytearray() # bytesSeq is used to convert the sequence number from the client (in bytes) to int
            bytesSeq.append(data[2]) # append sequence numbers from client to bytesSeq
            bytesSeq.append(data[3])
            intSeq = int.from_bytes(bytesSeq, "big") # convert sequence number in bytes to int
            #print("ack #: " + str(intSeq))
            intSeq = intSeq + 1 # increment sequence, indicating that data sent is the next segment of the file
            #print("seq #: " + str(intSeq))
            newBytesSeq = self.int2bytesSeq(intSeq) #convert the sequence number in int back to bytes to append to the packet
            
            toSend.append(newBytesSeq[0]) # append sequence number in bytes to the packet to be sent
            toSend.append(newBytesSeq[1])
            
            # append file in bytes to packet. access the file starting at intSeq - 1
            # -1 because sequence starts at one, while the array starts at index 0
            self.getFileSection(toSend, 512 * (intSeq - 1)) 
            
        self.sockThread.sendto(toSend, addr) # send packet to the client's address
        
    
################################################################### HELPER METHODS ############################################################
        
    # returns string that is the name of the file to be read.
    # takes filename in bytes in RRQ and returns filename as a string 
    def getFileName(self, data):
        i = 2
        fileName = bytearray()
        while (data[i] != 0): # loop through filename field in RRQ
            fileName.append(data[i])
            i = i + 1
        
        return fileName.decode() #convert byte filename into string
    
    # Returns byte that represents an integer. This integer may represent a string
    def int2bytes(self, integer):
        numBytes = 1
        temp = integer
        while (temp / 256 > 1):
            numBytes = numBytes + 1
            temp = temp / 256
        
        return (integer).to_bytes(numBytes, 'big')
    
    def int2bytesSeq(self, integer):
        x = bytearray()
        x.append(math.floor(integer / 256))
        x.append(integer % 256)
        return x
    
    # void return type. Edits the byteFile array if no file has been read yet.
    # Will not be called when WRQ or DATA is sent to thread.
    # Only called when RRQ or ACK is sent to thread.
    def generateByteFile(self, file): 
        if (len(self.byteFile) == 0):
            # generate bytearray that represents file
            byte = file.read(1)# read first byte
            intByte = int.from_bytes(byte, "big")
            self.byteFile.append(intByte)
            while (byte != b''):
                byte = file.read(1) # read next byte
                intByte = int.from_bytes(byte, "big")
                self.byteFile.append(intByte) # append bytes in file to bytearray
    
    # void return type. appends elements to the toSend array
    def getFileSection(self, toSend, startSeq):
        if (len(self.byteFile[startSeq:]) > 512):
            # just send next chunck of data
            for i in list(range(512)):
                toSend.append(self.byteFile[startSeq + i])
        else:
            # read data till end
            i = startSeq
            while (i < len(self.byteFile) - 1):
                toSend.append(self.byteFile[i])
                i = i + 1
                
    def printError(self, data):
        i = 4 # Only care about the error message, which starts at the 5th byte
        errorMessage = '' # error message is a string
        while (data[i] != 0):
            errorMessage = errorMessage + data[i]
            i = i + 1
        print('ERROR: ' + errorMessage.decode())