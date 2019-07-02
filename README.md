# Client-Server socket communication

This is a Client-Server communication service where server pings all the clients connected every 30 seconds with "PING" message. If client doesn't respond within 5 seconds with "PONG" , it is considered disconnected. A list is maintained to keep track of the clients currently connected. For communication between client server we are using TCP sockets.

Server also exposes a get method which returns the id's of connected clients.

For Scheduling broadcast "PING" message to clients, we've used `ScheduledExecutorService` which executes a ping broadcast method every 30 seconds. During this scheduled ping execution we create a threadpool from which threads are assigned to execute ping and receving response from each client. 
After pinging to client, thread goes to sleep for 4s so that it doesn't block any other threads from being assigned to this task. Then we check if there's any data available in the stream for next 1 second. If not we close the socket and remove the client from our connected clients list. If client has responded with "PONG" in this 5 second window, we reset socket timeout to 100s (which will again be set when the scheduled ping method gets executed after 30 seconds).

For checking if data is present in the stream, we use socket timeout for 1 second which will throw `SocketReadTimeoutException` if there's no data for 1 second in the stream.

For creating and binding socket, we are using `java.net.socket` library.

## Start Server
  `sh scripts/start-server.sh`
  
## Start Client
  `sh script/start-client.sh`

## Get Connected Clients
  `HTTP GET` `localhost:8080/clients`
