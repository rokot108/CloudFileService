package FileManager;

import java.io.File;
import java.io.IOException;

import Interfaces.Constants;

public class FileManager implements Constants {

    private String userID;
    private File userDir;


    public FileManager(String userID) {
        this.userID = userID;
        initServ();
    }

    public FileManager() {
        initUser();
    }

    private void initServ() {
        File homePath = new File(HOME_PATH);
        File serverPath = new File(SERVER_PAPH);
        if (!homePath.exists()) {
            homePath.mkdir();
        }
        if (!serverPath.exists()) {
            serverPath.mkdir();
        }
        String userPath = SERVER_PAPH + "/" + userID;
        this.userDir = new File(userPath);
        if (!userDir.exists()) {
            System.out.println("Creating a user directory.");
            userDir.mkdir();
        }
    }

    private void initUser() {
        File homePath = new File(HOME_PATH);
        userDir = new File(CLIENT_PATH);
        if (!homePath.exists()) {
            homePath.mkdir();
        }
        if (!userDir.exists()) {
            userDir.mkdir();
        }
    }

    public void writeFile(File file) {
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
    }

    public File getFile(String filename) {
        File tmp = new File(userDir + "/" + filename);
        if (tmp.exists()) {
            return tmp;
        }
        return null;
    }
}
