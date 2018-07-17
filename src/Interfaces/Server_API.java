package Interfaces;

public interface Server_API {
    String STRING_SPLITTER = "//";
    String CLOSE_CONNECTION = "/end";
    String AUTH = "/auth";
    String AUTH_SUCCESSFUL = "/authok";
    String FILE_REQUEST = "/freq";
    String REQEST_ALL = "/reqAll";
    String FILE_NOT_FOUND = "/not_found";
    String NEW_CURRENT_SERVER_DIR = "/newcurrdir";
    String CHANGE_CURRENT_SERVER_DIR = "/changedir";
    String UP_CURRENT_SERVER_DIR = "/updir";
    String CREATE_NEW_DIR = "/mkdir";
    String DELETE_FILE = "/del";
    String REFRESH = "/ref";
}
