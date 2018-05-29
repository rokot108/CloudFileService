package Evgeniy.Volkov.Server;

import java.io.*;
import java.net.Socket;

public class ClientHandler {

    String clientID;
    FielManager fielManager;
    private Socket socket;
    InputStream in;
    OutputStream os;
    ObjectInputStream inputObject;
    ObjectOutputStream outputStream;


    public ClientHandler(Socket socket) {
        this.clientID = "0001";
        this.socket = socket;
        init();
    }

    private void init() {
        fielManager = new FielManager(clientID);
        try {
            in = socket.getInputStream();
            os = socket.getOutputStream();
            inputObject = new ObjectInputStream(in);
            outputStream = new ObjectOutputStream(os);

            while (true) {
                Object request = new Object();
                while (true) {
                    request = inputObject.readObject();
                    if (request instanceof File) {
                        fielManager.writeFile((File) request);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
