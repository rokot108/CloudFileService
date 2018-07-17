package FileManager;

import java.io.*;
import java.util.ArrayList;

import Interfaces.*;

public class FileManager implements Constants, Server_API {

    private String userID;
    private File userDir;
    private File currentDir;
    CloudServiceConnectable connection;
    ArrayList<FileWriter> fileWriters;

    public FileManager(String userID, CloudServiceConnectable connection) {
        this.connection = connection;
        this.userID = userID;
        init();
        initServ();
    }

    public FileManager(CloudServiceConnectable connection) {
        this.connection = connection;
        init();
        initUser();
    }

    private void init() {
        fileWriters = new ArrayList<FileWriter>();
        File homeDir = new File(HOME_PATH);
        if (!homeDir.exists()) {
            homeDir.mkdir();
        }
    }

    private void initServ() {
        File serverPath = new File(SERVER_PATH);
        if (!serverPath.exists()) {
            serverPath.mkdir();
        }
        String userPath = SERVER_PATH + "/" + userID;
        userDir = new File(userPath);
        if (!userDir.exists()) {
            System.out.println("Creating a user directory.");
            userDir.mkdir();
        }
        currentDir = new File(userPath);
    }

    private void initUser() {
        userDir = new File(CLIENT_PATH);
        if (!userDir.exists()) {
            userDir.mkdir();
        }
        currentDir = new File(CLIENT_PATH);
    }

    public File createFile(String filename, boolean isDirectory) {
        File tmp = new File(currentDir + "\\" + filename);
        if (!tmp.exists()) {
            try {
                if (isDirectory) {
                    tmp.mkdir();
                } else {
                    tmp.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tmp;
        } else return null;
    }

    public void sendAFile(String filename) {
        File sendingFile = new File(currentDir + "\\" + filename);
        if (sendingFile.exists()) {
            if (sendingFile.isDirectory()) {
                sendDirectory(filename);
            } else {
                splitAndSend(filename);
            }
        }
    }

    public void splitAndSend(String filename) {
        File sendingFile = new File(currentDir + "\\" + filename);
        Thread t = new Thread(() -> {
            try (FileInputStream fis = new FileInputStream(sendingFile)) {
                int totalParts = (int) (sendingFile.length() / FILEPART_SIZE);
                if (sendingFile.length() / FILEPART_SIZE != 0 || totalParts == 0) totalParts++;
                try {
                    for (int part = 1; part <= totalParts; part++) {
                        if (part < totalParts) {
                            byte[] byteArray = new byte[FILEPART_SIZE];
                            fis.read(byteArray);
                            FilePart filePart = new FilePart(filename, totalParts, part, byteArray);
                            connection.send(filePart);
                        } else {
                            byte[] lastByteArray = new byte[fis.available()];
                            fis.read(lastByteArray);
                            FilePart filePart = new FilePart(filename, totalParts, part, lastByteArray);
                            connection.send(filePart);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();

    }

    public void writeSplitedFile(FilePart filePart) {
        FileWriter tmp = null;
        if (filePart.getPart() == 1 && filePart.getTotalParts() == 1) {
            new FileWriter(this, filePart);
        }
        if (filePart.getPart() == 1 && filePart.getTotalParts() > 1) {
            fileWriters.add(new FileWriter(this, filePart));
        }
        if (filePart.getPart() != 1) {
            for (FileWriter fw : fileWriters) {
                if (fw.getFilename().equals(filePart.getFilename())) {
                    fw.writeToFile(filePart);
                    tmp = fw;
                }
            }
            if (filePart.getPart() == filePart.getTotalParts()) {
                fileWriters.remove(tmp);
            }
        }
    }

    public String getRelativePath(File file, boolean fromCurrentDir) {
        if (!fromCurrentDir) {
            if (file.getPath().length() > userDir.getPath().length()) {
                String relativePath = file.getPath().substring(userDir.getPath().length() + 1);
                return relativePath;
            } else return "";
        } else {
            if (file.getPath().length() > currentDir.getPath().length()) {
                String relativePath = file.getPath().substring(currentDir.getPath().length() + 1);
                return relativePath;
            } else return "";
        }
    }

    public void sendCurrentDirName() {
        connection.send(NEW_CURRENT_SERVER_DIR + STRING_SPLITTER + getRelativePath(currentDir, false));
    }

    public File getCurrentDir() {
        return currentDir;
    }

    public File[] getFileArray() {
        return currentDir.listFiles();
    }

    public void changeCurrentDir(String goToDir) {
        File[] files = getFileArray();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().equals(goToDir) && files[i].isDirectory()) {
                currentDir = files[i];
                if (userID != null) {
                    sendCurrentDirName();
                    connection.send(getFileArray());
                }
            }
        }
    }

    public void directoryUP() {
        if (!currentDir.equals(userDir)) {
            currentDir = new File(currentDir.getParent());
            if (userID != null) {
                sendCurrentDirName();
                connection.send(getFileArray());
            }
        }
    }

    public void sendDirectory(String directoryName) {
        connection.send(CREATE_NEW_DIR + STRING_SPLITTER + directoryName);
        File sendingDir = new File(currentDir + "\\" + directoryName);
        for (File file : sendingDir.listFiles()) {
            if (file.isDirectory()) {
                sendDirectory(getRelativePath(file, true));
            } else {
                splitAndSend(getRelativePath(file, true));
            }
        }
    }

    public void sendAll() {
        for (String filename : currentDir.list()) {
            sendAFile(filename);
        }
    }

    public void deleteFile(String filename) {
        File toDelete = new File(currentDir + "\\" + filename);
        if (toDelete.exists()) {
            if (toDelete.isFile()) {
                toDelete.delete();
            } else {
                for (File file : toDelete.listFiles()) {
                    deleteFile(getRelativePath(file, true));
                    file.delete();
                }
                toDelete.delete();
            }
        }
        if (userID != null) {
            connection.send(getFileArray());
        }
    }

    public void setUserDir(File userDir) {
        this.userDir = userDir;
        this.currentDir = userDir;
    }
}
