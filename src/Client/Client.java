package Client;

import Interfaces.Constants;
import Interfaces.Server_API;

import java.io.File;

public class Client implements Constants, Server_API {

    ClientConnection clientConnection;
    File userDir;
    String[] files;

    Client() {
        clientConnection = new ClientConnection();
        Thread t = new Thread(clientConnection);
        t.start();
        userDir = new File(CLIENT_PATH);
        files = userDir.list();
        for (int i = 0; i < files.length; i++) {
            System.out.println("Sending a file: " + files[i]);
            clientConnection.sendFile(files[i]);
        }
    }
}
