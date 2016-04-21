package bayesnet_pszt;

import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.ActionEvent;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class DiagramTestWindowFrame extends JFrame {
    private static final long serialVersionUID = 6855979912985090468L;
    private JScrollPane scrollPane;
    private GuiDiagram diagram;
    
    /**
     * Create the frame.
     */
    public DiagramTestWindowFrame() {
        setTitle("DiagramTest <DEBUG>");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(buttonsPanel);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        
        JButton btnRemoveselected = new JButton("RemoveSelected");
        btnRemoveselected.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                diagram.removeSelectedElements();
                repaint();
            }
        });
        
        JButton btnNew = new JButton("ClearAll");
        btnNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                diagram.clearAllElements();
                repaint();
            }
        });
        buttonsPanel.add(btnNew);
        buttonsPanel.add(btnRemoveselected);
        
        Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
        buttonsPanel.add(rigidArea);
        
        JButton btnReposition = new JButton("Reposition");
        btnReposition.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                diagram.setSelectedTool(DiagramToolType.REPOSITION);
            }
        });
        
        JButton btnAddnode = new JButton("AddNode");
        btnAddnode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                diagram.setSelectedTool(DiagramToolType.ADDNODE);
                repaint();
            }
        });
        buttonsPanel.add(btnAddnode);
        buttonsPanel.add(btnReposition);
        
        JButton btnSelect = new JButton("Select");
        btnSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                diagram.setSelectedTool(DiagramToolType.SELECT);
            }
        });
        buttonsPanel.add(btnSelect);
        
        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ActionListener() {
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
        
        diagram = new GuiDiagram(3000,3000);
        
        JPanel diagramPanel = new JPanel(){
            private static final long serialVersionUID = 5167246880324842493L;
            
            @Override
            public void paintComponent(Graphics g) {
                diagram.paintConponent(g);
            }
        };
        diagramPanel.setPreferredSize(new Dimension(3000,3000));
        
        diagramPanel.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                diagram.handleMousePressed(e);
                diagramPanel.repaint();
            }
            public void mouseClicked(MouseEvent e){
                diagram.handleMouseClicked(e);
                diagramPanel.repaint();
            }
        });
        
        diagramPanel.addMouseMotionListener(new MouseMotionAdapter(){
            public void mouseDragged(MouseEvent e){
                diagram.handleMouseDragged(e);
                diagramPanel.repaint();
            }
        });
        
        scrollPane.setViewportView(diagramPanel);
    }
}
