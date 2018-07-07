package Client;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class CellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list,
                                                  Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus) {
        Component component = super.getListCellRendererComponent(list,
                value, index, isSelected, cellHasFocus);
        JLabel label = (JLabel) component;
        File file = (File) value;
        label.setText(file.getName());
        Font bold = new Font("Verdana", Font.BOLD, 12);
        Font normal = new Font("Verdana", 0, 12);
        Icon icon;
        if (file.isDirectory()) {
            icon = UIManager.getIcon("Tree.closedIcon");
            label.setFont(bold);
        } else {
            icon = UIManager.getIcon("FileView.fileIcon");
            label.setFont(normal);
        }
        label.setIcon(icon);
        return label;
    }

}
