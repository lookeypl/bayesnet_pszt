package bayesnet_pszt;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EditableNode {
    GuiNode gNode;
    JFrame frame;
    JPanel panel;
    JPanel listPanel;
    JButton add, delete, ok, cancel;
    ArrayList<JTextField> list = new ArrayList<JTextField>();
    JTextField state, name;

    public EditableNode() {

        frame = new JFrame("Edit Node");
        panel = new JPanel();
        panel.setLayout(new GridLayout(6,2));
        listPanel = new JPanel();
        listPanel.setLayout(new GridLayout(4,2));
        listPanel.setSize(1000,1000);

        JTextField txt;
        JCheckBox cb;
        for(int i=0;i<4;i++) {
            txt = new JTextField();
            cb = new JCheckBox();
            list.add(txt);
            txt.setEditable(false);
            listPanel.add(txt);
            listPanel.add(cb);
        }

        panel.add(new JLabel("Name"));
        name = new JTextField();
        panel.add(name);
        panel.add(new JLabel("State"));
        state = new JTextField();
        panel.add(state);

        add = new JButton("Add");
        delete = new JButton("Delete");
        ok = new JButton("Ok");
        cancel = new JButton("Cancel");

         cancel.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    frame.setVisible(false);
                }
            });

         add.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    list.get(0).setName(state.getName());
                    listPanel.repaint();
                }
            });

        panel.add(add);
        panel.add(delete);
        panel.add(new JLabel());
        panel.add(new JLabel("                   Read only"));
        panel.add(new JLabel("States"));
        panel.add(listPanel);
        panel.add(ok);
        panel.add(cancel);

        frame.add(panel);
        frame.setSize(400,400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
