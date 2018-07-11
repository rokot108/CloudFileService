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
    private JPanel jPanelTopLeftBottom;
    private JPanel jPanelTopCenter;
    private JPanel jPanelTopCenterButtons;
    private JPanel jPanelTopRight;
    private JPanel jPanelTopRightTop;
    private JPanel jPanelTopRightBottom;
    private JPanel jPanelBottom;
    private JLabel userCurrentDirLabel;
    private JLabel serverCurrentDirLabel;
    private JButton userDirUpBtn;
    private JButton serverDirUpBtn;
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
        jPanelTop.setBackground(Color.cyan);
        add(jPanelTop);

        jPanelBottom = new JPanel(new GridBagLayout());
        jPanelBottom.setBackground(Color.BLACK);

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
        jPanelTopLeft.setBackground(Color.GREEN);
        jPanelTopLeft.setMinimumSize(new Dimension(200, 225));
        jPanelTopLeft.setPreferredSize(new Dimension(250, 300));

        jPanelTopLeftTop = new JPanel(new FlowLayout());
        userDirUpBtn = new JButton("↰");
        userDirUpBtn.setToolTipText("Directory UP");
        userDirUpBtn.setMargin(new Insets(3, 1, 3, 1));
        jPanelTopLeftTop.add(userDirUpBtn);

        userDirUpBtn.addActionListener((e) -> {
                    fileManager.directoryUP();
                    fillUserFileList();
                    userCurrentDirLabel.setText(fileManager.getCurrentDir());
                }
        );

        userCurrentDirLabel = new JLabel();
        userCurrentDirLabel.setText(fileManager.getCurrentDir());
        jPanelTopLeftTop.add(userCurrentDirLabel);
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
        JButton userDelete = new JButton("Delete");
        jPanelTopLeftBottom.add(userOpenBtn);
        userOpenBtn.addActionListener((e) -> {
            File openDir = (File) userFilesJList.getSelectedValue();
            if (openDir.isDirectory()) {
                fileManager.changeCurrentDir(openDir.getName());
                fillUserFileList();
                userCurrentDirLabel.setText(fileManager.getCurrentDir());
                reDraw();
            }
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

        JButton send = new JButton(">");
        send.setPreferredSize(new Dimension(50, 50));
        send.setToolTipText("Send");
        jPanelTopCenterButtons.add(send);

        JButton request = new JButton("<");
        request.setPreferredSize(new Dimension(50, 50));
        request.setToolTipText("Download");
        jPanelTopCenterButtons.add(request);

        JButton requestAll = new JButton("<<");
        requestAll.setPreferredSize(new Dimension(50, 50));
        requestAll.setToolTipText("Download All");
        jPanelTopCenterButtons.add(requestAll);

        jPanelTopCenter.setBackground(Color.YELLOW);
        jPanelTopCenter.add(jPanelTopCenterButtons);

        jPanelTop.add(jPanelTopCenter, FlowLayout.CENTER);

        jPanelTopRight = new JPanel(new BorderLayout());
        jPanelTopRight.setBackground(Color.RED);
        jPanelTopRight.setMinimumSize(new Dimension(200, 225));
        jPanelTopRight.setPreferredSize(new Dimension(250, 300));

        jPanelTopRightTop = new JPanel(new FlowLayout());
        serverDirUpBtn = new JButton("↰");
        serverDirUpBtn.setToolTipText("Directory UP");
        serverDirUpBtn.setMargin(new Insets(3, 1, 3, 1));
        jPanelTopRightTop.add(serverDirUpBtn);

        serverDirUpBtn.addActionListener((e) -> {
            client.requestServerDir();
        });

        serverCurrentDirLabel = new JLabel();
        serverCurrentDirLabel.setText(":\\");
        jPanelTopRightTop.add(serverCurrentDirLabel);
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

        serverOpenBtn.addActionListener((e) -> {
            File openDir = (File) serverFilesJList.getSelectedValue();
            if (openDir.isDirectory()) {
                client.requestServerDir(openDir.getName());
            }
        });

        JButton serverDelete = new JButton("Delete");
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
        this.serverCurrentDirLabel.setText(serverCurrentDirLabel);
    }
}
