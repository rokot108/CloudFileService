package Server;

import Interfaces.ServerConst;

import java.io.File;
import java.io.IOException;

public class ServerFielManager implements ServerConst {

    private boolean isBusy;
    private String userID;
    private File userDir;

    public ServerFielManager(String userID) {
        isBusy = false;
        init();
        this.userID = userID;
        String userPath = SERVER_PAPH + "/" + userID;
        this.userDir = new File(userPath);
        if (!userDir.exists()) {
            System.out.println("Creating a user directory.");
            userDir.mkdir();
        }
    }

    private void init() {
        File homePath = new File(HOME_PATH);
        File serverPath = new File(SERVER_PAPH);
        if (!homePath.exists()) {
            homePath.mkdir();
        }
        if (!serverPath.exists()) {
            serverPath.mkdir();
        }
    }

    public void writeFile(File file) {
        isBusy = true;
        File tmp = new File(userDir + "/" + file.getName());
        file.renameTo(tmp);
        if (!file.exists()) {
            System.out.println("Writing a new file: " + file.getName());
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        isBusy = false;
    }

    public File getFile(String filename) {
        File tmp = new File(userDir + "/" + filename);
        if (tmp.exists()) {
            return tmp;
        }
        return null;
    }

    public boolean isBusy() {
        return isBusy;
    }
}
