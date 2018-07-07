package Client;

import javax.swing.*;
import java.awt.*;

public class ClientWindow extends JFrame {

    private Client client;

    private JPanel jPanelTop;
    private JPanel jPanelTopLeft;
    private JPanel jPanelTopCenter;
    private JPanel jPanelTopRight;
    private JPanel jPanelBottom;
    private JList userFiles;
    private JList serverFiles;
    private DefaultListModel listModelUserFiles;
    private DefaultListModel listModelServerFiles;
    private CellRenderer cellRenderer;


    public ClientWindow(Client client) {
        setTitle("Cloud File Service");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(400, 400));

        GridBagLayout gbAll = new GridBagLayout();
        setLayout(gbAll);
        GridBagConstraints c = new GridBagConstraints();

        GridBagLayout gbTop = new GridBagLayout();
        jPanelTop = new JPanel(gbTop);
        jPanelTop.setBackground(Color.cyan);

        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 2, 5);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 1;
        c.weighty = 0.6;

        gbAll.setConstraints(jPanelTop, c);
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

        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 0, 2);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 0.45;
        c.weighty = 1;

        gbTop.setConstraints(jPanelTopLeft, c);
        jPanelTop.add(jPanelTopLeft);

        GridBagLayout gbTopRight = new GridBagLayout();
        jPanelTopRight = new JPanel(gbTopRight);
        jPanelTopRight.setBackground(Color.RED);

        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 0;
        c.insets = new Insets(0, 2, 0, 0);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 0.45;
        c.weighty = 1;

        gbTop.setConstraints(jPanelTopRight, c);
        jPanelTop.add(jPanelTopRight);

        GridBagLayout gbTopCenter = new GridBagLayout();
        jPanelTopCenter = new JPanel(gbTopCenter);
        jPanelTopCenter.setBackground(Color.YELLOW);

        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 2, 5, 2);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 0.1;
        c.weighty = 1;

        gbTop.setConstraints(jPanelTopCenter, c);
        jPanelTop.add(jPanelTopCenter);

        cellRenderer = new CellRenderer();

        listModelUserFiles = new DefaultListModel();
        userFiles = new JList(listModelUserFiles);
        userFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userFiles.setCellRenderer(new CellRenderer());
        JScrollPane userListScroll = new JScrollPane(userFiles);
        userListScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        userListScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jPanelTopLeft.add(userListScroll, BorderLayout.CENTER);

        setVisible(true);
    }

    public DefaultListModel getListModelUserFiles() {
        return listModelUserFiles;
    }

    public DefaultListModel getListModelServerFiles() {
        return listModelServerFiles;
    }
}
