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
import java.util.Vector;

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
        setBounds(100, 100, 800, 600);

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

        JButton btnRecalc = new JButton("Recalc");
        btnRecalc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Bayes.getInstance().Recalculate();
                repaint();
            }
        });
        buttonsPanel.add(btnRecalc);

        JButton btnGrass = new JButton("LoadGrass");
        btnGrass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiNode rain = diagram.createNode("Rain", 300, 100, 2);
                GuiNode sprinkler = diagram.createNode("Sprinkler", 100, 200, 2);
                GuiNode grass = diagram.createNode("Grass wet", 300, 300, 2);

                diagram.createConnection(rain, sprinkler);
                diagram.createConnection(sprinkler, grass);
                diagram.createConnection(rain, grass);

                rain.getBayesNode().SetProbability(0, 0, 0.2f);
                rain.getBayesNode().SetProbability(0, 1, 0.8f);

                sprinkler.getBayesNode().SetProbability(0, 0, 0.01f);
                sprinkler.getBayesNode().SetProbability(0, 1, 0.99f);
                sprinkler.getBayesNode().SetProbability(1, 0, 0.4f);
                sprinkler.getBayesNode().SetProbability(1, 1, 0.6f);

                grass.getBayesNode().SetProbability(0, 0, 0.99f);
                grass.getBayesNode().SetProbability(0, 1, 0.01f);
                grass.getBayesNode().SetProbability(1, 0, 0.9f);
                grass.getBayesNode().SetProbability(1, 1, 0.1f);
                grass.getBayesNode().SetProbability(2, 0, 0.8f);
                grass.getBayesNode().SetProbability(2, 1, 0.2f);
                grass.getBayesNode().SetProbability(3, 0, 0.0f);
                grass.getBayesNode().SetProbability(3, 1, 1.0f);
                repaint();
            }
        });
        buttonsPanel.add(btnGrass);

        JButton btnCancer = new JButton("LoadCancer");
        btnCancer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiNode pollution = diagram.createNode("Pollution", 100, 100, 2);
                GuiNode smoker = diagram.createNode("Smoker", 300, 100, 2);
                GuiNode cancer = diagram.createNode("Cancer", 200, 200, 2);
                GuiNode xray = diagram.createNode("X-Ray Result", 100, 300, 2);
                GuiNode dyspnoea = diagram.createNode("Dyspnoea", 300, 300, 2);

                diagram.createConnection(pollution, cancer);
                diagram.createConnection(smoker, cancer);
                diagram.createConnection(cancer, xray);
                diagram.createConnection(cancer, dyspnoea);

                Vector<BayesAttributePair<Float>> attr = new Vector<BayesAttributePair<Float>>();
                attr.addElement(new BayesAttributePair<Float>("L", 0.0f));
                attr.addElement(new BayesAttributePair<Float>("H", 0.0f));
                pollution.getBayesNode().SetAttributes(attr);
                pollution.getBayesNode().SetProbability(0, 0, 0.9f);
                pollution.getBayesNode().SetProbability(0, 1, 0.1f);
                attr.clear();

                attr.addElement(new BayesAttributePair<Float>("T", 0.0f));
                attr.addElement(new BayesAttributePair<Float>("F", 0.0f));
                smoker.getBayesNode().SetAttributes(attr);
                smoker.getBayesNode().SetProbability(0, 0, 0.3f);
                smoker.getBayesNode().SetProbability(0, 1, 0.7f);
                attr.clear();

                attr.addElement(new BayesAttributePair<Float>("T", 0.0f));
                attr.addElement(new BayesAttributePair<Float>("F", 0.0f));
                cancer.getBayesNode().SetAttributes(attr);
                cancer.getBayesNode().SetProbability(0, 0, 0.03f);
                cancer.getBayesNode().SetProbability(0, 1, 0.97f);
                cancer.getBayesNode().SetProbability(1, 0, 0.001f);
                cancer.getBayesNode().SetProbability(1, 1, 0.999f);
                cancer.getBayesNode().SetProbability(2, 0, 0.05f);
                cancer.getBayesNode().SetProbability(2, 1, 0.95f);
                cancer.getBayesNode().SetProbability(3, 0, 0.02f);
                cancer.getBayesNode().SetProbability(3, 1, 0.98f);
                attr.clear();

                attr.addElement(new BayesAttributePair<Float>("P", 0.0f));
                attr.addElement(new BayesAttributePair<Float>("N", 0.0f));
                xray.getBayesNode().SetAttributes(attr);
                xray.getBayesNode().SetProbability(0, 0, 0.9f);
                xray.getBayesNode().SetProbability(0, 1, 0.1f);
                xray.getBayesNode().SetProbability(1, 0, 0.2f);
                xray.getBayesNode().SetProbability(1, 1, 0.8f);
                attr.clear();

                attr.addElement(new BayesAttributePair<Float>("T", 0.0f));
                attr.addElement(new BayesAttributePair<Float>("F", 0.0f));
                dyspnoea.getBayesNode().SetAttributes(attr);
                dyspnoea.getBayesNode().SetProbability(0, 0, 0.65f);
                dyspnoea.getBayesNode().SetProbability(0, 1, 0.35f);
                dyspnoea.getBayesNode().SetProbability(1, 0, 0.3f);
                dyspnoea.getBayesNode().SetProbability(1, 1, 0.7f);
                attr.clear();

                repaint();
            }
        });
        buttonsPanel.add(btnCancer);

        Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
        buttonsPanel.add(rigidArea);

        final JButton btnAddnode = new JButton("AddNode");
        final JButton btnSelect = new JButton("Select");
        final JButton btnConnect = new JButton("Connect");
        final JButton btnNone = new JButton("None");

        final JFrame frame = this;
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

                btnAddnode.setEnabled(false);
                btnSelect.setEnabled(true);
                btnConnect.setEnabled(true);
                btnNone.setEnabled(true);
            }
        });
        buttonsPanel.add(btnAddnode);

        btnSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diagram.setSelectedTool(DiagramToolType.SELECT);
                btnAddnode.setEnabled(true);
                btnSelect.setEnabled(false);
                btnConnect.setEnabled(true);
                btnNone.setEnabled(true);
            }
        });
        buttonsPanel.add(btnSelect);

        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diagram.setSelectedTool(DiagramToolType.CONNECT);
                btnAddnode.setEnabled(true);
                btnSelect.setEnabled(true);
                btnConnect.setEnabled(false);
                btnNone.setEnabled(true);
            }
        });
        buttonsPanel.add(btnConnect);

        btnNone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diagram.setSelectedTool(DiagramToolType.NONE);
                btnAddnode.setEnabled(true);
                btnSelect.setEnabled(true);
                btnConnect.setEnabled(true);
                btnNone.setEnabled(false);
            }
        });
        buttonsPanel.add(btnNone);

        diagram.setSelectedTool(DiagramToolType.NONE);
        btnNone.setEnabled(false);

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
