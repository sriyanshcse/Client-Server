package com.okcredit.server;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private String id;
    private PrintWriter writer;
    private BufferedReader reader;

    public ClientHandler(Socket socket) {
        this.id = UUID.randomUUID().toString();
        clientSocket = socket;
        try {
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    private void pingClient() {
        writer.println("PING");
        System.out.println("Pinged to Client " + id);
        try {
            // Sleep thread for 4s so that other tasks can be assigned to the thread
            Thread.sleep(4000);
        } catch (InterruptedException IEx) {

        }
    }

    // Disconnect Client if not response received from client
    public void disconnectIfNoResponse() {
        String input;
        try {
            // Check if socket read returns data within 1 second since thread already slept for 4s
            clientSocket.setSoTimeout(1000);
            if ((input = reader.readLine()) != null) {
                System.out.println("Message From Client " + id +  " : " + input);
                if (!"PONG".equals(input)) {
                    throw new Exception();
                }
            }
        } catch (Exception E) {
            E.printStackTrace();
            try {
                clientSocket.close();
            } catch (Exception Ex) {
                E.printStackTrace();
            }
        }
    }

    @Override
    public void run()  {
        try {
            // ping client
            pingClient();
            // check if there's response from client
            disconnectIfNoResponse();
            clientSocket.setSoTimeout(1000000);
        } catch (SocketException E) {
            E.printStackTrace();
        }
    }

    public boolean isSocketClosed() {
        return clientSocket.isClosed();
    }

    public void stop() {

    }
}
