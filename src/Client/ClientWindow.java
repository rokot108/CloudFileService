package Client;

import FileManager.FileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;

public class ClientWindow extends JFrame {

    private Client client;
    private FileManager fileManager;

    private JPanel jPanelTop;
    private JPanel jPanelTopLeft;
    private JPanel jPanelTopLeftTop;
    private JPanel jPanelTopLeftButtons;
    private JButton changeUserDirBtn;
    private JButton refreshUserFilesListBtn;
    private JButton userDirUpBtn;
    private JPanel jPanelTopLeftBottom;
    private JPanel jPanelTopCenter;
    private JPanel jPanelTopCenterButtons;
    private JPanel jPanelTopRight;
    private JPanel jPanelTopRightTop;
    private JPanel jPanelTopRightButtons;
    private JButton refreshServerFilesListBtn;
    private JButton serverDirUpBtn;
    private JPanel jPanelTopRightBottom;
    private JLabel userCurrentDirLabel;
    private JLabel serverCurrentDirLabel;
    private JList userFilesJList;
    private JList serverFilesJList;
    private JButton userOpenBtn;
    private JButton userDeleteBtn;
    private JButton serverOpenBtn;
    private JButton serverDeleteBtn;
    private DefaultListModel listModelUserFiles;
    private DefaultListModel listModelServerFiles;
    private CellRenderer cellRenderer;
    private JPanel jPanelBottom;
    private JLabel serverMsgLabel;
    private JPanel jPanelAuthPanel;
    private Container authLeftCont;
    private Container authRightCont;
    private JTextField loginTxtField;
    private JTextField passwordTxtField;
    private ButtonGroup authOptionsGroup;
    private JRadioButton loginRBtn;
    private JRadioButton registerRBtn;
    private JPanel jPanelBottomButtons;
    private JButton okBtn;

    public ClientWindow(Client client, FileManager fileManager) {
        this.client = client;
        this.fileManager = fileManager;

        ImageIcon refreshIcon = createIcon("images\\refresh20.png");
        ImageIcon deleteIcon = createIcon("images\\delete16.png");
        Image cloudIcon = createIcon("images\\cloud20.png").getImage();

        setTitle("Cloud File Service");
        setIconImage(cloudIcon);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(600, 400));
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    client.disconnect();
                } catch (Exception i) {
                }
            }
        });

        setLayout(new FlowLayout());

        jPanelTop = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        add(jPanelTop);


        jPanelTopLeft = new JPanel(new BorderLayout());
        jPanelTopLeft.setMinimumSize(new Dimension(200, 225));
        jPanelTopLeft.setPreferredSize(new Dimension(250, 300));

        jPanelTopLeftTop = new JPanel(new BorderLayout());
        jPanelTopLeftButtons = new JPanel(new FlowLayout());

        changeUserDirBtn = new JButton();
        changeUserDirBtn.setIcon(UIManager.getIcon("Tree.closedIcon"));
        changeUserDirBtn.setToolTipText("Open another directory");
        changeUserDirBtn.setMargin(new Insets(1, 0, 1, 0));
        jPanelTopLeftButtons.add(changeUserDirBtn);

        changeUserDirBtn.addActionListener(e -> {
            JFileChooser dirChoose = new JFileChooser();
            dirChoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int ret = dirChoose.showDialog(null, "Select");
            if (dirChoose.getSelectedFile() != null) {
                fileManager.setUserDir(dirChoose.getSelectedFile());
                fillUserFileList();
            }
        });

        refreshUserFilesListBtn = new JButton();
        refreshUserFilesListBtn.setToolTipText("Refresh filelist");
        refreshUserFilesListBtn.setMargin(new Insets(0, 0, 0, 0));
        refreshUserFilesListBtn.setIcon(refreshIcon);
        refreshUserFilesListBtn.addActionListener(e -> {
            fillUserFileList();
        });
        jPanelTopLeftButtons.add(refreshUserFilesListBtn);

        refreshUserFilesListBtn.addActionListener(e -> {
            fillUserFileList();
        });

        userDirUpBtn = new JButton();
        userDirUpBtn.setIcon(UIManager.getIcon("FileChooser.upFolderIcon"));
        userDirUpBtn.setToolTipText("Directory UP");
        userDirUpBtn.setMargin(new Insets(1, 0, 1, 0));
        jPanelTopLeftButtons.add(userDirUpBtn);
        jPanelTopLeftTop.add(jPanelTopLeftButtons, BorderLayout.WEST);

        userDirUpBtn.addActionListener((e) -> {
                    fileManager.directoryUP();
                    fillUserFileList();
                    userCurrentDirLabel.setText(":\\" + fileManager.getRelativePath(fileManager.getCurrentDir(), false));
                    reDraw();
                }
        );

        userCurrentDirLabel = new JLabel();
        userCurrentDirLabel.setHorizontalAlignment(JLabel.CENTER);
        userCurrentDirLabel.setText(":\\" + fileManager.getRelativePath(fileManager.getCurrentDir(), false));
        jPanelTopLeftTop.add(userCurrentDirLabel, BorderLayout.CENTER);
        jPanelTopLeft.add(jPanelTopLeftTop, BorderLayout.NORTH);

        cellRenderer = new CellRenderer();

        listModelUserFiles = new DefaultListModel();
        userFilesJList = new JList(listModelUserFiles);
        userFilesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userFilesJList.setCellRenderer(cellRenderer);
        JScrollPane userListScroll = new JScrollPane(userFilesJList);
        userListScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        userListScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jPanelTopLeft.add(userListScroll, BorderLayout.CENTER);

        userFilesJList.addListSelectionListener((e) ->
                reDraw()
        );

        jPanelTopLeftBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userOpenBtn = new JButton("Open");
        userOpenBtn.setIcon(UIManager.getIcon("Tree.closedIcon"));
        jPanelTopLeftBottom.add(userOpenBtn);
        userOpenBtn.setToolTipText("Open a direcrory");
        userOpenBtn.addActionListener((e) -> {
            File openDir = (File) userFilesJList.getSelectedValue();
            if (openDir.isDirectory()) {
                fileManager.changeCurrentDir(openDir.getName());
                fillUserFileList();
                userCurrentDirLabel.setText(":\\" + fileManager.getRelativePath(fileManager.getCurrentDir(), false));
                reDraw();
            }
        });

        userDeleteBtn = new JButton("Delete");
        userDeleteBtn.setToolTipText("Delete from your PC");
        userDeleteBtn.setIcon(deleteIcon);
        userDeleteBtn.addActionListener((e) -> {
            File tmp = (File) userFilesJList.getSelectedValue();
            fileManager.deleteFile(tmp.getName());
            fillUserFileList();
        });

        jPanelTopLeftBottom.add(userDeleteBtn);
        userOpenBtn.setSize(userDeleteBtn.getSize());
        jPanelTopLeft.add(jPanelTopLeftBottom, BorderLayout.SOUTH);

        jPanelTop.add(jPanelTopLeft, FlowLayout.LEFT);

        jPanelTopCenter = new JPanel();
        jPanelTopCenter.setLayout(new FlowLayout(FlowLayout.CENTER));
        jPanelTopCenterButtons = new JPanel();
        jPanelTopCenterButtons.setPreferredSize(new Dimension(54, 225));
        jPanelTopCenterButtons.setMaximumSize(new Dimension(54, 225));
        jPanelTopCenterButtons.setMinimumSize(new Dimension(54, 225));
        jPanelTopCenterButtons.setLayout(new FlowLayout(FlowLayout.CENTER));
        jPanelTopCenterButtons.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        jPanelTopCenterButtons.setAlignmentY(JComponent.CENTER_ALIGNMENT);

        JButton sendAll = new JButton(">>");
        sendAll.setPreferredSize(new Dimension(50, 50));
        sendAll.setToolTipText("Send All");
        jPanelTopCenterButtons.add(sendAll);
        sendAll.addActionListener(e -> {
                    fileManager.sendAll();
                }
        );

        JButton send = new JButton(">");
        send.setPreferredSize(new Dimension(50, 50));
        send.setToolTipText("Send");
        jPanelTopCenterButtons.add(send);

        send.addActionListener((e) -> {
            client.sendFile((File) userFilesJList.getSelectedValue());
        });

        JButton request = new JButton("<");
        request.setPreferredSize(new Dimension(50, 50));
        request.setToolTipText("Download");
        jPanelTopCenterButtons.add(request);

        request.addActionListener((e) -> {
            File tmp = (File) serverFilesJList.getSelectedValue();
            client.requestFile(tmp.getName());
        });

        JButton requestAll = new JButton("<<");
        requestAll.setPreferredSize(new Dimension(50, 50));
        requestAll.setToolTipText("Download All");
        jPanelTopCenterButtons.add(requestAll);

        requestAll.addActionListener(e -> {
            client.requestAll();
        });

        jPanelTopCenter.add(jPanelTopCenterButtons);

        jPanelTop.add(jPanelTopCenter, FlowLayout.CENTER);

        jPanelTopRight = new JPanel(new BorderLayout());
        jPanelTopRight.setMinimumSize(new Dimension(200, 225));
        jPanelTopRight.setPreferredSize(new Dimension(250, 300));

        jPanelTopRightTop = new JPanel(new BorderLayout());
        jPanelTopRightButtons = new JPanel(new FlowLayout());

        refreshServerFilesListBtn = new JButton();
        refreshServerFilesListBtn.setToolTipText("Refresh filelist");
        refreshServerFilesListBtn.setMargin(new Insets(0, 0, 0, 0));
        refreshServerFilesListBtn.setIcon(refreshIcon);
        jPanelTopRightButtons.add(refreshServerFilesListBtn);

        refreshServerFilesListBtn.addActionListener(e -> {
            client.requestForRefresh();
        });

        serverDirUpBtn = new JButton();
        serverDirUpBtn.setIcon(UIManager.getIcon("FileChooser.upFolderIcon"));
        serverDirUpBtn.setToolTipText("Directory UP");
        serverDirUpBtn.setMargin(new Insets(1, 0, 1, 0));
        jPanelTopRightButtons.add(serverDirUpBtn);

        serverDirUpBtn.addActionListener((e) -> {
            client.changeCurrentServerDir();
        });

        jPanelTopRightTop.add(jPanelTopRightButtons, BorderLayout.WEST);

        serverCurrentDirLabel = new JLabel();
        serverCurrentDirLabel.setHorizontalAlignment(JLabel.CENTER);
        serverCurrentDirLabel.setText(":\\");
        jPanelTopRightTop.add(serverCurrentDirLabel, BorderLayout.CENTER);
        jPanelTopRight.add(jPanelTopRightTop, BorderLayout.NORTH);

        listModelServerFiles = new DefaultListModel();
        serverFilesJList = new JList(listModelServerFiles);
        serverFilesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        serverFilesJList.setCellRenderer(cellRenderer);
        JScrollPane serverListScroll = new JScrollPane(serverFilesJList);
        serverListScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        serverListScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jPanelTopRight.add(serverListScroll, BorderLayout.CENTER);

        serverFilesJList.addListSelectionListener((e) ->
                reDraw()
        );

        jPanelTopRightBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        serverOpenBtn = new JButton("Open");
        serverOpenBtn.setToolTipText("Open a direcrory");
        serverOpenBtn.setIcon(UIManager.getIcon("Tree.closedIcon"));
        serverOpenBtn.addActionListener((e) -> {
            File openDir = (File) serverFilesJList.getSelectedValue();
            if (openDir.isDirectory()) {
                client.changeCurrentServerDir(openDir.getName());
            }
        });

        serverDeleteBtn = new JButton("Delete");
        serverDeleteBtn.setToolTipText("Delete from a cloud");
        serverDeleteBtn.setIcon(deleteIcon);
        serverDeleteBtn.addActionListener((e) -> {
            File tmp = (File) serverFilesJList.getSelectedValue();
            client.reqesFileDelete(tmp.getName());
        });

        jPanelTopRightBottom.add(serverOpenBtn);
        jPanelTopRightBottom.add(serverDeleteBtn);
        serverOpenBtn.setSize(serverDeleteBtn.getSize());
        jPanelTopRight.add(jPanelTopRightBottom, BorderLayout.SOUTH);

        jPanelTop.add(jPanelTopRight, FlowLayout.RIGHT);

        jPanelBottom = new JPanel(new BorderLayout());
        serverMsgLabel = new JLabel("Please, authorise:");
        jPanelBottom.add(serverMsgLabel, BorderLayout.NORTH);

        jPanelAuthPanel = new JPanel(new FlowLayout());
        authOptionsGroup = new ButtonGroup();

        authLeftCont = new Container();
        authLeftCont.setLayout(new BoxLayout(authLeftCont, BoxLayout.Y_AXIS));
        authLeftCont.add(new JLabel("Login:"));
        loginTxtField = new JTextField(15);
        authLeftCont.add(loginTxtField);
        loginRBtn = new JRadioButton("Login");
        authOptionsGroup.add(loginRBtn);
        loginRBtn.setSelected(true);
        authLeftCont.add(loginRBtn);
        jPanelAuthPanel.add(authLeftCont);

        authRightCont = new Container();
        authRightCont.setLayout(new BoxLayout(authRightCont, BoxLayout.Y_AXIS));
        authRightCont.add(new JLabel("Password:"));
        passwordTxtField = new JTextField(15);
        authRightCont.add(passwordTxtField);
        registerRBtn = new JRadioButton("Register");
        authOptionsGroup.add(registerRBtn);
        authRightCont.add(registerRBtn);
        jPanelAuthPanel.add(authRightCont);

        jPanelBottom.add(jPanelAuthPanel, BorderLayout.CENTER);

        jPanelBottomButtons = new JPanel(new FlowLayout());
        okBtn = new JButton("OK");
        jPanelBottomButtons.add(okBtn);
        jPanelBottom.add(jPanelBottomButtons, BorderLayout.SOUTH);

        okBtn.addActionListener(e -> {
            String login = loginTxtField.getText();
            String pass = passwordTxtField.getText();
            AuthActions action;
            if (loginRBtn.isSelected()) {
                action = AuthActions.LOGIN;
            } else action = AuthActions.REGISTER;
            if (login.length() != 0 && pass.length() != 0) {
                client.authAction(action, login, pass);
            }
        });

        add(jPanelBottom);

        reDraw();
        setVisible(true);
    }


    public void fillUserFileList() {
        fillFileList(listModelUserFiles, fileManager.getFileArray());
        userFilesJList.requestFocusInWindow();
    }

    public void fillServerFileList(File[] files) {
        fillFileList(listModelServerFiles, files);
        serverFilesJList.requestFocusInWindow();
    }

    private void fillFileList(DefaultListModel model, File[] files) {
        model.clear();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) model.addElement(files[i]);
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) model.addElement(files[i]);
        }
    }

    public void reDraw() {
        if (userCurrentDirLabel.getText().equals(":\\")) {
            userDirUpBtn.setEnabled(false);
        } else userDirUpBtn.setEnabled(true);
        if (serverCurrentDirLabel.getText().equals(":\\")) {
            serverDirUpBtn.setEnabled(false);
        } else serverDirUpBtn.setEnabled(true);
        File userSelectedFile = (File) userFilesJList.getSelectedValue();
        if (userSelectedFile != null && userSelectedFile.isDirectory()) {
            userOpenBtn.setEnabled(true);
        } else userOpenBtn.setEnabled(false);
        File serverSelectedFile = (File) serverFilesJList.getSelectedValue();
        if (serverSelectedFile != null && serverSelectedFile.isDirectory()) {
            serverOpenBtn.setEnabled(true);
        } else serverOpenBtn.setEnabled(false);
        if (userSelectedFile == null) {
            userDeleteBtn.setEnabled(false);
        } else userDeleteBtn.setEnabled(true);
        if (serverSelectedFile == null) {
            serverDeleteBtn.setEnabled(false);
        } else serverDeleteBtn.setEnabled(true);

    }

    public void setServerCurrentDirLabel(String serverCurrentDirLabel) {
        this.serverCurrentDirLabel.setText(":\\" + serverCurrentDirLabel);
    }

    protected static ImageIcon createIcon(String path) {
        URL imgURL = ClientWindow.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Icon image file not found " + path);
            return null;
        }
    }

    public void setServerMsgLabel(String serverMsg) {
        serverMsgLabel.setText(serverMsg);
    }
}
