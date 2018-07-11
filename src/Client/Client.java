package Client;

import FileManager.FileManager;
import Interfaces.Constants;
import Interfaces.Server_API;

import java.io.File;

public class Client implements Constants, Server_API {

    ClientWindow window;
    ClientConnection clientConnection;
    FileManager fileManager;

    Client() {
        init();
    }

    void init() {
        clientConnection = new ClientConnection(this);
        fileManager = clientConnection.getFileManager();
        window = new ClientWindow(this, fileManager);
        window.fillUserFileList();
        Thread t = new Thread(clientConnection);
        t.start();
    }

    public void disconnect() {
        clientConnection.disconnect();
    }

    public void fillServerFileList(File[] files) {
        window.fillServerFileList(files);
    }

    public void setServerCurrentDir(String serverCurrentDir) {
        window.setServerCurrentDirLabel(serverCurrentDir);
    }

    public void requestServerDir(String directory) {
        clientConnection.send(CHANGE_CURRENT_SERVER_DIR + " " + directory);
    }

    public void requestServerDir() {
        clientConnection.send(UP_CURRENT_SERVER_DIR);
    }
}
