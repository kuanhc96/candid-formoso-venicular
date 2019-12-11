import socket
import time
import threading
import Ping as ping
import queue as q

class PingClient:
    
    def __init__(self, serverIP, serverPort, count, period, timeout):
################################## class constant #############################
        self.START_TIME = time.time()
################################## class varibales ############################
        self.serverIP = serverIP
        self.serverPort = serverPort
        self.count = count
        self.period = period
        self.timeout = timeout
################################## additional variables #######################
        # use queue object to pass into each thread and collect of data (timestamp) that will be retained in queue
        self.elapsedTimes = q.Queue() # time elapsed in each ping. units of each element is in seconds
        
    
    # runs all relevant methods in the program
    def run(self):
        self.setTimer()
        self.calculateAggregateStats()
        
    # pint final statistics of the ping client program    
    def calculateAggregateStats(self):
        transmitted = self.count # transmitted pings
        received = 0 # calculate replies received from server. If received, then elapsedTime of the ping would be > 0
        totalTime = 0 # calculate total time elapsed. Units in seconds
        minTime = self.timeout / 1000 + 1 # min elapsed time should be less than or equal to the timeout time. converted to seconds
        maxTime = 0 # max elapsed time is at least 0
        avgTime = 0
        for i in range(transmitted): # need to calculate statistics based on ALL transmitted messages
            elapsedTime = self.elapsedTimes.get() # access element at the front of the queue
            totalTime = totalTime + elapsedTime # totalTime is the sum of all elapsed times in the queue
            if elapsedTime < minTime: # update minTime
                minTime = elapsedTime
            if elapsedTime > maxTime: # update maxTime
                maxTime = elapsedTime
            if elapsedTime != 0: # if elapsed time is 0, that means no message was received for the given ping
                received = received + 1
            self.elapsedTimes.put(elapsedTime) # put elapsedTime element back into the queue to preserve data structure
        
        percentLoss = (transmitted - received) / transmitted * 100 # percentage of pings not received by server or replies not received by client
        avgTime = totalTime / transmitted # average elapsed time
        print("--- " + self.serverIP + " ping statistics ---")
        #print("{} transmitted {}".format(transmitted,))
        print("{} transmitted, {} received, {}% loss, time {} ms".format(transmitted, received, percentLoss, int((time.time() - self.START_TIME) * 1000)))
        print("rtt min/avg/max = {}/{}/{}".format(int(minTime * 1000), int(avgTime * 1000), int(maxTime * 1000)))
        
    def setTimer(self):
        print("PING " + self.serverIP)
        threads = []
        for i in range(self.count): # send ping request count times
            #self.sendPings(i)
            #time.sleep(self.period/1000)
            t = threading.Timer(i * self.period / 1000, self.sendPings, [i]) # pause program for (self.period [ms] / 1000) seconds before sending next ping message
            threads.append(t)
        for i in range(self.count):
            threads[i].start()
        for i in range(self.count):
            threads[i].join()

    def sendPings(self, i):
        # construct ping object and call send method to send message to server
        pingThread = ping.Ping(self.elapsedTimes, self.serverIP, self.serverPort, i + 1, self.timeout) # sequence number = i + 1
        pingThread.run()
        