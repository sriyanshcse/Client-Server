package com.okcredit.server;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultiClientServer {

    private ServerSocket server;

    // List of clients connected
    private static List<ClientHandler> clients = new ArrayList<>();

    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);


    /*
        1. Start Server and accept client connections
        2. Schedule broadcast message
     */
    public void start(int port) {
        try {
            server = new ServerSocket(port);
            // schedule ping broadcast to clients
            scheduleBroadcast();
            while (true) {
                ClientHandler clientHandler = new ClientHandler(server.accept());
                clients.add(clientHandler);
            }
        } catch (Exception E) {
            E.printStackTrace();
        }
    }


    // Scheduling ping broadcast to clients every 30s
    private void scheduleBroadcast() {
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Starting ...." + clients.size());
                if (clients.size() == 0) {
                    return;
                }
                ExecutorService executorService = Executors.newFixedThreadPool(10);
                for (ClientHandler clientHandler : clients) {
                    executorService.submit(clientHandler);
                }
                // Finish assigning tasks to thread pool. No further tasks are accepted
                executorService.shutdown();
                try {
                    // Wait for all threads to finish (blocking call)
                    executorService.awaitTermination(10L, TimeUnit.SECONDS);
                } catch (InterruptedException E) {
                    E.printStackTrace();
                    System.out.println("All threads not completed within time");
                }
                synchronized (clients) {
                    ListIterator<ClientHandler> iterator = clients.listIterator();
                    // Remove clients for which socket has been closed
                    while (iterator.hasNext()) {
                        if (iterator.next().isSocketClosed()) {
                            iterator.remove();
                        }
                    }
                }
            }
        }, 0, 30, TimeUnit.SECONDS);
    }


    // stop server
    public void stop() throws IOException {
        server.close();
    }

    public synchronized static List<ClientHandler> getClients() {
        return clients;
    }

}
