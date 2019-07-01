package com.okcredit.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket clientSocket;
    private PrintWriter writer;
    private BufferedReader readerClient;
    private BufferedReader readerServer;

    public void start(String address, int port) {
        try {
            clientSocket = new Socket(address, port);
            readerClient = new BufferedReader(new InputStreamReader(System.in));
            readerServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            System.out.print("Connected With Server\n");
            boolean connected = true;
            do {
                String input;
                connected = true;
                try {
                    if ((input = readerServer.readLine()) != null) {
                        System.out.println(input);
                    }
                    System.out.println("Finished Reading server");
                    if ((input = readerClient.readLine()) != null) {
                        System.out.println(input);
                        writer.println(input);
                        if ("exit".equals(input)) {
                            connected = false;
                        }
                    }
                } catch (Exception E) {

                }
            } while (connected);
            System.out.println("client closed");
            clientSocket.close();
            writer.close();
            readerClient.close();
            readerServer.close();
        } catch (Exception E) {

        }
    }

    public static void main(String args[]) {
        Client client = new Client();
        client.start("127.0.0.1", 5555);
    }
}
