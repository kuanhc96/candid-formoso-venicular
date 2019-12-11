import argparse
import PingClient as pc

'''
<executable cmd>    --server_ip=<server ip addr>
                    --server_port=<server port>
                    --count=<number of pings to send>
                    --period=<wait interval>
                    --timeout=<timeout>
'''

parser = argparse.ArgumentParser(description = 'PingClientMain')

parser.add_argument("--server_ip", default = "localhost", type = str, help = "IP address of host where server is located. (Default: localhost, 127.0.0.1)")
parser.add_argument("--server_port", default = 56789, type = int, help = "Port # over which connection with server is established. (Default: 56789)")
parser.add_argument("--count", default = 10, type = int, help = "Number of ping messages to be sent. (Default: 10)")
parser.add_argument("--period", default = 1000, type = int, help = "Amount of time each ping message is separated by in milliseconds. (Default: 1000)")
parser.add_argument("--timeout", default = 60000, type = int, help = "Amount of time client will wait to receive pong reply in milliseconds. (Default: 60000)")

args = parser.parse_args() # parse arguments

# extract individual arguments to be passed to the constructor of PingClient
serverIP = args.server_ip
serverPort = args.server_port
count = args.count
period = args.period
timeout = args.timeout

pingClient = pc.PingClient(serverIP, serverPort, count, period, timeout)
pingClient.run()
