package Evgeniy.Volkov.Server;

import Evgeniy.Volkov.Server_API;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Server_API {

    Server server;
    private String clientID;
    private FielManager fielManager;
    private Socket socket;
    private InputStream in;
    private OutputStream os;
    private ObjectInputStream inputObject;
    private ObjectOutputStream outputStream;


    public ClientHandler(Server server, Socket socket) {
        this.server = server;
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
                    if (request instanceof String) {
                        String tmp = (String) request;
                        if (((String) request).startsWith(CLOSE_CONNECTION)) {
                            in.close();
                            os.close();
                            socket.close();
                            server.disconnect(this);
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
