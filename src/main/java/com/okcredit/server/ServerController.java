package com.okcredit.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class ServerController {

        @Autowired
        private MultiClientServer service;

        @GetMapping("/clients")
        public List<String> getConnectedClients() {
            List<String> ids = new ArrayList<>();
            for (ClientHandler client : service.clients) {
                ids.add(client.getId());
            }
            return ids;
        }
}
