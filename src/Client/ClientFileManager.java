package Client;

import Interfaces.ServerConst;

import java.io.File;
import java.io.IOException;

public class ClientFileManager implements ServerConst {

    public ClientFileManager() {
        init();
    }

    private void init() {
        File homePath = new File(HOME_PATH);
        File clientPath = new File(CLIENT_PATH);
        if (!homePath.exists()) {
            homePath.mkdir();
        }
        if (!clientPath.exists()) {
            clientPath.mkdir();
        }
    }

    public void writeFile(File file) {
        File tmp = new File(CLIENT_PATH + "/" + file.getName());
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
}
