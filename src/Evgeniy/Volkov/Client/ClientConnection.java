package Evgeniy.Volkov.Client;

import Evgeniy.Volkov.ServerConst;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientConnection implements ServerConst {

    Socket socket;
    DataOutputStream out;
    DataInputStream in;

    public ClientConnection() {
        init();
    }

    public void init() {
        try {
            this.socket = new Socket(SERVER_URL, PORT);
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
        }
    }
}