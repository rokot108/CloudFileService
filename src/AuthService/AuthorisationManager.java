package AuthService;

import Interfaces.Constants;

import java.io.*;
import java.util.Vector;

public class AuthorisationManager implements Identificationable, Constants, Serializable {

    private static File usersListFile;
    private static Vector<UserAccount> users;
    private static FileOutputStream fos;
    private static FileInputStream fis;
    private static ObjectOutputStream obos;
    private static ObjectInputStream obis;
    private String userLogin;
    private int userPassHash;
    private String feedbackMessage;
    private boolean authorised;

    public AuthorisationManager() {

        if (users == null) {
            init();
        }

        this.userLogin = null;
        this.userPassHash = 000;
        authorised = false;
        feedbackMessage = "AuthService is ready";
    }

    private static synchronized void init() {
        if (users == null) {
            users = new Vector<>();
            usersListFile = new File(SERVER_PATH + "\\" + USERS_DATA_BASE);
            try {
                if (usersListFile.exists()) {
                    fis = new FileInputStream(usersListFile);
                    obis = new ObjectInputStream(fis);
                    while (fis.available() != 0) {
                        users.add((UserAccount) obis.readObject());
                    }
                } else {
                    usersListFile.createNewFile();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos = new FileOutputStream(usersListFile, false);
                obos = new ObjectOutputStream(fos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public void setUserPassHash(int userPassHash) {
        this.userPassHash = userPassHash;
    }

    public String getUserLogin() {
        return userLogin;
    }

    @Override
    public boolean register() {
        return register(userLogin, userPassHash);
    }

    private synchronized boolean register(String login, int userPassHash) {
        for (UserAccount user : users) {
            if (user.getUsername() == login) {
                feedbackMessage = "Account " + login + " is already exists.";
                return false;
            }
        }
        users.add(new UserAccount(login, userPassHash));
        if (saveUserAccount()) {
            feedbackMessage = "Registration success, logged in as " + login + ".";
            authorised = true;
            return true;
        } else {
            feedbackMessage = "Inner problem. Registration failed.";
            return false;
        }
    }

    @Override
    public boolean unregister() {
        return false;
    }

    private boolean unregister(String login, int userPassHash) {
        return false;
    }

    @Override
    public boolean login() {
        return login(userLogin, userPassHash);
    }

    private boolean login(String login, int passHash) {
        for (UserAccount user : users) {
            if (user.getUsername().equals(login) && user.getPassHash() == passHash) {
                feedbackMessage = "Logged in successfully as " + login + ".";
                authorised = true;
                return true;
            }
            if (user.getUsername().equals(login) && user.getPassHash() != passHash) {
                feedbackMessage = "Login failed: password is incorrect.";
                return false;
            }
        }
        feedbackMessage = "User " + login + " does not exists";
        return false;
    }

    @Override
    public String getFeedbackMessage() {
        return feedbackMessage;
    }

    @Override
    public boolean isAuthorised() {
        return authorised;
    }

    private static synchronized boolean saveUserAccount() {
        try {
            for (UserAccount user : users) {
                obos.writeObject(user);
            }
            obos.flush();
            fos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
