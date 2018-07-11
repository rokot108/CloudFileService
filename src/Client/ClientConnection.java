package Client;

import FileManager.*;
import Interfaces.*;

import java.io.*;
import java.net.Socket;

public class ClientConnection implements Constants, Server_API, Runnable, CloudServiceConnectable {

    String ClientID = "0001";
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
    Client client;
    FileManager fileManager;
    private boolean isInterrupted = false;

    public ClientConnection(Client client) {
        this.client = client;
        init();
    }

    public void init() {
        fileManager = new FileManager(this);
        try {
            this.socket = new Socket(SERVER_URL, PORT);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized public void send(Object obj) {
        if (socket.isConnected()) {
            try {
                out.writeObject(obj);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendFile(String filename) {
        fileManager.splitAndSend(filename);
    }

    public void disconnect() {
        interrupt();
        send(CLOSE_CONNECTION);
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void interrupt() {
        isInterrupted = true;
    }

    @Override
    public void run() {
        Object request = new Object();
        while (!isInterrupted) {
            try {
                request = in.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (request instanceof FilePart) {
                fileManager.writeSplitedFile((FilePart) request);
            }
            if (request instanceof File[]) {
                client.fillServerFileList((File[]) request);
            }
            if (request instanceof String) {
                String tmp = (String) request;
                if (tmp.startsWith(CLOSE_CONNECTION)) {
                    disconnect();
                }
                if (tmp.startsWith(NEW_CURRENT_SERVER_DIR)) {
                    String[] req = tmp.split(" ");
                    client.setServerCurrentDir(req[1]);
                }
            }
        }
    }

    public FileManager getFileManager() {
        return fileManager;
    }
}