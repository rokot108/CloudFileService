package Client;

import FileManager.FileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class ClientWindow extends JFrame {

    private Client client;
    private FileManager fileManager;

    private JPanel jPanelTop;
    private JPanel jPanelTopLeft;
    private JPanel jPanelTopLeftTop;
    private JPanel jPanelTopLeftButtons;
    private JButton changeUserDirBtn;
    private JButton renewUserFilesListBtn;
    private JButton userDirUpBtn;
    private JPanel jPanelTopLeftBottom;
    private JPanel jPanelTopCenter;
    private JPanel jPanelTopCenterButtons;
    private JPanel jPanelTopRight;
    private JPanel jPanelTopRightTop;
    private JPanel jPanelTopRightButtons;
    private JButton renewServerFilesListBtn;
    private JButton serverDirUpBtn;
    private JPanel jPanelTopRightBottom;
    private JPanel jPanelBottom;
    private JLabel userCurrentDirLabel;
    private JLabel serverCurrentDirLabel;
    private JList userFilesJList;
    private JList serverFilesJList;
    private JButton userOpenBtn;
    private JButton serverOpenBtn;
    private DefaultListModel listModelUserFiles;
    private DefaultListModel listModelServerFiles;
    private CellRenderer cellRenderer;


    public ClientWindow(Client client, FileManager fileManager) {
        this.client = client;
        this.fileManager = fileManager;

        setTitle("Cloud File Service");
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

        GridBagLayout gbAll = new GridBagLayout();
        setLayout(gbAll);
        GridBagConstraints c = new GridBagConstraints();

        jPanelTop = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        add(jPanelTop);

        jPanelBottom = new JPanel(new GridBagLayout());

        c.anchor = GridBagConstraints.SOUTH;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(2, 5, 5, 5);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 1;
        c.weighty = 0.3;

        gbAll.setConstraints(jPanelBottom, c);
        add(jPanelBottom);

        jPanelTopLeft = new JPanel(new BorderLayout());
        jPanelTopLeft.setMinimumSize(new Dimension(200, 225));
        jPanelTopLeft.setPreferredSize(new Dimension(250, 300));

        jPanelTopLeftTop = new JPanel(new BorderLayout());
        jPanelTopLeftButtons = new JPanel(new FlowLayout());

        changeUserDirBtn = new JButton();
        changeUserDirBtn.setIcon(UIManager.getIcon("Tree.closedIcon"));
        changeUserDirBtn.setMargin(new Insets(0, 0, 0, 0));
        jPanelTopLeftButtons.add(changeUserDirBtn);

        changeUserDirBtn.addActionListener(e -> {
            JFileChooser dirChoose = new JFileChooser();
            dirChoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int ret = dirChoose.showDialog(null, "Select");
            fileManager.setUserDir(dirChoose.getSelectedFile());
            fillUserFileList();
        });

        renewUserFilesListBtn = new JButton();
        renewUserFilesListBtn.setIcon(UIManager.getIcon(""));
        renewUserFilesListBtn.setMargin(new Insets(0, 0, 0, 0));
        renewUserFilesListBtn.addActionListener(e -> {
            fillUserFileList();
        });
        jPanelTopLeftButtons.add(renewUserFilesListBtn);

        userDirUpBtn = new JButton("↰");
        userDirUpBtn.setIcon(UIManager.getIcon("FileChooser.upFolderIcon"));
        userDirUpBtn.setMargin(new Insets(0, 0, 0, 0));
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

        JButton userDelete = new JButton("Delete");
        userDelete.addActionListener((e) -> {
            File tmp = (File) userFilesJList.getSelectedValue();
            fileManager.deleteFile(tmp.getName());
            fillUserFileList();
        });

        jPanelTopLeftBottom.add(userDelete);
        userOpenBtn.setSize(userDelete.getSize());
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
        serverDirUpBtn = new JButton("↰");
        serverDirUpBtn.setToolTipText("Directory UP");
        serverDirUpBtn.setMargin(new Insets(3, 1, 3, 1));
        jPanelTopRightTop.add(serverDirUpBtn, BorderLayout.WEST);

        serverDirUpBtn.addActionListener((e) -> {
            client.changeCurrentServerDir();
        });

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
        serverOpenBtn.addActionListener((e) -> {
            File openDir = (File) serverFilesJList.getSelectedValue();
            if (openDir.isDirectory()) {
                client.changeCurrentServerDir(openDir.getName());
            }
        });

        JButton serverDelete = new JButton("Delete");
        serverDelete.addActionListener((e) -> {
            File tmp = (File) serverFilesJList.getSelectedValue();
            client.reqesFileDelete(tmp.getName());
        });

        jPanelTopRightBottom.add(serverOpenBtn);
        jPanelTopRightBottom.add(serverDelete);
        serverOpenBtn.setSize(serverDelete.getSize());
        jPanelTopRight.add(jPanelTopRightBottom, BorderLayout.SOUTH);

        jPanelTop.add(jPanelTopRight, FlowLayout.RIGHT);

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
    }

    public void setServerCurrentDirLabel(String serverCurrentDirLabel) {
        this.serverCurrentDirLabel.setText(":\\" + serverCurrentDirLabel);
    }
}
