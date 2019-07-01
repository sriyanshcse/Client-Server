package com.okcredit.server;

import com.okcredit.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class MultiClientServer {

    private ServerSocket server;
    List<ClientHandler> clients = new ArrayList<>();
    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);


    public void start(int port) {
        try {
            server = new ServerSocket(port);
            scheduleBroadcast();
            while (true) {
                ClientHandler clientHandler = new ClientHandler(server.accept());
                clients.add(clientHandler);
            }
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    private void scheduleBroadcast() {
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                    System.out.println("Starting ...." + clients.size());
                    if (clients.size() == 0) {
                        return;
                    }
                    ListIterator<ClientHandler> iterator = clients.listIterator();
                    while (iterator.hasNext()) {
                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        Future<Boolean> task = executorService.submit(iterator.next());
                        try {
                            task.get(5L, TimeUnit.SECONDS);
                        } catch (Exception E) {
                            iterator.remove();
                            task.cancel(true);
                        } finally {
                            executorService.shutdown();
                        }
                    }
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    public void stop() throws IOException {
        server.close();
    }

}
