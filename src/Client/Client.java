package Client;

import FileManager.FileManager;
import Interfaces.Constants;
import Interfaces.Server_API;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;

public class Client implements Constants, Server_API {

    ClientWindow window;
    ClientConnection clientConnection;
    FileManager fileManager;

    Client() {
        init();
    }

    void init() {
        clientConnection = new ClientConnection();
        fileManager = clientConnection.getFileManager();
        window = new ClientWindow(this);
        fillUserFileList();
        Thread t = new Thread(clientConnection);
        t.start();
    }

    public void fillUserFileList() {
        fillFileList(window.getListModelUserFiles(), fileManager.getFileArray());
    }

    private void fillFileList(DefaultListModel model, File[] files) {
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) model.addElement(files[i]);
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) model.addElement(files[i]);
        }
    }
}
