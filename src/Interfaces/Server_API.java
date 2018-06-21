package Interfaces;

public interface Server_API {
    String CLOSE_CONNECTION = "/end";
    String AUTH = "/auth";
    String AUTH_SUCCESSFUL = "/authok";
    String FILE_REQUEST = "/freq";
    String STRING_SPLITTER = ";";
    String FILE_NOT_FOUND = "/not_found";
}
