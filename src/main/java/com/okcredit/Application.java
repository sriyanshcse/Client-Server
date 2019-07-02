package com.okcredit;

import com.okcredit.server.MultiClientServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
        MultiClientServer multiClientServer = new MultiClientServer();
        multiClientServer.start(6666);
    }
}
