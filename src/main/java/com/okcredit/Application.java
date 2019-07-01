package com.okcredit;

import com.okcredit.server.MultiClientServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String args[]) {
        MultiClientServer multiClientServer = new MultiClientServer();
        multiClientServer.start(5555);
        SpringApplication.run(Application.class, args);
    }
}
