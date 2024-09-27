package org.eru;

import org.eru.models.websocket.XMPP;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        Variables.WSConnection = new XMPP();
        Variables.WSConnection.startServer();
    }
}
