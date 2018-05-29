package Evgeniy.Volkov.Server;

import Evgeniy.Volkov.ServerConst;
import Evgeniy.Volkov.Server_API;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server implements ServerConst, Server_API {

    LinkedList<ClientHandler> clients;

    public Server() {
        clients = new LinkedList<>();
        ServerSocket server = null;
        Socket socket = null;

        try {
            server = new ServerSocket(PORT);
            System.out.println("Server is up and running! Awaiting for connections");
            while (true) {
                socket = server.accept();
                System.out.println("Client connected!");
                clients.add(new ClientHandler(this, socket));
            }
        } catch (IOException e) {
        }
    }

    public void disconnect(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        System.out.println("Client disconnected!");
    }
}
