package Server;

import AuthService.AuthorisationManager;
import FileManager.*;
import Interfaces.*;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Server_API, Runnable, CloudServiceConnectible {

    Server server;
    private FileManager fileManager;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private AuthorisationManager authMgr;


    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        this.authMgr = new AuthorisationManager();
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Object request;
        try {
            while (!authMgr.isAuthorised()) {
                request = in.readObject();
                if (request instanceof String) {
                    String tmp = (String) request;
                    if (tmp.startsWith(AUTH)) {
                        String[] commands = tmp.split(STRING_SPLITTER, 3);
                        if (commands.length == 3) {
                            authMgr.setUserLogin(commands[1]);
                            authMgr.setUserPassHash(Integer.parseInt(commands[2]));
                            authMgr.login();
                        }
                    }
                    if (tmp.startsWith(REGISTRATION)) {
                        String[] commands = tmp.split(STRING_SPLITTER, 3);
                        if (commands.length == 3) {
                            authMgr.setUserLogin(commands[1]);
                            authMgr.setUserPassHash(Integer.parseInt(commands[2]));
                            authMgr.register();
                        }
                    }
                    if (tmp.startsWith(CLOSE_CONNECTION)) {
                        disconnect();
                        return;
                    }
                }
                send(SERVER_MSG + STRING_SPLITTER + authMgr.getFeedbackMessage());
            }
            if (authMgr.isAuthorised()) {
                send(AUTH_OK);
                this.fileManager = new FileManager(authMgr.getUserLogin().hashCode(), this);
                send(fileManager.getFileArray());
                fileManager.sendCurrentDirName();

                while (true) {
                    request = in.readObject();
                    if (request instanceof FilePart) {
                        fileManager.writeSplitFile((FilePart) request);
                        send(fileManager.getFileArray());
                    }
                    if (request instanceof String) {
                        String tmp = (String) request;
                        if (tmp.startsWith(CLOSE_CONNECTION)) {
                            break;
                        }
                        if (tmp.startsWith(FILE_DOWNLOAD_REQUEST)) {
                            String[] commands = tmp.split(STRING_SPLITTER, 2);
                            fileManager.sendAFile(commands[1]);
                        }
                        if (tmp.startsWith(FILE_SEND_REQUEST)) {
                            String[] commands = tmp.split(STRING_SPLITTER, 2);
                            if (fileManager.isAcceptable(commands[1])) {
                                send(FILE_DOWNLOAD_REQUEST + STRING_SPLITTER + commands[1]);
                            } else {
                                send(SERVER_MSG + STRING_SPLITTER + fileManager.getFeedbackMessage());
                            }
                        }
                        if (tmp.startsWith(CHANGE_CURRENT_SERVER_DIR)) {
                            String[] req = tmp.split(STRING_SPLITTER);
                            fileManager.changeCurrentDir(req[1]);
                            send(fileManager.getFileArray());
                            fileManager.sendCurrentDirName();
                        }
                        if (tmp.startsWith(UP_CURRENT_SERVER_DIR)) {
                            fileManager.directoryUP();
                            send(fileManager.getFileArray());
                            fileManager.sendCurrentDirName();
                        }
                        if (tmp.startsWith(CREATE_NEW_DIR)) {
                            String[] req = tmp.split(STRING_SPLITTER, 2);
                            fileManager.createFile(req[1], true);
                            send(fileManager.getFileArray());
                        }
                        if (tmp.startsWith(DELETE_FILE)) {
                            String[] req = tmp.split(STRING_SPLITTER, 2);
                            fileManager.deleteFile(req[1]);
                            send(fileManager.getFileArray());
                        }
                        if (tmp.startsWith(REQUEST_ALL)) {
                            fileManager.sendAll();
                        }
                        if (tmp.startsWith(REFRESH)) {
                            send(fileManager.getFileArray());
                        }
                    }
                }
            }
            disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    synchronized public void send(Object obj) {
        try {
            out.writeObject(obj);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            send(null);
            in.close();
            out.close();
            socket.close();
            server.disconnect(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
