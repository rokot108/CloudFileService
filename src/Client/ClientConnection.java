package Client;

import Interfaces.ServerConst;
import Interfaces.Server_API;

import java.io.*;
import java.net.Socket;

public class ClientConnection implements ServerConst, Server_API, Runnable {

    String ClientID = "0001";
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
    ClientFileManager clientFileManager;
    private boolean isInterrupted = false;

    public ClientConnection() {
        init();
    }

    public void init() {
        clientFileManager = new ClientFileManager();
        try {
            this.socket = new Socket(SERVER_URL, PORT);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Object obj) {
        try {
            out.writeObject(obj);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        send((Object) CLOSE_CONNECTION);
        interrupt();
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
        while (true) {
            Object request = new Object();
            while (!isInterrupted) {
                try {
                    request = in.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (request instanceof File) {
                    clientFileManager.writeFile((File) request);
                    return;
                }
                if (request instanceof String) {
                    String tmp = (String) request;
                    if (tmp.startsWith(CLOSE_CONNECTION)) {
                        disconnect();
                        return;
                    }
                }
            }
            return;
        }
    }
}