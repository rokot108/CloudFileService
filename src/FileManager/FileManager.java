package FileManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import Interfaces.Constants;

public class FileManager implements Constants {

    private String userID;
    private File userDir;

    public FileManager(String userID) {
        this.userID = userID;
        init();
        initServ();
    }

    public FileManager() {
        initUser();
    }

    private void init() {
        File homePath = new File(HOME_PATH);
        File tmpPath = new File(TMP_PAPH);
        if (!homePath.exists()) {
            homePath.mkdir();
        }
        if (!tmpPath.exists()) {
            tmpPath.mkdir();
        }
    }

    private void initServ() {
        File serverPath = new File(SERVER_PAPH);
        if (!serverPath.exists()) {
            serverPath.mkdir();
        }
        String userPath = SERVER_PAPH + "/" + userID;
        userDir = new File(userPath);
        if (!userDir.exists()) {
            System.out.println("Creating a user directory.");
            userDir.mkdir();
        }
    }

    private void initUser() {
        userDir = new File(CLIENT_PATH);
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
        File requestedFile = new File(userDir + "/" + filename);
        if (requestedFile.exists()) {
            File tmp = new File(TMP_PAPH + "/" + filename);
            try {
                Files.copy(requestedFile.toPath(), tmp.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tmp;
        }
        return null;
    }
}
