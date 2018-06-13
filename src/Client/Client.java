package Client;

import Interfaces.Constants;
import Interfaces.Server_API;

import java.io.File;

public class Client implements Constants, Server_API {

    ClientConnection clientConnection;
    File userDir;
    File[] files;

    Client() {
        clientConnection = new ClientConnection();
        userDir = new File(CLIENT_PATH);
        files = userDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.println("Sending a file: " + files[i].getName());
            clientConnection.sendFile(files[i].getName());
        }

        Thread tread = new Thread(clientConnection);
        tread.start();

        System.out.println("Requesting a file: hfs.exe");
        clientConnection.send(FILE_REQUEST + STRING_SPLITTER + "hfs.exe");

        try {
            tread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        clientConnection.disconnect();
    }
}
