package bayesnet_pszt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class GuiNode implements GuiElement {
    // fields
    public static int NODE_WIDTH = 125;
    private static Color NORMAL_NODE_COLOR = Color.decode("0xDFDAD2");
    private static Color EVIDENCE_NODE_COLOR = Color.decode("0xDEBD68");

    private GuiDiagram diagram;
    private String name;
    private int x;
    private int y;
    private int height;
    private int width;
    private List<GuiNodesConnection> connections = new ArrayList<GuiNodesConnection>();

    private boolean isEvidence;
    // TODO: List of possible options for a node
    // TODO: collection containing 'combination data' entries for a node that has any parents
    // TODO: collection containing 'probalilities data' entries for every node
    //          - given by user when evidence node, or calculated by Bayes algorithm when not

    // getters & setters
    public GuiDiagram getDiagram() { return diagram; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getHeight() { return height; }
    public int getWidth() { return width; }
    public List<GuiNodesConnection> getConnections() { return connections; }
    public void setIsEvidence(boolean value) { this.isEvidence = value; }
    public boolean getIsEvidence() { return isEvidence; }

    // constructors
    public GuiNode(GuiDiagram diagram, String name, int x, int y) {
        this.diagram = diagram;
        this.name = name;
        this.x = x;
        this.y = y;

        this.width = NODE_WIDTH;
        this.height = NODE_WIDTH/2; // TODO: temp only; will be calculated from the number of options
    }

    // methods
    public void paintConponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        Stroke normal = new BasicStroke(1);
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        Font textFont = new Font("TimesRoman", Font.PLAIN, 12);

        Shape s = new RoundRectangle2D.Double(x, y, width, height, 2, 2);
        g2.setColor(isEvidence? EVIDENCE_NODE_COLOR : NORMAL_NODE_COLOR);
        g2.fill(s);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(textFont);
        g2.setStroke(normal);
        g2.setColor(Color.BLACK);
        g2.drawString(this.name, x + 2, y + 12);
        g2.drawLine(x, y + 14, x + NODE_WIDTH, y + 14);

        g2.setStroke(normal);
        g2.setColor(Color.BLACK);
        g2.draw(s);

        // TODO: drawing data options and calculated/evidence probablilties for them

        if (diagram.isElementSelected(this)) {
            g2.setStroke(dashed);
            Shape s2 = new RoundRectangle2D.Double(x - 5, y - 5, width + 10, height + 10, 5, 5);
            g2.draw(s2);
        }
    }

    public Point getConnectionSourcePoint() {
        return new Point(x + width/2, y + height);
    }

    public Point getConnectionDestinationPoint() {
        return new Point(x + width/2, y);
    }

    @Override
    public boolean isLocationHit(int x, int y) {
        return ((x >= this.x && x <= this.x + this.width) &&
                (y >= this.y && y <= this.y + this.height));
    }
}

