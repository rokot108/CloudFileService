package Evgeniy.Volkov.Server;

import Evgeniy.Volkov.ServerConst;
import Evgeniy.Volkov.Server_API;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements ServerConst, Server_API {

    public Server() {
        ServerSocket server = null;
        Socket socket = null;

        try {
            server = new ServerSocket(PORT);
            System.out.println("Server is up and running! Awaiting for connections");
            while (true) {
                socket = server.accept();
                System.out.println("Client connected!");
            }
        } catch (IOException e) {
        }
    }
}
