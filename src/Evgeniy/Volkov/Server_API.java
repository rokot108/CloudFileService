package Evgeniy.Volkov;

public interface Server_API {
    String SYSTEM_SYMBOL = "/";
    String CLOSE_CONNECTION = "/end";
    String AUTH = "/auth";
    String AUTH_SUCCESSFUL = "/authok";
    String PRIVATE_MSG = "/w";
    String USERS_LIST = "/client_list";
}
