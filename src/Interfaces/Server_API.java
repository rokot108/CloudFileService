package Interfaces;

public interface Server_API {
    String STRING_SPLITTER = "//";
    String CLOSE_CONNECTION = "/end";
    String REGISTRATION = "/reg";
    String AUTH = "/auth";
    String AUTH_OK = "/authok";
    String SERVER_MSG = "/serverMsg";
    String FILE_DOWNLOAD_REQUEST = "/fileReq";
    String FILE_SEND_REQUEST = "/sendReq";
    String REQUEST_ALL = "/reqAll";
    String NEW_CURRENT_SERVER_DIR = "/newcurrdir";
    String CHANGE_CURRENT_SERVER_DIR = "/changedir";
    String UP_CURRENT_SERVER_DIR = "/updir";
    String CREATE_NEW_DIR = "/mkdir";
    String DELETE_FILE = "/del";
    String REFRESH = "/ref";
}
