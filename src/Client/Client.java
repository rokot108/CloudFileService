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

    public void setServerCurrentDirLabel(String serverCurrentDir) {
        window.setServerCurrentDirLabel(serverCurrentDir);
    }

    public void changeCurrentServerDir(String directory) {
        clientConnection.send(CHANGE_CURRENT_SERVER_DIR + STRING_SPLITTER + directory);
    }

    public void changeCurrentServerDir() {
        clientConnection.send(UP_CURRENT_SERVER_DIR);
    }

    public void sendFile(File file) {
        if (file.isDirectory()) {
            fileManager.sendDirectory(file.getName());
        } else {
            clientConnection.send(FILE_SEND_REQUEST + STRING_SPLITTER + file.getName());
        }
    }

    public void requestFile(String filename) {
        if (fileManager.isAcceptable(filename)) {
            clientConnection.send(FILE_DOWNLOAD_REQUEST + STRING_SPLITTER + filename);
        } else {
            window.setServerMsgLabel("Unable to download: File " + filename + " is already exists.");
        }
    }

    public void reqesFileDelete(String filename) {
        clientConnection.send(DELETE_FILE + STRING_SPLITTER + filename);
    }

    public void requestAll() {
        clientConnection.send(REQUEST_ALL);
    }

    public void requestForRefresh() {
        clientConnection.send(REFRESH);
    }

    public void authAction(AuthActions action, String login, String pass) {
        switch (action) {
            case LOGIN: {
                clientConnection.send(AUTH + STRING_SPLITTER + login + STRING_SPLITTER + pass.hashCode());
                break;
            }
            case REGISTER: {
                clientConnection.send(REGISTRATION + STRING_SPLITTER + login + STRING_SPLITTER + pass.hashCode());
                break;
            }
        }
    }

    public void showMsg(String serverMsg) {
        window.setServerMsgLabel(serverMsg);
    }
}
