package bayesnet_pszt;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

public class DiagramWindowFrame extends JFrame {
    private static final long serialVersionUID = 6855979912985090468L;
    public static DiagramWindowFrame Instance = new DiagramWindowFrame();
    private JScrollPane scrollPane;
    private GuiDiagram diagram;
    final JPanel diagramPanel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    DiagramWindowFrame frame = new DiagramWindowFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public DiagramWindowFrame() {
        setTitle("DiagramWindow");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);

        diagram = new GuiDiagram(3000,3000);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(buttonsPanel);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

        JButton btnRemoveselected = new JButton("RemoveSelected");
        btnRemoveselected.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diagram.removeSelectedElements();
                repaint();
            }
        });

        JButton btnNew = new JButton("ClearAll");
        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diagram.clearAllElements();
                repaint();
            }
        });
        buttonsPanel.add(btnNew);
        buttonsPanel.add(btnRemoveselected);

        Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
        buttonsPanel.add(rigidArea);

        JButton btnAddnode = new JButton("AddNode");
        JFrame frame = this;
        btnAddnode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = (String)JOptionPane.showInputDialog(
                        frame,
                        "New node attributes count:",
                        "Enter attributes count",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "3");

                int argCount = 3;
                try
                {
                    if (s != null)
                        argCount = Integer.parseInt(s);

                    if (argCount < 0)
                        argCount = 3;
                }
                finally { }

                diagram.setNewNodeAttributesCount(argCount);
                diagram.setSelectedTool(DiagramToolType.ADDNODE);
                repaint();
            }
        });
        buttonsPanel.add(btnAddnode);

        JButton btnSelect = new JButton("Select");
        btnSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diagram.setSelectedTool(DiagramToolType.SELECT);
            }
        });
        buttonsPanel.add(btnSelect);

        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diagram.setSelectedTool(DiagramToolType.CONNECT);
            }
        });
        buttonsPanel.add(btnConnect);

        scrollPane = new JScrollPane();
        scrollPane.setViewportBorder(new EmptyBorder(2, 2, 2, 2));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        getContentPane().add(scrollPane);

        diagramPanel = new JPanel(){
            private static final long serialVersionUID = 5167246880324842493L;

            @Override
            public void paintComponent(Graphics g) {
                diagram.paintConponent(g);
            }
        };
        diagramPanel.setPreferredSize(new Dimension(diagram.getWidth(), diagram.getHeight()));

        diagramPanel.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                diagram.handleMousePressed(e);
                diagramPanel.repaint();
            }
            @Override
            public void mouseClicked(MouseEvent e){
                diagram.handleMouseClicked(e);
                diagramPanel.repaint();
            }
        });

        diagramPanel.addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseDragged(MouseEvent e){
                diagram.handleMouseDragged(e);
                diagramPanel.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e){
                diagram.handleMouseMoved(e);
                diagramPanel.repaint();
            }
        });

        scrollPane.setViewportView(diagramPanel);
    }

    public void repaintDiagram()
    {
        diagramPanel.repaint();
    }
}
