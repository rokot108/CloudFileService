package AuthService;

public interface Identificationable {

    boolean register();

    boolean unregister();

    boolean login();

    String getFeedbackMessage();

    boolean isAuthorised();

    boolean logout();
}
