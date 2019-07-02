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


    // Setup clientsocket and client read streams
    public void start(String address, int port) {
        try {
            clientSocket = new Socket(address, port);
            readerClient = new BufferedReader(new InputStreamReader(System.in));
            readerServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            System.out.print("Connected With Server\n");
            do {
                String input;
                try {
                    // Read socket stream
                    if ((input = readerServer.readLine()) != null) {
                        System.out.println(input);
                    }
                    System.out.println("Finished Reading server");
                    // Read client input
                    if ((input = readerClient.readLine()) != null) {
                        System.out.println(input);
                        writer.println(input);
                        if ("exit".equals(input)) {
                            break;
                        }
                    }
                } catch (Exception E) {
                    E.printStackTrace();
                    break;
                }
            } while (true);
            System.out.println("client closed");
            clientSocket.close();
            writer.close();
            readerClient.close();
            readerServer.close();
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Client client = new Client();
        client.start("127.0.0.1", 6666);
    }
}
