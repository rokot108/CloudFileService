package Server;

import Interfaces.ServerConst;
import Interfaces.Server_API;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server implements ServerConst, Server_API {

    LinkedList<ClientHandler> clients;
    ThreadPoolExecutor executor;

    public Server() {
        clients = new LinkedList<>();
        ServerSocket server = null;
        Socket socket = null;
        executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        try {
            server = new ServerSocket(PORT);
            System.out.println("Server is up and running! Awaiting for connections");
            while (true) {
                socket = server.accept();
                System.out.println("Client connected!");
                ClientHandler tmp = new ClientHandler(this, socket);
                executor.execute(tmp);
            }
        } catch (IOException e) {
        }
    }

    public void disconnect(ClientHandler clientHandler) {
        executor.remove(clientHandler);
        System.out.println("Client disconnected!");
    }
}
