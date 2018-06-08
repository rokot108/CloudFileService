package Client;

import Interfaces.ServerConst;
import Interfaces.Server_API;

import java.io.File;
import java.io.IOException;

public class Client implements ServerConst, Server_API {

    ClientConnection clientConnection;
    File userDir;
    File[] files;

    Client() {
        clientConnection = new ClientConnection();
        userDir = new File(CLIENT_PATH);
        files = userDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            clientConnection.send(files[i]);
        }

        clientConnection.disconnect();
       /* Thread tread = new Thread(clientConnection);
        tread.start();

        System.out.println("Requesting a file: 785954341d62620301d9607bf3319d87.mp4");
        clientConnection.send(FILE_REQUEST + STRING_SPLITTER + "785954341d62620301d9607bf3319d87.mp4");*/

    }
}
