package Server;

import Interfaces.Constants;
import Interfaces.Server_API;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server implements Constants, Server_API {

    ThreadPoolExecutor executor;

    public Server() {
        ServerSocket server = null;
        Socket socket = null;
        executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        try {
            server = new ServerSocket(PORT);
            System.out.println("Server is up and running! Awaiting for connections...");
            while (true) {
                socket = server.accept();
                System.out.println("Client connected!");
                ClientHandler tmp = new ClientHandler(this, socket);
                executor.execute(tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(ClientHandler clientHandler) {
        executor.remove(clientHandler);
        System.out.println("Client disconnected!");
    }
}
