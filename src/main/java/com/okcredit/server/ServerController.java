package com.okcredit.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class ServerController {

        @GetMapping("/clients")
        public List<String> getConnectedClients() {
            List<String> ids = new ArrayList<>();
            for (ClientHandler client : MultiClientServer.getClients()) {
                ids.add(client.getId());
            }
            return ids;
        }
}
