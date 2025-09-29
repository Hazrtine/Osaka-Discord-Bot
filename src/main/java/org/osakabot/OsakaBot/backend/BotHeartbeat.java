package org.osakabot.OsakaBot.backend;

import com.sun.net.httpserver.HttpServer;
import org.osakabot.OsakaBot.commands.OutsideInteractionsBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class BotHeartbeat {

    private static final Logger LOGGER = LoggerFactory.getLogger(BotHeartbeat.class);

    public static void start(int port) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/ping", ex -> {
            String body = "{\"online\":true}";
            ex.getResponseHeaders().add("Content-Type", "application/json");
            ex.sendResponseHeaders(200, body.length());
            ex.getResponseBody().write(body.getBytes());
            ex.close();
        });
        server.start();
    }
}
