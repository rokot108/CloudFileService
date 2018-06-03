package Client;

import ServerConst;
import Server_API;

import java.io.*;
import java.net.Socket;

public class ClientConnection implements ServerConst, Server_API {

    String ClientID = "0001";
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;

    public ClientConnection() {
        init();
    }

    public void init() {
        try {
            this.socket = new Socket(SERVER_URL, PORT);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
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
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}