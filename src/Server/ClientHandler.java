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
        send(fileManager.getFileArray());
        fileManager.sendCurrentDirName();
        while (true) {
            try {
                Object request;
                while (true) {
                    request = in.readObject();
                    if (request instanceof FilePart) {
                        fileManager.writeSplitedFile((FilePart) request);
                        send(fileManager.getFileArray());
                    }
                    if (request instanceof String) {
                        String tmp = (String) request;
                        if (tmp.startsWith(CLOSE_CONNECTION)) {
                            disconnect();
                            return;
                        }
                        if (tmp.startsWith(FILE_REQUEST)) {
                            String[] commands = tmp.split(STRING_SPLITTER, 2);
                            fileManager.sendAFile(commands[1]);
                        }
                        if (tmp.startsWith(CHANGE_CURRENT_SERVER_DIR)) {
                            String[] req = tmp.split(STRING_SPLITTER);
                            fileManager.changeCurrentDir(req[1]);
                        }
                        if (tmp.startsWith(UP_CURRENT_SERVER_DIR)) {
                            fileManager.directoryUP();
                        }
                        if (tmp.startsWith(CREATE_NEW_DIR)) {
                            String[] req = tmp.split(STRING_SPLITTER, 2);
                            fileManager.createFile(req[1], true);
                            send(fileManager.getFileArray());
                        }
                        if (tmp.startsWith(DELETE_FILE)) {
                            String[] req = tmp.split(STRING_SPLITTER, 2);
                            fileManager.deleteFile(req[1]);
                            send(fileManager.getFileArray());
                        }
                        if (tmp.startsWith(REQEST_ALL)) {
                            fileManager.sendAll();
                        }
                        if (tmp.startsWith(REFRESH)) {
                            send(fileManager.getFileArray());
                        }
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
