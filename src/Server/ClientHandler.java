package Server;

import FileManager.*;
import Interfaces.*;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Server_API, Runnable, CloudServiceConnectable {

    Server server;
    private String clientID;
    private FileManager fileManager;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;


    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.clientID = "0001";
        this.socket = socket;
        this.fileManager = new FileManager(clientID, this);
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                Object request;
                while (true) {
                    request = in.readObject();
                    if (request instanceof FilePart) {
                        fileManager.writeSplitedFile((FilePart) request);
                    }
                    if (request instanceof String) {
                        String tmp = (String) request;
                        if (tmp.startsWith(CLOSE_CONNECTION)) {
                            disconnect();
                            return;
                        }
                       /* if (tmp.startsWith(FILE_REQUEST)) {
                            String[] commands = tmp.split(STRING_SPLITTER, 2);
                            File requestedFile = fileManager.getFile(commands[1]);
                            if (requestedFile != null) {
                                System.out.println("Sending a file: " + commands[1]);
                                send(requestedFile);
                            } else {
                                System.out.println("No such file: " + commands[1]);
                                send(FILE_NOT_FOUND);
                            }
                        }*/
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized public void send(Object obj) {
        try {
            out.writeObject(obj);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            in.close();
            out.close();
            socket.close();
            server.disconnect(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
