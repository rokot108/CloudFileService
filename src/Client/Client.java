package Client;

import ServerConst;
import Server_API;

import java.io.File;

public class Client implements ServerConst, Server_API {

    ClientConnection clientConnection;
    File userDir;
    File[] files;

    Client() {
        clientConnection = new ClientConnection();
        userDir = new File(CLIENT_PATH);
        files = userDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.println("Sending a file: " + files[i].getName());
            clientConnection.send((Object) files[i]);
        }
        clientConnection.disconnect();
    }
}
