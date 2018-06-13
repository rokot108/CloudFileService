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
            clientConnection.send(files[i]);
        }

        Thread tread = new Thread(clientConnection);
        tread.start();

        System.out.println("Requesting a file: Sense8.s01e01.1080p.WEBRip.NewStudio.TV.mkv");
        clientConnection.send(FILE_REQUEST + STRING_SPLITTER + "Sense8.s01e01.1080p.WEBRip.NewStudio.TV.mkv");

        try {
            tread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        clientConnection.disconnect();
    }
}
