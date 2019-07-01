package com.okcredit.server;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.Callable;

public class ClientHandler implements Callable<Boolean> {
    private Socket clientSocket;
    private String id;
    private PrintWriter writer;
    private BufferedReader reader;

    public ClientHandler(Socket socket) {
        this.id = UUID.randomUUID().toString();
        clientSocket = socket;
        try {
            socket.setSoTimeout(5000);
        } catch (Exception E) {

        }
        try {
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception E) {

        }
    }

    public String getId() {
        return id;
    }

    public long pingClient() {
        writer.println("PING");
        System.out.println("Pinged to Client " + id);
        return System.currentTimeMillis();
    }

    public boolean disconnectIfNoResponse(long pingTime) throws Exception {
        String input;
        if ((input = reader.readLine()) != null) {
            System.out.println("Message From Client " + id +  " : " + input);
            if ("PONG".equals(input)) {
                System.out.println("hello");
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean call() throws Exception {
        return disconnectIfNoResponse(pingClient());
    }

    public void stop() {

    }
}
