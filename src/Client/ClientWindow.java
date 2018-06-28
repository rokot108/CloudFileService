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


    public ClientWindow() {
        setTitle("Cloud File Service");
        //  client = new Client(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(400,400));

        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);
        GridBagConstraints c = new GridBagConstraints();

        jPanelTop = new JPanel(new GridBagLayout());
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

        gbl.setConstraints(jPanelTop, c);
        add(jPanelTop);

        jPanelBottom = new JPanel(new GridBagLayout());
        jPanelBottom.setBackground(Color.BLACK);

        c.anchor = GridBagConstraints.NORTH;
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

        gbl.setConstraints(jPanelBottom, c);
        add(jPanelBottom);

      /*  jPanelTopLeft = new JPanel(new BorderLayout());
        jPanelTopLeft.setPreferredSize(new Dimension(170, 210));
        jPanelTopLeft.setBackground(Color.GREEN);
        jPanelTop.add(jPanelTopLeft, BorderLayout.WEST);

        jPanelTopRight = new JPanel(new BorderLayout());
        jPanelTopRight.setPreferredSize(new Dimension(170, 210));
        jPanelTopRight.setBackground(Color.RED);
        jPanelTop.add(jPanelTopRight, BorderLayout.EAST);

        jPanelTopCenter = new JPanel(new GridLayout());
        jPanelTopCenter.setPreferredSize(new Dimension(60, 210));
        jPanelTopCenter.setBackground(Color.BLUE);
        jPanelTop.add(jPanelTopCenter, BorderLayout.CENTER);

        add(jPanelTop, BorderLayout.NORTH);*/


        jPanelTop.setVisible(true);
        jPanelBottom.setVisible(true);
        setVisible(true);
    }
}
